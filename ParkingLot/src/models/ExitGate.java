package models;

import enums.GateType;
import enums.PaymentMode;
import exceptions.InvalidPaymentMode;
import exceptions.InvalidPricingStrategy;
import service.ParkingLot;

public class ExitGate extends Gate{

    public ExitGate(String gateId){
        super(gateId, GateType.EXIT);
    }

    public void unparkVehicle(Ticket ticket, PaymentMode paymentMode) throws InvalidPricingStrategy, InvalidPaymentMode {
        ParkingLot.getInstance().unParkVehicle(ticket, paymentMode);
    }
}
