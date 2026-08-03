package strategies;

import models.Booking;

public interface PaymentStrategy {
    public void pay(Booking booking);
}
