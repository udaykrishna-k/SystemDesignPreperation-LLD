package strategies.booking;

import models.Branch;
import models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStrategy {
    public Vehicle bookVehicle(List<Vehicle> vehicles, Branch pickUpbranch, LocalDateTime startTime, LocalDateTime endTime);
}
