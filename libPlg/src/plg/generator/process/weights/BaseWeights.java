package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;

import java.util.HashMap;
import java.util.Map;

public class BaseWeights {

    public static int getPotentialIncreaseFor(RandomizationPattern pattern){
        return getPotential(pattern);
    }

    private static int getPotential(RandomizationPattern pattern) {
        switch(pattern){
            case SINGLE_ACTIVITY:
                return -1;
            case SEQUENCE:
                return 1;
            case PARALLEL_EXECUTION:
                return 1;
            case MUTUAL_EXCLUSION:
                return 1;
            case LOOP:
                return 1;
            case SKIP:
                return -1;
            /*case MUTUAL_EXCLUSION_SINGLEBRANCH:
                return -1;
            case PARALLEL_EXECUTION_SINGLEBRANCH:
                return -1;*/
        }
        return 0;
    }
}
