package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class ended {
	@FXML
	private AnchorPane root;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    void backtomenu(ActionEvent event) throws IOException {
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	Slider s=(Slider)page.getChildren().get(1);
    	System.out.println(Mainmenucontroller.playercount);
    	s.setValue(Mainmenucontroller.playercount);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }

    @FXML
    void restart(ActionEvent event) throws IOException {
    	color[] previous=new color[Mainmenucontroller.playercount];
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		color c=new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue());
    		previous[i]=c;
    	}
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players.add(new Player(new color(previous[i].red,previous[i].green,previous[i].blue),i));
    	}
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(Mainmenucontroller.gridchoice));
    	root.setBackground(null);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    @FXML
    void initialize() {

    }
}
