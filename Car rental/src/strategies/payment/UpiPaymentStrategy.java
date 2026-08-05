package strategies.payment;

import models.Booking;

public class UpiPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(Booking booking) {
        System.out.println("Paying " + booking.getAmount() + " via UPI");
    }
}
