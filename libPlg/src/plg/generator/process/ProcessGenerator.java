package plg.generator.process;

import plg.generator.process.weights.BaseWeights;
import plg.model.FlowObject;
import plg.model.Process;
import plg.model.UnknownComponent;
import plg.model.activity.Activity;
import plg.model.activity.Task;
import plg.model.data.DataObject;
import plg.model.data.IDataObjectOwner;
import plg.model.event.EndEvent;
import plg.model.event.Event;
import plg.model.event.StartEvent;
import plg.model.gateway.Gateway;
import plg.utils.Logger;
import plg.utils.Pair;
import plg.utils.SetUtils;

import java.math.BigInteger;
import java.util.Random;

public class ProcessGenerator {


    /**
     * This string contains the pattern for the generation of activities. The
     * pattern requires one parameter, which will be replaced with a progressive
     * letter, such as "A", ..., "Z", "AA", "AB", ...
     */
    public static final String ACTIVITY_NAME_PATTERN = "Activity %s";

    /**
     * This string contains the pattern for the generation of data objects. The
     * pattern requires one parameter, which will be replaced with a progressive
     * letter, such as "A", ..., "Z", "AA", "AB", ...
     */
    public static final String DATA_OBJECT_NAME_PATTERN = "variable_%s";

    protected Process process;
    private RandomizationConfiguration parameters;
    protected int generatedActivities = 0;
    protected int generatedDataObjects = 0;

    /**
     * This public static method is the main interface for the process
     * randomization. Specifically, this method adds to the provided process a
     * control-flow structure, which starts from a {@link StartEvent}, and
     * finishes into an {@link EndEvent}.
     *
     * <p> If the provided process is not empty, the new control-flow is added
     * to the existing process.
     *
     * @param process the process to randomize
     * @param parameters the randomization parameters to use
     */
    public static void randomizeProcess(Process process, RandomizationConfiguration parameters) {
        new ProcessGenerator(process, parameters).begin();
    }

    /**
     * Protected class constructor. This method is not publicly available since
     * we would like to interact only through the
     * {@link Plg2ProcessGenerator#randomizeProcess} method.
     *
     * @param process the process to randomize
     * @param parameters the randomization parameters to use
     */
    public ProcessGenerator(Process process, RandomizationConfiguration parameters) {
        this.process = process;
        this.parameters = parameters;
    }

    /**
     * This method initialize the randomization process, first by adding start
     * and end events, and then populating the internal structure.
     */
    protected void begin() {
        Logger.instance().info("Starting process randomization");
        Event start = process.newStartEvent();
        Event end = process.newEndEvent();

        UnknownComponent c = process.newUnknownComponent();
        PatternFrame.connect(start, c).connect(end);

        PatternFrame p = generateMainFrame(new CurrentGenerationState(0, true, true).setParentComponent(c));
        PatternFrame.connect(start, p).connect(end);
        Logger.instance().info("Process randomization complete");
        parameters.printResults();
    }

    protected PatternFrame generateMainFrame(CurrentGenerationState localState){
        return newInternalPattern(localState);
    }

    /**
     * This method generates a new internal pattern. The pattern to generate is
     * randomly selected with respect to the provided random generation policy.
     *
     * @param localState Data container with information local to the generated patters
     * @return the frame containing the generated pattern
     */
    protected PatternFrame newInternalPattern(CurrentGenerationState localState) {
        RandomizationPattern nextAction = parameters.generateRandomPattern(localState);
        return newInternalPattern(localState, nextAction);
    }

