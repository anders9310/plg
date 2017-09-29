package plg.analysis.bpmeter.model;

import java.util.List;

public class AnalysisResult {

    String name; //Model name
    List<Metric> metrics;

    public Metric findMetric(String name){
        for(Metric m : metrics){
            if(m.name.equals(name)){
                return m;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
