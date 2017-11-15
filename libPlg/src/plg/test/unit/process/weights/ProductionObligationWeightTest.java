package plg.test.unit.process.weights;

import org.junit.*;
import plg.generator.process.*;
import plg.generator.process.weights.ProductionObligationWeight;

public class ProductionObligationWeightTest {
    static RandomizationPattern pattern;
    static Metric genParam;
    static ProductionObligationWeight pow;
    static Obligation obligation;
    static double o_t;
    static double o_r;
    static double w_0;
    static double ruleContribution;

    @BeforeClass
    public static void setUp(){
        pattern = RandomizationPattern.PARALLEL_EXECUTION;
        genParam = Metric.NUM_ACTIVITIES;
        //ruleContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, genParam);
    }

    @Test
    public void testCompare(){
        double v1 = 1.5;
        double v2 = 1;
        double threshold = 0.5;
        assert ProductionObligationWeight.compare(v1, v2, threshold)==0;

        v1 = 1.50001;
        assert ProductionObligationWeight.compare(v1, v2, threshold)==1;

        v1 = 0.499999;
        assert ProductionObligationWeight.compare(v1, v2, threshold)==-1;

    }

    @Test
    public void testValueRemainingPositiveInitial() {
        /*o_t = 10;
        o_r = o_t;
        obligation = new Obligation(genParam, o_t);
        pow = new ProductionObligationWeight(pattern, obligation);

        double weight = pow.getValue();

        double expectedWeight = 1 + w_0 * Math.pow(o_r, 2) / o_t;
        assert weight == expectedWeight;*/
    }

    @Test
    public void testValueRemainingPositiveAfterProduction() {
        /*o_t = 10;
        o_r = o_t - ProductionRuleContributions.CONTRIBUTIONS.getContributionOf(pattern, genParam);
        obligation = new Obligation(genParam,o_t);
        pow = new ProductionObligationWeight(pattern, obligation);

        obligation.updatePotential(pattern);
        double weight = pow.getValue();

        double expectedWeight = 1 + w_0 * Math.pow(o_r, 2) / o_t;
        assert weight == expectedWeight;*/
    }

    @Test
    public void testValueRemainingZero(){
        /*o_t = 10;
        o_r = 0;
        obligation = new Obligation(genParam,(int)o_t);
        pow = new ProductionObligationWeight(pattern, obligation);
        updateObligation((int) (o_t/ruleContribution));

        double weight = pow.getValue();

        double expectedWeight = 1.0;
        assert weight == expectedWeight;*/
    }

    @Test
    public void testValueRemainingNegative(){
        /*o_t = 10;
        o_r = -ruleContribution;
        obligation = new Obligation(genParam,(int)o_t);
        pow = new ProductionObligationWeight(pattern, obligation);
        updateObligation((int) (o_t/ruleContribution + 1));

        double weight = pow.getValue();

        double expectedWeight = 1 / (1 + w_0 * Math.pow(o_r, 2) / o_t);
        assert weight == expectedWeight;*/
    }

    @Test
    public void testValueObligationZero(){
        /*o_t = 0;
        o_r = 0;
        obligation = new Obligation(genParam,(int)o_t);
        pow = new ProductionObligationWeight(pattern, obligation);

        double weight = pow.getValue();

        double expected = w_0*Math.pow(o_r, 2) + 1;
        assert weight == expected;*/
    }
    @Test
    public void testValueObligationZeroAndRemainingNegative(){
        /*o_t = 0;
        o_r = -ruleContribution;
        obligation = new Obligation(genParam,(int)o_t);
        pow = new ProductionObligationWeight(pattern, obligation);
        updateObligation(1);

        double weight = pow.getValue();

        double expected = 1 / (w_0*Math.pow(o_r, 2) + 1);
        assert weight == expected;*/
    }
}