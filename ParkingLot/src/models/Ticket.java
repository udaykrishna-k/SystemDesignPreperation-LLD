package models;

import lombok.Getter;

import java.util.Date;

@Getter
public class Ticket {
    private String ticketId;
    private Vehicle vehicle;
    private Date entryTime;
    private ParkingSpot parkingSpot;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot parkingSpot){
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = new Date();
    }
}
