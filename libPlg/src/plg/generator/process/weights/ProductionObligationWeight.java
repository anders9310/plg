package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;
import plg.generator.process.Obligation;
import plg.generator.process.RandomizationPattern;
import plg.model.Process;

public class ProductionObligationWeight extends Weight{
    private int productionPotentialIncrease;
    private Obligation obligation;
    private RandomizationPattern randomizationPattern;
    private double POTENTIAL_THRESHOLD = 1;
    private Process process;

    public ProductionObligationWeight(RandomizationPattern randomizationPattern, Obligation obligation){
        this.randomizationPattern = randomizationPattern;
        this.obligation = obligation;
        this.process = obligation.getProcess();
        cacheProductionPotential();
    }

    private void cacheProductionPotential() {
        productionPotentialIncrease = BaseWeights.BASE_WEIGHTS.getBasePotential(this.randomizationPattern);
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(obligation.getMean() == 0){
            return 0;
        }
        double metricContribution = process.getContribution(this.obligation.getType(), this.randomizationPattern);
        double targetValue = obligation.getTargetValue();
        double currentValue = obligation.getCurrentValue();
        double currentPotential = obligation.getPotential();
        double returnValue;

        double terminalWish;
        if(currentValue<targetValue){//increase
            if(metricContribution>0){
                terminalWish = 1;
            }else if(metricContribution==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }else if(currentValue==targetValue){//Stay the same
            if(metricContribution == 0){
                terminalWish = 1;
            }else{
                terminalWish = -1;
            }
        }else{//decrease
            if(metricContribution<0){
                terminalWish = 1;
            }else if(metricContribution==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }

        double potentialWish;
        if(currentValue<targetValue){//increase
            if(productionPotentialIncrease >0){
                if(currentPotential<=POTENTIAL_THRESHOLD){
                    return 1;
                }
                potentialWish = 1;
            }else if(productionPotentialIncrease ==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }else{//decrease
            if(productionPotentialIncrease <0){
                potentialWish = 1;
            }else if(productionPotentialIncrease ==0){
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
        return sum;
    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
}
