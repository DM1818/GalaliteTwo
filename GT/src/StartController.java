import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import core.Database;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ships.PlayerShip;

import java.beans.EventHandler;
import java.io.IOException;


public class StartController {

	private State state = State.START;


	Parent pane;

	private Stage stage;

	private Scene scene;


	@FXML
	private Pane gamePane;

	@FXML
	private Button start, load, save;
	
	@FXML
	private Label mainTitle, score, lives, currentHighscore;
	
	@FXML
	private ListView<String> highscoreList;

	@FXML
	private Button resume;



	@FXML
	private AnchorPane startScreen;



	@FXML
	private AnchorPane canvas;



	Database db = new Database();




	private long fps = 60L;
	private long interval = 1000000000L / fps;



	public void initialize() {
		stage = Main.getStage();

		db.insertHighscore("Fish", 1200);
		db.insertHighscore("Anna", 400000);
		db.insertHighscore("Jacob", 420);

		highscoreList.setItems(db.getAllHighscores());

	}
	
//	public void clickStart() {
//		displayGameScreen();
//	}
//
//	public void displayGameScreen() {
//		mainTitle.setVisible(false);
//		toMainScreen.setVisible(false);
//		load.setVisible(false);
//		highscoreList.setVisible(false);
//		score.setText("Score: 0");
//		lives.setText("Lives: 3");
//		currentHighscore.setText("Highscore: " + db.getHighscore());
//
//		clock.toMainScreen();          //
//		player.setVisisble();   // This is used in my version. Jacob
//		player.draw();          //
//	}

	@FXML
	private void toMainScreen() {
		changeScene("MainScreen.fxml");
	}

	@FXML
	private void load() {

	}




//	@FXML
//	private void resume() {
//		changeScene("MainScreen.fxml");
//
//
//	}



//	@FXML
//	private void pause() {
//		System.out.println("in pause");
//		System.out.println(state);
//		System.out.println("yes");
//		state = State.PAUSE;
//		// TODO pause should be a popup
//		changeScene("PauseScreen.fxml");
//	}


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
