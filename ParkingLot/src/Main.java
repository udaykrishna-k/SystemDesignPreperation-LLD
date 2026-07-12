import enums.PaymentMode;
import enums.PricingStrategyType;
import enums.VehicleType;
import exceptions.InvalidPaymentMode;
import exceptions.InvalidPricingStrategy;
import factories.VehicleFactory;
import models.*;
import service.ParkingLot;

public class Main {
    public static void main(String[] args) throws InvalidPricingStrategy, InvalidPaymentMode {

        ParkingLot parkingLot = ParkingLot.getInstance();

        // Add floorOne to the parking Lot
        Floor floorOne = new Floor("F1");
        // Add 3 parking spots to floorOne
        floorOne.addParkingSpot(new ParkingSpot("F1S1", VehicleType.CAR));
        floorOne.addParkingSpot(new ParkingSpot("F1S2", VehicleType.BIKE));
        floorOne.addParkingSpot(new ParkingSpot("F1S3", VehicleType.TRUCK));
        parkingLot.addParkingFloor(floorOne);

        // Add floorTwo to parking Lot
        Floor floorTwo = new Floor("F2");
        // Add 2 parking spots to floorTwo
        floorTwo.addParkingSpot(new ParkingSpot("F2S1", VehicleType.CAR));
        floorTwo.addParkingSpot(new ParkingSpot("F2S2", VehicleType.TRUCK));
        parkingLot.addParkingFloor(floorTwo);

        // create entry gate
        EntryGate entryGate = new EntryGate("EN1");
        // create exit gate
        ExitGate exitGate = new ExitGate("EX1");

        //create a bike
        Vehicle bike1 = VehicleFactory.createVehicle("AP39RH3838", VehicleType.BIKE);
        Vehicle bike2 = VehicleFactory.createVehicle("AP21BH0127", VehicleType.BIKE);
        Vehicle car = VehicleFactory.createVehicle("AP21CD3006", VehicleType.CAR);

        // park the bike

        Ticket ticketForBikeOne = entryGate.parkVehicle(bike1);
        Ticket ticketForBikeTwo = entryGate.parkVehicle(bike2);

        exitGate.unparkVehicle(ticketForBikeOne, PaymentMode.CARD);

        ticketForBikeTwo = entryGate.parkVehicle(bike1);
        exitGate.unparkVehicle(ticketForBikeTwo, PaymentMode.UPI);

        Thread thread1 = new Thread(() -> entryGate.parkVehicle(bike1));
        Thread thread2 = new Thread(() -> entryGate.parkVehicle(bike2));

        thread1.start();
        thread2.start();

    }
}