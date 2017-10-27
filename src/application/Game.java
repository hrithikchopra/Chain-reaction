package application;
import java.io.Serializable;
import javafx.scene.paint.Color;

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	int no_of_players;
	Player players[];
	boolean is_finished=false;
	Grid gamegrid;
	public Game(int no_of_players,String gridsize){
		this.no_of_players=no_of_players;
		players=new Player[this.no_of_players];
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
class Cell implements Serializable {
	private static final long serialVersionUID = 1L;
	Color currentcolor;
	Player currentplayer;
	public Cell(){
		this.currentcolor=null;
		this.currentplayer=null;
	}
	public void setplayer(Player p){
		this.currentplayer=p;
		this.currentcolor=p.color;
	}
}
class Grid implements Serializable {
	private static final long serialVersionUID = 2L;
	Cell[][] grid;
	public Grid(int c){
		if(c==1){
			grid=new Cell[5][9];
		}
		else{
			grid=new Cell[10][15];
		}
	}
}