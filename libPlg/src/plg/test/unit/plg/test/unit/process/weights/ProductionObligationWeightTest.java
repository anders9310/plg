package plg.test.unit.process.weights;

import org.junit.*;
import plg.generator.process.GenerationParameter;
import plg.generator.process.Obligation;
import plg.generator.process.weights.ProductionObligationWeight;
import plg.generator.process.weights.RandomizationPattern;

public class ProductionObligationWeightTest {

    @BeforeClass
    public static void setUp(){

    }

    @Test
    public void testValue() {
        RandomizationPattern pattern = RandomizationPattern.PARALLEL_EXECUTION;
        Obligation obligation = new Obligation(GenerationParameter.NUM_ACTIVITIES,10);
        ProductionObligationWeight pow = new ProductionObligationWeight(pattern, obligation);

        double weight = pow.getValue();

        assert weight == 21.0; //1 + baseWeight * remainingObligation^2 / obligation

    }

    @Test
    public void testValueWhenObligationFulfilled() {
        RandomizationPattern pattern = RandomizationPattern.PARALLEL_EXECUTION;
        Obligation obligation = new Obligation(GenerationParameter.NUM_ACTIVITIES,-1);
        ProductionObligationWeight pow = new ProductionObligationWeight(pattern, obligation);

        double weight = pow.getValue();

        assert weight == 1.0;
    }
}