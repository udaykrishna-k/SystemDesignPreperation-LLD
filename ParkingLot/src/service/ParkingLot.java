package service;

import enums.PaymentMode;
import enums.PricingStrategyType;
import exceptions.InvalidPaymentMode;
import exceptions.InvalidPricingStrategy;
import factories.PaymentProcessorFactory;
import factories.PricingStrategyFactory;
import interfaces.PaymentProcessor;
import interfaces.PricingStrategy;
import models.Floor;
import models.ParkingSpot;
import models.Ticket;
import models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkingLot {

    private static final ParkingLot INSTANCE = new ParkingLot();
    private final PricingStrategy pricingStrategy;
    private List<Floor> parkingFloors;

    private ParkingLot(){
        try {
            this.pricingStrategy = PricingStrategyFactory.createPricingStrategy(PricingStrategyType.TIME_BASED);
        } catch (InvalidPricingStrategy e) {
            throw new RuntimeException(e);
        }
        this.parkingFloors = new ArrayList<>();
    }

    public static ParkingLot getInstance(){
        return INSTANCE;
    }

    public void addParkingFloor(Floor parkingFloor){
        this.parkingFloors.add(parkingFloor);
    }

    public Ticket parkVehicle(Vehicle vehicle){
        for (Floor floor: parkingFloors) {
            for (ParkingSpot parkingSpot : floor.getParkingSpots()) {
                if (parkingSpot.getSupportedVehicleType() == vehicle.getVehicleType() && !parkingSpot.getIsOccupied()) {
                    String ticketId = UUID.randomUUID().toString();
                    Ticket ticket = new Ticket(ticketId, vehicle, parkingSpot);
                    parkingSpot.occupySpot();
                    System.out.println("Parked vehicle " + vehicle.getVehicleNumber());
                    return ticket;
                }
            }
        }
        System.out.println("No parking spot found for parking the vehicle " + vehicle.getVehicleNumber());
        return null;
    }

    public void unParkVehicle(Ticket ticket, PaymentMode paymentMode) throws InvalidPaymentMode {
        PaymentProcessor paymentProcessor = PaymentProcessorFactory.createPaymentProcessor(paymentMode);
        double amount = this.pricingStrategy.calculateAmount(ticket);
        paymentProcessor.pay(amount);
        ticket.getParkingSpot().freeSpot();
    }
}
