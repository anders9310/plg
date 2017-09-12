package plg.generator.process;

public class Obligation {
    private GenerationParameter type;
    private int value;
    private int remaining;

    public Obligation(GenerationParameter type, int value){
        this.type = type;
        this.value = value;
        this.remaining = value;
    }

    public int updateValue(Production generatedProduction){
        double productionContribution = generatedProduction.getContribution(type);
        this.remaining -= productionContribution;
        if(remaining < 0){
            remaining=0;
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

}
