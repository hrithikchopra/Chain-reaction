package application;
import java.io.Serializable;

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
		this.name="Player"+name;
	}
	@Override
	public String toString(){
		return name;
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