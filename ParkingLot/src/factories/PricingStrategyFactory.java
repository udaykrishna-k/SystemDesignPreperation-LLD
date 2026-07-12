package factories;

import enums.PricingStrategyType;
import exceptions.InvalidPricingStrategy;
import impls.EventBasedPricingStrategy;
import impls.TimeBasedPricingStrategy;
import interfaces.PricingStrategy;

public class PricingStrategyFactory {

    public static PricingStrategy createPricingStrategy(PricingStrategyType pricingStrategyType) throws InvalidPricingStrategy {
        if (pricingStrategyType == PricingStrategyType.TIME_BASED){
            return new TimeBasedPricingStrategy();
        }
        else if (pricingStrategyType == PricingStrategyType.EVENT_BASED){
            return new EventBasedPricingStrategy();
        }
        else{
            throw new InvalidPricingStrategy("Invalid Pricing Strategy selected");
        }
    }
}
