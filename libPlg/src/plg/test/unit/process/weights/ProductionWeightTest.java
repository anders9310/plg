package plg.test.unit.process.weights;

import org.junit.BeforeClass;
import org.junit.Test;
import plg.generator.process.Target;
import plg.generator.process.RandomizationPattern;

import java.util.List;

public class ProductionWeightTest {
    private static int valueForAllObligations = 4;
    private static RandomizationPattern productionPattern;
    private static List<Target> targets;

    @BeforeClass
    public static void setUp(){
        setUpTargets();
        productionPattern = RandomizationPattern.PARALLEL_EXECUTION;
    }

    private static void setUpTargets() {
        /*obligations = new LinkedList<>();
        obligations.add(new Obligation(Metric.NUM_ACTIVITIES, valueForAllObligations));
        obligations.add(new Obligation(Metric.NUM_GATEWAYS, valueForAllObligations));*/
    }

    @Test
    public void testValue() {
        /*ProductionWeight pw = new ProductionWeight(productionPattern, obligations);

        double weight = pw.getValue();

        assert weight == 14.0; //sum( baseWeight * remainingObligation^2 / obligation )*/

    }
}
