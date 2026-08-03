package models;

import enums.VehicleType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class Vehicle {
    private final String licensePlate;
    private final double pricePerKm;
    private final double pricePerHour;
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);
    private int numberOfBookings;

    public Vehicle(String licensePlate, double pricePerKm, double pricePerHour){
        this.licensePlate = licensePlate;
        this.pricePerKm = pricePerKm;
        this.pricePerHour = pricePerHour;
    }

    public abstract VehicleType getVehicleType();

    public void increamentBookings(){
        this.numberOfBookings += 1;
    }
}
