package factories;

import enums.VehicleType;
import models.HatchBack;
import models.SUV;
import models.Sedan;
import models.Vehicle;

public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType vehicleType, String licensePlate, double pricePerKm, double pricePerHour){
        return switch (vehicleType){
            case SEDAN -> new Sedan(licensePlate, pricePerKm, pricePerHour);
            case SUV -> new SUV(licensePlate, pricePerKm, pricePerHour);
            case HATCHBACK -> new HatchBack(licensePlate, pricePerKm, pricePerHour);
        };
    }
}
