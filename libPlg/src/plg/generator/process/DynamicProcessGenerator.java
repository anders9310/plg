package plg.generator.process;

import plg.model.Process;

import java.util.Map;

public class DynamicProcessGenerator extends ProcessGenerator{
    public DynamicProcessGenerator(Process process, Map<Metric, Double> inputs){
        super(process, new DynamicRandomizationConfiguration(process, inputs));
    }

    public Map<String, Map<String, Double>> getGenerationResults() {
        if(!hasPerformedGeneration()){
            return null;
        }
        return ((DynamicRandomizationConfiguration)parameters).getStatus();
    }
}
