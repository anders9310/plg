package plg.analysis.model.bpmeter;

public enum MetricType{
    NUM_ACTIVITIES("NumberOfActivities");

    private final String name;
    MetricType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
