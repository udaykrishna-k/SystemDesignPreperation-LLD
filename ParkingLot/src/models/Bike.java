package models;

import enums.VehicleType;

public class Bike extends Vehicle{
    private String vehicleNumber;
    private VehicleType vehicleType;
    public Bike(String vehicleNumber){
        super(vehicleNumber, VehicleType.BIKE);
    }
}
