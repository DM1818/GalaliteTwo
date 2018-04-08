import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import ships.PlayerShip;
import ships.Ship;

import java.util.ArrayList;

public class MainController {

    private PlayerShip player;
    private Scene scene;

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

    private ArrayList<Ship> friendlyObjects;
    private ArrayList<Ship> enemyObjects;


    private Parent parent;

    @FXML
    private void initialize() {

        Platform.runLater(() -> {
                    setUpKeyListener();
                    setUpBackground();
                    setUpPlayer();

        });
        System.out.println("Hello");

    }

    private void setUpBackground() {
        bg1 = new ImageView( getClass().getResource( "/assets/larger_bg2.png").toExternalForm());
        bg2 = new ImageView( getClass().getResource( "/assets/larger_bg2.png").toExternalForm());
        bg1.relocate( 0, -bg1.getImage().getHeight() + gamePane.getHeight());
        bg2.relocate(0, (-bg2.getImage().getHeight() * 2) + gamePane.getHeight());

        gamePane.getChildren().add(bg1);
        currentBG = bg1;
        nextBG = bg2;
    }

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
                        player.fire(false);
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

    private void setUpPlayer() {
        clock = new Movement();
        clock.start();
        Rectangle r = new Rectangle(5,5);
        gamePane.getChildren().add(r);
        player = new PlayerShip(r, gamePane, gamePane.getWidth(), gamePane.getHeight());
        player.draw();
    }


    private void pause() {
        // launch a popup new scene
        System.out.println("in pause");
    }

    private void detectCollision() {
        // check friendly collision

        // check enemy collision
    }

    private class Movement extends AnimationTimer {
        private long lastGameInterval = 0;
        private long lastBGInterval = 0;
        @Override
        public void handle(long now) {
            if (now - lastGameInterval >= interval) {
                lastGameInterval = now;
                player.update();
                player.draw();
                detectCollision();
            } if (now - lastBGInterval >= bgInterval) {
                lastBGInterval = now;


                checkBG(bg1, bg2);
                checkBG(bg2, bg1);
                System.out.println("1 " + bg1.getLayoutY());
                System.out.println("2 " + bg2.getLayoutY());
            }
        }
    }

    private void checkBG(ImageView curr, ImageView next) {
      if (Double.compare(curr.getLayoutY(),0.0) > 0) {
          if (gamePane.getChildren().contains(curr)) {
              gamePane.getChildren().remove(curr);
          }
          next.relocate( 0, -next.getImage().getHeight() + gamePane.getHeight());
          curr.relocate( 0, -curr.getImage().getHeight() + gamePane.getHeight());

          if (!gamePane.getChildren().contains(next)) {
              gamePane.getChildren().add(next);
          }
      }
      if (Double.compare(curr.getLayoutY(), 0.0) <= 0) {
          System.out.println("hello " + curr.getLayoutY());
          if (gamePane.getChildren().contains(curr)) {
              curr.setLayoutY(curr.getLayoutY() + bgSpeed);
          }
      }
    }
}
