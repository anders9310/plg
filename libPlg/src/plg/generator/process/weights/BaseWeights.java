package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;

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

    public double calcBaseWeight(RandomizationPattern pattern, GenerationParameter genParam){
        return contributions.getContribution(pattern, genParam) + getPotential(pattern);
    }

    public int getBasePotential(RandomizationPattern pattern){
        return getPotential(pattern);
    }

    private int getPotential(RandomizationPattern pattern) {
        switch(pattern){
            case SINGLE_ACTIVITY:
                return -1;
            case SEQUENCE:
                return 1;
            case PARALLEL_EXECUTION:
                return 1;
            case MUTUAL_EXCLUSION:
                return 1;
            case LOOP:
                return 1;
            case SKIP:
                return -1;
            /*case MUTUAL_EXCLUSION_SINGLEBRANCH:
                return -1;
            case PARALLEL_EXECUTION_SINGLEBRANCH:
                return -1;*/
        }
        return 0;
    }

}
