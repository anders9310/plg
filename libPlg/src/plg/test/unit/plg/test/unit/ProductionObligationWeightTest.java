package plg.test.unit;

import org.junit.*;
import plg.generator.process.weights.ProductionObligationWeight;

public class ProductionObligationWeightTest {

    private double baseweight = 2.0;
    private int obligationValue = 10;

    @BeforeClass
    public static void setUp(){
    }

    @Test
    public void testValue() {
        ProductionObligationWeight pow = new ProductionObligationWeight(baseweight, obligationValue);

        double weight = pow.getValue();

        assert weight == 20.0; //baseWeight * remainingObligation^2 / obligation

    }

    @Test
    public void testValueWhenObligationFulfilled() {
        ProductionObligationWeight pow = new ProductionObligationWeight(baseweight, obligationValue);
        pow.remainingObligation = -1.0;

        double weight = pow.getValue();

        assert weight == 0.0;
    }
}