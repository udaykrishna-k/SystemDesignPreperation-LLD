package models;

import enums.VehicleType;

public class HatchBack extends Vehicle{
    public HatchBack(String licensePlate, double pricePerKm, double pricePerHour) {
        super(licensePlate, pricePerKm, pricePerHour);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.HATCHBACK;
    }
}
