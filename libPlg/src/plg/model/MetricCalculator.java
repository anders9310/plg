package plg.model;

import plg.generator.process.GenerationParameter;
import plg.generator.process.ProductionRuleContributions;
import plg.generator.process.RandomizationPattern;
import plg.model.gateway.ExclusiveGateway;
import plg.model.gateway.Gateway;
import plg.model.gateway.ParallelGateway;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Set;

public class MetricCalculator {
    private Process p;

    public MetricCalculator(Process process) {
        this.p = process;
    }

    public double getMetric(GenerationParameter metric){
        switch(metric){
            case NUM_ACTIVITIES:
                return calcNumActivities();
            case NUM_GATEWAYS:
                return calcNumGateways();
            case NUM_AND_GATES:
                return calcNumAndGates();
            case NUM_XOR_GATES:
                return calcNumXorGates();
            case COEFFICIENT_OF_NETWORK_CONNECTIVITY:
                return calcCoefficientOfNetworkConnectivity();
            default:
                throw new IllegalArgumentException("Cannot handle the metric: " + metric.name());
        }
    }

    public double getContribution(GenerationParameter metric, RandomizationPattern pattern){
        try{
            return ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, metric);
        }catch(Exception e){//production contribution has to be calculated runtime TODO: find correct exception
            return calculateContribution(metric, pattern);
        }
    }

    private double calculateContribution(GenerationParameter metric, RandomizationPattern pattern) {
        double initialMetric = getMetric(metric);
        double projectedMetric = simulatePatternGeneration(pattern).getMetric(metric);
        return projectedMetric - initialMetric;
    }

    private Process simulatePatternGeneration(RandomizationPattern pattern) {
        //Simulate using process generator
        return new Process("replace");
    }

    private double calcNumActivities() {
        return p.getTasks().size();
    }

    private double calcNumGateways() {
        return p.getGateways().size();
    }

    private double calcNumAndGates() {
        Set<Gateway> gateways =  p.getGateways();
        int numAndGates = 0;
        for(Gateway gateway : gateways){
            if(gateway instanceof ParallelGateway){
                numAndGates++;
            }
        }
        return numAndGates;
    }

    private double calcNumXorGates() {
        Set<Gateway> gateways =  p.getGateways();
        int numXorGates = 0;
        for(Gateway gateway : gateways){
            if(gateway instanceof ExclusiveGateway){
                numXorGates++;
            }
        }
        return numXorGates;
    }

    private double calcCoefficientOfNetworkConnectivity(){
        double projectedNumArcs = p.getComponents().size() - p.getSequences().size() - p.getGateways().size() - 1 + (5.0/2.0)*p.getGateways().size() - p.getNumSkips();
        double numNodes = p.getComponents().size() - p.getSequences().size();
        if(!(projectedNumArcs>0)){
            return 0;
        }
        else if(!(numNodes>0)) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return projectedNumArcs / numNodes;
        }
    }
}
