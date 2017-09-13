package plg.generator.process;

import plg.utils.Random;

public abstract class RandomizationConfiguration {

    /**
     * The maximum number of XOR branches (if wrong value is provided)
     */
    protected static final int MAX_XOR_BRANCHES = 4;
    /**
     * The maximum number of AND branches (if wrong value is provided)
     */
    protected static final int MAX_AND_BRANCHES = 4;
    /**
     * The minimum number of XOR branches
     */
    protected static final int MIN_XOR_BRANCHES = 2;
    /**
     * The minimum number of AND branches
     */
    protected static final int MIN_AND_BRANCHES = 2;

    /* Class' private fields */

    private int ANDBranches;
    private int XORBranches;
    protected double dataObjectProbability;

    protected RandomizationConfiguration(int ANDBranches, int XORBranches, double dataObjectProbability) {
        setAndBranches(ANDBranches);
        setXorBranches(XORBranches);
        setDataObjectProbability(dataObjectProbability);
    }

    public abstract RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip);

    /**
     * This method tells the requester whether it is necessary to generate a
     * data object, according to the value set for the
     * {@link #dataObjectProbability} variable.
     *
     * @return <tt>true</tt> if a data object needs to be created,
     * <tt>false</tt> otherwise
     */
    public boolean generateDataObject() {
        return Random.randomFromWeight(dataObjectProbability);
    }

    /**
     * This method return the number of AND branches to generate, according to
     * the given weight
     *
     * @return the number of AND branches to generate
     */
    public int getRandomANDBranches() {
        return Random.nextInt(MIN_AND_BRANCHES, getAndBranches() - 1);
    }

    /**
     * This method return the number of XOR branches to generate, according to
     * the given weight
     *
     * @return the number of XOR branches to generate
     */
    public int getRandomXORBranches() {
        return Random.nextInt(MIN_XOR_BRANCHES, getXorBranches() - 1);
    }

    /**
     * Set the probability to generate data objects
     *
     * @param dataObjectProbability
     * @return the object after the modification
     */
    public RandomizationConfiguration setDataObjectProbability(double dataObjectProbability) {
        this.dataObjectProbability = (dataObjectProbability <= 1 || dataObjectProbability >= 0)?
                dataObjectProbability : 0;
        return this;
    }

    /**
     * Get the current value of the data object probability
     *
     * @return the value of the parameter
     */
    public double getDataObjectProbability() {
        return dataObjectProbability;
    }

    /**
     * Set the AND branches parameter
     *
     * @param andBranches the maximum number of AND branches
     * @return the object after the modification
     */
    public RandomizationConfiguration setAndBranches(int andBranches) {
        ANDBranches = (andBranches > 1)? andBranches : MAX_AND_BRANCHES;
        return this;
    }

    /**
     * Get the AND branches parameter
     *
     * @return the maximum number of AND branches
     */
    public int getAndBranches() {
        return ANDBranches;
    }

    /**
     * Set the XOR branches parameter
     *
     * @param xorBranches the maximum number of XOR branches
     * @return the object after the modification
     */
    public RandomizationConfiguration setXorBranches(int xorBranches) {
        XORBranches = (xorBranches > 1)? xorBranches : MAX_XOR_BRANCHES;
        return this;
    }

    /**
     * Get the XOR branches parameter
     *
     * @return the maximum number of XOR branches
     */
    public int getXorBranches() {
        return XORBranches;
    }
}
