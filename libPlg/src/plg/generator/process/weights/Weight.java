package plg.generator.process.weights;

public abstract class Weight {
    protected double value;

    public double getValue(){
        value = calculateValue();
        return value;
    }

    abstract protected double calculateValue();
}
