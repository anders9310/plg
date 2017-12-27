package plg.analysis.bpmeter.model;

public enum MetricType{
    NUM_NODES("NumberOfNodes"),
    NUM_ACTIVITIES("NumberOfActivities"),
    NUM_GATEWAYS("NumberOfGateways"),
    NUM_AND_GATES("NumberOfParallelGateways"),
    NUM_XOR_GATES("NumberOfExclusiveGateways"),
    CONTROL_FLOW_COMPLEXITY("ControlFlowComplexity"),
    DEPTH("Depth"),
    COEFFICIENT_OF_CONNECTIVITY("CoefficientOfConnectivity"),
    DIAMETER("Diameter");

    private final String name;
    MetricType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
