package application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.Pane;
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
	private GridPane gamegrid,gamegrid1;
    @FXML
    private ResourceBundle resources;
	public static Pane p;
	public static boolean allowundo=true;
    @FXML
    private URL location;
    @FXML
    private List<Pane> labelList;
    @FXML
    public void restartgame(ActionEvent event) throws Exception{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(Mainmenucontroller.gridchoice));
    	root.setBackground(null);
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players[i]=new Player(new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue()));
    	}
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    @FXML
    public void undo(ActionEvent event) throws ClassNotFoundException, IOException{
    	System.out.println("undo");
    	int x=GridPane.getRowIndex(p); int y=GridPane.getColumnIndex(p);
    	int index= balls[x][y];
    	if(allowundo)
    		p.getChildren().remove(index-1);
    	balls=restoregrid();
    	allowundo=false;
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
    private void useraddorb(MouseEvent e) throws OrbOverloadException, IOException {
    	Node target = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(target);
        Integer rowIndex = GridPane.getRowIndex(target);
        if (colIndex == null || rowIndex == null) {
        } else {
        	int k=calculatecriticalmass(colIndex.intValue(),rowIndex.intValue());
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==k)
        		System.out.println("no more");
        	else{
            	savegrid();
            	allowundo=true;
            	if(balls[rowIndex.intValue()][colIndex.intValue()]==0)
            		addorb1(colIndex.intValue(),rowIndex.intValue());
            	else if(balls[rowIndex.intValue()][colIndex.intValue()]==1)
            		addorb2(colIndex.intValue(),rowIndex.intValue());
            	else if(balls[rowIndex.intValue()][colIndex.intValue()]==2)
            		addorb3(colIndex.intValue(),rowIndex.intValue());
            	balls[rowIndex.intValue()][colIndex.intValue()]++;
            	Mainmenucontroller.g.gamegrid.grid[rowIndex.intValue()][colIndex.intValue()].n_orbs=balls[rowIndex.intValue()][colIndex.intValue()];
        	}
        	
        }
    }
    public void addorb1(int x,int y){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.LINE);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	s.setMaterial(pm);
    	Pane edit=labelList.get(6*y+x);
    	edit.getChildren().add(s);
    	p=edit;
    	//gamegrid.add(edit,x,y);
    	s.setTranslateX(55);
    	s.setTranslateY(30);
        RotateTransition rotateTransition = new RotateTransition(); 
    	rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setDuration(Duration.millis(1000)); 
        rotateTransition.setNode(s);
        rotateTransition.setRate(10);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.play();
        /*Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(170.0f);
        line.setEndY(0.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(1000));
         pathTransition.setInterpolator(Interpolator.LINEAR);
         pathTransition.setPath(line);
         pathTransition.setNode(s);
         pathTransition.setCycleCount(1);
         pathTransition.play(); */
    }
    public void addorb2(int x,int y){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.LINE);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	s.setMaterial(pm);
    	Pane edit=labelList.get(6*y+x);
    	edit.getChildren().add(s);
    	p=edit;
    	//gamegrid.add(s,x,y);
    	s.setTranslateX(40);
    	s.setTranslateY(30);
        Circle path=new Circle(54,30,14.4);
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
    	Pane edit=labelList.get(6*y+x);
    	p=edit;
    	edit.getChildren().add(s);
    	//gamegrid.add(s,x,y);
    	s.setTranslateX(43);
    	s.setTranslateY(20);
        Circle path=new Circle(54,30,12.4);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(5000));
        //pathTransition.setDelay(Duration.ZERO);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(path);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();
    }
    public static void savegrid() throws IOException {
		ObjectOutputStream out=null;
		try{
			out=new ObjectOutputStream(new FileOutputStream("grid1state.txt"));
			out.writeObject(balls);
		}
		finally{
			out.close();
		}
	}
	public static int[][] restoregrid() throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream("grid1state.txt"));
			int[][] g=(int[][])in.readObject();
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
		balls=new int[9][6];
	}
}