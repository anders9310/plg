package plg.generator.process;

public class Obligation {
    private GenerationParameter type;
    private int value;
    private int remaining;

    public Obligation(GenerationParameter type, int value){
        if(value < 0){
            throw new IllegalArgumentException("The obligation value must be equal to or greater than 0");
        }
        this.type = type;
        this.value = value;
        this.remaining = value;
    }

    public int updateValue(RandomizationPattern generatedPattern){
        double productionContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(generatedPattern).get(type);
        this.remaining -= productionContribution;
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

}
