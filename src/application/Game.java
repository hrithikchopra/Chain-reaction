package application;

import java.io.Serializable;
import java.util.ArrayList;
/**
* <h1>This program simply supports the game by providing all the attributes
* No output is displayed by this program</h1>
* @author  Hrithik Chopra
* @author  Anshul Gupta
* 
*/

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	int no_of_players;
	int current_turn;
	ArrayList<Player> players;
	boolean is_finished=false;
	Grid gamegrid;
	String gridsize;
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
 * This class represents the grid state of the game
 * @author hrithik
 *
 */
class Grid implements Serializable {
	private static final long serialVersionUID = 2L;
	Cell[][] grid;
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
 * @inclue
 * This class represents each grid cell in the game
 * @author hrithik
 *
 */
class Cell implements Serializable {
	private static final long serialVersionUID = 1L;
	color currentcolor;
	Player currentplayer;
	int n_orbs;
	public Cell(){
		this.currentcolor=null;
		this.currentplayer=null;
		n_orbs=0;
	}
	public void setplayer(Player p){
		this.currentplayer=p;
		this.currentcolor=p.Color;
	}
}