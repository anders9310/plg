package plg.generator.process;

import plg.generator.process.weights.ProductionObligationWeight;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private Map<GenerationParameter, Integer> obligations;
    private Map<GenerationParameter, Integer> remainingObligations;
    private Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, Double> productionWeights;
    public Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, Map<GenerationParameter, ProductionObligationWeight>> obligationBaseWeights;
    private Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, Map<GenerationParameter, ProductionObligationWeight>> obligationWeights;

    public static final ParameterRandomizationConfiguration BASIC_VALUES = new ParameterRandomizationConfiguration(10,4);

    public ParameterRandomizationConfiguration(int numActivities, int numGateways) {
        super(5,5,0.1,0.2,0.1,0.7,0.3,0.3,3,0.1);
        this.productionWeights = new HashMap<>();
        initObligations(numActivities, numGateways);

        this.obligationWeights = new HashMap<>();
        Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> patterns = getAllPatterns();
        for(RandomizationConfiguration.RANDOMIZATION_PATTERN p : patterns){
            Map<GenerationParameter, ProductionObligationWeight> baseWeightsForProduction = new HashMap<>();
            baseWeightsForProduction.put(GenerationParameter.NUM_ACTIVITIES, new ProductionObligationWeight(2.0, obligations.get(GenerationParameter.NUM_ACTIVITIES)));
            baseWeightsForProduction.put(GenerationParameter.NUM_GATEWAYS, new ProductionObligationWeight(2.0, obligations.get(GenerationParameter.NUM_GATEWAYS)));
            obligationWeights.put(p, baseWeightsForProduction);
        }
    }

    public RandomizationConfiguration.RANDOMIZATION_PATTERN getRandomPattern(boolean canLoop, boolean canSkip) {
        Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> options = getAllPatterns();

        return getRandomPattern(options);
    }

    public RandomizationConfiguration.RANDOMIZATION_PATTERN getRandomPattern(Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> patterns) {
        Set<Pair<RandomizationConfiguration.RANDOMIZATION_PATTERN, Double>> options = new HashSet<>();
        calculateWeights(patterns, getAllGenerationParameters());
        for(RandomizationConfiguration.RANDOMIZATION_PATTERN p : patterns) {
            options.add(new Pair<>(p, productionWeights.get(p)));
        }
        return SetUtils.getRandomWeighted(options);
    }

    public Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> getAllPatterns(){
        Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> options = new HashSet<RandomizationConfiguration.RANDOMIZATION_PATTERN>();
        options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.SINGLE_ACTIVITY);
        options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.SEQUENCE);
        options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.PARALLEL_EXECUTION);
        options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.MUTUAL_EXCLUSION);
            options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.SKIP);
            options.add(RandomizationConfiguration.RANDOMIZATION_PATTERN.LOOP);
        return options;
    }

    private Set<GenerationParameter> getAllGenerationParameters(){
        Set<GenerationParameter> parameters = new HashSet<>();
        parameters.add(GenerationParameter.NUM_ACTIVITIES);
        parameters.add(GenerationParameter.NUM_GATEWAYS);
        return parameters;
    }

    private void initObligations(int numActivities, int numGateways) {
        obligations = new HashMap<>();
        remainingObligations = new HashMap<>();
        initObligation(GenerationParameter.NUM_ACTIVITIES, numActivities);
        initObligation(GenerationParameter.NUM_GATEWAYS, numGateways);
    }

    private void initObligation(GenerationParameter parameter, int value) {
        obligations.put(parameter, value);
        remainingObligations.put(parameter, value);
    }

    private void calculateWeights(Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> patterns, Set<GenerationParameter> parameters){
        productionWeights = new HashMap<>();
        for(RandomizationConfiguration.RANDOMIZATION_PATTERN p : patterns){
            Map<GenerationParameter, ProductionObligationWeight> productionObligationWeights = obligationBaseWeights.get(p);
            double sumOfWeightsForObligations = 0.0;
            for(GenerationParameter gp : parameters){
                ProductionObligationWeight obligationWeight = productionObligationWeights.get(gp);
                sumOfWeightsForObligations += obligationWeight.getValue();
            }
            //find total production weight
            double productionWeight = sumOfWeightsForObligations;

            productionWeights.put(p, productionWeight);
        }
    }
}
