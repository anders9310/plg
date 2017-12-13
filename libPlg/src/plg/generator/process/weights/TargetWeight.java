package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;
import plg.generator.process.Metric;
import plg.generator.process.Target;
import plg.generator.process.RandomizationPattern;

import java.util.ArrayList;
import java.util.List;

public class TargetWeight extends Weight{
    RandomizationPattern randomizationPattern;
    Metric metric;
    List<ProductionTargetWeight> pows;

    public TargetWeight(RandomizationPattern randomizationPattern, List<RandomizationPattern> allRandomizationPatterns, Target target){
        this.randomizationPattern = randomizationPattern;
        this.metric = target.getType();
        pows = new ArrayList<>();
        for(RandomizationPattern pattern : allRandomizationPatterns){
            ProductionTargetWeight pow = new ProductionTargetWeight(pattern, target);
            pows.add(pow);
        }
    }

    @Override
    protected double calculateValue(CurrentGenerationState state) {
        double totalProductionTargetWeight = getTotalProductionTargetWeights(state);
        double thisTargetProductionWeight = getThisTargetProductionWeight(state);
        if(totalProductionTargetWeight>0){
            return thisTargetProductionWeight / totalProductionTargetWeight;
        }else{
            return 0;
        }
    }

    private double getTotalProductionTargetWeights(CurrentGenerationState state){
        double sumOfWeights = 0;
        for(ProductionTargetWeight pow : pows){
            sumOfWeights += Math.abs(pow.getValue(state));
        }
        return sumOfWeights;
    }

    private double getThisTargetProductionWeight(CurrentGenerationState state){
        ProductionTargetWeight thisProductionTargetWeight = findProductionObligatWeightForRandomizationPattern(this.randomizationPattern);
        if(thisProductionTargetWeight !=null){
            return thisProductionTargetWeight.calculateValue(state);
        }else{
            throw new RuntimeException("Unexpected TargetWeight State");
        }
    }

    private ProductionTargetWeight findProductionObligatWeightForRandomizationPattern(RandomizationPattern randomizationPattern){
        for(ProductionTargetWeight pow : pows){
            if(pow.getRandomizationPattern()==randomizationPattern){
                return pow;
            }
        }
        return null;
    }
}
