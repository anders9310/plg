package plg.generator.process.weights;

import plg.generator.process.Obligation;
import plg.generator.process.RandomizationPattern;

public class ProductionObligationWeight extends Weight{
    private double baseWeight;
    private Obligation obligation;
    private RandomizationPattern productionPattern;
    private double zeroIntersectionValue = 1;

    public ProductionObligationWeight(RandomizationPattern productionPattern, Obligation obligation){
        this.productionPattern = productionPattern;
        this.obligation = obligation;
        initBaseWeight();
    }

    private void initBaseWeight() {
        baseWeight = BaseWeights.BASE_WEIGHTS.getBaseWeight(this.productionPattern, this.obligation.getType());
    }

    protected double calculateValue() {
        //return calcExpValue();
        return getLinearValue();
        //getSquareValue();
    }

    private double calcExpValue(){
        if(obligation.getValue()==0){
            return 0;
        }else{
            return getExpValue();
        }
    }

    private double getLinearValue(){
        double coreValue = getLinearCore();
        return calcValue(coreValue);
    }

    private double getSquareValue(){
        double coreValue = getSquareCore();
        return calcValue(coreValue);
    }

    private double getSquareCore(){
        double coreValue = zeroIntersectionValue + baseWeight * Math.pow(obligation.getRemaining(), 2);
        return coreValue;
    }

    private double getLinearCore(){
        double coreValue = zeroIntersectionValue + baseWeight * Math.abs(obligation.getRemaining());
        return coreValue;
    }

    private double calcValue(double coreValue){
        if(obligation.getRemaining()>=0.0){
            return coreValue;
        }else{
            return 1 / ( coreValue);
        }
    }

    private double getExpValue(){
        return Math.exp(baseWeight * obligation.getRemaining()) + zeroIntersectionValue-1;
    }
}
