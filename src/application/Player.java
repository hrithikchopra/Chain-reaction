package application;
import java.io.Serializable;
/**
 * This is the class which handles the attributes of a player
 * such as orb color,number of orbs,number of turns
 * @author hrithik
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	color Color;
	int number_of_orbs_onboard;
	int turns=0;
	String name;
	boolean lost=false;
	public Player(color c,int name){
		this.Color=c;
		this.number_of_orbs_onboard=0;
		name++;
		this.name="Player"+name;
	}
	@Override
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
 * This class acts as controller class for the
 * This is the class which handles the settings page
 * @author hrithik
 */
class color implements Serializable {
	private static final long serialVersionUID = 1L;
	double red,green,blue;
	public color(double red,double green,double blue){
		this.red=red;
		this.green=green;
		this.blue=blue;
	}
	@Override
	public boolean equals(Object o){
		color c=(color)o;
		if(red==c.red && green==c.green && blue==c.blue)
			return true;
		else
			return false;
	}
	@Override
	public String toString(){
		return this.red+" "+this.green+" "+this.blue;
	}
}