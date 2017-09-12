package plg.generator.process;

import plg.generator.process.weights.RandomizationPattern;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private Set<Production> productions;

    public static final ParameterRandomizationConfiguration BASIC_VALUES = new ParameterRandomizationConfiguration(10,4);

    public ParameterRandomizationConfiguration(int numActivities, int numGateways) {
        super(5,5,0.1,0.2,0.1,0.7,0.3,0.3,3,0.1);
        initObligations(numActivities, numGateways);
        initProductions();
    }

    public RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip) {
        return generateRandomPattern(productions);
    }

    public RandomizationPattern generateRandomPattern(Set<Production> patterns) {
        Set<Pair<RandomizationPattern, Double>> options = new HashSet<>();
        for(Production p : patterns) {
            options.add(new Pair<>(p.getType(), p.getWeight()));
        }
        RandomizationPattern generatedPattern = SetUtils.getRandomWeighted(options);
        updateRemainingObligations(generatedPattern);
        return generatedPattern;
    }

    private void updateRemainingObligations(RandomizationPattern generatedPattern) {
        for(Production production : productions){
            if(production.getType() == generatedPattern){
                for(Obligation obligation : obligations){
                    obligation.updateValue(production);
                }
                break;
            }
        }
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
        productions = new HashSet<>();
        for(plg.generator.process.weights.RandomizationPattern pattern : plg.generator.process.weights.RandomizationPattern.values()){
            productions.add(new Production(pattern, obligations));
        }
    }


}
