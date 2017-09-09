package plg.generator.process.weights;

public abstract class Weight {
    protected double value;

    public double getValue(){
        calculateValue();
        return value;
    }

    abstract void calculateValue();
}
