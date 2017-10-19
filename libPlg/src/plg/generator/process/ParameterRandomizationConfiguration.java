package plg.generator.process;

import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private List<Production> productions;

    public ParameterRandomizationConfiguration(int numActivities, int numGateways, int numAndGates, int numXorGates) {
        this(numActivities, numGateways, numAndGates, numXorGates, 0.1);
    }

    public ParameterRandomizationConfiguration(int numActivities, int numGateways, int numAndGates, int numXorGates, double dataObjectProbability) {
        super(2, 2, dataObjectProbability);
        initObligations(numActivities, numGateways, numAndGates, numXorGates);
        initProductions();
    }

    public RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip) {
        return generateRandomPattern(productions);
    }

    private RandomizationPattern generateRandomPattern(List<Production> patterns) {
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        if(allProductionWeightsAre0(patterns)){
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), 1.0));
            }
        }else{
            /*Set<Pair<RandomizationPattern, Double>> tempOptions = new HashSet<>();
            double highestWeight = 0;
            for(Production p : patterns) {
                if(p.getWeight()>highestWeight){
                    tempOptions = new HashSet<>();
                    tempOptions.add(new Pair<>(p.getType(), p.getWeight()));
                    highestWeight = p.getWeight();
                } else if(p.getWeight()==highestWeight){
                    tempOptions.add(new Pair<>(p.getType(), p.getWeight()));
                }*/
            //}
            //options = tempOptions;
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), p.getWeight()));
            }
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

    private void initObligations(int numActivities, int numGateways, int numAndGates, int numXorGates) {
        Map<GenerationParameter, Integer> generationParameters = new HashMap<>();
        if(numActivities!=0) generationParameters.put(GenerationParameter.NUM_ACTIVITIES, numActivities);
        if(numGateways!=0) generationParameters.put(GenerationParameter.NUM_GATEWAYS, numGateways);
        if(numAndGates!=0) generationParameters.put(GenerationParameter.NUM_AND_GATES, numAndGates);
        if(numXorGates!=0) generationParameters.put(GenerationParameter.NUM_XOR_GATES, numXorGates);
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
