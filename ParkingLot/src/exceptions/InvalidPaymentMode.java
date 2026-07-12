package exceptions;

public class InvalidPaymentMode extends Exception{

    public InvalidPaymentMode(String message) {
        super(message);
    }
}
