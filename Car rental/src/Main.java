import enums.VehicleType;
import factories.VehicleFactory;
import models.Booking;
import models.Branch;
import models.Vehicle;
import repositories.BookingRepository;
import repositories.BranchRepository;
import service.CarRentalService;
import strategies.booking.BookingStrategy;
import strategies.booking.LeastBookedVehicleStrategy;
import strategies.payment.CashPaymentStrategy;
import strategies.payment.PaymentStrategy;
import strategies.pricing.PricingStrategy;
import strategies.pricing.TimeBasedPricingStrategy;

import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        System.out.println("Rent a Car!");

        BranchRepository branchRepository = new BranchRepository();
        BookingRepository bookingRepository = new BookingRepository();

        Branch branch1 = new Branch("Branch1", "Kurnool");
        Branch branch2 = new Branch("Branch2", "Hyderabad");

        Vehicle sedan1 = VehicleFactory.createVehicle(VehicleType.SEDAN, "AP39RH1234", 8, 10);
        Vehicle sedan2 = VehicleFactory.createVehicle(VehicleType.SEDAN, "AP39RH3434", 8.5, 12.5);
        Vehicle hatchBack1 = VehicleFactory.createVehicle(VehicleType.HATCHBACK, "AP21cd3006", 7, 9.5);
        Vehicle suv1 = VehicleFactory.createVehicle(VehicleType.SUV, "AP21CD4758", 10.5, 14.5);

        branch1.addVehicle(sedan1);
        branch1.addVehicle(sedan2);
        branch2.addVehicle(suv1);
        branch2.addVehicle(hatchBack1);

        branchRepository.addBranch(branch1);
        branchRepository.addBranch(branch2);

        PaymentStrategy paymentStrategy = new CashPaymentStrategy();
        PricingStrategy pricingStrategy = new TimeBasedPricingStrategy();
        BookingStrategy bookingStrategy = new LeastBookedVehicleStrategy();

        CarRentalService carRentalService = CarRentalService.getInstance(branchRepository, bookingRepository,
                pricingStrategy, bookingStrategy);

        LocalDateTime startTime = LocalDateTime.of(2026, 7, 1, 0,0,0);
        LocalDateTime endTime = LocalDateTime.of(2026, 7, 2, 0,0,0);

        Optional<Booking> booking1 = carRentalService.bookVehicle("Branch1", "Branch1", VehicleType.SEDAN,
                startTime, endTime, 100, paymentStrategy);

        if (booking1.isPresent()){
            System.out.println("Booking1 is successful");
            System.out.println();
        }
        else{
            System.out.println("No Vehicle found for booking1");
        }

        Optional<Booking> booking2 = carRentalService.bookVehicle("Branch1", "Branch1", VehicleType.SEDAN,
                startTime, endTime, 100, paymentStrategy);

        if (booking2.isPresent()){
            System.out.println("Booking2 is successful");
            System.out.println();
        }
        else{
            System.out.println("No Vehicle found for booking2");
        }

        Optional<Booking> booking3 = carRentalService.bookVehicle("Branch1", "Branch1", VehicleType.SEDAN,
                startTime, endTime, 100, paymentStrategy);

        if (booking3.isPresent()){
            System.out.println("Booking3 is successful");
            System.out.println();
        }
        else{
            System.out.println("No Vehicle found for booking3");
        }

        Optional<Booking> booking4 = carRentalService.bookVehicle("Branch1", "Branch1", VehicleType.HATCHBACK,
                startTime, endTime, 100, paymentStrategy);

        if (booking4.isPresent()){
            System.out.println("Booking4 is successful");
            System.out.println();
        }
        else{
            System.out.println("No Vehicle found for booking4");
        }

        Thread t1 = new Thread(() -> carRentalService.bookVehicle("Branch2", "Branch1", VehicleType.HATCHBACK,
                startTime, endTime, 100, paymentStrategy));


        Thread t2 = new Thread(() -> carRentalService.bookVehicle("Branch2", "Branch1", VehicleType.HATCHBACK,
                startTime, endTime, 100, paymentStrategy));

        t1.start();
        t2.start();
    }
}