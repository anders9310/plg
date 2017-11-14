package plg.model;

import plg.generator.process.*;
import plg.model.gateway.ExclusiveGateway;
import plg.model.gateway.Gateway;
import plg.model.gateway.ParallelGateway;
import plg.utils.Logger;

import java.util.List;

public class MetricCalculator {
    private Process process;
    private Process processWithoutUnknownComponents;

    public MetricCalculator(Process process) {
        this.process = process;
    }

    public double calculateMetric(GenerationParameter metric){
        processWithoutUnknownComponents = ((Process)process.clone()).replaceUnknownComponentsWithNull();
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

    public void setProcess(Process process) {
        this.process = process;
    }

    public double getContributionOf(CurrentGenerationState state, GenerationParameter metric, RandomizationPattern pattern){
        try{
            return ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, metric);
        }catch(IllegalArgumentException e){//production contribution has to be calculated runtime
            return calculateContribution(state, metric, pattern);
        }
    }

    private double calculateContribution(CurrentGenerationState state, GenerationParameter metric, RandomizationPattern pattern) {
        double initialMetric = process.getMetric(metric);
        double projectedMetric = simulateGenerationOf(state, pattern).getMetric(metric);
        return projectedMetric - initialMetric;
    }

    private Process simulateGenerationOf(CurrentGenerationState state, RandomizationPattern pattern) {
        Process simulationProcess = (Process) process.clone();
        CurrentGenerationState simulationState = state.makeCopy(simulationProcess);
        RandomizationConfiguration parameters = new ParameterRandomizationConfiguration(simulationProcess,0,0,0,0,0,0);
        ProcessGenerator simulator = new ProcessGenerator(simulationProcess, parameters);

        switch (pattern) {
            case SEQUENCE:
                simulator.replaceComponentWithSequencePatternDummies(simulationProcess, simulationState.parentComponent);
                break;
            case PARALLEL_EXECUTION:
                simulator.replaceComponentWithAndPatternDummies(simulationProcess, simulationState.parentComponent);
                break;
            case MUTUAL_EXCLUSION:
                simulator.replaceComponentWithXorPatternDummies(simulationProcess, simulationState.parentComponent);
                break;
            case LOOP:
                simulator.replaceComponentWithLoopPatternDummies(simulationProcess, simulationState.parentComponent);
                break;
            case SINGLE_ACTIVITY:
                PatternFrame activity = simulator.newActivity(simulationProcess);
                PatternFrame.connect(simulationState.parentComponent.getIncomingObjects().get(0), activity.getLeftBound()).connect(simulationState.parentComponent.getOutgoingObjects().get(0));
                simulationProcess.removeComponent(simulationState.parentComponent);
                break;
            case SKIP:
                PatternFrame.connect(simulationState.parentComponent.getIncomingObjects().get(0), simulationState.parentComponent.getOutgoingObjects().get(0));
                simulationProcess.removeComponent(simulationState.parentComponent);
                break;
            default:
                throw new RuntimeException("No case for production rule " + pattern.name());
                /*process.removeComponent(localState.parentComponent);
                generatedFrame = newActivity();*/
        }
        return simulationProcess;
    }

    private Process simulatePatternGeneration(RandomizationPattern pattern) {
        //Simulate using process generator
        return new Process("replace");
    }

    private double calcNumActivities() {
        return processWithoutUnknownComponents.getTasks().size();
    }

    private double calcNumGateways() {
        return processWithoutUnknownComponents.getGateways().size();
    }

    private double calcNumAndGates() {
        List<Gateway> gateways =  processWithoutUnknownComponents.getGateways();
        int numAndGates = 0;
        for(Gateway gateway : gateways){
            if(gateway instanceof ParallelGateway){
                numAndGates++;
            }
        }
        return numAndGates;
    }

    private double calcNumXorGates() {
        List<Gateway> gateways =  processWithoutUnknownComponents.getGateways();
        int numXorGates = 0;
        for(Gateway gateway : gateways){
            if(gateway instanceof ExclusiveGateway){
                numXorGates++;
            }
        }
        return numXorGates;
    }

    private double calcCoefficientOfNetworkConnectivity(){
        double projectedNumArcs = processWithoutUnknownComponents.getSequences().size();
        double numNodes = processWithoutUnknownComponents.getComponents().size() - processWithoutUnknownComponents.getSequences().size();
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
