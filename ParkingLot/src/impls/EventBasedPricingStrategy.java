package impls;

import enums.PricingStrategyType;
import enums.VehicleType;
import interfaces.PricingStrategy;
import models.Ticket;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class EventBasedPricingStrategy implements PricingStrategy {

    private final PricingStrategyType pricingStrategyType = PricingStrategyType.EVENT_BASED;

    private static final LocalTime PEAK_START = LocalTime.of(19, 0);
    private static final LocalTime PEAK_END = LocalTime.of(21, 0);

    @Override
    public double calculateAmount(Ticket ticket) {

        ZonedDateTime entry = ticket.getEntryTime()
                .toInstant()
                .atZone(ZoneId.systemDefault());

        ZonedDateTime exit = Instant.now()
                .atZone(ZoneId.systemDefault());

        double hourlyRate = getHourlyRate(ticket.getVehicle().getVehicleType());

        long peakHours = calculatePeakHours(entry, exit);

        long totalHours = Math.max(
                1,
                (long) Math.ceil(
                        ChronoUnit.MINUTES.between(entry, exit) / 60.0));

        long normalHours = totalHours - peakHours;

        return normalHours * hourlyRate +
                peakHours * hourlyRate * 2;
    }

    private long calculatePeakHours(ZonedDateTime entry,
                                    ZonedDateTime exit) {

        LocalDate date = entry.toLocalDate();

        ZonedDateTime peakStart =
                ZonedDateTime.of(date, PEAK_START, entry.getZone());

        ZonedDateTime peakEnd =
                ZonedDateTime.of(date, PEAK_END, entry.getZone());

        ZonedDateTime overlapStart =
                entry.isAfter(peakStart) ? entry : peakStart;

        ZonedDateTime overlapEnd =
                exit.isBefore(peakEnd) ? exit : peakEnd;

        if (!overlapEnd.isAfter(overlapStart)) {
            return 0;
        }

        return (long) Math.ceil(ChronoUnit.MINUTES.between(overlapStart, overlapEnd) / 60.0);
    }

    private double getHourlyRate(VehicleType type) {
        return switch (type) {
            case BIKE -> 10;
            case CAR -> 20;
            case TRUCK -> 30;
        };
    }
}
