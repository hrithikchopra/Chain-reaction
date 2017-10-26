package application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
public class SettingsController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane root;
    @FXML
    private ColorPicker color_p8,color_p7,color_p6,color_p5,color_p4,color_p3,color_p2,color_p1;
    static Color[] values;
    static int counter=0;
    @FXML
    void changed(ActionEvent event) {
    	ColorPicker c=(ColorPicker)event.getSource();
    	values[getindex(c)-1]=c.getValue();
    }
    @FXML
    void backtomenu(ActionEvent event) throws Exception{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	Slider s=(Slider)page.getChildren().get(0);
    	s.setValue(Mainmenucontroller.playercount);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    @FXML
    void initialize() {
    	if(counter==0)
    	{
    		values=new Color[8];
    		for(int i=0;i<8;i++)
    			values[i]=new Color(0,0,0,0);
    		setdefaults();
    	}
    }
    public void setdefaults(){
    	counter++;
    	color_p8.setValue(new Color(0,0,0,1.0));
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
    public int getindex(ColorPicker c){
    	String s=c.getId();
    	int index=Integer.parseInt(s.substring(s.length()-1));
    	return index;
    }
}
