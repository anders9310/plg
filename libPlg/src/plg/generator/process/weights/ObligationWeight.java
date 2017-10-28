package plg.generator.process.weights;

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
    protected double calculateValue() {
        double totalProductionObligationWeight = getTotalProductionObligationWeights();
        double thisObligationProductionWeight = getThisObligationProductionWeight();
        if(totalProductionObligationWeight>0){
            return thisObligationProductionWeight / totalProductionObligationWeight;
        }else{
            return 0;
        }
    }

    private double getTotalProductionObligationWeights(){
        double sumOfWeights = 0;
        for(ProductionObligationWeight pow : pows){
            sumOfWeights += Math.abs(pow.calculateValue());
        }
        return sumOfWeights;
    }

    private double getThisObligationProductionWeight(){
        ProductionObligationWeight thisProductionObligationWeight = findProductionObligatWeightForRandomizationPattern(this.randomizationPattern);
        if(thisProductionObligationWeight!=null){
            return thisProductionObligationWeight.calculateValue();
        }else{
            throw new RuntimeException("Unexpected ObligationWeightState");
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
