package plg.generator.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import plg.utils.Pair;
import plg.utils.Random;
import plg.utils.SetUtils;

/**
 * This class describes the parameters of the process generator. With this class
 * the user can control the process randomization.
 *
 * @author Andrea Burattin
 */
public class Plg2RandomizationConfiguration extends RandomizationConfiguration{

	/**
	 * This is a test configuration with basic random values
	 */
	public static final Plg2RandomizationConfiguration BASIC_VALUES = new Plg2RandomizationConfiguration(
			5, // max AND branches
			5, // max XOR branches
			0.1, // loop weight
			0.2, // single activity weight
			0.1, // skip weight
			0.7, // sequence weight
			0.3, // AND weight
			0.3, // XOR weight
			3, // maximum depth
			0.1 // data object probability
		);
	
	/* Class' private fields */
	private Map<RandomizationPattern, Double> weights;
	private int maxDepth;
	
	/**
	 * This constructor builds a parameters configuration all parameters are
	 * required.
	 * 
	 * @param ANDBranches the maximum number of AND branches (must be > 1)
	 * @param XORBranches the maximum number of XOR branches (must be > 1)
	 * @param loopWeight the loop weight (must be in [0, 1])
	 * @param singleActivityWeight the weight of single activity (must
	 * be in <tt>[0,1]</tt>)
	 * @param skipWeight the weight of a skip (must be in <tt>[0,1]</tt>)
	 * @param sequenceWeight he weight of sequence activity (must be
	 * in <tt>[0,1]</tt>)
	 * @param ANDWeight the weight of AND split-join (must be in <tt>[0,1]</tt>)
	 * @param XORWeight the weight of XOR split-join (must be in <tt>[0,1]</tt>)
	 * @param maxDepth the maximum network deep
	 * @param dataObjectProbability probability to generate data objects
	 * associated to sequences and events
	 */
	public Plg2RandomizationConfiguration(int ANDBranches, int XORBranches,
										  double loopWeight, double singleActivityWeight, double skipWeight,
										  double sequenceWeight, double ANDWeight, double XORWeight,
										  int maxDepth, double dataObjectProbability) {
		super(ANDBranches, XORBranches, dataObjectProbability);
		this.weights = new HashMap<>();
		setLoopWeight(loopWeight);
		setSingleActivityWeight(singleActivityWeight);
		setSkipWeight(skipWeight);
		setSequenceWeight(sequenceWeight);
		setANDWeight(ANDWeight);
		setXORWeight(XORWeight);
		setDepth(maxDepth);
	}
	
	/**
	 * Set the loop weight parameter
	 * 
	 * @param loopWeight
	 * @return 
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setLoopWeight(double loopWeight) {
		weights.put(RandomizationPattern.LOOP,
				(loopWeight >= 0.0 && loopWeight <= 1.0)?
					loopWeight :
					BASIC_VALUES.weights.get(RandomizationPattern.LOOP));
		return this;
	}
	
	/**
	 * Get the current value of the loop weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getLoopWeight() {
		return weights.get(RandomizationPattern.LOOP);
	}
	
	/**
	 * Set the single activity weight parameter
	 * 
	 * @param singleActivityWeight
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setSingleActivityWeight(double singleActivityWeight) {
		weights.put(RandomizationPattern.SINGLE_ACTIVITY,
				(singleActivityWeight >= 0.0 && singleActivityWeight <= 1.0)?
						singleActivityWeight :
						BASIC_VALUES.weights.get(RandomizationPattern.SINGLE_ACTIVITY));
		return this;
	}
	
	/**
	 * Get the current value of the single activity weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSingleActivityWeight() {
		return weights.get(RandomizationPattern.SINGLE_ACTIVITY);
	}
	
	/**
	 * Set the skip weight parameter
	 * 
	 * @param skipWeight
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setSkipWeight(double skipWeight) {
		weights.put(RandomizationPattern.SKIP,
				(skipWeight >= 0.0 && skipWeight <= 1.0)?
						skipWeight :
							BASIC_VALUES.weights.get(RandomizationPattern.SKIP));
		return this;
	}
	
	/**
	 * Get the current value of the skip weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSkipWeight() {
		return weights.get(RandomizationPattern.SKIP);
	}
	
	/**
	 * Set the sequence weight parameter
	 * 
	 * @param sequenceWeight
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setSequenceWeight(double sequenceWeight) {
		weights.put(RandomizationPattern.SEQUENCE,
				(sequenceWeight >= 0.0 && sequenceWeight <= 1.0)?
						sequenceWeight :
							BASIC_VALUES.weights.get(RandomizationPattern.SEQUENCE));
		return this;
	}
	
	/**
	 * Get the current value of the sequence weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSequenceWeight() {
		return weights.get(RandomizationPattern.SEQUENCE);
	}
	
	/**
	 * Set the AND weight parameter
	 * 
	 * @param ANDWeight
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setANDWeight(double ANDWeight) {
		weights.put(RandomizationPattern.PARALLEL_EXECUTION,
				(ANDWeight >= 0.0 && ANDWeight <= 1.0)?
						ANDWeight :
							BASIC_VALUES.weights.get(RandomizationPattern.PARALLEL_EXECUTION));
		return this;
	}
	
	/**
	 * Get the current value of the AND weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getANDWeight() {
		return weights.get(RandomizationPattern.PARALLEL_EXECUTION);
	}
	
	/**
	 * Set the XOR weight parameter
	 * 
	 * @param XORWeight
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setXORWeight(double XORWeight) {
		weights.put(RandomizationPattern.MUTUAL_EXCLUSION,
				(XORWeight >= 0.0 && XORWeight <= 1.0)?
						XORWeight :
							BASIC_VALUES.weights.get(RandomizationPattern.MUTUAL_EXCLUSION));
		return this;
	}
	
	/**
	 * Get the current value of the XOR weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getXORWeight() {
		return weights.get(RandomizationPattern.MUTUAL_EXCLUSION);
	}
	
	/**
	 * Set the maximum depth parameter
	 * 
	 * @param depth
	 * @return the object after the modification
	 */
	public Plg2RandomizationConfiguration setDepth(int depth) {
		this.maxDepth = (depth > 0)? depth : BASIC_VALUES.maxDepth;
		return this;
	}
	
