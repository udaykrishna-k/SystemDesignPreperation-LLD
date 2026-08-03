package repositories;

import models.Booking;

import java.util.HashMap;
import java.util.Map;

public class BookingRepository {

    private final Map<String, Booking> bookingMap;

    public BookingRepository() {
        this.bookingMap = new HashMap<>();
    }

    public Booking getBookingById(String id){
        return this.bookingMap.get(id);
    }

    public Booking addBooking(Booking booking){
        this.bookingMap.put(booking.getId(), booking);
        return booking;
    }
}
