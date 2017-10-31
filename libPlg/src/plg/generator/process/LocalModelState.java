package plg.generator.process;


public class LocalModelState {
    public int currentDepth;
    public boolean canLoop;
    public boolean canSkip;

    /**
     * @param currentDepth the current depth of the generation
     * @param canLoop specifies whether a loop is allowed here or not
     * @param canSkip specifies whether the pattern can be a skip
     */
    public LocalModelState(int currentDepth, boolean canLoop, boolean canSkip){
        this.currentDepth = currentDepth;
        this.canLoop = canLoop;
        this.canSkip = canSkip;
    }

    public LocalModelState increaseCurrentDepthBy(int n){
        currentDepth += n;
        return this;
    }

    public LocalModelState makeCopy(){
        return new LocalModelState(this.currentDepth, this.canLoop, this.canSkip);
    }

    public LocalModelState setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        return this;
    }

    public LocalModelState setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
        return this;
    }
}
