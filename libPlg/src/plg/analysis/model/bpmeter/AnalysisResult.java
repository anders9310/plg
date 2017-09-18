package plg.analysis.model.bpmeter;

import java.util.List;
import java.util.Objects;

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
}
