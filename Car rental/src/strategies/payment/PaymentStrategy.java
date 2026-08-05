package strategies.payment;

import models.Booking;

public interface PaymentStrategy {
    public void pay(Booking booking);
}
