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

public class Mainmenucontroller implements Initializable {
	static String gridchoice="Grid1.fxml";
	@FXML
    private AnchorPane rootpane;
	@FXML
    private ResourceBundle resources;
	@FXML
    private Slider no_of_players;
	@FXML 
	private Button Resumegame;
	static int playercount=2;
    @FXML
    private URL location;
    public static Game g;
    @FXML
    void Resume(ActionEvent event) throws Exception{
    	Game old=deserialize("GAME.txt");
    	if(old.gridsize.equals("Grid1.fxml"))
    		Build1(old);
    	else
    		Build2(old);
    }
    private void Build2(Game old) throws Exception {
    	//Gridcontroller2 h=new Gridcontroller2();
    	gridchoice=old.gridsize;
    	//h.initialize();
    	playercount=old.no_of_players;
    	g=old;
    	Gridcontroller2.resume=true;
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));
    	for(Player a:g.players){
    		Gridcontroller2.counter+=a.number_of_orbs_onboard;
    	}
    	rootpane.setBackground(null);
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
	}
    private void Build1(Game old) throws Exception {
    	gridchoice=old.gridsize;
    	playercount=old.no_of_players;
    	g=old;
    	Gridcontroller1.resume=true;
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));
    	for(Player a:g.players){
    		Gridcontroller1.counter+=a.number_of_orbs_onboard;
    	}
    	rootpane.setBackground(null);
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
	}
	@FXML
    void StartGame(ActionEvent event) throws Exception {
    	SettingsController.calldefault();
    	playercount=(int)no_of_players.getValue();
    	g=new Game(playercount,gridchoice);
    	for(int i=0;i<playercount;i++){
    		g.players.add(new Player(new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue()),i));
    	}
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));    	
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
