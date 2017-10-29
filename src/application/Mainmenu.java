package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class Mainmenu extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane rootpane= (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
        Scene scene = new Scene(rootpane);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	public static void main(String[] arg) throws IOException{
		launch(arg);
		System.out.println("Exit");
		//Mainmenucontroller.serialize(Mainmenucontroller.g,"GAME.txt");
		
	}
}
