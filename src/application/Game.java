package application;

import java.io.Serializable;
import java.util.ArrayList;
/**
* <h1>This program simply supports the game by providing all the attributes</h1>
* No output is displayed by this program
* @author  Hrithik Chopra
* @author  Anshul Gupta
* 
*/

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * Integer value denoting number of players playing the game
	 */
	int no_of_players;
	/**
	 * Integer value denoting number of total turns
	 */ 
	int current_turn;
	/**
	 * ArrayList of players playing the game
	 */ 
	ArrayList<Player> players;
	/**
	 * Boolean value telling whether game is finished or not
	 */ 
	boolean is_finished=false;
	/**
	 * Grid on which game is played
	 */
	Grid gamegrid;
	/**
	 * String value denoting size of Grid
	 */
	String gridsize;
	/** Constructor for initializing a new game in chain reaction
	 * @param no_of_players Integer value denoting number of players 
	 * @param gridsize String value indicating size of the grid
	 */
	public Game(int no_of_players,String gridsize){
		this.no_of_players=no_of_players;
		this.gridsize=gridsize;
		players=new ArrayList<Player>(no_of_players);
		if(gridsize.equals("Grid1.fxml")){
			gamegrid=new Grid(1);
		}
		else{
			gamegrid=new Grid(2);
		}
	}
	public static void main(String[] args) {
		
	}

}
/**
 * 
 * This class represents the grid state of the game
 * @author hrithik
 */
class Grid implements Serializable {
	private static final long serialVersionUID = 2L;
	/**
	 * 2D array of Cell class representing our grid
	 */
	Cell[][] grid;
	/**
	 * Constructor for Grid class
	 * @param c represent the grid choice (9X6 or 15X10)
	 */
	public Grid(int c){
		if(c==1){
			grid=new Cell[9][6];
			for(int i=0;i<9;i++){
				for(int j=0;j<6;j++){
					grid[i][j]=new Cell();
				}
			}
		}
		else{
			grid=new Cell[15][10];
			for(int i=0;i<15;i++){
				for(int j=0;j<10;j++){
					grid[i][j]=new Cell();
				}
			}
		}
	}
}
/**
 * This class represents each grid cell in the game
 * @author hrithik
 */
class Cell implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * represents color of the player which currently occupies this cell
	 */
	color currentcolor;
	/**
	 * represents the player which currently occupies this cell
	 */
	Player currentplayer;
	/**
	 * represents number of orbs in this cell
	 */
	int n_orbs;
	public Cell(){
		this.currentcolor=null;
		this.currentplayer=null;
		n_orbs=0;
	}
	/**
	 * This method is used to set the player which occupies 
	 * this grid cell
	 * @param p Represents the player which now occupies the cell
	 */
	public void setplayer(Player p){
		this.currentplayer=p;
		this.currentcolor=p.Color;
	}
}