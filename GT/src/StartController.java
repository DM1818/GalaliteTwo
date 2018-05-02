import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import core.Database;
import core.BadNews;
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

import javax.swing.*;
import java.beans.EventHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class StartController {

	private State state = State.START;


	Parent pane;

	private Stage stage;

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



	public static Database db;
	private static BadNews error;
	
	static {
		try {
			db = new Database();
		} catch (ClassNotFoundException | SQLException e) {
			error = new BadNews("We could not load your database.");
			e.printStackTrace();
		}
	}


	public void initialize() {
		stage = Main.getStage();
		try {
			highscoreList.setItems(db.getAllHighscores());
		} catch (SQLException e) {
			error = new BadNews("We could not load your highscores.");
			e.printStackTrace();
		}
	}

	@FXML
	private void toMainScreen() {
		changeScene("MainScreen.fxml");
	}

	@FXML
	private void load() {
		try {
		ArrayList<String> list = db.getSaveNames();
		Object[] options = new Object[list.size()];
		System.out.println(list);
		for(int i=0; i<list.size(); i++) {

			options[i] = list.get(i);
		}

		SwingUtilities.invokeLater(() -> {String s = (String) JOptionPane.showInputDialog(
		                    null,
		                   "Choose your saved game",
		                    "Customized Dialog",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    options,
		                    null);
		if (s != null) {
			System.out.println("hi");
			loadGameState(s);
		}});
		} catch (SQLException e) {
			error = new BadNews("We could not load your saves.");
			e.printStackTrace();
		}
	}





	private void loadGameState(String s) {
		try {
			db.setActiveLoad(s);
		} catch (SQLException e) {
			error = new BadNews("We could not set that load.");
			e.printStackTrace();
		}

		toMainScreen();
	}



	private void changeScene(String fxml) {
		try {
			pane = FXMLLoader.load(
                    getClass().getResource(fxml));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.getScene().setRoot(pane);
	}
}
