package plg.generator.process;

import java.math.BigInteger;
import java.util.Random;

import plg.model.Process;
import plg.model.activity.Task;
import plg.model.data.DataObject;
import plg.model.data.IDataObjectOwner;
import plg.model.data.IDataObjectOwner.DATA_OBJECT_DIRECTION;
import plg.model.event.EndEvent;
import plg.model.event.Event;
import plg.model.event.StartEvent;
import plg.model.gateway.Gateway;
import plg.utils.Logger;
import plg.utils.SetUtils;

/**
 * This class contains the random generator of processes. Actually, this class
 * is responsible for the randomization of a process.
 * 
 * @author Andrea Burattin
 */
public class Plg2ProcessGenerator extends ProcessGenerator{
	/**
	 * Protected class constructor. This method is not publicly available since
	 * we would like to interact only through the
	 * {@link Plg2ProcessGenerator#randomizeProcess} method.
	 * 
	 * @param process the process to randomize
	 * @param parameters the randomization parameters to use
	 */
	protected Plg2ProcessGenerator(Process process, Plg2RandomizationConfiguration parameters) {
		super(process, parameters);
	}

	public Plg2RandomizationConfiguration getParameters(){
		return (Plg2RandomizationConfiguration) super.getParameters();
	}

	protected PatternFrame generateMainFrame(){
		return newInternalPattern(0, true, false);
	}

	protected PatternFrame newInternalPattern(int currentDepth, boolean canLoop, boolean canSkip){
		if (currentDepth <= getParameters().getMaximumDepth()) {
			return super.newInternalPattern(currentDepth, canLoop, canSkip);
		} else {
			if (canSkip) {
				RandomizationPattern nextAction = getParameters().generateRandomPattern(RandomizationPattern.SKIP, RandomizationPattern.SINGLE_ACTIVITY);
				if (nextAction == RandomizationPattern.SKIP) {
					Logger.instance().debug("Skip forced");
					return null;
				}
			}
			Logger.instance().debug("Activity forced");
			return newActivity();
		}
	}
}
