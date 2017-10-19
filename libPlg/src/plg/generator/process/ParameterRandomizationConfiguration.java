package plg.generator.process;

import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private List<Production> productions;

    public ParameterRandomizationConfiguration(int numActivities, int numGateways) {
        this(numActivities, numGateways, 0.1);
    }

    public ParameterRandomizationConfiguration(int numActivities, int numGateways, double dataObjectProbability) {
        super(2, 2, dataObjectProbability);
        initObligations(numActivities, numGateways);
        initProductions();
    }

    public RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip) {
        return generateRandomPattern(productions);
    }

    private RandomizationPattern generateRandomPattern(List<Production> patterns) {
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        for(Production p : patterns) {
            options.add(new Pair<>(p.getType(), p.getWeight()));
        }

        RandomizationPattern generatedPattern = SetUtils.getRandomWeighted(options);
        updateRemainingObligations(generatedPattern);
        return generatedPattern;
    }

    private boolean allProductionWeightsAre0(List<Production> patterns) {
        double sum = 0;
        for(Production p : patterns) {
            sum += p.getWeight();
        }
        return sum==0;
    }

    private void initObligations(int numActivities, int numGateways) {
        Map<GenerationParameter, Integer> generationParameters = new HashMap<>();
        generationParameters.put(GenerationParameter.NUM_ACTIVITIES, numActivities);
        generationParameters.put(GenerationParameter.NUM_GATEWAYS, numGateways);
        initObligations(generationParameters);
    }

    private void initObligations(Map<GenerationParameter, Integer> genParams) {
        obligations = new ArrayList<>();
        for (Map.Entry gpAndValue : genParams.entrySet()) {
            GenerationParameter gp = (GenerationParameter) gpAndValue.getKey();
            int value = (int) gpAndValue.getValue();
            initObligation(gp, value);
        }
    }

    private void initObligation(GenerationParameter gp, int value) {
        Obligation obligation = new Obligation(gp, value);
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

    private void updateRemainingObligations(RandomizationPattern generatedPattern) {
        for(Production production : productions){
            if(production.getType() == generatedPattern){
                for(Obligation obligation : obligations){
                    obligation.updateValue(production.getType());
                }
                break;
            }
        }
    }






    public void printResults(){
        for(Obligation o : obligations){
            o.printStatus();
        }
    }



}
