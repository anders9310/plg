package plg.generator.process.weights;

import plg.generator.process.Obligation;

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
        double smallestValue = 1;
        if(obligation.getRemaining()<0.0){
            return smallestValue;
        } else if(obligation.getValue()<=0){
            return smallestValue;
        }else{
            return smallestValue + baseWeight * Math.pow(obligation.getRemaining(), 2) / obligation.getValue();
        }
    }
}
