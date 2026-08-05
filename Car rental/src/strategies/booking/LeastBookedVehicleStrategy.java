package strategies.booking;

import models.Branch;
import models.Vehicle;
import repositories.BookingRepository;
import strategies.booking.BookingStrategy;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeastBookedVehicleStrategy extends BaseBookingStrategy implements BookingStrategy {

    public LeastBookedVehicleStrategy(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Optional<Vehicle> bookVehicle(List<Vehicle> vehicles, Branch pickUpbranch, LocalDateTime startTime, LocalDateTime endTime) {
        Comparator<Vehicle> comp = new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                if (o1.getNumberOfBookings() > o2.getNumberOfBookings()){
                    return 1;
                }
                else if (o1.getNumberOfBookings() < o2.getNumberOfBookings()){
                    return -1;
                }
                else {
                    return 0;
                }
            }
        };
        vehicles.sort(comp);
        for (Vehicle vehicle: vehicles){
            if (this.canBookVehicle(vehicle, pickUpbranch, startTime, endTime) && vehicle.getTempLock().compareAndSet(true, false)){
                return Optional.of(vehicle);
            }
        }
        return Optional.empty();
    }
}
