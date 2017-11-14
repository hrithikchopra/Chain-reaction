package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class Mainmenu extends Application {
	static AnchorPane rootpane;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Game old=Mainmenucontroller.deserialize("GAME.txt");
		rootpane=(AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
		Button resume=(Button)rootpane.getChildren().get(0);
		resume.setDisable(true);
		Scene scene = new Scene(rootpane);
		if(old!=null && !old.is_finished){
        	resume.setDisable(false);
        }
        if(old==null){
        	System.out.println("null");
        } 	
        primaryStage.setScene(scene);
        primaryStage.show(); 
	}
	public static void main(String[] arg) throws IOException{
		launch(arg);
		for(Player p:Mainmenucontroller.g.players){
			p.number_of_orbs_onboard=0;
		}
		if(Mainmenucontroller.gridchoice.equals("Grid1.fxml")){
			Mainmenucontroller.g.current_turn=Gridcontroller1.index;
			for(int i=0;i<9;i++){
	    		for(int j=0;j<6;j++){
	    			Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs=Gridcontroller1.balls[i][j];
	    			Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor=Gridcontroller1.beta[i*6+j];
	    			for(Player p:Mainmenucontroller.g.players){
	    				if(Gridcontroller1.beta[i*6+j]==null)
	    					break;
	    				else if(p.Color.equals(Gridcontroller1.beta[i*6+j])){
	    					p.number_of_orbs_onboard+=Gridcontroller1.balls[i][j];
	    					break;
	    				}
	    			}
	    		}
	    	}
		}
		else{
			Mainmenucontroller.g.current_turn=Gridcontroller2.index;
			for(int i=0;i<15;i++){
	    		for(int j=0;j<10;j++){
	    			Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs=Gridcontroller2.balls[i][j];
	    			Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor=Gridcontroller2.beta[i*10+j];
	    			for(Player p:Mainmenucontroller.g.players){
	    				if(Gridcontroller2.beta[i*10+j]==null)
	    					break;
	    				else if(p.Color.equals(Gridcontroller2.beta[i*10+j])){
	    					p.number_of_orbs_onboard+=Gridcontroller2.balls[i][j];
	    					break;
	    				}
	    			}
	    		}
	    	}
		}
	    Mainmenucontroller.serialize(Mainmenucontroller.g,"GAME.txt"); 
	}
}
