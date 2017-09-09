package plg.generator.process.weights;

import plg.generator.process.GenerationParameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProductionWeight {
    private double value;
    private Map<GenerationParameter, ProductionObligationWeight> obligationBaseWeights;

    public ProductionWeight(Map<GenerationParameter, Double> obligationBaseWeights, Map<GenerationParameter, Integer> obligationValues){
        this.obligationBaseWeights = new HashMap<>();
        for (Object o : obligationBaseWeights.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            GenerationParameter gp = (GenerationParameter) pair.getKey();
            double obligationBaseWeight = (double) pair.getValue();
            double obligationValue = obligationValues.get(gp);

            ProductionObligationWeight pow = new ProductionObligationWeight(obligationBaseWeight, obligationValue);

            this.obligationBaseWeights.put(gp, pow);
        }
    }

    public double getValue(){
        calculateValue();
        return value;
    }

    private void calculateValue(){
        double sumOfWeights = 0.0;
        for (Object o : obligationBaseWeights.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            sumOfWeights += ((ProductionObligationWeight) pair.getValue()).getValue();
        }
        this.value = sumOfWeights;
    }
}
