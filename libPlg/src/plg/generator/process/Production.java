package plg.generator.process;

import plg.generator.process.weights.ProductionWeight;
import plg.generator.process.weights.RandomizationPattern;

import java.util.List;

public class Production {
    private RandomizationPattern type;
    private ProductionWeight weight;

    public Production(RandomizationPattern type, List<Obligation> obligations){
        this.type = type;
        this.weight = new ProductionWeight(type, obligations);
    }

    public double getContribution(GenerationParameter genParameter) {
        return ProductionRuleContributions.CONTRIBUTIONS.getContribution(this.type, genParameter);
    }
    public RandomizationPattern getType() {
        return type;
    }
    public double getWeight() {
        return weight.getValue();
    }


}
