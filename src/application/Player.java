package application;
import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	color Color;
	int number_of_orbs_onboard;
	public Player(color c){
		this.Color=c;
		this.number_of_orbs_onboard=0;
	}
}
class color implements Serializable {
	private static final long serialVersionUID = 1L;
	double red,green,blue;
	public color(double red,double green,double blue){
		this.red=red;
		this.green=green;
		this.blue=blue;
	}
}