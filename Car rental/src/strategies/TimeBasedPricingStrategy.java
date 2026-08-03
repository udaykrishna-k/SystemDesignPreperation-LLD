package strategies;

import models.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeBasedPricingStrategy implements PricingStrategy{
    @Override
    public double calculateAmount(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime, double distance) {
        double duration = Math.max(1, Duration.between(startTime, endTime).toHours());
        return vehicle.getPricePerHour() * duration;
    }
}
