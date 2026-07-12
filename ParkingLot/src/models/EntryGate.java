package models;

import enums.GateType;
import service.ParkingLot;

public class EntryGate extends Gate {

    public EntryGate(String gateId){
        super(gateId, GateType.ENTRY);
    }

    public Ticket parkVehicle(Vehicle vehicle){
        return ParkingLot.getInstance().parkVehicle(vehicle);
    }

}
