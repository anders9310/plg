package plg.generator.process;

import plg.generator.process.weights.PatternWeight;

import java.util.List;

public class Pattern {
    private RandomizationPattern type;
    private PatternWeight weight;

    public Pattern(RandomizationPattern type, List<Target> target, List<RandomizationPattern> allRandomizationPatterns){
        this.type = type;
        this.weight = new PatternWeight(type, target, allRandomizationPatterns);
    }
    public RandomizationPattern getType() {
        return type;
    }
    public double getWeight(CurrentGenerationState state) {
        double weightValue = weight.getValue(state);
        return weightValue;
    }


}
