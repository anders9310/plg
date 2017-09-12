package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;
import plg.generator.process.ProductionRuleContributions;

import java.util.HashMap;
import java.util.Map;

public class BaseWeights {
    private Map<RandomizationPattern, Map<GenerationParameter, Double>> baseWeights;

    private static ProductionRuleContributions contributions = ProductionRuleContributions.CONTRIBUTIONS;
    public static BaseWeights BASE_WEIGHTS = new BaseWeights();

    private BaseWeights(){
        baseWeights = new HashMap<>();
        for(RandomizationPattern pattern : RandomizationPattern.values()){
            initPatternWeights(pattern);
        }
    }

    private void initPatternWeights(RandomizationPattern pattern) {
        Map<GenerationParameter, Double> obligationsValues = new HashMap<>();
        obligationsValues.put(GenerationParameter.NUM_ACTIVITIES, calcBaseWeight(pattern, GenerationParameter.NUM_ACTIVITIES));
        obligationsValues.put(GenerationParameter.NUM_GATEWAYS, calcBaseWeight(pattern, GenerationParameter.NUM_GATEWAYS));
        baseWeights.put(pattern, obligationsValues);
    }

    private double calcBaseWeight(RandomizationPattern pattern, GenerationParameter genParam){
        return contributions.getContribution(pattern, genParam) + getExpectedRandomContribution(pattern, genParam);
    }

    private double getExpectedRandomContribution(RandomizationPattern pattern, GenerationParameter genParam) {
        switch(pattern){
            case SINGLE_ACTIVITY:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
            case SEQUENCE:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
            case PARALLEL_EXECUTION:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
            case MUTUAL_EXCLUSION:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
            case LOOP:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
            case SKIP:
                switch(genParam){
                    case NUM_ACTIVITIES:
                        return 0;
                    case NUM_GATEWAYS:
                        return 0;
                    default:
                        return 0;
                }
        }
        return 0;
    }

    public double getBaseWeight(RandomizationPattern pattern, GenerationParameter parameter){
        return baseWeights.get(pattern).get(parameter);
    }
}
