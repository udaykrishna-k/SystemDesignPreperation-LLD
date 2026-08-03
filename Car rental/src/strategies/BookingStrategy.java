package strategies;

import models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface BookingStrategy {
    public Optional<Vehicle> bookVehicle(List<Vehicle> vehicles);
}
