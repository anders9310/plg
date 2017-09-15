package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.RandomizationPattern;

public class ProductionObligationWeight extends Weight{
    private double baseWeight;
    private Obligation obligation;
    private RandomizationPattern productionPattern;

    public ProductionObligationWeight(RandomizationPattern productionPattern, Obligation obligation){
        this.productionPattern = productionPattern;
        this.obligation = obligation;
        initBaseWeight();
    }

    private void initBaseWeight() {
        baseWeight = BaseWeights.BASE_WEIGHTS.getBaseWeight(this.productionPattern, this.obligation.getType());
    }

    protected double calculateValue() {
        double zeroIntersectionValue = 1;
        double coreValue;
        if(obligation.getValue()>0){
            coreValue = zeroIntersectionValue + baseWeight * Math.pow(obligation.getRemaining(), 2) / obligation.getValue();
        }else{
            coreValue = zeroIntersectionValue + baseWeight * Math.pow(obligation.getRemaining(), 2);
        }

        if(obligation.getRemaining()>=0.0){
            return coreValue;
        }else{
            return 1 / ( coreValue);
        }

    }
}
