package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;

public class ProductionObligationWeight extends Weight{
    private int productionTerminals;
    private int productionPotential;
    private Obligation obligation;
    private RandomizationPattern productionPattern;
    private double zeroIntersectionValue = 1;

    public ProductionObligationWeight(RandomizationPattern productionPattern, Obligation obligation){
        this.productionPattern = productionPattern;
        this.obligation = obligation;
        cacheProductionContributions();
    }

    private void cacheProductionContributions() {
        productionTerminals = ProductionRuleContributions.CONTRIBUTIONS.getContribution(this.productionPattern, this.obligation.getType());
        productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(this.productionPattern, this.obligation.getType());
    }

    protected double calculateValue() {
        if(obligation.getMean() == 0){
            return 0;
        }
        int targetValue = obligation.getTargetValue();
        int currentSize = obligation.getTerminals() + obligation.getPotential();
        int productionSizeContribution = calcSizeContribution(productionTerminals, productionPotential);

        if(currentSize<targetValue){
            //increase
            if(productionSizeContribution>0 && productionPotential>0){
                return 1;
            }else if(productionSizeContribution>0){
                throw new RuntimeException("Cannot handle the type of production rule: " + productionPattern.name());
            }else{
                return 0;
            }
        }else if(currentSize==targetValue){
            //Stay the same
            if(productionSizeContribution == 0){
                return 1;
            }else{
                return 0;
            }
        }else{//decrease
            if(productionSizeContribution<0){
                return 1;
            }else{
                return 0;
            }
        }
    }

    private int calcSizeContribution(int productionTerminals, int productionPotential){
        return productionTerminals + productionPotential;
    }

    private double calcValuePotential(double coreValuePotential){
        if(obligation.getRemaining()-obligation.getPotential()>=0.0){
            return coreValuePotential;
        }else{
            return 1 / ( coreValuePotential);
        }
    }

    /*private double getExpValue(){
        return Math.exp(baseWeight * obligation.getRemaining()) + zeroIntersectionValue-1;
    }*/
}
