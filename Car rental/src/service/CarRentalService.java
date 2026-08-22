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

import java.time.LocalDateTime;
import java.util.*;


public class CarRentalService {
    private static CarRentalService instance = null;
    @Setter
    private BookingStrategy bookingStrategy;
    private BookingRepository bookingRepository;
    private BranchRepository branchRepository;
    private PricingStrategy pricingStrategy;

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
            LocalDateTime endTime) {

        List<Vehicle> vehicles = pickupBranch.getVehiclesByType(vehicleType);
        List<Vehicle> availableVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
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

    public Optional<Booking> bookVehicle(String pickUpBranchId, String dropOffBranchId, VehicleType vehicleType,
                                         LocalDateTime startTime, LocalDateTime endTime, double distanceKm,
                                         PaymentStrategy paymentStrategy){
        Branch pickUpbranch = this.branchRepository.getBranchById(pickUpBranchId);
        Branch dropOffBranch = this.branchRepository.getBranchById(dropOffBranchId);

        List<Vehicle> branchVehicles = pickUpbranch.getVehiclesByType(vehicleType);
        if (branchVehicles.isEmpty()){
            System.out.println("No available vehicles of type " + vehicleType);
            return Optional.empty();
        }
        List<Vehicle> availableVehicles = this.findAvailableVehicle(vehicleType, pickUpbranch, startTime, endTime);
        if (availableVehicles.isEmpty()){
            System.out.println("No available vehicles of type " + vehicleType);
            return Optional.empty();
        }
        Vehicle vehicle = this.bookingStrategy.bookVehicle(availableVehicles, pickUpbranch, startTime, endTime);

        double amount = this.pricingStrategy.calculateAmount(vehicle, startTime, endTime, distanceKm);

        Booking booking = Booking.getBuilder().setId(UUID.randomUUID().toString())
                .setStartTime(startTime)
                .setEndTime(endTime).setPaymentStrategy(paymentStrategy)
                .setPickUpBranch(pickUpbranch)
                .setDropOffBranch(dropOffBranch)
                .setVehicle(vehicle)
                .setAmount(amount).build();

        paymentStrategy.pay(booking);
        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        this.bookingRepository.addBooking(booking);
        vehicle.increamentBookings();
        System.out.println(booking);
        return Optional.of(booking);
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