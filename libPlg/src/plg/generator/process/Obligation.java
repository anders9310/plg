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
