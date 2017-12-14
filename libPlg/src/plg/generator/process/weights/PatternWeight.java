package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;
import plg.generator.process.Target;
import plg.generator.process.RandomizationPattern;

import java.util.ArrayList;
import java.util.List;

public class PatternWeight extends Weight{
    private List<TargetWeight> targetWeights;
    private RandomizationPattern randomizationPattern;

    public PatternWeight(RandomizationPattern randomizationPattern, List<Target> targets, List<RandomizationPattern> allRandomizationPatterns){
        this.randomizationPattern = randomizationPattern;
        targetWeights = new ArrayList<>();
        for(Target target : targets){
            TargetWeight ow = new TargetWeight(randomizationPattern, allRandomizationPatterns, target);
            targetWeights.add(ow);
        }
    }

    protected double calculateValue(CurrentGenerationState state){
        double sumOfWeights = 0;
        for (TargetWeight ow : targetWeights) {
            sumOfWeights += ow.getValue(state);
        }
        //if(sumOfWeights<0) return 0;
        //else return sumOfWeights;
        return sumOfWeights;
    }
}
