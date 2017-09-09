package plg.test.unit;

import org.junit.*;
import plg.generator.process.GenerationParameter;
import plg.generator.process.weights.ProductionWeight;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductionWeightTest {
    private static int valueForAllObligations = 4;
    private static double baseWeightForAllObligations = 2.0;
    private static List<GenerationParameter> genParams;
    private static Map<GenerationParameter, Double> obligationBaseWeights;
    private static Map<GenerationParameter, Integer> obligationValues;

    @BeforeClass
    public static void setUp(){
        setUpGenParams();
        setUpObligationBaseWeights();
        setUpObligationValues();
    }

    private static void setUpGenParams() {
        genParams = new LinkedList<>();
        genParams.add(GenerationParameter.NUM_ACTIVITIES);
        genParams.add(GenerationParameter.NUM_GATEWAYS);
    }

    private static void setUpObligationBaseWeights() {
        ProductionWeightTest.obligationBaseWeights = new HashMap<>();
        for(GenerationParameter gp : genParams){
            ProductionWeightTest.obligationBaseWeights.put(gp, baseWeightForAllObligations);
        }
    }

    private static void setUpObligationValues() {
        ProductionWeightTest.obligationValues = new HashMap<>();
        for(GenerationParameter gp : genParams){
            ProductionWeightTest.obligationValues.put(gp, valueForAllObligations);
        }
    }

    @Test
    public void testValue() {
        ProductionWeight pw = new ProductionWeight(obligationBaseWeights, obligationValues);

        double weight = pw.getValue();

        assert weight == 16.0; //baseWeight * remainingObligation^2 / obligation

    }
}
