package plg.generator.process;


import plg.model.PlaceholderComponent;
import plg.model.Process;

public class CurrentGenerationState {
    public int currentDepth;
    public boolean canLoop;
    public boolean canSkip;
    public PlaceholderComponent parentComponent;

    /**
     * @param currentDepth the current depth of the generation
     * @param canLoop specifies whether a loop is allowed here or not
     * @param canSkip specifies whether the pattern can be a skip
     */
    public CurrentGenerationState(int currentDepth, boolean canLoop, boolean canSkip){
        this.currentDepth = currentDepth;
        this.canLoop = canLoop;
        this.canSkip = canSkip;

    }

    public CurrentGenerationState increaseCurrentDepthBy(int n){
        currentDepth += n;
        return this;
    }

    public CurrentGenerationState makeCopy(Process process){
        return new CurrentGenerationState(this.currentDepth, this.canLoop, this.canSkip).setParentComponent((PlaceholderComponent) process.searchComponent(this.parentComponent.getId()));
    }

    public CurrentGenerationState setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        return this;
    }
    public CurrentGenerationState setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
        return this;
    }

    public CurrentGenerationState setParentComponent(PlaceholderComponent parentComponent) {
        this.parentComponent = parentComponent;
        return this;
    }
}
