package factories;

import enums.PaymentType;
import strategies.CardPaymentStrategy;
import strategies.CashPaymentStrategy;
import strategies.PaymentStrategy;
import strategies.UpiPaymentStrategy;

public class PaymentFactory {
    public static PaymentStrategy createPaymentStrategy(PaymentType paymentType){
        return switch (paymentType){
            case UPI -> new UpiPaymentStrategy();
            case CASH -> new CashPaymentStrategy();
            case CARD -> new CardPaymentStrategy();
        };
    }
}
