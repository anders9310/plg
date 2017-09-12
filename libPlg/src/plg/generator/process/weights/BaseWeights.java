package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;

import java.util.HashMap;
import java.util.Map;

public class BaseWeights {
    private Map<RandomizationPattern, Map<GenerationParameter, Double>> baseWeights;

    public static BaseWeights BASE_WEIGHTS = new BaseWeights();

    private BaseWeights(){
        baseWeights = new HashMap<>();

        RandomizationPattern pattern = RandomizationPattern.SEQUENCE;
        Map<GenerationParameter, Double> obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 2.0);
        baseWeights.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SINGLE_ACTIVITY;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 2.0);
        baseWeights.put(pattern, obligationsValues);

        pattern = RandomizationPattern.MUTUAL_EXCLUSION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 2.0);
        baseWeights.put(pattern, obligationsValues);

        pattern = RandomizationPattern.PARALLEL_EXECUTION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 1.0);
        baseWeights.put(pattern, obligationsValues);

        pattern = RandomizationPattern.LOOP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 2.0);
        baseWeights.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SKIP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, 2.0);
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, 2.0);
        baseWeights.put(pattern, obligationsValues);
    }

    public double getBaseWeight(RandomizationPattern pattern, GenerationParameter parameter){
        return baseWeights.get(pattern).get(parameter);
    }
}
