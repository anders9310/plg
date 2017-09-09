package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;

public class ProductionObligationWeight {
    private double value;
    double baseWeight;
    double obligationValue;
    public double remainingObligation;


    public ProductionObligationWeight(double baseWeight, double obligationValue){
        this.baseWeight = baseWeight;
        this.obligationValue = obligationValue;
        this.remainingObligation = obligationValue;
    }

    public double getValue(){
        calculateValue();
        return value;
    }

    private void calculateValue() {
        if(remainingObligation<0.0){
            value=0;
        }else{
            value = baseWeight * Math.pow(remainingObligation, 2) / obligationValue;
        }
    }
}
