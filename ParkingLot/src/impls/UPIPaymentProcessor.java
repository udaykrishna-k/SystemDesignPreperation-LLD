package impls;

import enums.PaymentMode;
import interfaces.PaymentProcessor;

public class UPIPaymentProcessor implements PaymentProcessor {

    private final PaymentMode mode = PaymentMode.UPI;

    @Override
    public void pay(double amount) {
        System.out.println("Paying " + amount + " rupees via " + this.mode + " mode");
    }
}