    public PatternFrame newInternalPattern(CurrentGenerationState localState,RandomizationPattern nextAction) {
        PatternFrame generatedFrame;
        localState.increasePotentialBy(BaseWeights.BASE_WEIGHTS.getBasePotential(nextAction));

        switch (nextAction) {
            case SEQUENCE:
                generatedFrame = newSequence(localState.increaseCurrentDepthBy(1));
                break;
            case PARALLEL_EXECUTION:
                generatedFrame = newAndBranches(localState.increaseCurrentDepthBy(1));
                break;
            case MUTUAL_EXCLUSION:
                generatedFrame = newXorBranches(localState.increaseCurrentDepthBy(1));
                break;
            case LOOP:
                generatedFrame = newLoopBranch(localState.increaseCurrentDepthBy(1));
                break;
            case SKIP:
                process.removeComponent(localState.parentComponent);
                generatedFrame = newSkip();
                break;
            /*case PARALLEL_EXECUTION_SINGLEBRANCH:
                generatedFrame = newAndBranchesSingle(currentDepth + 1, canLoop);
                break;
            case MUTUAL_EXCLUSION_SINGLEBRANCH:
                generatedFrame = newXorBranchesSingle(currentDepth + 1, canLoop, canSkip);
                break;*/
            default:
                process.removeComponent(localState.parentComponent);
                generatedFrame = newActivity(this.process);
                Logger.instance().debug("New activity created (`" + ((Task)generatedFrame.getLeftBound()).getName() + "')");
                break;
        }
        return generatedFrame;
    }

    private PatternFrame newSkip() {
        process.newSkip();
        return null;
    }

    /**
     * This method generates a new activity.
     *
     * @return the frame containing the generated pattern
     */
    public PatternFrame newActivity(Process process) {
        String activityName = askNewActivityName();
        Task t = process.newTask(activityName);
        if (parameters.generateDataObject()) {
            newDataObject(process).setObjectOwner(t, SetUtils.getRandom(IDataObjectOwner.DATA_OBJECT_DIRECTION.values()));
        }
        return new PatternFrame(t);
    }//

    /**
     * This method generates a new sequence pattern. A sequence is connecting
     * two internal frames, generate using
     * {@link Plg2ProcessGenerator#newInternalPattern}.
     *
     * @return the frame containing the generated pattern
     */
    protected PatternFrame newSequence(CurrentGenerationState localState) {
        Logger.instance().debug("New sequence pattern to create");
        Pair<UnknownComponent, UnknownComponent> newComponents = replaceComponentWithSequencePatternDummies(this.process, localState.parentComponent);
        PatternFrame p1 = newInternalPattern(localState.makeCopy(this.process).setParentComponent(newComponents.getFirst()));
        PatternFrame p2 = newInternalPattern(localState.makeCopy(this.process).setParentComponent(newComponents.getSecond()));
        return PatternFrame.connect(p1, p2);
    }

    public Pair<UnknownComponent, UnknownComponent> replaceComponentWithSequencePatternDummies(Process process, UnknownComponent parentComponent){
        UnknownComponent c1 = process.newUnknownComponent();
        UnknownComponent c2 = process.newUnknownComponent();
        PatternFrame.connect((FlowObject)parentComponent.getIncomingObjects().toArray()[0], c1);
        PatternFrame.connect(c1, c2);
        PatternFrame.connect(c2, (FlowObject)parentComponent.getOutgoingObjects().toArray()[0]);
        process.removeComponent(parentComponent);
        return new Pair<>(c1, c2);
    }

    /**
     * This method generates a new AND pattern. Each branch is populated using
     * the generate using {@link Plg2ProcessGenerator#newInternalPattern} method.
     *
     * @return the frame containing the generated pattern
     */
    protected PatternFrame newAndBranches(CurrentGenerationState localState) {
        Logger.instance().debug("New AND pattern to create");
        Pair<UnknownComponent, UnknownComponent> dummyEntryExit = new Pair<>(process.newUnknownComponent(), process.newUnknownComponent());
        PatternFrame.connect((FlowObject)localState.parentComponent.getIncomingObjects().toArray()[0], dummyEntryExit.getFirst());
        PatternFrame.connect(dummyEntryExit.getSecond(), (FlowObject)localState.parentComponent.getOutgoingObjects().toArray()[0]);

        UnknownComponent[] branchComponents = replaceComponentWithAndPatternDummies(this.process, localState.parentComponent, dummyEntryExit);

        Task beforeSplit = (Task) dummyEntryExit.getFirst().getOutgoingObjects().toArray()[0];
        Task afterJoin = (Task) dummyEntryExit.getSecond().getIncomingObjects().toArray()[0];
        Gateway split = (Gateway) beforeSplit.getOutgoingObjects().toArray()[0];
        Gateway join = (Gateway) afterJoin.getIncomingObjects().toArray()[0];

        for(int i = 0; i < branchComponents.length; i++) {
            PatternFrame p = newInternalPattern(localState.makeCopy(this.process).setCanSkip(false).setParentComponent(branchComponents[i]));
            PatternFrame.connect(split, p).connect(join);
        }


        process.removeComponent(dummyEntryExit.getFirst());
        process.removeComponent(dummyEntryExit.getSecond());
        return new PatternFrame(beforeSplit, afterJoin);
    }

