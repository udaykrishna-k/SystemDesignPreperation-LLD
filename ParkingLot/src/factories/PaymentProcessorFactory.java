package factories;

import enums.PaymentMode;
import exceptions.InvalidPaymentMode;
import impls.CardPaymentProcessor;
import impls.CashPaymentProcessor;
import impls.UPIPaymentProcessor;
import interfaces.PaymentProcessor;

public class PaymentProcessorFactory {
    public static PaymentProcessor createPaymentProcessor(PaymentMode paymentMode) throws InvalidPaymentMode {
        if (paymentMode == PaymentMode.UPI){
            return new UPIPaymentProcessor();
        }
        else if (paymentMode == PaymentMode.CARD){
            return new CardPaymentProcessor();
        }
        else if (paymentMode == PaymentMode.CASH){
            return new CashPaymentProcessor();
        }
        else{
            throw new InvalidPaymentMode("Invalid Payment Mode");
        }
    }
}
