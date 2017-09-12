package plg.generator.process;

import plg.generator.process.weights.RandomizationPattern;

import java.util.HashMap;
import java.util.Map;

public class ProductionRuleContributions {
    private Map<RandomizationPattern, Map<GenerationParameter, Double>> contributions;

    public static ProductionRuleContributions CONTRIBUTIONS = new ProductionRuleContributions();

    private ProductionRuleContributions(){
        contributions = new HashMap<>();

        RandomizationPattern pattern = RandomizationPattern.SEQUENCE;
        Map<GenerationParameter, Double> obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 0.0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SINGLE_ACTIVITY;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 1.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 0.0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.MUTUAL_EXCLUSION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 1.0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.PARALLEL_EXECUTION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 1.0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.LOOP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 1.0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SKIP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 0.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 0.0);
        contributions.put(pattern, obligationsValues);
    }

    public double getContribution(RandomizationPattern pattern, GenerationParameter parameter){
        return contributions.get(pattern).get(parameter);
    }

    public Map<GenerationParameter, Double> getContribution(RandomizationPattern pattern){
        return contributions.get(pattern);
    }
}