	/**
	 * Get the current value of the maximum depth parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}
	
	/**
	 * This method is used for the definition of the presence of a loop
	 * 
	 * @return true if a loop must be inserted, false otherwise
	 */
	public boolean getLoopPresence() {
		return Random.randomFromWeight(getLoopWeight());
	}
	
	/**
	 * This method returns a pattern, randomly selected between:
	 * <ul>
	 * 	<li>Single activity</li>
	 * 	<li>Sequence pattern</li>
	 * 	<li>AND pattern</li>
	 * 	<li>XOR pattern</li>
	 * 	<li>Skip (according to the parameter)</li>
	 * 	<li>Loop (according to the parameter)</li>
	 * </ul>
	 * 
	 * <p> The selection is done according to the given probabilities
	 * 
	 * @param canLoop specifies whether the pattern can be a loop
	 * @param canSkip specifies whether the pattern can be a skip
	 * @return the random pattern
	 */
	public RandomizationPattern generateRandomPattern(boolean canLoop, boolean canSkip) {
		Set<RandomizationPattern> options = getAllPatterns(canLoop, canSkip);
		
		return generateRandomPattern(options.toArray(new RandomizationPattern[options.size()]));
	}

	private Set<RandomizationPattern> getAllPatterns(boolean canLoop, boolean canSkip){
		Set<RandomizationPattern> options = new HashSet<RandomizationPattern>();
		options.add(RandomizationPattern.SINGLE_ACTIVITY);
		options.add(RandomizationPattern.SEQUENCE);
		options.add(RandomizationPattern.PARALLEL_EXECUTION);
		options.add(RandomizationPattern.MUTUAL_EXCLUSION);
		if (canSkip) {
			options.add(RandomizationPattern.SKIP);
		}
		if (canLoop) {
			options.add(RandomizationPattern.LOOP);
		}
		return options;
	}
	
	/**
	 * This method returns a random pattern, selected between the provided ones
	 * 
	 * <p>
	 * The selection is done according to the given probabilities
	 * 
	 * @param patterns the patterns to choose from
	 * @return the random pattern
	 */
	public RandomizationPattern generateRandomPattern(RandomizationPattern... patterns) {
		Set<Pair<RandomizationPattern, Double>> options = new HashSet<Pair<RandomizationPattern, Double>>();
		for(RandomizationPattern p : patterns) {
			options.add(new Pair<RandomizationPattern, Double>(p, weights.get(p)));
		}
		return SetUtils.getRandomWeighted(options);
	}

	@Override
	public String toString() {
		String toRet = "";
		toRet += "And Branches = " + getAndBranches() + "\n";
		toRet += "Xor Branches = " + getXorBranches() + "\n";
		toRet += "Loop Weight = " + getLoopWeight() + "\n";
		toRet += "Single Activity Weight = " + getSingleActivityWeight() + "\n";
		toRet += "Skip Weight = " + getSkipWeight() + "\n";
		toRet += "Sequence Weight = " + getSequenceWeight() + "\n";
		toRet += "AND Weight = " + getANDWeight() + "\n";
		toRet += "XOR Weight = " + getXORWeight() + "\n";
		toRet += "Maximum Depth = " + getMaximumDepth() + "\n";
		toRet += "Data Object Probability = " + getDataObjectProbability() + "\n";
		return toRet;
	}

	public void printResults(){};
}
