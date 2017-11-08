package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.FlowObject;
import plg.model.Process;
import plg.model.UnknownComponent;
import plg.utils.Pair;

public class ProductionObligationWeight extends Weight{
    private int productionPotentialIncrease;
    private Obligation obligation;
    private RandomizationPattern randomizationPattern;
    private double POTENTIAL_THRESHOLD = 1;
    private Process process;

    public ProductionObligationWeight(RandomizationPattern randomizationPattern, Obligation obligation){
        this.randomizationPattern = randomizationPattern;
        this.obligation = obligation;
        this.process = obligation.getProcess();
        cacheProductionPotential();
    }

    private void cacheProductionPotential() {
        productionPotentialIncrease = BaseWeights.BASE_WEIGHTS.getBasePotential(this.randomizationPattern);
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(obligation.getMean() == 0){
            return 0;
        }

        double metricContribution = getContributionBySimulation(state);
        double targetValue = obligation.getTargetValue();
        double currentValue = obligation.getCurrentValue();
        double currentPotential = state.potential;

        double terminalWish;
        if(currentValue<targetValue){//increase
            if(metricContribution>0){
                terminalWish = 1;
            }else if(metricContribution==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }else if(currentValue==targetValue){//Stay the same
            if(metricContribution == 0){
                terminalWish = 1;
            }else{
                terminalWish = -1;
            }
        }else{//decrease
            if(metricContribution<0){
                terminalWish = 1;
            }else if(metricContribution==0){
                terminalWish = 0;
            }else{
                terminalWish = -1;
            }
        }

        double potentialWish;
        if(currentValue<targetValue && currentPotential<=2.0*POTENTIAL_THRESHOLD){//increase
            if(productionPotentialIncrease >0){
                if(currentPotential<=POTENTIAL_THRESHOLD){
                    return 1;
                }
                potentialWish = 1;
            }else if(productionPotentialIncrease ==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }else{//decrease
            if(productionPotentialIncrease <0){
                potentialWish = 1;
            }else if(productionPotentialIncrease ==0){
                potentialWish = 0;
            }else{
                potentialWish = -1;
            }
        }

        return terminalWish + potentialWish;
    }

    private double getContributionBySimulation(CurrentGenerationState state) {
        double currentValue = process.getMetric(this.obligation.getType());
        Process simulationProcess = (Process) process.clone();
        CurrentGenerationState simulationState = state.makeCopy(simulationProcess);
        RandomizationConfiguration parameters = new ParameterRandomizationConfiguration(simulationProcess,0,0,0,0,0,0);
        ProcessGenerator simulator = new ProcessGenerator(simulationProcess, parameters);

        switch (this.randomizationPattern) {
            case SEQUENCE:
                //FlowObject beforePattern = (FlowObject)simulationState.parentComponent.getIncomingObjects().toArray()[0];
                //FlowObject afterPattern = (FlowObject)simulationState.parentComponent.getOutgoingObjects().toArray()[0];
                simulator.replaceComponentWithSequencePatternDummies(simulationProcess, simulationState.parentComponent);
                //PatternFrame.connect(beforePattern, entryExit.getFirst());
                //PatternFrame.connect(entryExit.getSecond(), afterPattern);
                break;
            case PARALLEL_EXECUTION:
                Pair<UnknownComponent, UnknownComponent> dummyEntryExitPar = new Pair<>(simulationProcess.newUnknownComponent(), simulationProcess.newUnknownComponent());
                PatternFrame.connect((FlowObject)simulationState.parentComponent.getIncomingObjects().toArray()[0], dummyEntryExitPar.getFirst());
                PatternFrame.connect(dummyEntryExitPar.getSecond(), (FlowObject)simulationState.parentComponent.getOutgoingObjects().toArray()[0]);
                simulator.replaceComponentWithAndPatternDummies(simulationProcess, simulationState.parentComponent, dummyEntryExitPar);
                break;
            case MUTUAL_EXCLUSION:
                Pair<UnknownComponent, UnknownComponent> dummyEntryExitMut = new Pair<>(simulationProcess.newUnknownComponent(), simulationProcess.newUnknownComponent());
                PatternFrame.connect((FlowObject)simulationState.parentComponent.getIncomingObjects().toArray()[0], dummyEntryExitMut.getFirst());
                PatternFrame.connect(dummyEntryExitMut.getSecond(), (FlowObject)simulationState.parentComponent.getOutgoingObjects().toArray()[0]);
                simulator.replaceComponentWithAndPatternDummies(simulationProcess, simulationState.parentComponent, dummyEntryExitMut);
                break;
            case SINGLE_ACTIVITY:
                PatternFrame activity = simulator.newActivity(simulationProcess);
                PatternFrame.connect((FlowObject) simulationState.parentComponent.getIncomingObjects().toArray()[0], activity.getLeftBound()).connect((FlowObject) simulationState.parentComponent.getOutgoingObjects().toArray()[0]);
                simulationProcess.removeComponent(simulationState.parentComponent);
                break;
            case SKIP:
                PatternFrame.connect((FlowObject) simulationState.parentComponent.getIncomingObjects().toArray()[0], (FlowObject) simulationState.parentComponent.getOutgoingObjects().toArray()[0]);
                simulationProcess.removeComponent(simulationState.parentComponent);
                break;
            /*case MUTUAL_EXCLUSION:
                newXorBranches(localState.increaseCurrentDepthBy(1));
            case LOOP:
                newLoopBranch(localState.increaseCurrentDepthBy(1));
            */
            default:
                throw new RuntimeException("No case for production rule " + this.randomizationPattern.name());
                /*process.removeComponent(localState.parentComponent);
                generatedFrame = newActivity();*/
        }

        double simulatedValue = simulationProcess.getMetric(this.obligation.getType());
        double contribution = simulatedValue - currentValue;
        return contribution;


    }

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
}
