package application;
import java.io.Serializable;
/**
 * This is the class which handles the attributes of a player
 * such as orb color,number of orbs,number of turns
 * @author hrithik
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Color assigned to the player (Orb color)
	 */
	color Color;
	/**
	 * Number of orbs of this player on the grid
	 */
	int number_of_orbs_onboard;
	/**
	 * Number of turns taken by the player
	 */
	int turns=0;
	/**
	 * Name of the player
	 */
	String name;
	/**
	 * Boolean variable telling whether player is still in game or not
	 */
	boolean lost=false;
	/**
	 *Constructor to initialize new player
	 *@param c This arguement denotes color assigned to the player
	 *@param name Name assigned to the player
	 */
	public Player(color c,int name){
		this.Color=c;
		this.number_of_orbs_onboard=0;
		name++;
		this.name="Player"+name;
	}
	@Override
	/**
	 * This method returns name of the player
	 * @return name
	 */
	public String toString(){
		return name;
	}
	@Override
	public boolean equals(Object o){
		Player comp=(Player)o;
		if(comp.Color.equals(this.Color))
			return true;
		else
			return false;
	}
}
/**
 *This class was created to mimic JavaFx.Color class.
 *Since javafx objects are not serializable this class was created.
 * @author hrithik
 */
class color implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Red component of color
	 */
	double red;
	/**
	 * Green component of color
	 */
	double green;
	/**
	 * Blue component of color
	 */
	double blue;
	/**
	 * constructor to initialize a new color object
	 * @param red This denotes Red component of color
	 * @param green This denotes Green component of color
	 * @param blue This denotes Blue component of color
	 */
	public color(double red,double green,double blue){
		this.red=red;
		this.green=green;
		this.blue=blue;
	}
	
	@Override
	/**This method determines whether two color objects are equal or not
	 * @param o This is the object which is being compared to
	 */
	public boolean equals(Object o){
		color c=(color)o;
		if(red==c.red && green==c.green && blue==c.blue)
			return true;
		else
			return false;
	}
	@Override
	/**
	 *This method returns a String denoting RGB value of color
	 */
	public String toString(){
		return this.red+" "+this.green+" "+this.blue;
	}
}