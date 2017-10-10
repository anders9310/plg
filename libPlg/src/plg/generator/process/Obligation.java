package plg.generator.process;

import plg.generator.process.weights.BaseWeights;

public class Obligation {
    private GenerationParameter type;
    private int value;
    private int remaining;
    private int potential;
    private int prevPotentialMaximum;

    public Obligation(GenerationParameter type, int value){
        if(value < 0){
            throw new IllegalArgumentException("The obligation value must be equal to or greater than 0");
        }
        this.type = type;
        this.value = value;
        this.remaining = value;
        this.potential = 1;
        this.prevPotentialMaximum = 1;
    }

    public int updateValue(RandomizationPattern generatedPattern){
        double productionContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(generatedPattern).get(type);
        double productionPotential = BaseWeights.BASE_WEIGHTS.getBasePotential(generatedPattern, type);
        this.remaining -= productionContribution;
        if(productionPotential>0){
            this.potential += productionPotential;
            this.prevPotentialMaximum = this.potential;
        }else{
            this.potential --;
        }
        return remaining;
    }

    public int getValue() {
        return value;
    }
    public int getRemaining() {
        return remaining;
    }
    public GenerationParameter getType() {
        return type;
    }
    public int getPotential(){
        return potential;
    }
    public int getPrevPotentialMaximum(){
        return prevPotentialMaximum;
    }

}
