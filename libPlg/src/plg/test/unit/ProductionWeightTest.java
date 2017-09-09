import org.junit.*;
import plg.generator.process.GenerationParameter;
import plg.generator.process.weights.ProductionObligationWeight;
import plg.generator.process.weights.ProductionWeight;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductionWeightTest {

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
        double obligationBaseWeight = 2.0;
        obligationBaseWeights = new HashMap<>();
        for(GenerationParameter gp : genParams){
            obligationBaseWeights.put(gp, obligationBaseWeight);
        }
    }

    private static void setUpObligationValues() {
        int obligationValue = 4;
        obligationValues = new HashMap<>();
        for(GenerationParameter gp : genParams){
            obligationValues.put(gp, obligationValue);
        }
    }

    @Test
    public void testValue() {
        ProductionWeight po = new ProductionWeight(obligationBaseWeights, obligationValues);

        double weight = po.getValue();

        assert weight == 16.0; //baseWeight * remainingObligation^2 / obligation

    }
}
