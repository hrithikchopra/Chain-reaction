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
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import application.Player;

public class Gridcontroller2 implements Initializable{
	static volatile int[][] balls;
	static int counter=0;
	public static boolean resume=false;
	public static boolean undoflag=false;
	static Game ongoing=Mainmenucontroller.g;
	static Player current;
	static int index;
	@FXML
    private AnchorPane root;
	@FXML
	GridPane gamegrid;
	public Sphere[][] alpha;
	static volatile public color[] beta;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    public void restartgame(ActionEvent event) throws Exception{
    	color[] previous=new color[Mainmenucontroller.playercount];
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		color c=new color(SettingsController.values[i].getRed(),SettingsController.values[i].getGreen(),SettingsController.values[i].getBlue());
    		previous[i]=c;
    	}
    	Mainmenucontroller.g=new Game(Mainmenucontroller.playercount,Mainmenucontroller.gridchoice);
    	for(int i=0;i<Mainmenucontroller.playercount;i++){
    		Mainmenucontroller.g.players.add(new Player(new color(previous[i].red,previous[i].green,previous[i].blue),i));
    	}
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Grid2.fxml"));
    	index=0;
    	ongoing=Mainmenucontroller.g; 
    	root.setBackground(null);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page);
    }
    @FXML
    void backtomenu(ActionEvent event) throws Exception{
    	resume=true;
    	savegrid();
    	if(ongoing.no_of_players==1){
    		ongoing.is_finished=true;
    	}
    	for(int i=0;i<15;i++){
    		for(int j=0;j<10;j++){
    			Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs=balls[i][j];
    			Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor=beta[i*10+j];
    		}
    	}
    	ongoing.current_turn=index;
	    Mainmenucontroller.serialize(Mainmenucontroller.g,"GAME.txt");
    	AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("Mainmenu.fxml"));
    	Slider s=(Slider)page.getChildren().get(1);
    	s.setValue(Mainmenucontroller.playercount);
    	Button r=(Button)page.getChildren().get(0);
    	if(ongoing.is_finished)
    		r.setDisable(true);
    	else
    		checkcondition();
    	for(Player a:ongoing.players)
    		System.out.println(a.number_of_orbs_onboard);
    	if(root==null){
    		//System.out.println("fdfsf");
    	}
    	else
    		root.getChildren().setAll(page); 
    }
    @FXML
    public void undo(ActionEvent event) throws Exception{
    	if(undoflag){
    		gamegrid.getChildren().remove(151,gamegrid.getChildren().size());
    		counter--;
    		current.turns--;
    		balls=restoregrid();
    		beta=restoregrid1();
    		for(int i=0;i<15;i++){
    			for(int j=0;j<10;j++){
    				int n=balls[i][j];
    				switch(n){
    				case 1:
    					addorb1(j,i,beta[i*10+j]);
    					break;
    				case 2:
    					addorb1(j,i,beta[i*10+j]);
    					addorb2(j,i,beta[i*10+j]);
    					break;
    				case 3:
    					addorb1(j,i,beta[i*10+j]);
    					addorb2(j,i,beta[i*10+j]);
    					addorb3(j,i,beta[i*10+j]);
    					break;
    				}
    			}
    		}
    		for(Player p:ongoing.players){
    			p.number_of_orbs_onboard=0;
    		}
    		for(int i=0;i<15;i++){
        		for(int j=0;j<10;j++){
        			for(Player p:ongoing.players){
        				if(beta[i*10+j]==null)
        					break;
        				else if(p.Color.equals(beta[i*10+j])){
        					p.number_of_orbs_onboard++;
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
    int calculatecriticalmass(int x,int y){
    	if(x==0 && y==0 || x==9 && y==14 || x==9 && y==0 || x==0 && y==14)
    		return 1;
    	else if(x==0 || x==9 || y==0 || y==14)
    		return 2;
    	else
    		return 3;
    }
    public void setgridlines(){
    	int i=0;
    	Player next=ongoing.players.get(index);
    	Color c=new Color(next.Color.red,next.Color.green,next.Color.blue,1.0);
    	String colour=c.toString();
    	colour="#"+colour.substring(2);
    	String style=new String("-fx-border-color:"+colour+";"+"-fx-alignment: center;");
    	for (Node n: gamegrid.getChildren()) {
	    	if(i<151){
	            n.setStyle(style);
	    	}
	    	else
	    		break;
	    	i++;
	    }
    }
    @FXML
    private void useraddorb(MouseEvent e) throws IOException, InterruptedException, ExecutionException{
    	Node target = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(target);
        Integer rowIndex = GridPane.getRowIndex(target);
        if (colIndex == null || rowIndex == null) {
        } 
        else {
        	int k=calculatecriticalmass(colIndex.intValue(),rowIndex.intValue());
            current=ongoing.players.get(index);
        	if(beta[rowIndex.intValue()*10+colIndex.intValue()]!=null && !beta[rowIndex.intValue()*10+colIndex.intValue()].equals(current.Color)){
        	}
        	else {
        	undoflag=true;
        	current.turns++;
            index+=1; index=index%ongoing.no_of_players;
            counter++;
        	if(balls[rowIndex.intValue()][colIndex.intValue()]==k){
        		savegrid();
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
    		                    while(counter!=csum()){
    		                    	//System.out.println(counter+" "+csum());
    		                    	//Thread.sleep(1000);
    		                    }
    		                    checkcondition();
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
    
     void useraddorb(int x,int y){
    	int k=calculatecriticalmass(x,y);
    	if(balls[y][x]==k){
    		balls[y][x]=0;
    		split(x,y);
    	}
    	else{
        	if(balls[y][x]==0)
        		addorb1(x,y,null);
        	else if(balls[y][x]==1)
        		addorb2(x,y,null);
        	else if(balls[y][x]==2)
        		addorb3(x,y,null);
        	balls[y][x]++; 
    	}
    }
    public void addorb1(int x,int y,color c){
    	Sphere s=new Sphere(8);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();

    	if(c==null){
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	beta[y*10+x]=current.Color;
    	}
    	else{
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	}
    	s.setMaterial(pm);
    	alpha[y*10+x][0]=s;
    	gamegrid.add(s,x,y);
    	s.setTranslateX(40);
        RotateTransition rotateTransition = new RotateTransition(); 
    	rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setDuration(Duration.millis(1000)); 
        rotateTransition.setNode(s);
        rotateTransition.setRate(10);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.play(); 
    }
    public void addorb2(int x,int y,color c){
    	Sphere s=new Sphere(8);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();
    	if(c==null)
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
    	else
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	s.setMaterial(pm);
    	alpha[y*10+x][1]=s;
    	gamegrid.getChildren().remove(alpha[y*10+x][0]);
    	addorb1(x,y,c);
    	gamegrid.add(s,x,y);
    	s.setTranslateX(25);
        Circle path=new Circle(39,6.5,10.4);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(path);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();   
    }
    public void addorb3(int x,int y,color c){
    	Sphere s=new Sphere(8);
    	s.setDrawMode(DrawMode.FILL);
    	PhongMaterial pm=new PhongMaterial();
    	if(c==null)
    		pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
    	else
    		pm.setDiffuseColor(new Color(c.red,c.green,c.blue,1.0));
    	s.setMaterial(pm);
    	alpha[y*10+x][2]=s;
    	gamegrid.getChildren().remove(alpha[y*10+x][1]);
    	addorb2(x,y,c);
    	gamegrid.add(s,x,y);
    	s.setTranslateX(30);
    	s.setTranslateY(-5);
        Circle path=new Circle(38,-5,9.5);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setPath(path);
        pathTransition.setNode(s);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();
    }
    public void split(int x,int y){
    	int k=calculatecriticalmass(x,y);
    	PhongMaterial pm=new PhongMaterial();
		beta[y*10+x]=null;
    	switch(k){
    	case 1:
    		gamegrid.getChildren().remove(alpha[y*10+x][0]);
    		Sphere s=new Sphere(8);
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
        	else if(x==9 && y==0){
        		goleft(s,x-1,y); godown(n,x,y+1);
        	}
        	else if(x==0 && y==14){
        		goright(s,x+1,y); goUp(n,x,y-1);
        	}
        	else{
        		goleft(s,x-1,y); goUp(n,x,y-1);
        	}
    		break;
    	case 2:
    		gamegrid.getChildren().remove(alpha[y*10+x][0]);
    		gamegrid.getChildren().remove(alpha[y*10+x][1]);
    		Sphere n1=new Sphere(8);
        	n1.setDrawMode(DrawMode.FILL);
        	pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	n1.setMaterial(pm);
        	gamegrid.add(n1,x,y);
        	n1.setTranslateX(25);
        	Sphere n2=new Sphere(8);
        	n2.setDrawMode(DrawMode.FILL);
        	n2.setMaterial(pm);
        	gamegrid.add(n2,x,y);
        	n2.setTranslateX(27);
        	Sphere n3=new Sphere(8);
        	n3.setDrawMode(DrawMode.FILL);
        	n3.setMaterial(pm);
        	gamegrid.add(n3,x,y);
        	n3.setTranslateX(24);
        	if(x==0){
        		godown(n1,x,y+1);goright(n2,x+1,y); goUp(n3,x,y-1);
        	}
        	else if(x==9){
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
    		gamegrid.getChildren().remove(alpha[y*10+x][0]);
    		gamegrid.getChildren().remove(alpha[y*10+x][1]);
    		gamegrid.getChildren().remove(alpha[y*10+x][2]);
    		Sphere s1=new Sphere(8);
        	s1.setDrawMode(DrawMode.FILL);
        	pm.setDiffuseColor(new Color(current.Color.red,current.Color.green,current.Color.blue,1.0));
        	s1.setMaterial(pm);
        	gamegrid.add(s1,x,y);
        	s1.setTranslateX(25);
        	Sphere s2=new Sphere(8);
        	s2.setDrawMode(DrawMode.FILL);
        	s2.setMaterial(pm);
        	gamegrid.add(s2,x,y);
        	s2.setTranslateX(27);
        	Sphere s3=new Sphere(8);
        	s3.setDrawMode(DrawMode.FILL);
        	s3.setMaterial(pm);
        	gamegrid.add(s3,x,y);
        	s1.setTranslateX(25);
        	Sphere s4=new Sphere(8);
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
    void goright(Sphere s,int x,int y){
    	Line line=new Line();
        line.setStartX(45.0f);
        line.setStartY(0.0f);
        line.setEndX(105.0f);
        line.setEndY(0.0f);
        PathTransition pathTransition = new PathTransition(); 
        pathTransition.setDuration(Duration.millis(300));
        pathTransition.setInterpolator(Interpolator.LINEAR);
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
    public static void savegrid() throws IOException {
		ObjectOutputStream out=null;
		ObjectOutputStream out1=null;
		try{
			out=new ObjectOutputStream(new FileOutputStream("grid2state.txt"));
			out.writeObject(balls);
			out1=new ObjectOutputStream(new FileOutputStream("grid2state1.txt"));
			out1.writeObject(beta);
		}
		finally{
			out.close();
			out1.close();
		}
	}
	public static int[][] restoregrid() throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream("grid2state.txt"));
			int[][] g=(int[][])in.readObject();
			return g;
		}
		finally{
			in.close();
		}
	}
	public static color[] restoregrid1() throws IOException, ClassNotFoundException {
		ObjectInputStream in=null;
		try{
			in=new ObjectInputStream(new FileInputStream("grid2state1.txt"));
			color[] g=(color[])in.readObject();
			return g;
		}
		finally{
			in.close();
		}
	}
	@FXML
	public void checkcondition() throws IOException{
		for(Player p:ongoing.players){
			p.number_of_orbs_onboard=0;
		}
		for(int i=0;i<15;i++){
    		for(int j=0;j<10;j++){
    			for(Player p:ongoing.players){
    				if(beta[i*10+j]==null)
    					break;
    				else if(p.Color.equals(beta[i*10+j])){
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
						/*AnchorPane page = (AnchorPane) FXMLLoader.load(Mainmenu.class.getResource("ended.fxml"));
						root.setBackground(null);
						Label l=(Label)page.getChildren().get(0);
						l.setText(ongoing.players.get(0).toString()+" wins");
						root.getChildren().setAll(page); */
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
                }});
		}
	}
	static int csum(){
		int s=0;
		for(int i=0;i<15;i++){
    		for(int j=0;j<10;j++){
    			s+=balls[i][j];
    		}
		}
		return s;
	}
    @FXML
    void initialize() {
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		balls=new int[15][10];
		alpha=new Sphere[150][3];
		beta=new color[150];
		index=0;
		ongoing=Mainmenucontroller.g;
		if(resume){
			for(int i=0;i<15;i++){
	    		for(int j=0;j<10;j++){
	    			int n=Mainmenucontroller.g.gamegrid.grid[i][j].n_orbs;
	    			color c=Mainmenucontroller.g.gamegrid.grid[i][j].currentcolor;
	    			beta[i*10+j]=c;
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
			counter=0;
			setgridlines();
		}
		}
}