    public UnknownComponent[] replaceComponentWithAndPatternDummies(Process process, UnknownComponent parentComponent, Pair<UnknownComponent, UnknownComponent> entries){
        PatternFrame beforeSplit = newActivity(process);
        Gateway split = process.newParallelGateway();
        Gateway join = process.newParallelGateway();
        PatternFrame afterJoin = newActivity(process);
        int branchesToGenerate = parameters.getRandomANDBranches();

        PatternFrame.connect(entries.getFirst(), beforeSplit).connect(split);
        PatternFrame.connect(join, afterJoin).connect(entries.getSecond());
        process.removeComponent(parentComponent);

        UnknownComponent[] components = new UnknownComponent[branchesToGenerate];
        for(int i = 0; i < branchesToGenerate; i++) {
            components[i] = process.newUnknownComponent();
            PatternFrame.connect(split, components[i]).connect(join);
        }

        return components;
    }

    protected PatternFrame newAndBranchesSingle(int currentDepth, boolean loopAllowed) {
        Logger.instance().debug("New ANDSKIP pattern to create");
        PatternFrame beforeSplit = newActivity(this.process);
        Gateway split = process.newParallelGateway();
        Gateway join = process.newParallelGateway();
        PatternFrame afterJoin = newActivity(this.process);

        PatternFrame.connect(split, join);

        PatternFrame.connect(beforeSplit, split);
        PatternFrame.connect(join, afterJoin);

        return new PatternFrame(beforeSplit.getLeftBound(), afterJoin.getRightBound());
    }

