package plg.generator.process;

import plg.model.Process;
import plg.utils.Logger;
import plg.utils.Random;

import java.util.HashMap;
import java.util.Map;

public class Target {
    private Process process;
    private Metric type;
    private double mean;
    private double targetValue;
    private double currentValue;

    public Target(Process process, Metric type, double mean){
        if(mean < 0){
            throw new IllegalArgumentException("The target value must be equal to or greater than 0");
        }
        this.process =process;
        this.type = type;
        this.mean = mean;
        //this.targetValue = mean;
        this.targetValue = Random.poissonRandom(mean);
        Logger.instance().debug("Target for " + type.name() + " created with value = " + this.targetValue + " for mean = " + mean);
    }

    public double getTargetValue() {
        return targetValue;
    }
    public Metric getType() {
        return type;
    }
    public double getMean() {
        return mean;
    }
    public double getCurrentValue() {
        this.currentValue = process.getMetric(type);
        return currentValue;
    }
    public Process getProcess() {
        return process;
    }

    public void printStatus(){
        Logger.instance().debug("Metric: " + type.name() + ". value = " + (currentValue) + ". targetValue = " + targetValue + ". isTarget: " + (currentValue==targetValue));
        if(mean!=0 && !(currentValue==targetValue)){
            Logger.instance().debug("Did not hit target value");
        }
    }

    public Map<String, Double> getStatus() {
        Map<String, Double> status = new HashMap<>();
        status.put("Current value", getCurrentValue());
        status.put("Target value", getTargetValue());
        status.put("Diff", getCurrentValue() - getTargetValue());
        return status;
    }
}
