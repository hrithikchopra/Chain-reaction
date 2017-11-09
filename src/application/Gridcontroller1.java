package application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Gridcontroller1 implements Initializable {
	static int[][] balls;
	public static ObservableList<Node> components;
	public Sphere[][] alpha;
	@FXML
    private AnchorPane root;
	@FXML
	GridPane gamegrid;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    public void restartgame(ActionEvent event) throws Exception{
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource(Mainmenucontroller.gridchoice));
    	root.setBackground(null);
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	Mainmenucontroller.g.players.removeAll(Mainmenucontroller.g.players);
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players.add(new Player(new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue()),i));
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
    	gamegrid.getChildren().setAll(components);
    	balls=restoregrid();
    }
    @FXML
    void backtomenu(ActionEvent event) throws Exception{
    	savegrid();
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	//root.setBackground(null);
    	Slider s=(Slider)page.getChildren().get(1);
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
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==k){
        		components.remove(0,components.size());
            	components.addAll(gamegrid.getChildren());
            	savegrid();
        		split(colIndex.intValue(),rowIndex.intValue());
            	balls[rowIndex.intValue()][colIndex.intValue()]=0;
        	}
        	else{
        		components.remove(0,components.size());
            	components.addAll(gamegrid.getChildren());
            	savegrid();
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
    	alpha[y*6+x][0]=s;
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
    	alpha[y*6+x][1]=s;
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
    	gamegrid.getChildren().remove(alpha[y*6+x][1]);
    	addorb2(x,y);
    	alpha[y*6+x][2]=s;
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
    public void split(int x,int y){
    	int k=calculatecriticalmass(x,y);
    	PhongMaterial pm=new PhongMaterial();
    	switch(k){
    	case 1:
    		gamegrid.getChildren().remove(alpha[y*6+x][0]);
    		Sphere s=new Sphere(10);
        	s.setDrawMode(DrawMode.LINE);
        	pm.setDiffuseColor(Color.LIME);
        	s.setMaterial(pm);
        	gamegrid.add(s,x,y);
        	s.setTranslateX(25);
        	Sphere n=new Sphere(10);
        	n.setDrawMode(DrawMode.LINE);
        	n.setMaterial(pm);
        	gamegrid.add(n,x,y);
        	s.setTranslateX(27);
        	if(x==0 && y==0){
        		goright(s,x+1,y); godown(n,x,y+1);
        	}
        	else if(x==5 && y==0){
        		goleft(s,x-1,y); godown(n,x,y+1);
        	}
        	else if(x==0 && y==8){
        		goright(s,x+1,y); goUp(n,x,y-1);
        	}
        	else{
        		goleft(s,x-1,y); goUp(n,x,y-1);
        	}
    		break;
    	case 2:
    		gamegrid.getChildren().remove(alpha[y*6+x][0]);
    		gamegrid.getChildren().remove(alpha[y*6+x][1]);
    		Sphere n1=new Sphere(10);
        	n1.setDrawMode(DrawMode.LINE);
        	pm.setDiffuseColor(Color.LIME);
        	n1.setMaterial(pm);
        	gamegrid.add(n1,x,y);
        	n1.setTranslateX(25);
        	Sphere n2=new Sphere(10);
        	n2.setDrawMode(DrawMode.LINE);
        	n2.setMaterial(pm);
        	gamegrid.add(n2,x,y);
        	n2.setTranslateX(27);
        	Sphere n3=new Sphere(10);
        	n3.setDrawMode(DrawMode.LINE);
        	n3.setMaterial(pm);
        	gamegrid.add(n3,x,y);
        	n3.setTranslateX(24);
        	if(x==0){
        		godown(n1,x,y+1);goright(n2,x+1,y); goUp(n3,x,y-1);
        	}
        	else if(x==5){
        		godown(n1,x,y+1); goleft(n2,x-1,y); goUp(n3,x,y-1);
        	}
        	else if(y==0){
        		goleft(n1,x-1,y); goright(n2,x+1,y); godown(n3,x,y+1);
        	}
        	else{
        		goleft(n1,x-1,y); goright(n2,x+1,y); goUp(n3,x,y-1);
        	}
    		break;
    	case 3:
    		gamegrid.getChildren().remove(alpha[y*6+x][0]);
    		gamegrid.getChildren().remove(alpha[y*6+x][1]);
    		gamegrid.getChildren().remove(alpha[y*6+x][2]);
    		Sphere s1=new Sphere(10);
        	s1.setDrawMode(DrawMode.LINE);
        	pm.setDiffuseColor(Color.LIME);
        	s1.setMaterial(pm);
        	gamegrid.add(s1,x,y);
        	s1.setTranslateX(25);
        	Sphere s2=new Sphere(10);
        	s2.setDrawMode(DrawMode.LINE);
        	s2.setMaterial(pm);
        	gamegrid.add(s2,x,y);
        	s2.setTranslateX(27);
        	Sphere s3=new Sphere(10);
        	s3.setDrawMode(DrawMode.LINE);
        	s3.setMaterial(pm);
        	gamegrid.add(s3,x,y);
        	s1.setTranslateX(25);
        	Sphere s4=new Sphere(10);
        	s4.setDrawMode(DrawMode.LINE);
        	s4.setMaterial(pm);
        	gamegrid.add(s4,x,y);
        	s4.setTranslateX(25);
        	s4.setTranslateY(-3);
    		godown(s1,x,y+1);
    		goleft(s2,x-1,y);
    		goright(s3,x+1,y);
    		goUp(s4,x,y-1);
    		break;
    	}
    }
    void goright(Sphere s,int x,int y){
    	Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(105.0f);
        line.setEndY(0.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(300));
        pathTransition.setInterpolator(Interpolator.LINEAR);
       // pathTransition.setRate(100); 
        pathTransition.setPath(line);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	gamegrid.getChildren().remove(s);                                                                     
					useraddorb(x,y);
            }
        });
    }
    void goleft(Sphere s,int x,int y){
    	Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(-50.0f);
        line.setEndY(0.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(300));
        pathTransition.setInterpolator(Interpolator.LINEAR);
       // pathTransition.setRate(100); 
        pathTransition.setPath(line);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	gamegrid.getChildren().remove(s);                                                                     
					useraddorb(x,y);
            }
        });
    }
    void godown(Sphere s,int x,int y){
    	Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(45.0f);
        line.setEndY(40.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(300));
        pathTransition.setInterpolator(Interpolator.LINEAR);
       // pathTransition.setRate(100); 
        pathTransition.setPath(line);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	gamegrid.getChildren().remove(s);                                                                     
					useraddorb(x,y);
            }
        });
    }
	void goUp(Sphere s,int x,int y){
		Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(45.0f);
        line.setEndY(-40.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(300));
        pathTransition.setInterpolator(Interpolator.LINEAR);
       // pathTransition.setRate(100); 
        pathTransition.setPath(line);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	gamegrid.getChildren().remove(s);                                                                     
					useraddorb(x,y);
            }
        });
    }
	void useraddorb(int x,int y){
    	int k=calculatecriticalmass(x,y);
    	if(balls[y][x]==k){
    		split(x,y);
        	balls[y][x]=0;
    	}
    	else{
        	if(balls[y][x]==0)
        		addorb1(x,y);
        	else if(balls[y][x]==1)
        		addorb2(x,y);
        	else if(balls[y][x]==2)
        		addorb3(x,y);
        	balls[y][x]++;
    	}
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
		alpha=new Sphere[54][3];
		components = FXCollections.observableArrayList();
	}
}