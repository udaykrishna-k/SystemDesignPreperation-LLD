package strategies.pricing;

import models.Vehicle;

import java.time.LocalDateTime;

public interface PricingStrategy {
    public double calculateAmount(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime, double distance);
}
