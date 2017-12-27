package plg.model;

import plg.generator.process.*;
import plg.model.gateway.ExclusiveGateway;
import plg.model.gateway.Gateway;
import plg.model.gateway.ParallelGateway;
import plg.model.graphs.ElementaryCyclesSearch;
import plg.model.sequence.Sequence;

import java.util.*;

public class MetricCalculator {
    private Process process;
    private Process processWithoutPlaceholders;
    private Map<RandomizationPattern, Integer> placeholderIncreasesCache;

    public MetricCalculator(Process process) {
        this.process = process;
        this.placeholderIncreasesCache = new HashMap<>();
    }

    public double calculateMetric(Metric metric) {
        processWithoutPlaceholders = ((Process) process.clone()).replacePlaceholdersWithNull();
        switch (metric) {
            case NUM_NODES:
                return calcNumNodes();
            case NUM_ACTIVITIES:
                return calcNumActivities();
            case NUM_GATEWAYS:
                return calcNumGateways();
            case NUM_AND_GATES:
                return calcNumAndGates();
            case NUM_XOR_GATES:
                return calcNumXorGates();
            case AVG_DEGREE_OF_CONNECTORS:
                return calcAvgDegreeOfConnectors();
            case COEFFICIENT_OF_NETWORK_CONNECTIVITY:
                return calcCoefficientOfNetworkConnectivity();
            case SEQUENTIALITY:
                return calcSequentiality();
            case CONNECTOR_HETEROGENEITY:
                return calcConnectorHeterogeneity();
            case CONTROL_FLOW_COMPLEXITY:
                return calcCFC();
            case NUMBER_OF_CYCLES:
                return calcNumCycles();
            case TOKEN_SPLIT:
                return calcTokenSplit();
            default:
                throw new IllegalArgumentException("Cannot handle the metric: " + metric.name());
        }
    }

    public double getContributionOf(CurrentGenerationState state, Metric metric, RandomizationPattern pattern) {
        try {
            return ProductionRuleContributions.CONTRIBUTIONS.getContribution(pattern, metric);
        } catch (IllegalArgumentException e) {//production contribution has to be calculated runtime
            return calculateContribution(state, metric, pattern);
        }
    }

