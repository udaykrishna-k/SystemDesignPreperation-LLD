package interfaces;

import models.Ticket;

public interface PricingStrategy {
    double calculateAmount(Ticket ticket);
}
