package plg.generator.process;

import plg.generator.process.weights.ProductionObligationWeight;
import plg.generator.process.weights.ProductionWeight;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private Map<GenerationParameter, Integer> obligations;
    private Map<GenerationParameter, Integer> remainingObligations;
    private Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, ProductionWeight> productionWeights;
    public Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, Map<GenerationParameter, ProductionObligationWeight>> obligationBaseWeights;
    private Map<RandomizationConfiguration.RANDOMIZATION_PATTERN, Map<GenerationParameter, ProductionObligationWeight>> obligationWeights;

    public static final ParameterRandomizationConfiguration BASIC_VALUES = new ParameterRandomizationConfiguration(10,4);

    public ParameterRandomizationConfiguration(int numActivities, int numGateways) {
        super(5,5,0.1,0.2,0.1,0.7,0.3,0.3,3,0.1);
        initProductionWeights();
        initObligations(numActivities, numGateways);
    }

    private void initProductionWeights() {
        this.productionWeights = new HashMap<>();

        Map<GenerationParameter, Double> obligationBaseWeights = setUpObligationBaseWeights();
        Map<GenerationParameter, Integer> obligationValues = setUpObligationValues();
        for(RANDOMIZATION_PATTERN pattern : RANDOMIZATION_PATTERN.values()){
            ProductionWeight pw = new ProductionWeight(obligationBaseWeights, obligationValues);
            productionWeights.put(pattern, pw);
        }

    }

    private Map<GenerationParameter, Double> setUpObligationBaseWeights() {
        double obligationBaseWeight = 2.0;
        Map<GenerationParameter, Double> obligationBaseWeights = new HashMap<>();
        for(GenerationParameter gp : GenerationParameter.values()){
            obligationBaseWeights.put(gp, obligationBaseWeight);
        }
        return obligationBaseWeights;
    }

    private Map<GenerationParameter, Integer> setUpObligationValues() {
        int obligationValue = 4;
        Map<GenerationParameter, Integer> obligationValues = new HashMap<>();
        for(GenerationParameter gp : GenerationParameter.values()){
            obligationValues.put(gp, obligationValue);
        }
        return obligationValues;
    }

    public RandomizationConfiguration.RANDOMIZATION_PATTERN getRandomPattern(boolean canLoop, boolean canSkip) {
        Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> options = getAllPatterns();

        return getRandomPattern(options);
    }

    public RandomizationConfiguration.RANDOMIZATION_PATTERN getRandomPattern(Set<RandomizationConfiguration.RANDOMIZATION_PATTERN> patterns) {
        Set<Pair<RandomizationConfiguration.RANDOMIZATION_PATTERN, Double>> options = new HashSet<>();
        for(RandomizationConfiguration.RANDOMIZATION_PATTERN p : patterns) {
            options.add(new Pair<>(p, productionWeights.get(p).getValue()));
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
}
