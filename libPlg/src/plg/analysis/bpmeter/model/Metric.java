package plg.analysis.bpmeter.model;

import java.util.List;

public class Metric {
    String name;
    String category;
    String type; //Single value or distribution
    List<Value> values;

    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public String getType() {
        return type;
    }
    public List<Value> getValues() {
        return values;
    }
}
