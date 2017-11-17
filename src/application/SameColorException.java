package application; 
/**This class defines the SameColor Exception.
* It is thrown when a user sets the color of a player
* to a value which is already assigned to another player.
* @author hrithik
*/

public class SameColorException extends Exception{
	private static final long serialVersionUID = 1L;
	SameColorException(String message){
		super(message);
	}
}

