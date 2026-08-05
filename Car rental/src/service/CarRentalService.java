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
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    public void addBranch(Branch branch){
        this.branchRepository.addBranch(branch);
    }

    public void addBooking(Booking booking){
        this.bookingRepository.addBooking(booking);
    }

    public Optional<Booking> bookVehicle(String pickUpBranchId, String dropOffBranchId, VehicleType vehicleType,
                                         LocalDateTime startTime, LocalDateTime endTime, double distanceKm,
                                         PaymentStrategy paymentStrategy){
        Branch pickUpbranch = this.branchRepository.getBranchById(pickUpBranchId);
        Branch dropOffBranch = this.branchRepository.getBranchById(dropOffBranchId);

        List<Vehicle> availableVehicles = pickUpbranch.getVehiclesByType(vehicleType);
        if (availableVehicles.isEmpty()){
            System.out.println("No available vehicles of type " + vehicleType);
            return Optional.empty();
        }
        Optional<Vehicle> vehicleBooked = this.bookingStrategy.bookVehicle(availableVehicles, pickUpbranch, startTime, endTime);
        if (vehicleBooked.isEmpty()){
            System.out.println("No available vehicles of type " + vehicleType);
            return Optional.empty();
        }
        Vehicle vehicle = vehicleBooked.get();

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
        vehicle.getTempLock().compareAndSet(false, true);
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