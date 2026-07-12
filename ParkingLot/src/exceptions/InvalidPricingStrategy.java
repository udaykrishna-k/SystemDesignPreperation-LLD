package exceptions;

public class InvalidPricingStrategy extends Exception{
    public InvalidPricingStrategy(String message){
        super(message);
    }
}
