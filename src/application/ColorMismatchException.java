package application;
/**
 * <h1>This class defines the ColorMismatch Exception</h1>
 * It is thrown when a user clicks on a cell which 
 * is loaded with orbs of another player.
 * @author hrithik
 */

public class ColorMismatchException extends Exception{
	private static final long serialVersionUID = 1L;
	/**
	 * this is constructor
	 * @param message
	 */
	ColorMismatchException(String message){
		super(message);
	}
}
