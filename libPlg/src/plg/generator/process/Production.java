package plg.generator.process;

import plg.generator.process.weights.ProductionWeight;
import plg.utils.Logger;

import java.util.List;

public class Production {
    private RandomizationPattern type;
    private ProductionWeight weight;

    public Production(RandomizationPattern type, List<Obligation> obligations, List<RandomizationPattern> allRandomizationPatterns){
        this.type = type;
        this.weight = new ProductionWeight(type, obligations, allRandomizationPatterns);
    }

    public double getContribution(GenerationParameter genParameter) {
        return ProductionRuleContributions.CONTRIBUTIONS.getContribution(this.type, genParameter);
    }
    public RandomizationPattern getType() {
        return type;
    }
    public double getWeight(CurrentGenerationState state) {
        double weightValue = weight.getValue(state);
        return weightValue;
    }


}
