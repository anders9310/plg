package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.Process;

public class Benefit extends Weight{
    private final double POTENTIAL_THRESHOLD = 1;
    private final double VALUE_GRANULARITY = 0.01;

    private Target target;
    private RandomizationPattern randomizationPattern;
    private Process process;

    public Benefit(RandomizationPattern randomizationPattern, Target target){
        this.randomizationPattern = randomizationPattern;
        this.target = target;
        this.process = target.getProcess();
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(target.getMean() == 0){
            return 0;
        }

        double metricContribution = process.getContributionOf(state, this.target.getType(), randomizationPattern);
        double targetValue = target.getTargetValue();
        double currentValue = target.getCurrentValue();
        //double currentPotential = process.getNumUnknownComponents();
        //double potentialIncrease = process.getPotentialIncreaseOf(randomizationPattern);

        double currentStateOfBenefit = (currentValue-targetValue) / targetValue;
        double projectedStateOfBenefit = (currentValue + metricContribution - targetValue) / targetValue;
        return compare(Math.abs(currentStateOfBenefit), Math.abs(projectedStateOfBenefit), VALUE_GRANULARITY);



        /*double terminalWish;
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
        }*/

        /*double potentialWish;
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
        return terminalWish + potentialWish;*/
    }

    public double calculatePotentialGrowthBenefit(CurrentGenerationState state){
        double potentialGrowthContribution = process.getPotentialIncreaseOf(randomizationPattern);;
        double currentValue = target.getCurrentValue();
        double targetValue = target.getTargetValue();
        double diff = compare(targetValue, currentValue, VALUE_GRANULARITY);
        if(diff>0){
            if(potentialGrowthContribution > 0){
                return diff;
            } else if(potentialGrowthContribution==0){
                return 0;
            } else{
                return -diff;
            }
        } else {
            if(potentialGrowthContribution > 0){
                return -diff;
            }else if(potentialGrowthContribution==0){
                return 0;
            } else {
                return diff;
            }
        }

    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
    public Process getProcess() {
        return process;
    }

    public Target getTarget() {
        return target;
    }

    public static double compare(double value, double comparedTo, double threshold){
        if(value > comparedTo + threshold){
            return value - comparedTo;
        }else if(value < comparedTo - threshold){
            return value - comparedTo;
        }else{
            return 0;
        }
    }
}
