package plg.test.unit.process.weights;

import org.junit.BeforeClass;
import org.junit.Test;
import plg.generator.process.GenerationParameter;
import plg.generator.process.Obligation;
import plg.generator.process.weights.ProductionWeight;
import plg.generator.process.RandomizationPattern;

import java.util.LinkedList;
import java.util.List;

public class ProductionWeightTest {
    private static int valueForAllObligations = 4;
    private static RandomizationPattern productionPattern;
    private static List<Obligation> obligations;

    @BeforeClass
    public static void setUp(){
        setUpObligations();
        productionPattern = RandomizationPattern.PARALLEL_EXECUTION;
    }

    private static void setUpObligations() {
        obligations = new LinkedList<>();
        obligations.add(new Obligation(GenerationParameter.NUM_ACTIVITIES, valueForAllObligations));
        obligations.add(new Obligation(GenerationParameter.NUM_GATEWAYS, valueForAllObligations));
    }

    @Test
    public void testValue() {
        ProductionWeight pw = new ProductionWeight(productionPattern, obligations);

        double weight = pw.getValue();

        assert weight == 14.0; //sum( baseWeight * remainingObligation^2 / obligation )

    }
}
