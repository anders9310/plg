package plg.generator.process;


import plg.model.UnknownComponent;

public class CurrentGenerationState {
    public int currentDepth;
    public boolean canLoop;
    public boolean canSkip;
    public int potential;
    UnknownComponent parentComponent;

    /**
     * @param currentDepth the current depth of the generation
     * @param canLoop specifies whether a loop is allowed here or not
     * @param canSkip specifies whether the pattern can be a skip
     */
    public CurrentGenerationState(int currentDepth, boolean canLoop, boolean canSkip){
        this.currentDepth = currentDepth;
        this.canLoop = canLoop;
        this.canSkip = canSkip;
        this.potential = 1;

    }

    public CurrentGenerationState increaseCurrentDepthBy(int n){
        currentDepth += n;
        return this;
    }

    public CurrentGenerationState makeCopy(){
        return new CurrentGenerationState(this.currentDepth, this.canLoop, this.canSkip).setPotential(this.potential).setParentComponent(this.parentComponent);
    }

    public CurrentGenerationState setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        return this;
    }
    public CurrentGenerationState setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
        return this;
    }
    public CurrentGenerationState setPotential(int potential) {
        this.potential = potential;
        return this;
    }

    public CurrentGenerationState setParentComponent(UnknownComponent parentComponent) {
        this.parentComponent = parentComponent;
        return this;
    }

    public CurrentGenerationState increasePotentialBy(int n) {
        this.potential += n;
        return this;
    }
}
