package factories;

import enums.PaymentType;
import strategies.payment.CardPaymentStrategy;
import strategies.payment.CashPaymentStrategy;
import strategies.payment.PaymentStrategy;
import strategies.payment.UpiPaymentStrategy;

public class PaymentFactory {
    public static PaymentStrategy createPaymentStrategy(PaymentType paymentType){
        return switch (paymentType){
            case UPI -> new UpiPaymentStrategy();
            case CASH -> new CashPaymentStrategy();
            case CARD -> new CardPaymentStrategy();
        };
    }
}
