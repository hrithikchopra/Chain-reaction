package application;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Gridcontroller implements Initializable {
	static int[][] balls;
	@FXML
    private AnchorPane root;
	@FXML
	private GridPane gamegrid;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    public void restartgame(ActionEvent event) throws Exception{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(Mainmenucontroller.gridchoice));
    	root.setBackground(null);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    @FXML
    void backtomenu(ActionEvent event) throws Exception{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	//root.setBackground(null);
    	Slider s=(Slider)page.getChildren().get(0);
    	s.setValue(Mainmenucontroller.playercount);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    int calculatecriticalmass(int x,int y){
    	if(x==0 && y==0 || x==5 && y==8 || x==5 && y==0 || x==0 && y==8)
    		return 1;
    	else if(x==0 || x==5 || y==0 || y==8)
    		return 2;
    	else
    		return 3;
    }
    @FXML
    private void mouseEntered(MouseEvent e) {
    	Node target = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(target);
        Integer rowIndex = GridPane.getRowIndex(target);
        if (colIndex == null || rowIndex == null) {
        } else {
        	int k=calculatecriticalmass(colIndex.intValue(),rowIndex.intValue());
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==k)
        		System.out.println("no more");
        	else{
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==0)
        		addorb1(colIndex.intValue(),rowIndex.intValue());
        	else if(balls[rowIndex.intValue()][colIndex.intValue()]==1)
        		addorb2(colIndex.intValue(),rowIndex.intValue());
        	else if(balls[rowIndex.intValue()][colIndex.intValue()]==2)
        		addorb3(colIndex.intValue(),rowIndex.intValue());
        	balls[rowIndex.intValue()][colIndex.intValue()]++;
        	}
        }
    }
    public void addorb1(int x,int y){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.LINE);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	s.setMaterial(pm);
    	gamegrid.add(s,x,y);
    	s.setTranslateX(45);
        RotateTransition rotateTransition = new RotateTransition(); 
    	rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setDuration(Duration.millis(1000)); 
        rotateTransition.setNode(s);
        rotateTransition.setRate(10);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.play();
    }
    public void addorb2(int x,int y){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.LINE);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	s.setMaterial(pm);
    	gamegrid.add(s,x,y);
    	s.setTranslateX(30);
        Circle path=new Circle(44,6.5,14.4);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(5000));
       // pathTransition.setDelay(Duration.ZERO);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(path);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();   
    }
    public void addorb3(int x,int y){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.LINE);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	s.setMaterial(pm);
    	gamegrid.add(s,x,y);
    	s.setTranslateX(33);
    	s.setTranslateY(-10);
        Circle path=new Circle(44,-6.5,12.4);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(5000));
        //pathTransition.setDelay(Duration.ZERO);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(path);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();
    }
    @FXML
    void initialize() {
    	
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		balls=new int[9][6];
		}
}