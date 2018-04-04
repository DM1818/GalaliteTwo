import javafx.fxml.FXML;
import core.Database;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.io.IOException;


public class GalaliteController {

	private State state = State.START;


	Parent pane;

	private Stage stage;

	private Scene scene;

	@FXML
	private Button start, load, save;
	
	@FXML
	private Label mainTitle, score, lives, currentHighscore; 
	
	@FXML
	private ListView<String> highscoreList;

	@FXML
	private Button resume;

	@FXML
	private SplitPane mainScreen;

	@FXML
	private AnchorPane startScreen;

	@FXML
	private AnchorPane pauseScreen;

	@FXML
	private AnchorPane canvas;

	@FXML
	private EventHandler keyListener;

	Database db = new Database();

	private Movement clock;
	private PlayerShip player;

	private long fps = 60L;
	private long interval = 1000000000L / fps;

	private class Movement extends AnimationTimer {
		private long lastInterval = 0;

		@Override
		public void handle(long now) {
			if (now - lastInterval >= interval) {
				lastInterval = now;
			}
		}
	}

	public void initialize() {
		stage = Main.getStage();

		db.insertHighscore("Fish", 1200);
		db.insertHighscore("Anna", 400000);
		db.insertHighscore("Jacob", 420);
		if (highscoreList != null) {
			highscoreList.setItems(db.getAllHighscores());
		}

		clock = new Movement();
		Rectangle r = new Rectangle();
		pane.getChildren().add(r);

		r.setOnKeyPressed((KeyEvent key) -> { //https://stackoverflow.com/questions/41900685/waiting-a-keyevent?noredirect=1&lq=1
			System.out.println("movePlayer");
			if (key.getCode() == KeyCode.A) {
				player.move(-1);
			} else if (key.getCode() == KeyCode.D) {
				player.move(1);
			}
			player.draw();
			pane.requestFocus();
		});

		player = new PlayerShip(r, pane);
		player.setInvisible();

	}
	
//	public void clickStart() {
//		displayGameScreen();
//	}
//
//	public void displayGameScreen() {
//		mainTitle.setVisible(false);
//		start.setVisible(false);
//		load.setVisible(false);
//		highscoreList.setVisible(false);
//		score.setText("Score: 0");
//		lives.setText("Lives: 3");
//		currentHighscore.setText("Highscore: " + db.getHighscore());
//
//		clock.start();          //
//		player.setVisisble();   // This is used in my version. Jacob
//		player.draw();          //
//	}

	@FXML
	private void start() {
		state = State.MAIN;
		changeScene("MainScreen.fxml");
		pauseListener();

	}

	private void pauseListener() {


			this.scene = Main.getScene();
			System.out.println(state);

			scene.setOnKeyPressed(new javafx.event.EventHandler<javafx.scene.input.KeyEvent>() {
				@Override
				public void handle(javafx.scene.input.KeyEvent event) {
					System.out.println("hey");
					switch (event.getCode()) {
						case SPACE:
							System.out.println("its space");
							System.out.println(state);
							pause();

					}
				}
			});

	}


	@FXML
	private void resume() {
		state = State.MAIN;
		changeScene("MainScreen.fxml");

		pauseListener();
	}



	@FXML
	private void pause() {
		System.out.println("in pause");
		System.out.println(state);
		//if (state.equals(State.MAIN)) {
			System.out.println("yes");
			state = State.PAUSE;
			changeScene("PauseScreen.fxml");
		//}
	}


	private void changeScene(String fxml) {
		System.out.println(state);
		try {
			pane = FXMLLoader.load(
                    getClass().getResource(fxml));
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.getScene().setRoot(pane);


	}


}
