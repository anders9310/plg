package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.Process;

public class ProductionObligationWeight extends Weight{
    private Obligation obligation;
    private RandomizationPattern randomizationPattern;
    private double POTENTIAL_THRESHOLD = 1;
    private Process process;

    public ProductionObligationWeight(RandomizationPattern randomizationPattern, Obligation obligation){
        this.randomizationPattern = randomizationPattern;
        this.obligation = obligation;
        this.process = obligation.getProcess();
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(obligation.getMean() == 0){
            return 0;
        }

        double metricContribution = process.getContributionOf(state, this.obligation.getType(), randomizationPattern);;
        double targetValue = obligation.getTargetValue();
        double currentValue = obligation.getCurrentValue();
        double currentPotential = process.getNumUnknownComponents();
        double potentialIncrease = process.getPotentialIncreaseOf(randomizationPattern);

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
        if(currentValue<targetValue && currentPotential<=2.0*POTENTIAL_THRESHOLD){//increase
            if(potentialIncrease >0){
                if(currentPotential<=POTENTIAL_THRESHOLD){
                    return 1;
                }
                potentialWish = 1;
            }else if(potentialIncrease ==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }else{//decrease
            if(potentialIncrease <0){
                potentialWish = 1;
            }else if(potentialIncrease ==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }

        return terminalWish + potentialWish;
    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
}
