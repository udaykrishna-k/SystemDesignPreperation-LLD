package models;

import enums.VehicleType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    @Getter
    private String spotId;
    @Getter
    private VehicleType supportedVehicleType;
    private AtomicBoolean isOccupied;

    public ParkingSpot(String spotId, VehicleType supportedVehicleType){
        this.spotId = spotId;
        this.supportedVehicleType = supportedVehicleType;
        this.isOccupied = new AtomicBoolean(false);
    }

    public Boolean getIsOccupied(){
        return this.isOccupied.get();
    }

    public void occupySpot(){
        this.isOccupied.compareAndSet(false, true);
    }

    public void freeSpot(){
        this.isOccupied.compareAndSet(true, false);
    }
}
