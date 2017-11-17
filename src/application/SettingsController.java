package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
/**
 * <h1>This class acts as controller class for the Settings.fxml file</h1>
 * This is the class which handles the settings page
 * @author hrithik
 */

public class SettingsController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    /**
     * root/parent node of the settings page
     */
    @FXML
    private AnchorPane root;
    /**
     * Each colorpicker object is used to take color input for players
     */
    @FXML
    private static ColorPicker color_p8,color_p7,color_p6,color_p5,color_p4,color_p3,color_p2,color_p1;
    /**
     * Array to store color values of players
     */
    static Color[] values;
    /**
     * This variable is used so that default values are set
     * only once.
     */
    public static int counter=0;
    
    /**
     * This method is used to handle the event when the user tries to
     * change the color of the orb correponding to a player.
     * @param event This is the ActionEvent which is generated when
     * user changes color for a player
     * @throws SameColorException 
     */
    @FXML
    public void changed(ActionEvent event) throws SameColorException{
    	ColorPicker c=(ColorPicker)event.getSource();
    	int index=getindex(c)-1;
    	for(int i=0;i<8;i++){
    		if(i!=index)
    			if(values[i].equals(c.getValue())){
    				c.setValue(values[index]);
    				throw new SameColorException("Same colour for player "+(index+1)+" and player "+(i+1));
    			}
    	}
    	values[index]=c.getValue();    	
    }
   
    /**
     * This method is used to handle the event when player clicks the menu
     * button on the settings page. It simply loads the Mainmenu.fxml page.
     * @param event This parameter is the ActionEvent which is generated
     * when the user clicks the menu button.
     * @throws IOEException
     */
    @FXML
    public void backtomenu(ActionEvent event) throws IOException{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	Slider s=(Slider)page.getChildren().get(1);
    	Button b=(Button)page.getChildren().get(0);
    	s.setValue(Mainmenucontroller.playercount);
    	if(Mainmenucontroller.g==null || Mainmenucontroller.g.is_finished)
    		b.setDisable(true);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    /**
     * Initializes this class objects when Settings.fxml is loaded
     */
    @FXML
    void initialize() {
    	if(counter==0)
    	{
    		values=new Color[8];
    		for(int i=0;i<8;i++)
    			values[i]=new Color(0,0,0,0);
    		color_p8=new ColorPicker();
    		color_p7=new ColorPicker();
    		color_p6=new ColorPicker();
    		color_p5=new ColorPicker();
    		color_p4=new ColorPicker();
    		color_p3=new ColorPicker();
    		color_p2=new ColorPicker();
    		color_p1=new ColorPicker();
    		setdefaults();
    	}
    }
    /**
     * This method is used to initialize the class objects
     * and call the setdefaults() method only once.
     */
    public static void calldefault(){
    	if(counter==0)
    	{
    		values=new Color[8];
    		for(int i=0;i<8;i++){
    			values[i]=new Color(0,0,0,0);
    		}
    		color_p8=new ColorPicker();
    		color_p7=new ColorPicker();
    		color_p6=new ColorPicker();
    		color_p5=new ColorPicker();
    		color_p4=new ColorPicker();
    		color_p3=new ColorPicker();
    		color_p2=new ColorPicker();
    		color_p1=new ColorPicker();
    		setdefaults();
    	}
    }
    /**
     * This method is used to define the default color
     * of the players.
     */
    public static void setdefaults(){
    	counter++;
    	color_p8.setValue(new Color(0.5,0.5,0.5,1.0));
    	color_p7.setValue(new Color(1.0,1.0,1.0,1.0));
    	color_p6.setValue(new Color(1.0,1.0,0,1.0));
    	color_p5.setValue(new Color(1.0,0,1.0,1.0));
    	color_p4.setValue(new Color(1.0,0,0,1.0));
    	color_p3.setValue(new Color(0,1.0,1.0,1.0));
    	color_p2.setValue(new Color(0,1.0,0,1.0));
    	color_p1.setValue(new Color(0,0,1.0,1.0));
    	values[0]=color_p1.getValue();
    	values[1]=color_p2.getValue();
    	values[2]=color_p3.getValue();
    	values[3]=color_p4.getValue();
    	values[4]=color_p5.getValue();
    	values[5]=color_p6.getValue();
    	values[6]=color_p7.getValue();
    	values[7]=color_p8.getValue();
    }
    /**
     * This method is used to get the index of the earlier defined
     * colorpicker objects.
     * @param c This is the first paramter whose index we find
     * @return int This returns index of the parameter (colorpicker)
     */
    public int getindex(ColorPicker c){
    	String s=c.getId();
    	int index=Integer.parseInt(s.substring(s.length()-1));
    	return index;
    }
}
