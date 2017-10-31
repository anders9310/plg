package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;

public class ProductionObligationWeight extends Weight{
    private int productionTerminals;
    private int productionPotential;
    private Obligation obligation;
    private RandomizationPattern randomizationPattern;
    private double POTENTIAL_THRESHOLD = 2;

    public ProductionObligationWeight(RandomizationPattern randomizationPattern, Obligation obligation){
        this.randomizationPattern = randomizationPattern;
        this.obligation = obligation;
        cacheProductionContributions();
    }

    private void cacheProductionContributions() {
        productionTerminals = ProductionRuleContributions.CONTRIBUTIONS.getContribution(this.randomizationPattern, this.obligation.getType());
        productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(this.randomizationPattern, this.obligation.getType());
    }

    protected double calculateValue() {
        if(obligation.getMean() == 0){
            return 0;
        }
        double targetValue = obligation.getTargetValue();
        double currentValue = obligation.getCurrentValue();
        double currentPotential = obligation.getPotential();
        double currentSize = obligation.getCurrentValue() + obligation.getPotential();
        double returnValue;

        double terminalWish;
        if(currentValue<targetValue){//increase
            if(productionTerminals>0){
                terminalWish = 1;
            }else if(productionTerminals==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }else if(currentValue==targetValue){//Stay the same
            if(productionTerminals == 0){
                terminalWish = 1;
            }else{
                terminalWish = -1;
            }
        }else{//decrease
            if(productionTerminals<0){
                terminalWish = 1;
            }else if(productionTerminals==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }

        double potentialWish;
        if(currentValue<targetValue){//increase
            if(productionPotential>0){
                if(currentPotential<POTENTIAL_THRESHOLD){
                    return 1;
                }
                potentialWish = 1;
            }else if(productionPotential==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }else{//decrease
            if(productionPotential<0){
                potentialWish = 1;
            }else if(productionPotential==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }

        double sum = terminalWish + potentialWish;
        if(sum>0){
            returnValue = 1;
        }else if(sum < 0){
            returnValue = -1;
        }else{
            returnValue=0;
        }
        return returnValue;
    }

    private double calcSizeContribution(double productionTerminals, double productionPotential){
        return productionTerminals + productionPotential;
    }

    private double calcValuePotential(double coreValuePotential){
        if(obligation.getRemaining()-obligation.getPotential()>=0.0){
            return coreValuePotential;
        }else{
            return 1 / ( coreValuePotential);
        }
    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
}