    public int getPlaceholderIncrease(RandomizationPattern pattern) {
        if (placeholderIncreasesCache.get(pattern) == null) {
            CurrentGenerationState state = new CurrentGenerationState(0, true, true).setParentComponent(process.getPlaceholderComponents().get(0));
            placeholderIncreasesCache.put(pattern, calculatePlaceholderIncrease(state, pattern));
        }
        return placeholderIncreasesCache.get(pattern);
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    private int calculatePlaceholderIncrease(CurrentGenerationState state, RandomizationPattern pattern) {
        int initialPlaceholders = process.getNumPlaceholderComponents();
        int projectedPlaceholders = simulateGenerationOf(state, pattern).getNumPlaceholderComponents();
        return projectedPlaceholders - initialPlaceholders;
    }

    private double calculateContribution(CurrentGenerationState state, Metric metric, RandomizationPattern pattern) {
        double initialMetric = process.getMetric(metric);
        double projectedMetric = simulateGenerationOf(state, pattern).getMetric(metric);
        return projectedMetric - initialMetric;
    }

    private Process simulateGenerationOf(CurrentGenerationState state, RandomizationPattern pattern) {
        Process simulationProcess = (Process) process.clone();
        CurrentGenerationState simulationState = state.makeCopy(simulationProcess);
        RandomizationConfiguration parameters = new DynamicRandomizationConfiguration(simulationProcess, new HashMap<>());
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

    private double calcNumNodes() {
        return processWithoutPlaceholders.getComponents().size() - processWithoutPlaceholders.getSequences().size();
    }

    private double calcNumActivities() {
        return processWithoutPlaceholders.getTasks().size();
    }

    private double calcNumGateways() {
        return processWithoutPlaceholders.getGateways().size();
    }

    private double calcNumAndGates() {
        List<Gateway> gateways = processWithoutPlaceholders.getGateways();
        int numAndGates = 0;
        for (Gateway gateway : gateways) {
            if (gateway instanceof ParallelGateway) {
                numAndGates++;
            }
        }
        return numAndGates;
    }

    private double calcNumXorGates() {
        List<Gateway> gateways = processWithoutPlaceholders.getGateways();
        int numXorGates = 0;
        for (Gateway gateway : gateways) {
            if (gateway instanceof ExclusiveGateway) {
                numXorGates++;
            }
        }
        return numXorGates;
    }

    private double calcDensity() {
        double numArcs = processWithoutPlaceholders.getSequences().size();
        double numNodes = processWithoutPlaceholders.getComponents().size() - processWithoutPlaceholders.getSequences().size();
        return numNodes > 1 ? numArcs / (numNodes * (numNodes - 1)) : Double.POSITIVE_INFINITY;
    }

    private double calcAvgDegreeOfConnectors() {
        double numGateways = processWithoutPlaceholders.getGateways().size();
        double numConnections = 0;
        for (Gateway g : processWithoutPlaceholders.getGateways()) {
            numConnections += g.getOutgoingObjects().size();
        }
        return numGateways > 0 ? numConnections/numGateways : 0;
    }

    private double calcCoefficientOfNetworkConnectivity() {
        double numArcs = processWithoutPlaceholders.getSequences().size();
        double numNodes = processWithoutPlaceholders.getComponents().size() - processWithoutPlaceholders.getSequences().size();
        return numNodes != 0 ? numArcs / numNodes : Double.POSITIVE_INFINITY;
    }

    private double calcCFC() {
        int andGateContributions = calcAndGateContributionForCFC();
        int xorGateContributions = calcXorGateContributionForCFC();
        return andGateContributions + xorGateContributions;
    }

    private int calcXorGateContributionForCFC() {
        List<ExclusiveGateway> xorGates = new LinkedList<>();
        for (Gateway g : processWithoutPlaceholders.getGateways()) {
            if (g instanceof ExclusiveGateway) {
                xorGates.add((ExclusiveGateway) g);
            }
        }
        List<ExclusiveGateway> xorGateSplits = new LinkedList<>();
        for (ExclusiveGateway g : xorGates) {
            if (g.getIncomingObjects().size() == 1 && g.getOutgoingObjects().size() > 1) {
                xorGateSplits.add(g);
            }
        }
        int sumOfFanout = 0;
        for (ExclusiveGateway g : xorGateSplits) {
            sumOfFanout += g.getOutgoingObjects().size();
        }
        return sumOfFanout;
    }

    private int calcAndGateContributionForCFC() {
        List<ParallelGateway> andGates = new LinkedList<>();
        for (Gateway g : processWithoutPlaceholders.getGateways()) {
            if (g instanceof ParallelGateway) {
                andGates.add((ParallelGateway) g);
            }
        }
        List<ParallelGateway> andGateSplits = new LinkedList<>();
        for (ParallelGateway g : andGates) {
            if (g.getIncomingObjects().size() == 1 && g.getOutgoingObjects().size() > 1) {
                andGateSplits.add(g);
            }
        }
        return andGateSplits.size();
    }

    private double calcConnectorHeterogeneity() {
        double numGateways = processWithoutPlaceholders.getGateways().size();
        double numParallelGateways = 0;
        double numExclusiveGateways = 0;
        for (Gateway g : processWithoutPlaceholders.getGateways()) {
            if (g instanceof ParallelGateway) {
                numParallelGateways++;
            } else if (g instanceof ExclusiveGateway) {
                numExclusiveGateways++;
            }
        }
        if(numParallelGateways + numExclusiveGateways != numGateways){
            throw new RuntimeException("number of parallel and exclusive gateways does not match the total number of gateways");
        }

        if(numGateways > 0){
            double clPar = numParallelGateways / numGateways;
            double clExc = numExclusiveGateways / numGateways;
            double chPar = clPar > 0 ? clPar * log(clPar, 2) : 0;
            double chExc = clExc > 0 ? clExc * log(clExc, 2) : 0;
            return -(chPar + chExc);
        }else{
            return 0;
        }
    }

    private double log(double x, double base){
        return Math.log10(x) / Math.log10(base);
    }

    private double calcSequentiality() {
        List<Sequence> sequencesBetweenNonConnectorNodes = new LinkedList<>();
        for (Sequence seq : processWithoutPlaceholders.getSequences()) {
            FlowObject source = seq.getSource();
            FlowObject sink = seq.getSink();
            if (!(source instanceof Gateway) && !(sink instanceof Gateway)) {
                sequencesBetweenNonConnectorNodes.add(seq);
            }
        }
        return (double) sequencesBetweenNonConnectorNodes.size() / (double) processWithoutPlaceholders.getSequences().size();
    }

    private double calcNumCycles() {
        List<FlowObject> nodes = new LinkedList<>();
        for (Component c : processWithoutPlaceholders.getComponents()) {
            if (c instanceof FlowObject) {
                nodes.add((FlowObject) c);
            }
        }
        boolean[][] adjacencyMatrixForProcess = new AdjacencyMatrix(nodes, processWithoutPlaceholders.getSequences()).getAdjacencyMatrix();
        List<Vector> cycles = new ElementaryCyclesSearch(adjacencyMatrixForProcess, nodes.toArray()).getElementaryCycles();
        Set cycleStartObjects = new HashSet();
        for (Vector cycle : cycles) {
            cycleStartObjects.add(cycle.get(0));
        }
        return cycleStartObjects.size();
    }

    private double calcTokenSplit() {
        List<Gateway> gateSplits = new LinkedList<>();
        for (Gateway g : processWithoutPlaceholders.getGateways()) {
            if (g.getIncomingObjects().size() == 1 && g.getOutgoingObjects().size() > 1) {
                gateSplits.add(g);
            }
        }
        int sumOfTS = 0;
        for (Gateway g : gateSplits) {
            sumOfTS += g.getOutgoingObjects().size() - 1;
        }
        return sumOfTS;
    }

}
