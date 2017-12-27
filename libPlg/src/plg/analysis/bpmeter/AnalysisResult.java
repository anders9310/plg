package plg.analysis.bpmeter;

import plg.analysis.bpmeter.model.Metric;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.analysis.bpmeter.model.Value;

import java.util.List;

public class AnalysisResult {
    String name; //Model name
    List<Metric> metrics;

    public AnalysisResult(RawAnalysisResult rawResult){
        metrics = rawResult.getMetrics();

        /*for(NonNativeMetricType m : NonNativeMetricType.values()){
            switch (m){
                case AVG_DEGREE_OF_CONNECTORS:
                    calculateAvgDegreeOfConnectors();
                case CONNECTOR_HETEROGENEITY:
                    calculateConnectorHeterogeneity();
                case NUMBER_OF_CYCLES:
                    calculateNumberOfCycles();
                case TOKEN_SPLIT:
                    calculateTokenSplit();
                case SEQUENTIALITY:
                    calculateSequentiality();
            }
        }*/
    }

    private double calculateAvgDegreeOfConnectors() {
        List<Value> exValues  = findMetric("ExclusiveGatewayFanOut").getValues();

        return 0;
    }

    public Metric findMetric(String name){
        for(Metric m : metrics){
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
    public List<Metric> getMetrics() {
        return metrics;
    }
}
