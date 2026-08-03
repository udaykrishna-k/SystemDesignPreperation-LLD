package strategies;

import models.Booking;

public class CashPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(Booking booking) {
        System.out.println("Paying " + booking.getAmount() + " via CASH");
    }
}
