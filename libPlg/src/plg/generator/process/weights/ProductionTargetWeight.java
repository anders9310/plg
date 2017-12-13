package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.Process;

public class ProductionTargetWeight extends Weight{
    private final double POTENTIAL_THRESHOLD = 1;
    private final double VALUE_GRANULARITY = 0.01;

    private Target target;
    private RandomizationPattern randomizationPattern;
    private Process process;

    public ProductionTargetWeight(RandomizationPattern randomizationPattern, Target target){
        this.randomizationPattern = randomizationPattern;
        this.target = target;
        this.process = target.getProcess();
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(target.getMean() == 0){
            return 0;
        }

        double metricContribution = process.getContributionOf(state, this.target.getType(), randomizationPattern);;
        double targetValue = target.getTargetValue();
        double currentValue = target.getCurrentValue();
        double currentPotential = process.getNumUnknownComponents();
        double potentialIncrease = process.getPotentialIncreaseOf(randomizationPattern);

        double terminalWish;
        int comparedValue = compare(currentValue, targetValue, VALUE_GRANULARITY/2);
        if(comparedValue==-1){//increase
            if(metricContribution>0){
                terminalWish = 1;
            }else if(metricContribution==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }else if(comparedValue==0){//Stay the same
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
        if(comparedValue==-1 && currentPotential<=2.0*POTENTIAL_THRESHOLD){//increase
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
        } else{//decrease
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

    public static int compare(double value, double comparedTo, double threshold){
        if(value > comparedTo + threshold){
            return 1;
        }else if(value < comparedTo - threshold){
            return -1;
        }else{
            return 0;
        }
    }
}
