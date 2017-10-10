package plg.generator.process;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.MathUtils;
import org.python.modules.math;
import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.util.*;

public class ParameterRandomizationConfiguration extends RandomizationConfiguration{

    private List<Obligation> obligations;
    private Set<Production> productions;

    public static final ParameterRandomizationConfiguration BASIC_VALUES = new ParameterRandomizationConfiguration(10,4);

    public ParameterRandomizationConfiguration(int numActivities, int numGateways) {
        this(numActivities, numGateways, 0.1);
    }

    public ParameterRandomizationConfiguration(int numActivities, int numGateways, double dataObjectProbability) {
        super(0, 0, dataObjectProbability);
        initObligations(numActivities, numGateways);
        initProductions();
    }

    public RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip) {
        //contribution or potential?
        double pi_p = getProbForPotential();

        Set<Pair<String, Double>> options = new HashSet<>();
        options.add(new Pair<>("1",pi_p));
        options.add(new Pair<>("2",1-pi_p));
        String chosenValue = SetUtils.getRandomWeighted(options);
        switch (chosenValue) {
            case "1":
                return generateRandomPattern(getPotentialProductions());
            case "2":
                return generateRandomPattern(getContributingProductions());
            default:
                throw new RuntimeException("Invalid value of 'chosenValue': " + chosenValue);
        }
    }

    private double getProbPotential() {
        Obligation obligation = obligations.get(0);

        double o_e = obligation.getRemaining() - obligation.getPotential();
        double o_t = obligation.getValue();
        double o_r = obligation.getRemaining();
        double o_p = obligation.getPotential();
        double k = o_t - o_r + o_p;
        double lambda = o_t;
        Logger.instance().debug("k = " + k + ". lambda = " + lambda);
        double sum = getSumExpression(lambda, k);
        double pi_p = 1 - ( Math.pow(Math.exp(-lambda) * sum, 1.0 / o_p) );
        Logger.instance().debug("pi_p for o_e = " + o_e + " and o_t = " + o_t + ": " + pi_p);

        return pi_p;
    }

    private double getProbForPotential(){
        Obligation obligation = obligations.get(0);
        double o_e = obligation.getRemaining() - obligation.getPotential();
        double o_t = obligation.getValue();
        double o_r = obligation.getRemaining();
        double o_p = obligation.getPotential();
        double k = o_t - o_r + o_p;
        double lambda = o_t;

        double probSuccessAtI = Math.exp(-lambda);
        double probFailForAllPrevious = 1;

        for(int i = 1; i<=k; i++){
            probFailForAllPrevious*= 1.0-probSuccessAtI;
            probSuccessAtI = Math.exp(-lambda) * calcFraction(lambda,i) / probFailForAllPrevious;
        }

        return 1 - Math.pow(probSuccessAtI, 1.0/obligation.getPrevPotentialMaximum());
    }

    private double getSumExpression(double lambda, double k) {
        double sum = 0;
        for(int i = 0;i<=k; i++){
            sum += calcFraction(lambda, i);
        }
        return sum;
    }

    private double calcFraction(double lambda, int i){
        double product = 1;
        for(int c = 1; c<=i; c++){
            product *= lambda/c;
        }
        return product;
    }

    private Set<Production> getContributingProductions(){
        Set<Production> contributing = new HashSet<>();
        for(Production p : productions){
            if(p.getType()==RandomizationPattern.SINGLE_ACTIVITY || p.getType()==RandomizationPattern.SKIP){
                contributing.add(p);
            }
        }
        return contributing;
    }
    private Set<Production> getPotentialProductions(){
        Set<Production> potentials = new HashSet<>();
        for(Production p : productions){
            if(p.getType()==RandomizationPattern.SEQUENCE){
                potentials.add(p);
            }
        }
        return potentials;
    }

    private void initObligations(int numActivities, int numGateways) {
        Map<GenerationParameter, Integer> generationParameters = new HashMap<>();
        generationParameters.put(GenerationParameter.NUM_ACTIVITIES, numActivities);
        //generationParameters.put(GenerationParameter.NUM_GATEWAYS, numGateways);
        initObligations(generationParameters);
    }

    private void initObligation(GenerationParameter gp, int value) {
        Obligation obligation = new Obligation(gp, value);
        obligations.add(obligation);
    }

    private void initProductions(){
        productions = new HashSet<>();
        productions.add(new Production(RandomizationPattern.SEQUENCE, obligations));
        productions.add(new Production(RandomizationPattern.SINGLE_ACTIVITY, obligations));
        //productions.add(new Production(RandomizationPattern.SKIP, obligations));
        /*for(RandomizationPattern pattern : RandomizationPattern.values()){
            productions.add(new Production(pattern, obligations));
        }*/
    }

    private RandomizationPattern generateRandomPattern(Set<Production> patterns) {
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






}
