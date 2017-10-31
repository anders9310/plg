package plg.generator.process;

import plg.generator.process.weights.BaseWeights;
import plg.model.Process;
import plg.utils.Logger;
import plg.utils.Random;

public class Obligation {
    private Process process;
    private GenerationParameter type;
    private double mean;
    private double targetValue;
    private double currentValue;
    private int potential;

    public Obligation(Process process, GenerationParameter type, double mean){
        if(mean < 0){
            throw new IllegalArgumentException("The obligation value must be equal to or greater than 0");
        }
        this.process = process;
        this.type = type;
        this.mean = mean;
        if(!type.isRatioBased()){
            this.targetValue = mean;
            //this.targetValue = Random.poissonRandom(mean);
        } else{
            this.targetValue = mean;
        }
        this.currentValue = 0; //TODO: Refactor to get value from this.process
        this.potential = 1;
        Logger.instance().debug("Obligation for " + type.name() + " created with target value = " + this.targetValue + " for mean = " + mean);
    }

    public void updateValue(RandomizationPattern generatedPattern){
        double productionContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(generatedPattern).get(type);
        double productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(generatedPattern, type);
        if(type.isRatioBased()){
            this.currentValue = process.getMetrics().getCoefficientOfNetworkConnectivity(); //TODO: get support for other ratio-metrics too...
        }else{
            this.currentValue +=productionContribution;
        }
        this.potential+=productionPotential;
        Logger.instance().debug("Generated pattern: " + generatedPattern.name() + ". Obligation metric: " + type.name() + ". Value: " + (currentValue) + "/" + targetValue + ". Potential: " + potential);
    }

    public double getTargetValue() {
        return targetValue;
    }
    public double getRemaining() {
        return targetValue - currentValue;
    }
    public GenerationParameter getType() {
        return type;
    }
    public double getMean() {
        return mean;
    }
    public double getCurrentValue() {
        return currentValue;
    }
    public int getPotential(){
        return potential;
    }

    public void printStatus(){
        Logger.instance().debug("Metric: " + type.name() + ". value = " + (currentValue +potential) + ". targetValue = " + targetValue + ". isTarget: " + (currentValue +potential==targetValue));
        if(mean!=0 && !(currentValue +potential==targetValue)){
            Logger.instance().debug("Did not hit target value");
        }
    }
}
