package plg.test.unit.process.model;

import org.junit.Test;
import plg.exceptions.IllegalSequenceException;
import plg.generator.process.Metric;
import plg.generator.process.PatternFrame;
import plg.model.FlowObject;
import plg.model.Process;

public class MetricCalculatorTest {
    @Test
    public void testCalcCFCNoGates() throws IllegalSequenceException {
        Process process = new Process("myProcess");
        FlowObject startEvent = process.newStartEvent();
        FlowObject endEvent = process.newEndEvent();
        process.newSequence(startEvent, endEvent);

        assert process.getMetric(Metric.CONTROL_FLOW_COMPLEXITY) == 0;
    }

    @Test
    public void testCalcCFCAndGates() throws IllegalSequenceException {
        Process process = new Process("myProcess");
        FlowObject startEvent = process.newStartEvent();
        FlowObject andSplit = process.newParallelGateway();
        FlowObject branch1 = process.newTask("branch 1");
        FlowObject branch2 = process.newTask("branch 2");
        FlowObject andJoin = process.newParallelGateway();
        FlowObject endEvent = process.newEndEvent();

        process.newSequence(startEvent, andSplit);
        process.newSequence(andSplit, branch1);
        process.newSequence(branch1, andJoin);
        process.newSequence(andSplit, branch2);
        process.newSequence(branch2, andJoin);
        process.newSequence(andJoin, endEvent);

        assert process.getMetric(Metric.CONTROL_FLOW_COMPLEXITY) == 1;
    }

    @Test
    public void testCalcCFCXorGates() throws IllegalSequenceException {
        Process process = new Process("myProcess");

        FlowObject startEvent = process.newStartEvent();
        FlowObject xorSplit = process.newExclusiveGateway();
        FlowObject branch1 = process.newTask("branch 1");
        FlowObject branch2 = process.newTask("branch 2");
        FlowObject xorJoin = process.newExclusiveGateway();
        FlowObject endEvent = process.newEndEvent();

        process.newSequence(startEvent, xorSplit);
        process.newSequence(xorSplit, branch1);
        process.newSequence(branch1, xorJoin);
        process.newSequence(xorSplit, branch2);
        process.newSequence(branch2, xorJoin);
        process.newSequence(xorJoin, endEvent);

        assert process.getMetric(Metric.CONTROL_FLOW_COMPLEXITY) == 2;
    }

    @Test
    public void testCalcCFCLoop() throws IllegalSequenceException {
        Process process = new Process("myProcess");

        FlowObject startEvent = process.newStartEvent();
        FlowObject loopJoin = process.newExclusiveGateway();
        FlowObject body = process.newTask("branch 1");
        FlowObject rollback = process.newTask("branch 2");
        FlowObject loopSplit = process.newExclusiveGateway();
        FlowObject endEvent = process.newEndEvent();

        process.newSequence(startEvent, loopJoin);
        process.newSequence(loopJoin, body);
        process.newSequence(body, loopSplit);
        process.newSequence(loopSplit, rollback);
        process.newSequence(rollback, loopJoin);
        process.newSequence(loopSplit, endEvent);

        assert process.getMetric(Metric.CONTROL_FLOW_COMPLEXITY) == 2;
    }

    @Test
    public void testCalcCFCXorGatesWithSkip() throws IllegalSequenceException {
        Process process = new Process("myProcess");

        FlowObject startEvent = process.newStartEvent();
        FlowObject xorSplit = process.newExclusiveGateway();
        FlowObject xorJoin = process.newExclusiveGateway();
        FlowObject endEvent = process.newEndEvent();

        process.newSequence(startEvent, xorSplit);
        process.newSequence(xorSplit, xorJoin);
        process.newSequence(xorSplit, xorJoin);
        process.newSequence(xorJoin, endEvent);

        assert process.getMetric(Metric.CONTROL_FLOW_COMPLEXITY) == 0;
    }
}
