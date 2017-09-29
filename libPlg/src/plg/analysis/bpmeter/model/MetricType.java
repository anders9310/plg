package plg.analysis.bpmeter.model;

public enum MetricType{
    NUM_ACTIVITIES("NumberOfActivities"),
    NUM_GATEWAYS("NumberOfGateways");

    private final String name;
    MetricType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
