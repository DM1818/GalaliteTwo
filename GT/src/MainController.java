import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
<<<<<<< HEAD
//<<<<<<< HEAD
import javafx.scene.image.Image;
//=======
import javafx.scene.control.Label;
//>>>>>>> origin/master
=======
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.control.Label;

>>>>>>> 48bdf6c398f50a3386832998685ec4f137fc6110
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ships.Bullet;
import ships.EnemyShip;
import ships.PlayerShip;
import ships.Ship;

import java.util.ArrayList;

import core.Database;

public class MainController {

    private PlayerShip player;
    private Scene scene;


    @FXML
    private Pane backgroundPane1;

    @FXML
    private ImageView background;

    @FXML
    private Pane backgroundPane2;
    private Movement clock;
    KeyCode currentKey;

    private long fps = 60L;
    private long interval = 5000000000L / fps;
    private ImageView bg1;
    private ImageView bg2;

    private ImageView currentBG;
    private ImageView nextBG;
    private double bgSpeed = 10;
    private long bgInterval = 100000L / fps;

    @FXML
    private Pane gamePane;
    @FXML
    private Label livesLabel, scoreLabel, highscoreLabel;

    private ArrayList<Ship> enemyObjects = new ArrayList<Ship>();
    private ArrayList<Ship> playerBullets = new ArrayList<Ship>();
    private ArrayList<Ship> enemyBullets = new ArrayList<Ship>();

    private int enemyRows    = 3;
    private int enemyColumns = 5;

    private Parent parent;
    
    private Database db = new Database();
    private int lives = 3;
    private int score = 0;

    @FXML
    private void initialize() {

        Platform.runLater(() -> {
                    setUpKeyListener();
                    //setUpBackground();
                    setUpPlayer();
                    setUpEnemy();
                    setUpGameState();
        });
        //System.out.println("Hello");
    }

<<<<<<< HEAD

=======
//<<<<<<< HEAD
//    private void setUpBackground() {
//        bg1 = new ImageView( getClass().getResource( "/assets/large_vertical_bg.png").toExternalForm());
//        bg2 = new ImageView( getClass().getResource( "/assets/large_vertical_bg.png").toExternalForm());
//        bg1.relocate( 0, -bg1.getImage().getHeight() + backgroundPane1.getHeight());
//        //bg2.relocate(0, -bg2.getImage().getHeight() + backgroundPane2.getHeight());
//=======
>>>>>>> 48bdf6c398f50a3386832998685ec4f137fc6110
    private void setUpGameState() {
    	String loading = db.getActiveLoad();
    	if (!loading.equals("")) {
    		loadGame(loading);
    	}
    	updateLives();
    	updateScore();
    	highscoreLabel.setText("Highscore: " + db.getHighscore());
	}
    
    private void loadGame(String loading) {
    	lives = db.getAspectofGameState(loading, "lives");
    	score = db.getAspectofGameState(loading, "score");
	}


	private void updateLives() {livesLabel.setText("Lives: " + lives);}
    private void updateScore() {scoreLabel.setText("Score: " + score);}

<<<<<<< HEAD

=======
	private void setUpBackground() {
        bg1 = new ImageView( getClass().getResource( "/assets/larger_bg2.png").toExternalForm());
        bg2 = new ImageView( getClass().getResource( "/assets/larger_bg2.png").toExternalForm());
        bg1.relocate( 0, -bg1.getImage().getHeight() + gamePane.getHeight());
        bg2.relocate(0, (-bg2.getImage().getHeight() * 2) + gamePane.getHeight());


        backgroundPane1.getChildren().add(bg1);
        //backgroundPane2.getChildren().add(bg2);
        currentBG = bg1;
        nextBG = bg2;
    }
>>>>>>> 48bdf6c398f50a3386832998685ec4f137fc6110