    /**
     * This method generates a new XOR pattern. Each branch is populated using
     * the generate using {@link Plg2ProcessGenerator#newInternalPattern} method.
     *
     * @return the frame containing the generated pattern
     */
    protected PatternFrame newXorBranches(CurrentGenerationState localState) {
        Logger.instance().debug("New XOR pattern to create");
        Pair<UnknownComponent, UnknownComponent> dummyEntryExit = new Pair<>(process.newUnknownComponent(), process.newUnknownComponent());
        PatternFrame.connect((FlowObject)localState.parentComponent.getIncomingObjects().toArray()[0], dummyEntryExit.getFirst());
        PatternFrame.connect(dummyEntryExit.getSecond(), (FlowObject)localState.parentComponent.getOutgoingObjects().toArray()[0]);

        UnknownComponent[] branchComponents = replaceComponentWithXorPatternDummies(this.process, localState.parentComponent, dummyEntryExit);

        Task beforeSplit = (Task) dummyEntryExit.getFirst().getOutgoingObjects().toArray()[0];
        Task afterJoin = (Task) dummyEntryExit.getSecond().getIncomingObjects().toArray()[0];
        Gateway split = (Gateway) beforeSplit.getOutgoingObjects().toArray()[0];
        Gateway join = (Gateway) afterJoin.getIncomingObjects().toArray()[0];

        for(int i = 0; i < branchComponents.length; i++) {
            PatternFrame p = newInternalPattern(localState.makeCopy(this.process).setCanSkip(false).setParentComponent(branchComponents[i]));
            PatternFrame.connect(split, p).connect(join);
        }


        process.removeComponent(dummyEntryExit.getFirst());
        process.removeComponent(dummyEntryExit.getSecond());
        return new PatternFrame(beforeSplit, afterJoin);
        /*
        PatternFrame beforeSplit = newActivity(this.process);
        Gateway split = process.newExclusiveGateway();
        Gateway join = process.newExclusiveGateway();
        PatternFrame afterJoin = newActivity(this.process);
        int branchesToGenerate = parameters.getRandomXORBranches();
        //Logger.instance().debug("branchesToGenerate: " + branchesToGenerate);

        UnknownComponent dummyEntry = process.newUnknownComponent();
        UnknownComponent dummyExit = process.newUnknownComponent();

        PatternFrame.connect((FlowObject)localState.parentComponent.getIncomingObjects().toArray()[0], dummyEntry).connect(beforeSplit).connect(split);
        PatternFrame.connect(join, afterJoin).connect(dummyExit).connect((FlowObject)localState.parentComponent.getOutgoingObjects().toArray()[0]);
        process.removeComponent(localState.parentComponent);

        UnknownComponent[] components = new UnknownComponent[branchesToGenerate];
        for(int i = 0; i < branchesToGenerate; i++) {
            components[i] = process.newUnknownComponent();
            PatternFrame.connect(split, components[i]).connect(join);
        }
        for(int i = 0; i < branchesToGenerate; i++) {
            PatternFrame p = newInternalPattern(localState.makeCopy(this.process).setParentComponent(components[i]));
            PatternFrame.connect(split, p).connect(join);
            if(p!=null){
                if (parameters.generateDataObject() && p.getLeftBound() instanceof IDataObjectOwner) {
                    newDataObject(this.process).setObjectOwner((IDataObjectOwner) p.getLeftBound(), IDataObjectOwner.DATA_OBJECT_DIRECTION.REQUIRED);
                }
            }
        }

        process.removeComponent(dummyEntry);
        process.removeComponent(dummyExit);
        return new PatternFrame(beforeSplit.getLeftBound(), afterJoin.getRightBound());*/
    }

    public UnknownComponent[] replaceComponentWithXorPatternDummies(Process process, UnknownComponent parentComponent, Pair<UnknownComponent, UnknownComponent> entries){
        PatternFrame beforeSplit = newActivity(process);
        Gateway split = process.newExclusiveGateway();
        Gateway join = process.newExclusiveGateway();
        PatternFrame afterJoin = newActivity(process);
        int branchesToGenerate = parameters.getRandomXORBranches();

        PatternFrame.connect(entries.getFirst(), beforeSplit).connect(split);
        PatternFrame.connect(join, afterJoin).connect(entries.getSecond());
        process.removeComponent(parentComponent);

        UnknownComponent[] components = new UnknownComponent[branchesToGenerate];
        for(int i = 0; i < branchesToGenerate; i++) {
            components[i] = process.newUnknownComponent();
            PatternFrame.connect(split, components[i]).connect(join);
        }

        return components;
    }


    protected PatternFrame newXorBranchesSingle(int currentDepth, boolean loopAllowed, boolean canSkip) {
        Logger.instance().debug("New XORSKIP pattern to create");
        PatternFrame beforeSplit = newActivity(this.process);
        Gateway split = process.newExclusiveGateway();
        Gateway join = process.newExclusiveGateway();
        PatternFrame afterJoin = newActivity(this.process);

        PatternFrame.connect(split, join);


        PatternFrame.connect(beforeSplit, split);
        PatternFrame.connect(join, afterJoin);

        return new PatternFrame(beforeSplit.getLeftBound(), afterJoin.getRightBound());
    }

