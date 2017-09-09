package plg.generator.process.weights;

public class ProductionObligationWeight extends Weight{
    private double baseWeight;
    private double obligationValue;
    public double remainingObligation;


    public ProductionObligationWeight(double baseWeight, double obligationValue){
        this.baseWeight = baseWeight;
        this.obligationValue = obligationValue;
        this.remainingObligation = obligationValue;
    }

    void calculateValue() {
        if(remainingObligation<0.0){
            value=0;
        }else{
            value = baseWeight * Math.pow(remainingObligation, 2) / obligationValue;
        }
    }
}
