package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;

public abstract class Weight {
    protected double value;

    public double getValue(CurrentGenerationState state){
        value = calculateValue(state);
        return value;
    }

    abstract protected double calculateValue(CurrentGenerationState state);
}
