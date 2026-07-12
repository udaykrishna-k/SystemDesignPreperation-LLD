package models;
import lombok.Getter;

import java.util.*;

@Getter
public class Floor {
    private String floorId;
    private List<ParkingSpot> parkingSpots;

    public Floor(String floorId){
        this.floorId = floorId;
        this.parkingSpots = new ArrayList<>();
    }

    public void addParkingSpot(ParkingSpot parkingSpot){
        this.parkingSpots.add(parkingSpot);
    }
}
