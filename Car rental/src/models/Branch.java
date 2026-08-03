package models;

import enums.VehicleType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Branch {
    private final String id;
    private final String city;
    private final List<Vehicle> vehicles;

    public Branch(String id, String city) {
        this.id = id;
        this.city = city;
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        this.vehicles.remove(vehicle);
    }

    public List<Vehicle> getVehiclesByType(VehicleType vehicleType){
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle: this.vehicles){
            if (vehicle.getVehicleType() == vehicleType){
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
}
