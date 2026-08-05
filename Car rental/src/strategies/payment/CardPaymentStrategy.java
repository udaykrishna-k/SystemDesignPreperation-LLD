package strategies.payment;

import models.Booking;

public class CardPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(Booking booking) {
        System.out.println("Paying " + booking.getAmount() + " via CARD");
    }
}
