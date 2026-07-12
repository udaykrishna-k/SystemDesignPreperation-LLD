package factories;

import enums.VehicleType;
import models.Bike;
import models.Car;
import models.Truck;
import models.Vehicle;

public class VehicleFactory {

    public static Vehicle createVehicle(String vehicleNumber, VehicleType vehicleType){
        return switch (vehicleType){
            case VehicleType.BIKE -> new Bike(vehicleNumber);
            case VehicleType.CAR -> new Car(vehicleNumber);
            case VehicleType.TRUCK -> new Truck(vehicleNumber);
        };
    }
}
