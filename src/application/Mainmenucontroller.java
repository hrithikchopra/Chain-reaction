package application;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
/**
 * This class acts as controller class for the Mainmenu.fxml file
 * This is the class which handles the mainmenu page.
 * All user actions such as starting a game,changing settings is supported
 * by this class.
 * @author hrithik
 */
public class Mainmenucontroller implements Initializable {
	/**
	 * String variable denoting user selection of grid size
	 */
	
	static String gridchoice="Grid1.fxml";
	
	/**
	 * root/parent node of the mainmenu page
	 */
	@FXML
    private AnchorPane rootpane;
	@FXML
    private ResourceBundle resources;
	/**
	 * JavaFX slider object used to set number of players playing chain reaction
	 */
	@FXML
    private Slider no_of_players;
	 
	/**
	 * JavaFX button object used to resume a game
	 */
	@FXML
	private Button Resumegame;
	
	/**
	 * JavaFX button object used to open settings page
	 */
	@FXML
	private Button SettingsButton;
	/**
	 * Integer variable denoting number of players playing the game
	 */
	static int playercount=2;
    @FXML
    private URL location;
    /**
	 * Game object which is initialized when user clicks Play Game
	 */
    public static Game g;
   
    /**
     * This method is used to handle the event when player clicks the resume
     * button on the mainmenu page. It is possible to resume only if the previous
     * game is unfinished.
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the resume button.
     * @throws Exception
     */
    @FXML
    void Resume(ActionEvent event) throws Exception{
    	Game old=deserialize("GAME.txt");
    	if(old.gridsize.equals("Grid1.fxml"))
    		Build1(old);
    	else
    		Build2(old);
    }
    /**
     * This method is used to build the 15X10 grid incase the user wants to resume a previous game
     * @param old instance of game which will be restored
     * @throws IOException
     */
    private void Build2(Game old) throws IOException {
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
    /**
     * This method is used to build the 9X6 grid incase the user wants to resume a previous game
     * @param old instance of game which will be restored
     * @throws IOException
     */
    private void Build1(Game old) throws IOException {
    	gridchoice=old.gridsize;
    	playercount=old.no_of_players;
    	g=old;
    	Gridcontroller1.resume=true;
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(gridchoice));
    	rootpane.setBackground(null);
    	//System.out.println(Gridcontroller1.counter);
    	for(Player a:g.players){
    		//System.out.println(a.number_of_orbs_onboard);
    		Gridcontroller1.counter+=a.number_of_orbs_onboard;
    	}
    	//System.out.println(Gridcontroller1.counter);
    	if(rootpane==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		rootpane.getChildren().setAll(page);
	}
	
	/**
     * This method is used to handle the event when player clicks the Start game
     * button on the mainmenu page. 
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the play game button.
     * @throws Exception
     */
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
    /**
     * This method handles grid selection. It is initialized when user selects 9X6
     * @param event This parameter is the ActionEvent which is generated
     * when the user chooses grid.
     */
    @FXML
    void gridselection1(ActionEvent event){
    	gridchoice="Grid1.fxml";
    }
   
    /**
     * This method handles grid selection. It is initialized when user selects 15X10
     * @param event This parameter is the ActionEvent which is generated
     * when the user chooses grid.
     */
    @FXML
    void gridselection2(ActionEvent event){
    	gridchoice="Grid2.fxml";
    }
    
    /**
     * This method shows the about screen
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the about button.
     */
    @FXML
    void about(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("ABOUT");
    	alert.setHeaderText("About");
    	alert.setContentText("Chain Reaction Game (AP Project 2)"+"\n"+"By:"+"\n"+"     Hrithik chopra"+"\n"+"     Anshul gupta");
    	alert.showAndWait();
    }
    
    /**
     * This method shows the instructions of the game
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the instructions button.
     * @throws FileNotFoundException
     */
    @FXML
    void instructions(ActionEvent event) throws FileNotFoundException {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Instructions");
    	alert.setHeaderText("Instructions");
    	File s=new File("Instructions.txt");
    	String instructions="";
    	Scanner scan=new Scanner(s);
    	while(scan.hasNextLine()){
    		String add=scan.nextLine();
    		if(Integer.parseInt(add.substring(0,1))>=2 && Integer.parseInt(add.substring(0,1))<=6){
    			instructions+="\n";
    		}
    		instructions+=add;
    	}
    	scan.close();
    	alert.setContentText(instructions);
    	alert.getDialogPane().setPrefHeight(475);
    	alert.getDialogPane().setPrefWidth(500);
    	alert.showAndWait();
    }
    
    /**
     * This method is used to handle the event when player clicks the settings
     * button on the mainmenu page.
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the settings button.
     * @throws Exception
     */
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
    /**
     * This method is used to serialize/save the game at any point of time
     * @param g Game class object which is to be saved
     * @param v Input file denoting the destination where game is saved
     * @throws IOException
     */
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
    /**
     * This method is used to deserialize/restore the game
     * @param h String value denoting the file from which we want to restore
     * @return Game object deserialized from input file
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
