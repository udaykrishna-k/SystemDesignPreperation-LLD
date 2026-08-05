package strategies.booking;

import models.Branch;
import models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStrategy {
    public Optional<Vehicle> bookVehicle(List<Vehicle> vehicles, Branch pickUpbranch, LocalDateTime startTime, LocalDateTime endTime);
}
