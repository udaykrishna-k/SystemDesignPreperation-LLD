package models;

import enums.VehicleType;

public class Sedan extends Vehicle{

    public Sedan(String licensePlate, double pricePerKm, double pricePerHour) {
        super(licensePlate, pricePerKm, pricePerHour);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.SEDAN;
    }
}
