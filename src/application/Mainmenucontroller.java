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
	static int playercount=2;
    @FXML
    private URL location;
    @FXML
    void clicked(ActionEvent event) throws Exception {
    	playercount=(int)no_of_players.getValue();
    	Game g=new Game(playercount,gridchoice);
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));
    	rootpane.setBackground(null);
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
    }
    @FXML
    void itemclicked(ActionEvent event){
    	gridchoice="Grid1.fxml";
    }
    @FXML
    void itemclicked2(ActionEvent event){
    	gridchoice="Grid2.fxml";
    }
    @FXML
    public void settingsclicked(ActionEvent event) throws Exception {
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
