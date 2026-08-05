package repositories;

import models.Booking;
import models.Vehicle;

import java.util.*;

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

    public List<Booking> getBookingsForVehicle(Vehicle vehicle) {
        List<Booking> vehicleBookings = new ArrayList<>();
        for(String bookingId: this.bookingMap.keySet()){
            Booking booking = this.bookingMap.get(bookingId);
            if (Objects.equals(booking.getVehicle().getLicensePlate(), vehicle.getLicensePlate())){
                vehicleBookings.add(booking);
            }
        }

        return vehicleBookings;
    }
}
