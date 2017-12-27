package plg.generator.process;

import plg.model.Process;

import java.util.Map;

public class ObligationsProcessGenerator extends ProcessGenerator{
    public ObligationsProcessGenerator(Process process, Map<Metric, Double> inputs){
        super(process, new DynamicRandomizationConfiguration(process, inputs));
    }

    public Map<String, Map<String, Double>> getGenerationResults() {
        if(!hasPerformedGeneration()){
            return null;
        }
        return ((DynamicRandomizationConfiguration)parameters).getStatus();
    }
}
