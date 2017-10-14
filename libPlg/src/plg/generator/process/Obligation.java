package plg.generator.process;

import plg.generator.process.weights.BaseWeights;
import plg.utils.Logger;
import plg.utils.Random;

public class Obligation {
    private GenerationParameter type;
    private int mean;
    private int targetValue;
    private int terminals;
    private int potential;

    public Obligation(GenerationParameter type, int mean){
        if(mean < 0){
            throw new IllegalArgumentException("The obligation value must be equal to or greater than 0");
        }
        this.type = type;
        this.mean = mean;
        this.targetValue = Random.poissonRandom(mean);
        this.terminals = 0;
        this.potential = 1;
        Logger.instance().debug("Obligation for " + type.name() + " created with target value = " + this.targetValue + " for mean = " + mean);
    }

    public void updateValue(RandomizationPattern generatedPattern){
        double productionContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(generatedPattern).get(type);
        double productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(generatedPattern, type);
        this.terminals+=productionContribution;
        this.potential+=productionPotential;
        Logger.instance().debug("Pattern; " + generatedPattern.name() + ". Value: " + (terminals+potential) + "/" + targetValue);
    }

    public int getTargetValue() {
        return targetValue;
    }
    public int getRemaining() {
        return targetValue-terminals;
    }
    public GenerationParameter getType() {
        return type;
    }
    public int getMean() {
        return mean;
    }
    public int getTerminals() {
        return terminals;
    }
    public int getPotential(){
        return potential;
    }

    public void printStatus(){
        Logger.instance().debug("Metric: " + type.name() + ". value = " + (terminals+potential) + ". targetValue = " + targetValue + ". isTarget: " + (terminals+potential==targetValue));
        if(mean!=0 && !(terminals+potential==targetValue)){
            Logger.instance().debug("Did not hit target value");
        }
    }

}
