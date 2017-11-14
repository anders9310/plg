package plg.generator.process.weights;

import plg.generator.process.CurrentGenerationState;
import plg.generator.process.GenerationParameter;
import plg.generator.process.Obligation;
import plg.generator.process.RandomizationPattern;

import java.util.ArrayList;
import java.util.List;

public class ObligationWeight extends Weight{
    RandomizationPattern randomizationPattern;
    GenerationParameter generationParameter;
    List<ProductionObligationWeight> pows;

    public ObligationWeight(RandomizationPattern randomizationPattern, List<RandomizationPattern> allRandomizationPatterns, Obligation obligation){
        this.randomizationPattern = randomizationPattern;
        this.generationParameter = obligation.getType();
        pows = new ArrayList<>();
        for(RandomizationPattern pattern : allRandomizationPatterns){
            ProductionObligationWeight pow = new ProductionObligationWeight(pattern, obligation);
            pows.add(pow);
        }
    }

    @Override
    protected double calculateValue(CurrentGenerationState state) {
        double totalProductionObligationWeight = getTotalProductionObligationWeights(state);
        double thisObligationProductionWeight = getThisObligationProductionWeight(state);
        if(totalProductionObligationWeight>0){
            return thisObligationProductionWeight / totalProductionObligationWeight;
        }else{
            return 0;
        }
    }

    private double getTotalProductionObligationWeights(CurrentGenerationState state){
        double sumOfWeights = 0;
        for(ProductionObligationWeight pow : pows){
            sumOfWeights += Math.abs(pow.getValue(state));
        }
        return sumOfWeights;
    }

    private double getThisObligationProductionWeight(CurrentGenerationState state){
        ProductionObligationWeight thisProductionObligationWeight = findProductionObligatWeightForRandomizationPattern(this.randomizationPattern);
        if(thisProductionObligationWeight!=null){
            return thisProductionObligationWeight.calculateValue(state);
        }else{
            throw new RuntimeException("Unexpected ObligationWeight State");
        }
    }

    private ProductionObligationWeight findProductionObligatWeightForRandomizationPattern(RandomizationPattern randomizationPattern){
        for(ProductionObligationWeight pow : pows){
            if(pow.getRandomizationPattern()==randomizationPattern){
                return pow;
            }
        }
        return null;
    }
}