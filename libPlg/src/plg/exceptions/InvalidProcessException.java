package plg.exceptions;

/**
 * This class is an exception thrown when a process is somehow illegal.
 * 
 * @author Andrea Burattin
 */
public class InvalidProcessException extends RuntimeException {

	private static final long serialVersionUID = -6440458144830308073L;

	/**
	 * Exception constructor
	 * 
	 * @param message the exception message
	 */
	public InvalidProcessException(String message) {
		super(message);
	}
}
