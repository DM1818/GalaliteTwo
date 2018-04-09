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
import javafx.stage.Popup;
import javafx.stage.Stage;
import ships.PlayerShip;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


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

		highscoreList.setItems(db.getAllHighscores());
		db.insertGameInfo("Best Save Ever", 99, 420, 69);
		db.insertGameInfo("2nd Best Save Ever", 99, 420, 69);

	}

	@FXML
	private void toMainScreen() {
		changeScene("MainScreen.fxml");
	}

	@FXML
	private void load() {
		ArrayList<String> list = db.getSaveNames();
		Object[] options = new Object[list.size()];
		for(int i=0; i<list.size(); i++) {
			options[i] = list.get(i);
		}
		
		String s = (String)JOptionPane.showInputDialog(
		                    null,
		                   "Choose your saved game",
		                    "Customized Dialog",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    options,
		                    null);
		if (s != null) {
			loadGameState(s);
		}
	}




private void loadGameState(String s) {

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
