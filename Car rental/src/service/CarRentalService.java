package service;

import enums.BookingStatus;
import enums.PaymentStatus;
import enums.VehicleType;
import lombok.Setter;
import models.Booking;
import models.Branch;
import models.Vehicle;
import repositories.BookingRepository;
import repositories.BranchRepository;
import strategies.booking.BookingStrategy;
import strategies.payment.PaymentStrategy;
import strategies.pricing.PricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class CarRentalService {
    private static CarRentalService instance = null;
    @Setter
    private BookingStrategy bookingStrategy;
    private BookingRepository bookingRepository;
    private BranchRepository branchRepository;
    private PricingStrategy pricingStrategy;
    private VehicleLockManager vehicleLockManager = new VehicleLockManager();

    private CarRentalService(BranchRepository branchRepository, BookingRepository bookingRepository,
                             PricingStrategy pricingStrategy, BookingStrategy bookingStrategy){
        this.branchRepository = branchRepository;
        this.bookingRepository = bookingRepository;
        this.pricingStrategy = pricingStrategy;
        this.bookingStrategy = bookingStrategy;
    }

    public static CarRentalService getInstance(BranchRepository branchRepository, BookingRepository bookingRepository,
                                               PricingStrategy pricingStrategy,
                                               BookingStrategy bookingStrategy){
        if (instance == null){
            synchronized (CarRentalService.class) {
                if (instance == null){
                    instance = new CarRentalService(branchRepository, bookingRepository,
                            pricingStrategy, bookingStrategy);
                }
            }
        }
        return instance;
    }

    private boolean isOverlapping(
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd,
            Booking existingBooking) {

        return requestedStart.isBefore(existingBooking.getEndTime())
                && requestedEnd.isAfter(existingBooking.getStartTime());
    }

    private Branch getVehicleLocation(
            Vehicle vehicle,
            Branch requestedBranch,
            LocalDateTime requestedStart,
            List<Booking> bookings) {

        Booking latestBooking = null;

        for (Booking booking : bookings) {

            // Booking hasn't finished before requested start
            if (booking.getEndTime().isAfter(requestedStart)) {
                continue;
            }

            if (latestBooking == null ||
                    booking.getEndTime().isAfter(latestBooking.getEndTime())) {
                latestBooking = booking;
            }
        }

        // Vehicle has never been booked before this time.
        // So it should be at its original branch.
        if (latestBooking == null) {
            return requestedBranch;
        }

        return latestBooking.getDropOffBranch();
    }

    public List<Vehicle> findAvailableVehicle(
            VehicleType vehicleType,
            Branch pickupBranch,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String userId) {

        List<Vehicle> vehicles = pickupBranch.getVehiclesByType(vehicleType);
        List<Vehicle> availableVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {

            // Check temporary booking lock
            if (vehicleLockManager.isLockedByOther(vehicle.getLicensePlate(), userId)) {
                continue;
            }

            List<Booking> bookings = bookingRepository.getBookingsForVehicle(vehicle);

            boolean hasOverlap = false;

            for (Booking booking : bookings) {
                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                    continue;
                }

                if (isOverlapping(startTime, endTime, booking)) {
                    hasOverlap = true;
                    break;
                }
            }

            if (hasOverlap) {
                continue;
            }

            Branch vehicleLocation = this.getVehicleLocation(vehicle, pickupBranch, startTime, bookings);

            if (vehicleLocation.equals(pickupBranch)) {
                availableVehicles.add(vehicle);
            }
        }

        return availableVehicles;
    }

    public Optional<Booking> bookVehicle(
            String pickUpBranchId,
            String dropOffBranchId,
            VehicleType vehicleType,
            LocalDateTime startTime,
            LocalDateTime endTime,
            double distanceKm,
            PaymentStrategy paymentStrategy,
            String userId) {

        Branch pickUpBranch = this.branchRepository.getBranchById(pickUpBranchId);
        Branch dropOffBranch = this.branchRepository.getBranchById(dropOffBranchId);
        List<Vehicle> branchVehicles = pickUpBranch.getVehiclesByType(vehicleType);

        if (branchVehicles.isEmpty()) {
            System.out.println("No vehicles of type " + vehicleType);
            return Optional.empty();
        }

        /*
         * Step 1:
         * Find vehicles that appear to be available.
         *
         * This is only a candidate list.
         * We cannot trust this list for final booking because
         * another user could book a vehicle immediately afterwards.
         */
        List<Vehicle> availableVehicles = this.findAvailableVehicle(vehicleType, pickUpBranch, startTime, endTime, userId);

        if (availableVehicles.isEmpty()) {
            System.out.println("No available vehicles of type " + vehicleType);
            return Optional.empty();
        }

        /*
         * Step 2:
         * Try to acquire a lock on one vehicle.
         *
         * We don't lock all vehicles.
         * We try them one by one until we successfully acquire one.
         */
        for (Vehicle vehicle : availableVehicles) {

            boolean lockAcquired = vehicleLockManager.tryLock(vehicle.getLicensePlate(), userId, Duration.ofMinutes(5));

            if (!lockAcquired) {
                // Someone else currently has this vehicle locked.
                continue;
            }

            /*
             * From this point until unlock(), this user owns
             * the temporary lock on this vehicle.
             */
            try {

                /*
                 * Step 3:
                 * VERY IMPORTANT:
                 * Re-check availability after acquiring the lock.
                 *
                 * findAvailableVehicle() was only a snapshot.
                 */
                List<Booking> bookings = bookingRepository.getBookingsForVehicle(vehicle);

                boolean hasOverlap = false;

                for (Booking booking : bookings) {

                    if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                        continue;
                    }

                    if (isOverlapping(startTime, endTime, booking)) {
                        hasOverlap = true;
                        break;
                    }
                }

                if (hasOverlap) {
                    // Vehicle was booked between our initial
                    // availability check and lock acquisition.
                    continue;
                }

                /*
                 * Step 4:
                 * Create the booking.
                 */
                double amount = this.pricingStrategy.calculateAmount(
                                vehicle, startTime, endTime, distanceKm);

                Booking booking = Booking.getBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setStartTime(startTime)
                                .setEndTime(endTime)
                                .setPaymentStrategy(paymentStrategy)
                                .setPickUpBranch(pickUpBranch)
                                .setDropOffBranch(dropOffBranch)
                                .setVehicle(vehicle)
                                .setAmount(amount)
                                .build();

                this.bookingRepository.addBooking(booking);

                /*
                 * Step 5:
                 * Process payment while the vehicle is reserved.
                 */
                paymentStrategy.pay(booking);

                /*
                 * Step 6:
                 * Payment successful.
                 */
                booking.setPaymentStatus(PaymentStatus.SUCCESS);
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                vehicle.increamentBookings();
                System.out.println(booking);
                return Optional.of(booking);

            } catch (Exception e) {

                /*
                 * Payment failed or something else went wrong.
                 *
                 * Don't leave the vehicle permanently reserved.
                 */
                System.out.println("Booking failed for vehicle " + vehicle.getLicensePlate());

                // If a booking was created, mark it CANCELLED.
                // You can keep a reference to the booking and
                // update it here if your repository supports that.

                return Optional.empty();

            } finally {

                /*
                 * Step 7:
                 * ALWAYS release the temporary lock.
                 *
                 * This executes on:
                 * - successful payment
                 * - payment failure
                 * - exception
                 * - re-check failure
                 */
                vehicleLockManager.unlock(vehicle.getLicensePlate(), userId);
            }
        }

        System.out.println("Could not lock any available vehicle");
        return Optional.empty();
    }

    public void dropOffVehicle(String bookingId){
        Booking booking = this.bookingRepository.getBookingById(bookingId);
        Vehicle vehicle = booking.getVehicle();
        Branch pickUpBranch = booking.getPickUpBranch();
        Branch dropOffBranch = booking.getDropOffBranch();
        pickUpBranch.removeVehicle(vehicle);
        dropOffBranch.addVehicle(vehicle);
        booking.setBookingStatus(BookingStatus.COMPLETED);
    }
}