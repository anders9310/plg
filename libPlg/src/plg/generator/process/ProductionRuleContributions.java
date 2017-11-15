package plg.generator.process;

import java.util.HashMap;
import java.util.Map;

public class ProductionRuleContributions {
    private Map<RandomizationPattern, Map<Metric, Integer>> contributions;

    public static ProductionRuleContributions CONTRIBUTIONS = new ProductionRuleContributions();

    private ProductionRuleContributions(){
        contributions = new HashMap<>();

        /*RandomizationPattern pattern = RandomizationPattern.SEQUENCE;
        Map<Metric, Integer> obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 0);
        obligationsValues.put(Metric.NUM_GATEWAYS, 0);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SINGLE_ACTIVITY;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 1);
        obligationsValues.put(Metric.NUM_GATEWAYS, 0);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.MUTUAL_EXCLUSION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 2);
        obligationsValues.put(Metric.NUM_GATEWAYS, 1);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 1);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.PARALLEL_EXECUTION;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 2);
        obligationsValues.put(Metric.NUM_GATEWAYS, 1);
        obligationsValues.put(Metric.NUM_AND_GATES, 1);
        obligationsValues.put(Metric.NUM_XOR_GATES, 0);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.LOOP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 2);
        obligationsValues.put(Metric.NUM_GATEWAYS, 2);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 1);
        contributions.put(pattern, obligationsValues);

        pattern = RandomizationPattern.SKIP;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 0);
        obligationsValues.put(Metric.NUM_GATEWAYS, 0);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 0);
        contributions.put(pattern, obligationsValues);*/

        /*pattern = RandomizationPattern.MUTUAL_EXCLUSION_SINGLEBRANCH;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 2);
        obligationsValues.put(Metric.NUM_GATEWAYS, 1);
        obligationsValues.put(Metric.NUM_AND_GATES, 0);
        obligationsValues.put(Metric.NUM_XOR_GATES, 1);
        contributions.put(pattern, obligationsValues);*/

        /*pattern = RandomizationPattern.PARALLEL_EXECUTION_SINGLEBRANCH;
        obligationsValues = new HashMap<>();
        obligationsValues.put(Metric.NUM_ACTIVITIES, 2);
        obligationsValues.put(Metric.NUM_GATEWAYS, 1);
        obligationsValues.put(Metric.NUM_AND_GATES, 1);
        obligationsValues.put(Metric.NUM_XOR_GATES, 0);
        contributions.put(pattern, obligationsValues);*/
    }

    public int getContribution(RandomizationPattern pattern, Metric parameter){
        Map<Metric, Integer> patternMap = contributions.get(pattern);
        if(patternMap == null){
            throw new IllegalArgumentException("No predetermined contributions for " + pattern.name());
        }

        Integer contribution = patternMap.get(parameter);
        if(contribution == null){
            throw new IllegalArgumentException("No predetermined contribution for " + parameter.name() + " in " + pattern.name());
        }

        return contribution;
    }
}
