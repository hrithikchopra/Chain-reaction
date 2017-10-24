package application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
public class Mainmenucontroller implements Initializable {
	static String gridchoice="Grid1.fxml";
	@FXML
    private AnchorPane rootpane;
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void slided(ActionEvent event) {

    }
    @FXML
    void clicked(ActionEvent event) throws Exception {
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
    	//System.out.println("working1");
    }
    @FXML
    void itemclicked2(ActionEvent event){
    	gridchoice="Grid2.fxml";
    	//System.out.println("working2");
    }
    @FXML
    public void settingsclicked(ActionEvent event) throws Exception {
        Settings set=new Settings();
        set.showit();
    }

    @FXML
    void initialize() {
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
