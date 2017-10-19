package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.Production;
import plg.generator.process.RandomizationPattern;
import plg.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProductionWeight extends Weight{
    private List<ObligationWeight> obligationWeights;
    private RandomizationPattern randomizationPattern;

    public ProductionWeight(RandomizationPattern randomizationPattern, List<Obligation> obligations, List<RandomizationPattern> allRandomizationPatterns){
        this.randomizationPattern = randomizationPattern;
        obligationWeights = new ArrayList<>();
        for(Obligation obligation : obligations){
            ObligationWeight ow = new ObligationWeight(randomizationPattern, allRandomizationPatterns, obligation);
            obligationWeights.add(ow);
        }
    }

    protected double calculateValue(){
        double sumOfWeights = 0;
        for (ObligationWeight ow : obligationWeights) {
            sumOfWeights += ow.getValue();
        }
        return sumOfWeights;
    }
}
