package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    	s.setValue(Mainmenucontroller.playercount);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }

    @FXML
    void restart(ActionEvent event) throws IOException {
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Grid2.fxml"));
    	color[] previous=new color[Mainmenucontroller.playercount];
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		//previous[i]=Mainmenucontroller.g.players.get(i).Color;
    	}
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players.add(new Player(new color(previous[i].red,previous[i].green,previous[i].blue),i));
    	}
    	//index=0;
    	//ongoing=Mainmenucontroller.g; 
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
