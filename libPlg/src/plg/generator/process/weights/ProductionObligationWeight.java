package plg.generator.process.weights;

import plg.generator.process.*;
import plg.model.Process;

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
        productionPotentialIncrease = BaseWeights.getPotentialIncreaseFor(this.randomizationPattern);
    }

    protected double calculateValue(CurrentGenerationState state) {
        if(obligation.getMean() == 0){
            return 0;
        }

        double metricContribution = process.getContributionOf(state, this.obligation.getType(), randomizationPattern);;
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

    /*private double getContributionBySimulation(CurrentGenerationState state) {
        double currentValue = process.getMetric(this.obligation.getType());
        Process simulationProcess = (Process) process.clone();
        CurrentGenerationState simulationState = state.makeCopy(simulationProcess);
        RandomizationConfiguration parameters = new ParameterRandomizationConfiguration(simulationProcess,0,0,0,0,0,0);
        ProcessGenerator simulator = new ProcessGenerator(simulationProcess, parameters);

        switch (this.randomizationPattern) {
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
                throw new RuntimeException("No case for production rule " + this.randomizationPattern.name());
        }

        double simulatedValue = simulationProcess.getMetric(this.obligation.getType());
        double contribution = simulatedValue - currentValue;
        return contribution;
    }*/

    public RandomizationPattern getRandomizationPattern() {
        return randomizationPattern;
    }
}
