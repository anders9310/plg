package plg.test.unit.process.weights;

import org.junit.*;
import plg.generator.process.*;
import plg.generator.process.weights.BaseWeights;
import plg.generator.process.weights.ProductionObligationWeight;

public class ProductionObligationWeightTest {
    static RandomizationPattern pattern;
    static GenerationParameter genParam;
    static ProductionObligationWeight pow;
    static Obligation obligation;
    static double o_t;
    static double o_r;
    static double w_0;
    static double ruleContribution;

    @BeforeClass
    public static void setUp(){
        pattern = RandomizationPattern.PARALLEL_EXECUTION;
        genParam = GenerationParameter.NUM_ACTIVITIES;
        ruleContribution = ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, genParam);
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