    /**
     * This method generates a new XOR pattern. Each branch is populated using
     * the generate using {@link Plg2ProcessGenerator#newInternalPattern} method.
     *
     * @return the frame containing the generated pattern
     */
    protected PatternFrame newLoopBranch(CurrentGenerationState localState) {
        Logger.instance().debug("New loop pattern to create");
        PatternFrame beforeSplit = newActivity(this.process);
        Gateway split = process.newExclusiveGateway();
        Gateway join = process.newExclusiveGateway();
        PatternFrame afterJoin = newActivity(this.process);

        UnknownComponent dummyEntry = process.newUnknownComponent();
        UnknownComponent dummyExit = process.newUnknownComponent();

        PatternFrame.connect((FlowObject)localState.parentComponent.getIncomingObjects().toArray()[0], dummyEntry).connect(beforeSplit);
        PatternFrame.connect(beforeSplit, split);
        PatternFrame.connect(join, afterJoin);
        PatternFrame.connect(afterJoin, dummyExit).connect((FlowObject)localState.parentComponent.getOutgoingObjects().toArray()[0]);
        process.removeComponent(localState.parentComponent);

        UnknownComponent cBody = process.newUnknownComponent();
        PatternFrame.connect(split, cBody).connect(join);
        UnknownComponent cRollback = process.newUnknownComponent();
        PatternFrame.connect(join, cRollback).connect(split);

        PatternFrame body = newInternalPattern(localState.makeCopy(this.process).setCanLoop(false).setCanSkip(false).setParentComponent(cBody));
        PatternFrame rollback = newInternalPattern(localState.makeCopy(this.process).setCanLoop(false).setCanSkip(true).setParentComponent(cRollback));

        PatternFrame.connect(split, body).connect(join);
        PatternFrame.connect(join, rollback).connect(split);

        process.removeComponent(dummyEntry);
        process.removeComponent(dummyExit);

        return new PatternFrame(beforeSplit.getLeftBound(), afterJoin.getRightBound());
    }//

    /**
     * This method generates a new data object and returns it.
     *
     * @return the new data object generated
     */
    protected DataObject newDataObject(Process process) {
        DataObject d = new DataObject(process);
        d.setName(askNewDataObjectName());
        d.setValue(new BigInteger(65, new Random()).toString(32));
        Logger.instance().debug("New data object created (`" + d.getName() + "' = `" + d.getValue() + "')");
        return d;
    }//

    /**
     * This method returns a new data object name, based on a progressive
     * pattern.
     *
     * @return a new name for a data object
     * @see Plg2ProcessGenerator#DATA_OBJECT_NAME_PATTERN
     */
    protected String askNewDataObjectName() {
        generatedDataObjects++;
        return String.format(DATA_OBJECT_NAME_PATTERN, numberToAlpha(generatedDataObjects).toLowerCase());
    }//

    /**
     * This method returns a new activity name, based on a progressive pattern.
     *
     * @return a new name for an activity
     * @see Plg2ProcessGenerator#ACTIVITY_NAME_PATTERN
     */
    protected String askNewActivityName() {
        generatedActivities++;
        String candidateActivityName = String.format(ACTIVITY_NAME_PATTERN, numberToAlpha(generatedActivities));

        // check whether the candidate activity name is already present in the
        // process
        boolean activityAlreadyPresent = false;
        for (Task t : process.getTasks()) {
            if (t.getName().equals(candidateActivityName)) {
                activityAlreadyPresent = true;
                break;
            }
        }
        // if the activity is already there, then generate a new one, otherwise
        // the candidate activity name is returned
        if (activityAlreadyPresent) {
            return askNewActivityName();
        } else {
            return candidateActivityName;
        }
    }//

    /**
     * This method converts a number into a string representation
     *
     * @param num the number to convert
     * @return a string equivalent to the number, e.g., 1 becomes "A", 2 becomes
     * "B", etc.
     */
    public static String numberToAlpha(int num) {
        String result = "";
        while (num > 0) {
            num--;
            int remainder = num % 26;
            char digit = (char) (remainder + 'A');
            result = digit + result;
            num = (num - remainder) / 26;
        }
        return result;
    }

    public RandomizationConfiguration getParameters() {
        return parameters;
    }
    public int getGeneratedConnections(){
        return process.getSequences().size();
    }
}
