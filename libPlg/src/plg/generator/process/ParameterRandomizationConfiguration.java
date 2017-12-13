package plg.generator.process;

import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;
import plg.model.Process;
import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Target> targets;
    private List<Production> productions;
    private Process process;
    private CurrentGenerationState state;

    public ParameterRandomizationConfiguration(Process process, Map<Metric, Double> inputs) {
        super(2, 2, 0.0);
        this.process = process;
        initTargets(inputs);
        initProductions();
    }

    /*private void initTargets(double numActivities, double numGateways, double numAndGates, double numXorGates, double diameter, double coefficientOfNetworkConnectivity) {
        Map<Metric, Double> generationParameters = new HashMap<>();
        if(numActivities>0) generationParameters.put(Metric.NUM_ACTIVITIES, numActivities);
        if(numGateways>0) generationParameters.put(Metric.NUM_GATEWAYS, numGateways);
        if(numAndGates>0) generationParameters.put(Metric.NUM_AND_GATES, numAndGates);
        if(numXorGates>0) generationParameters.put(Metric.NUM_XOR_GATES, numXorGates);
        if(diameter>0) generationParameters.put(Metric.DIAMETER, diameter);
        if(coefficientOfNetworkConnectivity>0) generationParameters.put(Metric.COEFFICIENT_OF_NETWORK_CONNECTIVITY, coefficientOfNetworkConnectivity);
        initTargets(generationParameters);
    }*/

    private void initTargets(Map<Metric, Double> genParams) {
        targets = new ArrayList<>();
        for (Map.Entry gpAndValue : genParams.entrySet()) {
            Metric gp = (Metric) gpAndValue.getKey();
            double value = (double) gpAndValue.getValue();
            initTarget(gp, value);
        }
    }

    private void initTarget(Metric gp, double value) {
        Target target = new Target(process, gp, value);
        targets.add(target);
    }

    private void initProductions(){
        List<RandomizationPattern> randomizationPatterns = new LinkedList<>();
        randomizationPatterns.addAll(Arrays.asList(RandomizationPattern.values()));
        productions = new LinkedList<>();
        for(RandomizationPattern pattern : randomizationPatterns){
            productions.add(new Production(pattern, targets, randomizationPatterns));
        }
    }

    public RandomizationPattern generateRandomPattern(CurrentGenerationState state) {
        this.state = state;
        return generateRandomPattern(productions);
    }

    private RandomizationPattern generateRandomPattern(List<Production> patterns) {
        Logger.instance().debug("-------------------------------------------");

        for(Target o : targets){
            Logger.instance().debug("Metric name: " + o.getType().name() + ". Value: " + process.getMetric(o.getType()));
        }
        Logger.instance().debug("Potential: " + process.getNumUnknownComponents());
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        if(allProductionWeightsAre0(patterns)){
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), 1.0));
                Logger.instance().debug("Pattern: " + p.getType().name() + " - Weight: " + 1.0);
            }
        }else{
            for(Production p : patterns) {
                double weight = p.getWeight(state);
                options.add(new Pair<>(p.getType(), weight));
                Logger.instance().debug("Pattern: " + p.getType().name() + " - Weight: " + weight);
            }
        }
        RandomizationPattern generatedPattern = SetUtils.getRandomWeighted(options);
        Logger.instance().debug("Next pattern: " + generatedPattern.name());
        Logger.instance().debug("-------------------------------------------");
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
        for(Target o : targets){
            o.printStatus();
        }
    }
    public Map<String, Map<String, Double>> getStatus(){
        Map<String, Map<String, Double>> results = new HashMap<>();
        for(Target o : targets){
            results.put(o.getType().name(), o.getStatus());
        }
        return results;
    }
}
