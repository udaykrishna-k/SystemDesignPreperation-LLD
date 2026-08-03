package models;

import enums.VehicleType;

public class SUV extends Vehicle{
    public SUV(String licensePlate, double pricePerKm, double pricePerHour) {
        super(licensePlate, pricePerKm, pricePerHour);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.SUV;
    }
}
