package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class Mainmenu extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Game old=Mainmenucontroller.deserialize("GAME.txt");
		AnchorPane rootpane= (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
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
		Mainmenucontroller.g.current_turn=Gridcontroller2.index;
	    for(int i=0;i<15;i++){
    		for(int j=0;j<10;j++){
    			Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs=Gridcontroller2.balls[i][j];
    			Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor=Gridcontroller2.beta[i*10+j];
    		}
    	}
	    Mainmenucontroller.serialize(Mainmenucontroller.g,"GAME.txt");
	}
}
