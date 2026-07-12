package models;

import enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Vehicle {
    private String vehicleNumber;
    private VehicleType vehicleType;
}
