package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;
import plg.utils.Logger;

public class ProductionObligationWeight extends Weight{
    private double baseWeightContribution;
    private double baseWeightPotential;
    private Obligation obligation;
    private RandomizationPattern productionPattern;
    private double zeroIntersectionValue = 1;

    public ProductionObligationWeight(RandomizationPattern productionPattern, Obligation obligation){
        this.productionPattern = productionPattern;
        this.obligation = obligation;
        initBaseWeight();
    }

    private void initBaseWeight() {
        baseWeightContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(this.productionPattern, this.obligation.getType());
        baseWeightPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(this.productionPattern, this.obligation.getType());
    }

    protected double calculateValue() {
        //return calcExpValue();
        double linearValue = getLinearValue();
        Logger.instance().debug("Weight value for obligation " + this.obligation.getType().name() + " with production " + this.productionPattern.name() + ": " + linearValue);
        return linearValue;
        //getSquareValue();
    }

    /*private double calcExpValue(){
        if(obligation.getValue()==0){
            return 0;
        }else{
            return getExpValue();
        }
    }*/

    private double getLinearValue(){
        if(obligation.getValue()==0){
            return 0;
        }
        double coreValueContribution = getLinearCoreContribution();
        double coreValuePotential = getLinearCorePotential();
        return calcValue(coreValueContribution, coreValuePotential);
    }

    /*private double getSquareValue(){
        double coreValue = getSquareCore();
        return calcValue(coreValue);
    }*/

    private double getSquareCore(){
        double coreValue = zeroIntersectionValue + baseWeightContribution * Math.pow(obligation.getRemaining(), 2);
        return coreValue;
    }

    private double getLinearCoreContribution(){
        double coreValueContribution = zeroIntersectionValue + baseWeightContribution * Math.abs(obligation.getRemaining());
        return coreValueContribution;
    }

    private double getLinearCorePotential(){
        double coreValuePotential = zeroIntersectionValue + baseWeightPotential * Math.abs(obligation.getRemaining()-obligation.getPotential());
        return coreValuePotential;
    }

    private double calcValue(double coreValueContribution, double coreValuePotential){
        double valueContribution = calcValueContribution(coreValueContribution);
        double valuePotential = calcValuePotential(coreValuePotential);

        //Logger.instance().debug("Contribution value for obligation " + this.obligation.getType().name() + " with production " + this.productionPattern.name() + ": " + valueContribution);
        //Logger.instance().debug("Potential value for obligation " + this.obligation.getType().name() + " with production " + this.productionPattern.name() + ": " + valuePotential);

        if(valueContribution==zeroIntersectionValue){
            return valuePotential;
        }else{
            return valueContribution;
        }
    }

    private double calcValueContribution(double coreValueContribution){
        if(obligation.getRemaining()>=0.0){
            return coreValueContribution;
        }else{
            return 1 / ( coreValueContribution);
        }
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
