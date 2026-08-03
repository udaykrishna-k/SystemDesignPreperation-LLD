package strategies;

import models.Vehicle;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeastPricedVehicleStrategy implements BookingStrategy{
    @Override
    public Optional<Vehicle> bookVehicle(List<Vehicle> vehicles) {
        Comparator<Vehicle> comp = new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                if (o1.getPricePerKm() > o2.getPricePerKm()){
                    return 1;
                }
                else if (o1.getPricePerKm() < o2.getPricePerKm()){
                    return -1;
                }
                else {
                    return 0;
                }
            }
        };
        vehicles.sort(comp);
        for (Vehicle vehicle: vehicles){
            if (vehicle.getIsAvailable().compareAndSet(true, false)){
                return Optional.of(vehicle);
            }
        }
        return Optional.empty();
    }
}
