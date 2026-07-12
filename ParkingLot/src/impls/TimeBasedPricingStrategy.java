package impls;

import enums.PricingStrategyType;
import enums.VehicleType;
import interfaces.PricingStrategy;
import models.Ticket;

import java.util.Date;

public class TimeBasedPricingStrategy implements PricingStrategy {

    private final PricingStrategyType pricingStrategyType = PricingStrategyType.TIME_BASED;
    @Override
    public double calculateAmount(Ticket ticket) {
        Date entryTime = ticket.getEntryTime();
        Date exitTime = new Date(); // Current time

        long durationInMillis = exitTime.getTime() - entryTime.getTime();

        long durationInHours = (long) Math.ceil(durationInMillis / (1000.0 * 60 * 60));

        // Charge for at least one hour
        durationInHours = Math.max(1, durationInHours);

        double hourlyRate = this.getHourlyRate(ticket.getVehicle().getVehicleType());

        return durationInHours * hourlyRate;
    }

    private double getHourlyRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> 10;
            case CAR -> 20;
            case TRUCK -> 30;
        };
    }
}
