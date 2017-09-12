package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.Production;

import java.util.ArrayList;
import java.util.List;

public class ProductionWeight extends Weight{
    private List<ProductionObligationWeight> obligationWeights;

    public ProductionWeight(RandomizationPattern randomizationPattern, List<Obligation> obligations){
        obligationWeights = new ArrayList<>();
        for(Obligation obligation : obligations){
            ProductionObligationWeight pow = new ProductionObligationWeight(randomizationPattern, obligation);
            obligationWeights.add(pow);
        }
    }

    protected double calculateValue(){
        double sumOfWeights = 0.0;
        for (ProductionObligationWeight obligationWeight : obligationWeights) {
            sumOfWeights += obligationWeight.getValue();
        }
        return sumOfWeights;
    }
}
