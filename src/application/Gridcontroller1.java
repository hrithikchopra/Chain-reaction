package application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
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
/**
 *<h1> This class acts as controller class for the 9X6 Grid.</h1>
 * This is the class which handles all the operations of 
 * the game such as inserting an orb,undo,restart,splitting when
 * fully loaded
 * @author hrithik
 */
public class Gridcontroller1 implements Initializable {
	/**
	 * Integer variable which tells number of orbs in each cell
	 */
	static volatile int[][] balls;
	/**
	 * Integer variable to keep count of number of turns
	 */
	static int counter=0;
	/**
	 * Boolean variable which tells whether we are resuming a game or not
	 */
	public static boolean resume=false;
	/**
	 * Boolean variable which tells whether user can undo or not
	 */
	public static boolean undoflag=false;
	/**
	 * Game instance which represents the ongoing game
	 */
	static Game ongoing=Mainmenucontroller.g;
	/**
	 * Player instance whose turn it is
	 */
	static Player current;
	/**
	 * Integer variable which tells whose turn it is
	 */
	static int index;
	/**
	 * 2D array of orbs which stores instances of JavaFX spheres inserted in grid
	 */
	public Sphere[][] alpha;
	/**
	 * Boolean variable which tells whether a past move is over or not
	 */
	static boolean makemove=true;
	/**
	 * 2D array of color which stores instances of JavaFX Color 
	 */
	static volatile public color[] beta;
	/**
	 * root/parent node of the page
	 */
	@FXML
    private AnchorPane root;
	@FXML
	/**
	 * Gridpane
	 */
	GridPane gamegrid;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    /**
     * This method is used to restart a ongoing game
     * @param event This is the event which is generated when user clicks restart button 
     * @throws Exception
     */
    @FXML
    public void restartgame(ActionEvent event) throws Exception{
    	color[] previous=new color[Mainmenucontroller.playercount];
    	if(SettingsController.values==null)
    		SettingsController.calldefault();
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		color c=new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue());
    		previous[i]=c;
    	}
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	System.gc();
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players.add(new Player(new color(previous[i].red,previous[i].green,previous[i].blue),i));
    	}
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Grid1.fxml"));
    	index=0;
    	ongoing=Mainmenucontroller.g; 
    	root.setBackground(null);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    /**
     *  This method is used to undo the last move
     * @param event This is the event which is generated when user clicks undo button 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    public void undo(ActionEvent event) throws ClassNotFoundException, IOException{
    	if(undoflag){
    		gamegrid.getChildren().remove(55,gamegrid.getChildren().size());
    		counter--;
    		current.turns--;
    		balls=restoregrid();
    		beta=restoregrid1();
    		for(int i=0;i<9;i++){
    			for(int j=0;j<6;j++){
    				int n=balls[i][j];
    				switch(n){
    				case 1:
    					addorb1(j,i,beta[i*6+j]);
    					break;
    				case 2:
    					addorb1(j,i,beta[i*6+j]);
    					addorb2(j,i,beta[i*6+j]);
    					break;
    				case 3:
    					addorb1(j,i,beta[i*6+j]);
    					addorb2(j,i,beta[i*6+j]);
    					addorb3(j,i,beta[i*6+j]);
    					break;
    				}
    			}
    		}
    		for(Player p:ongoing.players){
    			p.number_of_orbs_onboard=0;
    		}
    		for(int i=0;i<9;i++){
        		for(int j=0;j<6;j++){
        			for(Player p:ongoing.players){
        				if(beta[i*6+j]==null)
        					break;
        				else if(p.Color.equals(beta[i*6+j])){
        					p.number_of_orbs_onboard+=balls[i][j];
        					break;
        				}
        			}
        		}
    		}
    		undoflag=false;
    		index-=1; index%=ongoing.no_of_players;
    		if(index<0)
    			index=ongoing.no_of_players-1;
    		}
    		setgridlines();
    }
    /**
     * This method is used to return to the main menu page
     * @param event This is the event which is generated when user clicks main menu button 
     * @throws Exception
     */
    @FXML
    void backtomenu(ActionEvent event) throws Exception{
    	resume=true;
    	if(ongoing.no_of_players==1)
    		ongoing.is_finished=true;
    	else{
    	for(int i=0;i<9;i++){
    		for(int j=0;j<6;j++){
    			Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs=balls[i][j];
    			Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor=beta[i*6+j];
    		}
    		
    	}ongoing.current_turn=index;
    	}
    	System.gc();
       	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	Slider s=(Slider)page.getChildren().get(1);
    	s.setValue(Mainmenucontroller.playercount);
    	Button r=(Button)page.getChildren().get(0);
    	if(ongoing.is_finished){
    		r.setDisable(true);
    		resume=false;
    	}
    	else{
    		for(Player p:ongoing.players){
    			p.number_of_orbs_onboard=0;
    		}
    		for(int i=0;i<9;i++){
        		for(int j=0;j<6;j++){
        			for(Player p:ongoing.players){
        				if(beta[i*6+j]==null)
        					break;
        				else if(p.Color.equals(beta[i*6+j])){
        					p.number_of_orbs_onboard+=balls[i][j];
        					break;
        				}
        			}
        		}
    		}
    	}
    	savegrid();
	    Mainmenucontroller.serialize(Mainmenucontroller.g,"GAME.txt");
    	if(root==null){
    	}
    		//System.out.println("fdfsf");
    	else
    		root.getChildren().setAll(page);
    }
    /**
     * This method is used to calculate the critical mass of a cell
     * @param x Column number
     * @param y Row Number
     * @return The critical mass of that particular cell
     */
    int calculatecriticalmass(int x,int y){
    	if(x==0 && y==0 || x==5 && y==8 || x==5 && y==0 || x==0 && y==8)
    		return 1;
    	else if(x==0 || x==5 || y==0 || y==8)
    		return 2;
    	else
    		return 3;
    }
    /**
     * This method helps the user in adding an orb
     * @param e This is the event which is generated when user click on a cell
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws ColorMismatchException
     */
    @FXML
    private void useraddorb(MouseEvent e) throws IOException, InterruptedException, ExecutionException, ColorMismatchException{
    	Node target = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(target);
        Integer rowIndex = GridPane.getRowIndex(target);
        if (colIndex == null || rowIndex == null || !makemove) {
        } 
        else {
        	int k=calculatecriticalmass(colIndex.intValue(),rowIndex.intValue());
            current=ongoing.players.get(index);
        	if(beta[rowIndex.intValue()*6+colIndex.intValue()]!=null && !beta[rowIndex.intValue()*6+colIndex.intValue()].equals(current.Color)){
        		try{
        		throw new ColorMismatchException("This cell is occupied by another player");
        		}
        		catch(Exception ex){
        			System.out.println(ex.getMessage());
        		}
        	}
        	else {
        	undoflag=true; 
        	current.turns++;
            index+=1; index=index%ongoing.no_of_players;
            counter++;
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==k){
        		savegrid(); makemove=false;
        		if(ongoing.gamegrid.grid[rowIndex.intValue()][colIndex.intValue()].currentplayer==null){
                	ongoing.gamegrid.grid[rowIndex.intValue()][colIndex.intValue()].currentplayer=current;
                }
            	balls[rowIndex.intValue()][colIndex.intValue()]=0;
            	Service<Void> service = new Service<Void>() {
    		        @Override
    		        protected Task<Void> createTask() {
    		            return new Task<Void>() {           
    		                @Override
    		                protected Void call() throws Exception {
    		                    //Background work                       
    		                    final CountDownLatch latch = new CountDownLatch(1);
    		                    Platform.runLater(new Runnable() {                          
    		                        @Override
    		                        public void run() {
    		                            try{
    		                    			split(colIndex.intValue(),rowIndex.intValue());
    		              			}finally{
    		                                latch.countDown();
    		                            }
    		                        }
    		                    });
    		                    latch.await();    
    		                    int i=0;
    		                    while(counter!=csum() && i<12){
    		                    	i++;
    		                    	Thread.sleep(500);
    		                    }
    		                    checkcondition();
    		                    makemove=true;
    		                    setgridlines();
    		                    return null;
    		                }
    		            };
    		        }
    		    };
    		    service.start();
    		    
        	}
        	else{
            	savegrid();
            	if(balls[rowIndex.intValue()][colIndex.intValue()]==0){
            		addorb1(colIndex.intValue(),rowIndex.intValue(),null);
            	}
            	else if(balls[rowIndex.intValue()][colIndex.intValue()]==1){
            		addorb2(colIndex.intValue(),rowIndex.intValue(),null);
            	}
            	else if(balls[rowIndex.intValue()][colIndex.intValue()]==2){
            		addorb3(colIndex.intValue(),rowIndex.intValue(),null);
            	}
            	balls[rowIndex.intValue()][colIndex.intValue()]++;
            	setgridlines();
        	}
        	}
        }
    }
    /**
     * This method is used to set the color of the gridlines of the gamegrid.
     * The color of the lines is set to the color of the player whose turn it is.
     */
    public void setgridlines(){
    	int i=0;
    	Player next=ongoing.players.get(index);
    	Color c=new Color(next.Color.red,next.Color.green,next.Color.blue,1.0);
    	String colour=c.toString();
    	colour="#"+colour.substring(2);
    	String style=new String("-fx-border-color:"+colour+";"+"-fx-alignment: center;");
    	for (Node n: gamegrid.getChildren()) {
	    	if(i<54){
	            n.setStyle(style);
	    	}
	    	else
	    		break;
	    	i++;
	    }
    }
    /**
     * This method is used to add the first orb in the cell
     * @param x Column number
     * @param y Row number
     * @param c Color of the orb,can be null
     */
    public void addorb1(int x,int y,color c){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();
    	if(c==null){
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	beta[y*6+x]=current.Color;
    	}
    	else{
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	}
    	s.setMaterial(pm);
    	alpha[y*6+x][0]=s;
    	gamegrid.add(s,x,y);
    	s.setTranslateX(45);
        RotateTransition rotateTransition = new RotateTransition(); 
    	rotateTransition.setAxis(Rotate.X_AXIS);
        rotateTransition.setDuration(Duration.millis(1000)); 
        rotateTransition.setNode(s);
        rotateTransition.setRate(10);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.play();
    }
    /**
    * This method is used to add the second orb in the cell
    * @param x Column number
    * @param y Row number
    * @param c Color of the orb,can be null
    */
    public void addorb2(int x,int y,color c){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();
    	if(c==null)
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
    	else
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	s.setMaterial(pm);
    	alpha[y*6+x][1]=s;
    	gamegrid.getChildren().remove(alpha[y*6+x][0]);
    	addorb1(x,y,c);
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
    /**
    * This method is used to add the third orb in the cell
    * @param x Column number
    * @param y Row number
    * @param c Color of the orb,can be null
    */
    public void addorb3(int x,int y,color c){
    	Sphere s=new Sphere(10);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();
    	if(c==null)
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
    	else
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	s.setMaterial(pm);
    	alpha[y*6+x][2]=s;
    	gamegrid.getChildren().remove(alpha[y*6+x][1]);
    	addorb2(x,y,c);
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
    /**
    * This method is used to blast the cell and split the orbs in orthogonal direction
    * when number of orbs equal the critical mass
    * @param x Column number
    * @param y Row number
    */
    public void split(int x,int y){
    	int k=calculatecriticalmass(x,y);
    	PhongMaterial pm=new PhongMaterial();
		beta[y*6+x]=null;
    	switch(k){
    	case 1:
    		gamegrid.getChildren().remove(alpha[y*6+x][0]);
    		Sphere s=new Sphere(10);
        	s.setDrawMode(DrawMode.FILL);
        	pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	s.setMaterial(pm);
        	gamegrid.add(s,x,y);
        	s.setTranslateX(25);
        	Sphere n=new Sphere(8);
        	n.setDrawMode(DrawMode.FILL);
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
        		for(Player p:ongoing.players){
        			if(p.equals(current))
        				continue;
        			if(beta[6*(x+1)+y]!=null && beta[6*(x+1)+y].equals(p.Color))
        				p.number_of_orbs_onboard-=1;
        			if(beta[6*(x)+y-1]!=null && beta[6*(x)+y-1].equals(p.Color))
        				p.number_of_orbs_onboard-=1;
        		}
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
        	n1.setDrawMode(DrawMode.FILL);
        	pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	n1.setMaterial(pm);
        	gamegrid.add(n1,x,y);
        	n1.setTranslateX(25);
        	Sphere n2=new Sphere(10);
        	n2.setDrawMode(DrawMode.FILL);
        	n2.setMaterial(pm);
        	gamegrid.add(n2,x,y);
        	n2.setTranslateX(27);
        	Sphere n3=new Sphere(10);
        	n3.setDrawMode(DrawMode.FILL);
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
        	s1.setDrawMode(DrawMode.FILL);
        	pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	s1.setMaterial(pm);
        	gamegrid.add(s1,x,y);
        	s1.setTranslateX(25);
        	Sphere s2=new Sphere(10);
        	s2.setDrawMode(DrawMode.FILL);
        	s2.setMaterial(pm);
        	gamegrid.add(s2,x,y);
        	s2.setTranslateX(27);
        	Sphere s3=new Sphere(10);
        	s3.setDrawMode(DrawMode.FILL);
        	s3.setMaterial(pm);
        	gamegrid.add(s3,x,y);
        	s1.setTranslateX(25);
        	Sphere s4=new Sphere(10);
        	s4.setDrawMode(DrawMode.FILL);
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
    /**
     * This method helps in sending the orb orthogonally to the right when the
     * cell blasts
     * @param s JavaFx sphere which is to be sent in the right direction
     * @param x Destination colummn address
     * @param y Destination row address
     */
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
					addorb(x,y);
            }
        });
    }
    /**
     * This method helps in sending the orb orthogonally to the left when the
     * cell blasts
     * @param s JavaFx sphere which is to be sent in the left direction
     * @param x Destination colummn address
     * @param y Destination row address
     */
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
					addorb(x,y);
            }
        });
    }
    /**
     * This method helps in sending the orb orthogonally to down when the
     * cell blasts
     * @param s JavaFx sphere which is to be sent in the south direction
     * @param x Destination colummn address
     * @param y Destination row address
     */
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
					addorb(x,y);
            }
        });
    }
    /**
     * This method helps in sending the orb orthogonally to up when the
     * cell blasts
     * @param s JavaFx sphere which is to be sent in the north direction
     * @param x Destination colummn address
     * @param y Destination row address
     */
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
					addorb(x,y);
            }
        });
    }
	/**
	 * Simply adds an orb to a cell by calling the addorbn function.
	 * n is the nth orb it is adding.
	 * @param x Column address
	 * @param y Row address
	 */
	 void addorb(int x,int y){
		 if(ongoing.is_finished)
			 return;
	    	int k=calculatecriticalmass(x,y);
	    	if(balls[y][x]==k){
	    		balls[y][x]=0;
	    		split(x,y);
	    	}
	    	else{
	    		beta[y*6+x]=current.Color;
	    		//System.out.println(beta[y*6+x]);
	        	if(balls[y][x]==0)
	        		addorb1(x,y,null);
	        	else if(balls[y][x]==1)
	        		addorb2(x,y,null);
	        	else if(balls[y][x]==2)
	        		addorb3(x,y,null);
	    		balls[y][x]++;
	    	}
	    }
	 /**
	  * This method is used to store the state of the grid
	  * @throws IOException
	  */
    public static void savegrid() throws IOException {
    	ObjectOutputStream out=null;
		ObjectOutputStream out1=null;
		try{
			out=new ObjectOutputStream(new FileOutputStream("gridstate.txt"));
			out.writeObject(balls);
			out1=new ObjectOutputStream(new FileOutputStream("gridstate1.txt"));
			out1.writeObject(beta);
		}
		finally{
			out.close();
			out1.close();
		}
	}
    /**
     * This method is used to restore the gamegrid
     * @return 2D integer array which has previously saved state of the grid
     * @throws IOException
     * @throws ClassNotFoundException
     */
	public static int[][] restoregrid() throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream("gridstate.txt"));
			int[][] g=(int[][])in.readObject();
			return g;
		}
		finally{
			in.close();
		}
	}
	/**
	 * Method used to restore the configuration of the grid which simply means which player is
	 * occupying which cell
	 * @return Array of type color
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static color[] restoregrid1() throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream("gridstate1.txt"));
			color[] g=(color[])in.readObject();
			return g;
		}
		finally{
			in.close();
		}
	}
	/**
	 * Method which checks which all players have lost till now
	 * If all players have lost it stops the game and displays the winner.
	 * @throws IOException
	 */
	@FXML
	public void checkcondition() throws IOException{
		for(Player p:ongoing.players){
			p.number_of_orbs_onboard=0;
		}
		for(int i=0;i<9;i++){
    		for(int j=0;j<6;j++){
    			for(Player p:ongoing.players){
    				if(beta[i*6+j]==null)
    					break;
    				else if(p.Color.equals(beta[i*6+j])){
    					p.number_of_orbs_onboard+=balls[i][j];
    					break;
    				}
    			}
    		}
		}
		Player[] play=new Player[ongoing.no_of_players];
		int i=0;
		for(Player p:ongoing.players){
			if(p.turns>0 && p.number_of_orbs_onboard==0){
				p.lost=true;
				System.out.println("Lost"+" "+p);
				play[i++]=p;
				ongoing.no_of_players--;
			}
		}
		for(int j=0;j<i;j++){
			ongoing.players.remove(play[j]);
		}
		index=ongoing.players.indexOf(current);
		index++;
		index%=ongoing.no_of_players;
		if(ongoing.no_of_players==1){
			Platform.runLater(new Runnable(){                          
                @Override
                public void run() {
                	try {
                		ongoing.is_finished=true;
                		gamegrid.setVisible(false);
                		Color c=new Color(current.Color.red,current.Color.green,current.Color.blue,1.0);
                    	String colour=c.toString();
                    	colour="#"+colour.substring(2);
                    	String style=new String("-fx-background-color:"+colour+";"+"-fx-alignment: center;");
                		root.setStyle(style);
                		Alert alert = new Alert(AlertType.INFORMATION);
                		ButtonType playagain=new ButtonType("Play Again");
                		alert.getButtonTypes().remove(0);
                		Image z=new Image(Gridcontroller2.class.getResource("NIGHTOWL107.png").toExternalForm());
                		ImageView img=new ImageView(z);
                		alert.setGraphic(img);
                		alert.getGraphic().setTranslateX(-30);
                		alert.getButtonTypes().add(playagain);
                		alert.getButtonTypes().add(ButtonType.FINISH);
                		alert.setHeaderText("h");
                		alert.setContentText(ongoing.players.get(0).toString()+" wins!!");
                		alert.showAndWait();
                		if(alert.getResult().equals(ButtonType.FINISH)){
                			backtomenu(new ActionEvent());
                			System.out.println("ended");
                		}
                		if(alert.getResult().equals(playagain)){
                			restartgame(new ActionEvent());
                		}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
                }});
		}
	}
	/**
	 * This method calculates the total number of orbs present on the
	 * grid irrespective of their color.
	 * @return s This is total number of orbs on the grid presently
	 */
	static int csum(){
	int s=0;
	for(int i=0;i<9;i++){
    	for(int j=0;j<6;j++){
    			s+=balls[i][j];
    		}
		}
		return s;
	}
	@FXML
	void initialize() {
    }
	/**This method initializes the grid (9X6)
	 * In case we have a previous unfinished game this method
	 * creates that grid so that game can be resumed.
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		balls=new int[9][6];
		alpha=new Sphere[54][3];
		beta=new color[54];
		index=0;
		ongoing=Mainmenucontroller.g;
		counter=0;
		if(resume){
			System.out.println("hello");
			for(int i=0;i<9;i++){
	    		for(int j=0;j<6;j++){
	    			int n=Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs;
	    			color c=Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor;
	    			beta[i*6+j]=c;
	    			balls[i][j]=n;
	    			switch(n){
	    			case 1:
	    				addorb1(j,i,c);
	    				break;
	    			case 2:
	    				addorb1(j,i,c);
	    				addorb2(j,i,c);
	    				break;
	    			case 3:
	    				addorb1(j,i,c);
	    				addorb2(j,i,c);
	    				addorb3(j,i,c);
	    				break;
	    			}
	    		}
	    	}
			resume=false;
			index=ongoing.current_turn;
			setgridlines();
		}
		else{
			setgridlines();
		}
		}
}