    private void setUpKeyListener() {
        this.scene = Main.getScene();
        scene.setOnKeyPressed(new javafx.event.EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {

                switch (event.getCode()) {
                    case SPACE:
                        player.fire(true);
                        break;
                    case A:
                        currentKey = event.getCode();

                        player.move(-1);
                        scene.getRoot().requestFocus();
                        break;
                    case D:
                        currentKey = event.getCode();

                        player.move(1);

                        scene.getRoot().requestFocus();
                        break;
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        //player.fire(false);
                    	spawnPlayerBullet();
                        break;
                    case A:
                        if (currentKey == KeyCode.A) {
                            player.stop();
                        }
                        break;
                    case D:
                        if (currentKey == KeyCode.D) {
                            player.stop();
                        }
                        break;
                }
            }
        });
    }
    
    private void spawnPlayerBullet() {
    	if (playerBullets.size() < 2) {
    		Rectangle r = new Rectangle(5, 5);
    		gamePane.getChildren().add(r);
    		Bullet bullet = new Bullet(r, 5, 12, player.getXCord() + (player.getXSize() / 2), player.getYCord(), 0, gamePane.getHeight() / -10);
    		playerBullets.add(bullet);
    		bullet.draw();
    	}
    }

    private void setUpPlayer() {
        clock = new Movement();
        clock.start();
        Rectangle r = new Rectangle(5,5);
        gamePane.getChildren().add(r);
        player = new PlayerShip(r, gamePane.getWidth(), gamePane.getHeight());
        player.draw();
    }

    private void setUpEnemy() {
    	for (int i = 0; i < enemyColumns; i++) {
    		for (int i2 = 0; i2 < enemyRows; i2++) {
    			double xLength = ((gamePane.getWidth() * 0.5) / (enemyColumns * 2)); 
    			double yLength = ((gamePane.getHeight() * 0.5) / (enemyRows * 2));
    			double xCord   = (xLength * 2) * i + (gamePane.getWidth() * 0.4);
    			double yCord   = (yLength * 2) * i2 + (gamePane.getHeight() * 0.1);
    			
    			Rectangle r = new Rectangle(5, 5);
    			gamePane.getChildren().add(r);
    			EnemyShip baddy = new EnemyShip(r, xLength, yLength, xCord, yCord);
    			enemyObjects.add(baddy);
    			baddy.draw();
    		}
    	}
    }
    
    private void pause() {

    }


    @FXML
    private void saveHighScore() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.getStage());
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Enter your name to save your high score"));
        TextField tf = new TextField();
        dialogVbox.getChildren().add(tf);

        Button b = new Button("Save Score");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf.getText();

                // score = score
                // TODO database interaction with score + name here
            }
        });

        dialogVbox.getChildren().add(new Button("Save Score"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private ArrayList<ArrayList<Ship>> detectCollision() {
        // check friendly collision

        ArrayList<Ship> enemiesToRemove = new ArrayList<>();
        ArrayList<Ship> bulletsToRemove = new ArrayList<>();
//        if (playerBullets.isEmpty() || enemyObjects.isEmpty()) {
//            ArrayList<ArrayList<Ship>> toReturn = new ArrayList<ArrayList<Ship>>();
//            toReturn.add(new ArrayList<>());
//            toReturn.add(new ArrayList<>());
//            return toReturn;
//        }
        for (Ship b : playerBullets) {
            for (Ship s : enemyObjects) {
                if (detectCollisionHelper(s, b)) {
                    enemiesToRemove.add(s);
                    bulletsToRemove.add(b);
                    score += 100;
                }
            }
            if (detectOutOfBoundBullets(b)) {
                bulletsToRemove.add(b);
            }

        }

        ArrayList<ArrayList<Ship>> toReturn = new ArrayList<>();
        toReturn.add(enemiesToRemove);
        toReturn.add(bulletsToRemove);
        return toReturn;

        // check enemy collision
    }
    
    private boolean detectCollisionHelper(Ship b, Ship s) {
        if (s.getRect().getBoundsInParent().intersects(b.getRect().getBoundsInParent())) {
            return true;
        } return false;

    	
//    	if ((s.getXCord() >= b.getXCord() && s.getXCord() <= b.getXCord() + b.getXSize()) || (s.getXCord() + s.getXSize() >= b.getXCord() && s.getXCord() + s.getXSize() <= b.getXCord() + b.getXSize())) {
//    		if ((s.getYCord() >= b.getYCord() && s.getYCord() <= b.getYCord() + b.getYSize()) || (s.getYCord() + s.getYSize() >= b.getYCord() && s.getYCord() + s.getYSize() <= b.getYCord() + b.getYSize())) {
//    			return true;
//    		}
//		}
//    	return false;
    }
    
    private boolean detectOutOfBoundBullets(Ship b) {
    	if (b.getXCord() < 0 || b.getYCord() < 0 || b.getXCord() > gamePane.getWidth() || b.getYCord() > gamePane.getHeight()) {
    		return true;
    	}
    	return false;
    }
    
    private void enemyFire(Ship e) {
    	if (Math.random() * enemyObjects.size() * 5 <= 1) {
    		spawnEnemyBullet(e);
    	}
    }
    
    private void spawnEnemyBullet(Ship e) {
    	Rectangle r = new Rectangle();
    	gamePane.getChildren().add(r);
    	
    	
    	
    	//Bullet bullet = new Bullet(r, 5, 12, e.getXCord() + (e.getXSize() / 2), e.getYCord() + e.getYSize(), )TODO
    }
    
    private class Movement extends AnimationTimer {
        private long lastGameInterval = 0;
        private long lastBGInterval = 0;
        @Override
        public void handle(long now) {
            if (now - lastGameInterval >= interval) {
                lastGameInterval = now;

                
                Platform.runLater(() -> {
                    ArrayList<ArrayList<Ship>> toRemove = detectCollision();
                    enemyObjects.removeAll(toRemove.get(0));
                    playerBullets.removeAll(toRemove.get(1));
                    redraw();
                    updateScore();
                    updateLives();
                });

                
            } /*if (now - lastBGInterval >= bgInterval) {
                lastBGInterval = now;


                //checkBG();
                //System.out.println("1 " + bg1.getLayoutY());
                //System.out.println("2 " + bg2.getLayoutY());
            }*/
        }
    }

    private void redraw() {
        gamePane.getChildren().clear();


        gamePane.getChildren().add(player.getRect());
        player.update();
        player.draw();

        for (Ship e : enemyObjects) {
            gamePane.getChildren().add(e.getRect());
            e.draw();
        }
        for (Ship s : playerBullets) {
            gamePane.getChildren().add(s.getRect());
            s.move();
            s.draw();
        }

    }

    private void checkBG() { //BG = background
        if (Double.compare(bg1.getLayoutY(),0.0) > 0) {
            bg2.relocate( 0, -bg2.getImage().getHeight() + backgroundPane2.getHeight());
        }
        else if (Double.compare(bg2.getLayoutY(),0.0) > 0) {
            bg1.relocate( 0, -bg1.getImage().getHeight() + backgroundPane1.getHeight());

        }
        System.out.println("1 " + bg1.getLayoutY() + " " + backgroundPane1.getHeight());
        System.out.println("2 " + bg2.getLayoutX() + " " + backgroundPane2.getHeight());

        if (Double.compare(bg1.getLayoutY(), backgroundPane1.getHeight()) <= 0) {
            bg1.setLayoutY(bg1.getLayoutY() + bgSpeed);
        } if (Double.compare(bg2.getLayoutY(), backgroundPane2.getHeight()) <= 0) {
            bg2.setLayoutY(bg2.getLayoutY() + bgSpeed);
        }

      }
}

