package application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
public class Mainmenucontroller implements Initializable {
	static String gridchoice="Grid1.fxml";
	@FXML
    private AnchorPane rootpane;
	@FXML
    private ResourceBundle resources;
	@FXML
    private Slider no_of_players;
	@FXML 
	public static Button Resumegame;
	static int playercount=2;
    @FXML
    private URL location;
    public static Game g;
    @FXML
    void StartGame(ActionEvent event) throws Exception {
    	SettingsController.calldefault();
    	playercount=(int)no_of_players.getValue();
    	g=new Game(playercount,gridchoice);
    	for(int i=0;i<playercount;i++){
    		g.players[i]=new Player(new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue()));
    	}
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));
    	GridPane grid=(GridPane)page.getChildren().get(0);
    	if(gridchoice.equals("Grid1.fxml"))
    		Gridcontroller.components.addAll(grid.getChildren());
    	else
    		Gridcontroller2.components.addAll(grid.getChildren());
    	rootpane.setBackground(null);
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
    }
    @FXML
    void gridselection1(ActionEvent event){
    	gridchoice="Grid1.fxml";
    }
    @FXML
    void gridselection2(ActionEvent event){
    	gridchoice="Grid2.fxml";
    }
    @FXML
    public void ClickedSettings(ActionEvent event) throws Exception {
    	playercount=(int)no_of_players.getValue();
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Settings.fxml"));
    	rootpane.setBackground(null);
    	for(int i=1;i<=8;i++){
    		ColorPicker c=(ColorPicker)page.getChildren().get(i);
    		c.setValue(SettingsController.values[i-1]);
    	}
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
    }
    public static void serialize(Game g,String v) throws IOException {
		ObjectOutputStream out=null;
		try{
			out=new ObjectOutputStream(new FileOutputStream(v));
			out.writeObject(g);
		}
		finally{
			out.close();
		}
	}
	public static Game deserialize(String h) throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream(h));
			Game g=(Game)in.readObject();
			return g;
		}
		finally{
			in.close();
		}
	}
    @FXML
    void initialize() {
    	
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}
