package plg.generator.process;

import plg.generator.process.weights.BaseWeights;
import plg.model.Process;
import plg.utils.Logger;

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
        this.process =process;
        this.type = type;
        this.mean = mean;
        if(!type.isRatioBased()){
            this.targetValue = mean;
            //this.targetValue = Random.poissonRandom(mean);
        } else{
            this.targetValue = mean;
        }
        this.currentValue = process.getMetric(type);
        this.potential = 1;
        Logger.instance().debug("Obligation for " + type.name() + " created with target value = " + this.targetValue + " for mean = " + mean);
    }

    /*public void updatePotential(RandomizationPattern generatedPattern){ //TODO: refactor so this is not needed anymore
        double productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(generatedPattern);
        this.potential+=productionPotential;
        Logger.instance().debug("Generated pattern: " + generatedPattern.name() + ". Obligation metric: " + type.name() + ". Value: " + getCurrentValue() + "/" + targetValue + ". Potential: " + potential);
    }*/

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
        this.currentValue = process.getMetric(type);
        return currentValue;
    }
    public int getPotential(){
        return potential;
    }
    public Process getProcess() {
        return process;
    }

    public void printStatus(){
        Logger.instance().debug("Metric: " + type.name() + ". value = " + (currentValue +potential) + ". targetValue = " + targetValue + ". isTarget: " + (currentValue +potential==targetValue));
        if(mean!=0 && !(currentValue +potential==targetValue)){
            Logger.instance().debug("Did not hit target value");
        }
    }
}
