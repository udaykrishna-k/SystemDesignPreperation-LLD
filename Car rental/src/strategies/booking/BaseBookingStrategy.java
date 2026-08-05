package strategies.booking;

import enums.BookingStatus;
import models.Booking;
import models.Branch;
import models.Vehicle;
import repositories.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

public abstract class BaseBookingStrategy {
    private final BookingRepository bookingRepository;

    public BaseBookingStrategy(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    private boolean isOverlapping(
            LocalDateTime requestStart,
            LocalDateTime requestEnd,
            Booking booking) {

        return requestStart.isBefore(booking.getEndTime()) &&
                requestEnd.isAfter(booking.getStartTime());
    }

    protected boolean canBookVehicle(
            Vehicle vehicle,
            Branch pickupBranch,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        List<Booking> bookings = this.bookingRepository.getBookingsForVehicle(vehicle);

        Branch currentBranch = pickupBranch;

        for (Booking booking : bookings) {

            if (booking.getBookingStatus() == BookingStatus.COMPLETED ||
                    booking.getBookingStatus() == BookingStatus.CANCELLED) {
                continue;
            }

            if (isOverlapping(startTime, endTime, booking)) {
                return false;
            }

            // Keep track of the vehicle's location
            if (booking.getEndTime().isBefore(startTime)) {
                currentBranch = booking.getDropOffBranch();
            }
        }

        return currentBranch.equals(pickupBranch);
    }
}
