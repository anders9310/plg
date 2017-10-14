package plg.generator.process;

import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private Set<Production> productions;

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

    private RandomizationPattern generateRandomPattern(Set<Production> patterns) {
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        if(!allProductionWeightsAre0(patterns)){
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), p.getWeight()));
            }
        }else{
            for(Production p : patterns) {
                options.add(new Pair<>(p.getType(), 1.0));
            }
        }

        RandomizationPattern generatedPattern = SetUtils.getRandomWeighted(options);
        updateRemainingObligations(generatedPattern);
        return generatedPattern;
    }

    private boolean allProductionWeightsAre0(Set<Production> patterns) {
        int sum = 0;
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

    private void initObligation(GenerationParameter gp, int value) {
        Obligation obligation = new Obligation(gp, value);
        obligations.add(obligation);
    }

    private void initProductions(){
        productions = new HashSet<>();
        /*productions.add(new Production(RandomizationPattern.SEQUENCE, obligations));
        productions.add(new Production(RandomizationPattern.MUTUAL_EXCLUSION, obligations));
        productions.add(new Production(RandomizationPattern.SINGLE_ACTIVITY, obligations));
        productions.add(new Production(RandomizationPattern.SKIP, obligations));*/
        for(RandomizationPattern pattern : RandomizationPattern.values()){
            productions.add(new Production(pattern, obligations));
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



    private void initObligations(Map<GenerationParameter, Integer> genParams) {
        obligations = new ArrayList<>();
        for (Map.Entry gpAndValue : genParams.entrySet()) {
            GenerationParameter gp = (GenerationParameter) gpAndValue.getKey();
            int value = (int) gpAndValue.getValue();
            initObligation(gp, value);
        }
    }


    public void printResults(){
        for(Obligation o : obligations){
            o.printStatus();
        }
    }



}
