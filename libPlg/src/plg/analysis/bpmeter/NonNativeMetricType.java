package plg.analysis.bpmeter;

public enum NonNativeMetricType {
    AVG_DEGREE_OF_CONNECTORS("AvgDegreeOfConnectors"),
    SEQUENTIALITY("Sequentiality"),
    CONNECTOR_HETEROGENEITY("ConnectorHeterogeneity"),
    NUMBER_OF_CYCLES("NumberOfCycles"),
    TOKEN_SPLIT("TokenSplit");

    private final String name;
    NonNativeMetricType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
