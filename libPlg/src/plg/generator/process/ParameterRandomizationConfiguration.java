package plg.generator.process;

import plg.utils.Pair;
import plg.utils.SetUtils;
import plg.model.Process;
import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private List<Production> productions;
    private Process process;
    private CurrentGenerationState state;

    public ParameterRandomizationConfiguration(Process process, int numActivities, int numGateways, int numAndGates, int numXorGates, double diameter, double coefficientOfNetworkConnectivity) {
        super(2, 2, 0.0);
        this.process = process;
        initObligations(numActivities, numGateways, numAndGates, numXorGates, diameter, coefficientOfNetworkConnectivity);
        initProductions();
    }

    private void initObligations(double numActivities, double numGateways, double numAndGates, double numXorGates, double diameter, double coefficientOfNetworkConnectivity) {
        Map<GenerationParameter, Double> generationParameters = new HashMap<>();
        if(numActivities>0) generationParameters.put(GenerationParameter.NUM_ACTIVITIES, numActivities);
        if(numGateways>0) generationParameters.put(GenerationParameter.NUM_GATEWAYS, numGateways);
        if(numAndGates>0) generationParameters.put(GenerationParameter.NUM_AND_GATES, numAndGates);
        if(numXorGates>0) generationParameters.put(GenerationParameter.NUM_XOR_GATES, numXorGates);
        if(diameter>0) generationParameters.put(GenerationParameter.DIAMETER, diameter);
        if(coefficientOfNetworkConnectivity>0) generationParameters.put(GenerationParameter.COEFFICIENT_OF_NETWORK_CONNECTIVITY, coefficientOfNetworkConnectivity);
        initObligations(generationParameters);
    }

    private void initObligations(Map<GenerationParameter, Double> genParams) {
        obligations = new ArrayList<>();
        for (Map.Entry gpAndValue : genParams.entrySet()) {
            GenerationParameter gp = (GenerationParameter) gpAndValue.getKey();
            double value = (double) gpAndValue.getValue();
            initObligation(gp, value);
        }
    }

    private void initObligation(GenerationParameter gp, double value) {
        Obligation obligation = new Obligation(process, gp, value);
        obligations.add(obligation);
    }

    private void initProductions(){
        List<RandomizationPattern> randomizationPatterns = new LinkedList<>();
        randomizationPatterns.addAll(Arrays.asList(RandomizationPattern.values()));
        productions = new LinkedList<>();
        for(RandomizationPattern pattern : RandomizationPattern.values()){
            productions.add(new Production(pattern, obligations, randomizationPatterns));
        }
    }

    public RandomizationPattern generateRandomPattern(CurrentGenerationState state) {
        this.state = state;
        return generateRandomPattern(productions);
    }

    private RandomizationPattern generateRandomPattern(List<Production> patterns) {
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        if(allProductionWeightsAre0(patterns)){
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), 1.0));
            }
        }else{
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), p.getWeight(state)));
            }
        }
        RandomizationPattern generatedPattern = SetUtils.getRandomWeighted(options);
        return generatedPattern;
    }

    private boolean allProductionWeightsAre0(List<Production> patterns) {
        double sum = 0;
        for(Production p : patterns) {
            sum += p.getWeight(state);
        }
        return sum==0;
    }

    public void printResults(){
        for(Obligation o : obligations){
            o.printStatus();
        }
    }
}
