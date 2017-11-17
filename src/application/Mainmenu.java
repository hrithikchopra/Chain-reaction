package application;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
/**
 * <h1>This class displays the Main menu of the game</h1>
 * It is also the root of the program as all other pages are
 * accessible from the main page
 * @author hrithik
 */
public class Mainmenu extends Application {
	static AnchorPane rootpane;
	/**
	 * This method creates the stage and start the game
	 * @param primaryStage This is stage on which the scene is created
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		rootpane=new AnchorPane();
		Scene scene = new Scene(rootpane);
		loadsplash(primaryStage);
		primaryStage.setScene(scene);
	}
	/**
	 * main function of the Mainmenu class
	 * It defines the initial point of execution
	 * @param arg
	 * @throws IOException
	 */
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
	/**
	 * This function helps in showing the splash screen at the beginning
	 * @param primaryStage this is main stage on which scene was build
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadsplash(Stage primaryStage) throws IOException, ClassNotFoundException{
		Stage p=new Stage();
		p.initStyle(StageStyle.TRANSPARENT);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("welcome.fxml"));
		StackPane splash= loader.load();
		//rootpane.getChildren().setAll(splash);
		FadeTransition transitionin=new FadeTransition(Duration.seconds(2),splash);
		transitionin.setFromValue(0);
		transitionin.setToValue(1);
		transitionin.setCycleCount(1);
		transitionin.play();
		Scene c=new Scene(splash);
		Platform.runLater(()->{
			p.setScene(c);
			p.show();
		});
		FadeTransition transitionout=new FadeTransition(Duration.seconds(1),splash);
		transitionout.setFromValue(1);
		transitionout.setToValue(0.4);
		transitionout.setCycleCount(1);
		transitionin.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	transitionout.play();
            }
        });
		Game old=Mainmenucontroller.deserialize("GAME.txt");
		AnchorPane mainpane=(AnchorPane)FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
		Button resume=(Button)mainpane.getChildren().get(0);
		resume.setDisable(true);
		
		if(old!=null && !old.is_finished){
        	resume.setDisable(false);
        }
        if(old==null){
        	System.out.println("null");
        } 
		transitionout.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	p.hide();
				rootpane.getChildren().setAll(mainpane);
				primaryStage.show();
				primaryStage.sizeToScene();
				primaryStage.resizableProperty().setValue(Boolean.FALSE);
            }
        });		
	}
}
