package plg.model;

import plg.generator.process.*;
import plg.model.gateway.ExclusiveGateway;
import plg.model.gateway.Gateway;
import plg.model.gateway.ParallelGateway;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MetricCalculator {
    private Process process;
    private Process processWithoutUnknownComponents;
    private Map<RandomizationPattern, Integer> potentialIncreasesCache;

    public MetricCalculator(Process process) {
        this.process = process;
        this.potentialIncreasesCache = new HashMap<>();
    }

    public double calculateMetric(Metric metric){
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
            case CONTROL_FLOW_COMPLEXITY:
                return calcCFC();
            default:
                throw new IllegalArgumentException("Cannot handle the metric: " + metric.name());
        }
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public double getContributionOf(CurrentGenerationState state, Metric metric, RandomizationPattern pattern){
        try{
            return ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, metric);
        }catch(IllegalArgumentException e){//production contribution has to be calculated runtime
            return calculateContribution(state, metric, pattern);
        }
    }

    public int getPotentialIncreaseOf(RandomizationPattern pattern){
        if(potentialIncreasesCache.get(pattern)==null){
            CurrentGenerationState state = new CurrentGenerationState(0, true, true).setParentComponent(process.getUnknownComponents().get(0));
            potentialIncreasesCache.put(pattern,calculatePotentialIncrease(state, pattern));
        }
        return potentialIncreasesCache.get(pattern);
    }

    private int calculatePotentialIncrease(CurrentGenerationState state, RandomizationPattern pattern){
        int initialPotential = process.getNumUnknownComponents();
        int projectedPotential = simulateGenerationOf(state, pattern).getNumUnknownComponents();
        return projectedPotential - initialPotential;
    }

    private double calculateContribution(CurrentGenerationState state, Metric metric, RandomizationPattern pattern) {
        double initialMetric = process.getMetric(metric);
        double projectedMetric = simulateGenerationOf(state, pattern).getMetric(metric);
        return projectedMetric - initialMetric;
    }

    private Process simulateGenerationOf(CurrentGenerationState state, RandomizationPattern pattern) {
        Process simulationProcess = (Process) process.clone();
        CurrentGenerationState simulationState = state.makeCopy(simulationProcess);
        RandomizationConfiguration parameters = new ParameterRandomizationConfiguration(simulationProcess, new HashMap<>());
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

    private double calcCFC() {
        int andGateContributions = calcAndGateContributionForCFC();
        int xorGateContributions = calcXorGateContributionForCFC();
        return andGateContributions + xorGateContributions;
    }

    private int calcXorGateContributionForCFC() {
        List<ExclusiveGateway> xorGates = new LinkedList<>();
        for(Gateway g : processWithoutUnknownComponents.getGateways()){
            if(g instanceof ExclusiveGateway){
                xorGates.add((ExclusiveGateway) g);
            }
        }
        List<ExclusiveGateway> xorGateSplits = new LinkedList<>();
        for(ExclusiveGateway g : xorGates){
            if(g.getIncomingObjects().size()==1 && g.getOutgoingObjects().size()>1){
                xorGateSplits.add(g);
            }
        }
        int sumOfFanout = 0;
        for(ExclusiveGateway g : xorGateSplits){
            sumOfFanout += g.getOutgoingObjects().size();
        }
        return sumOfFanout;
    }

    private int calcAndGateContributionForCFC() {//TODO there is probably a bug here
        List<ParallelGateway> andGates = new LinkedList<>();
        for(Gateway g : processWithoutUnknownComponents.getGateways()){
            if(g instanceof ParallelGateway){
                andGates.add((ParallelGateway) g);
            }
        }
        List<ParallelGateway> andGateSplits = new LinkedList<>();
        for(ParallelGateway g : andGates){
            if(g.getIncomingObjects().size()==1 && g.getOutgoingObjects().size()>1){
                andGateSplits.add(g);
            }
        }
        return andGateSplits.size();
    }
}
