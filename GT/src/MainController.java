import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ships.Bullet;
import ships.EnemyShip;
import ships.PlayerShip;
import ships.Ship;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import core.BadNews;
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
	private long interval = 100000000L / fps;
	private ImageView bg1;
	private ImageView bg2;

	private ImageView currentBG;
	private ImageView nextBG;
	private double bgSpeed = 10;

	@FXML
	public Pane gamePane;
	@FXML
	private Label livesLabel, scoreLabel, highscoreLabel;

	private ArrayList<Ship> enemyObjects = new ArrayList<Ship>();
	private ArrayList<Ship> playerBullets = new ArrayList<Ship>();
	private ArrayList<Ship> enemyBullets = new ArrayList<Ship>();

	private int enemyRows = 3;
	private int enemyColumns = 5;

	private int count;
	private int dir;

	private int lives = 3;
	private int score = 0;

	private static BadNews error;

	@FXML
	private void initialize() {
		count = 50;
		dir = 1;
		Platform.runLater(() -> {
			setUpGameState();
			setUpPlayer();
			setUpEnemy();
			setUpKeyListener();
			gamePane.requestFocus();

		});

		// System.out.println("Hello");
	}

	private void setUpGameState() {
		try {
			String loading = StartController.db.getActiveLoad();
			if (!loading.equals("")) {
				loadGame(loading);
				Platform.runLater(() -> {
					pause();
				});
			}
			StartController.db.deactivateLoad();
			updateLives();
			updateScore();
			highscoreLabel.setText("Highscore: " + StartController.db.getHighscore());
		} catch (SQLException e) {
			error = new BadNews("Error in loading retreiving load information.");
			e.printStackTrace();
		}

	}

	private void loadGame(String loading) {
		try {
			lives = StartController.db.getAspectofGameState(loading, "lives");
			score = StartController.db.getAspectofGameState(loading, "score");
			enemyObjects = StartController.db.getEnemyShips(loading);
			player = StartController.db.getPlayerShip(loading);
			loadBullets(loading);
		} catch (SQLException e) {
			error = new BadNews("Error in loading game state.");
			e.printStackTrace();
		}
	}

	private void loadBullets(String loadName) {
		try {
			ArrayList<Bullet> loadingBullets = StartController.db.getEnemyBullets(loadName);
			for (Bullet each : loadingBullets) {
				enemyBullets.add(each);
				each.draw();
			}
			loadingBullets = StartController.db.getMyBullets(loadName);
			for (Bullet each : loadingBullets) {
				playerBullets.add(each);
				each.draw();
			}
		} catch (SQLException e) {
			error = new BadNews("Error in loading bullets.");
			e.printStackTrace();
		}
	}

	private void updateLives() {
		livesLabel.setText("Lives: " + lives);
	}

	private void updateScore() {
		scoreLabel.setText("Score: " + score);
	}

	private void setUpBackground() {
		bg1 = new ImageView(getClass().getResource("/assets/larger_bg2.png").toExternalForm());
		bg2 = new ImageView(getClass().getResource("/assets/larger_bg2.png").toExternalForm());
		bg1.relocate(0, -bg1.getImage().getHeight() + gamePane.getHeight());
		bg2.relocate(0, (-bg2.getImage().getHeight() * 2) + gamePane.getHeight());

		backgroundPane1.getChildren().add(bg1);
		// backgroundPane2.getChildren().add(bg2);
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
					spawnPlayerBullet();

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
					// player.fire(false);
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
			score -= 5;
			Rectangle r = new Rectangle(5, 5);
			gamePane.getChildren().add(r);
			Bullet bullet = new Bullet(r, 5, 12, player.getXCord() + (player.getXSize() / 2), player.getYCord(), 0,
					-20);
			playerBullets.add(bullet);
			bullet.draw();
		}
	}

	private void setUpPlayer() {
		clock = new Movement();
		clock.start();
		Rectangle r = new Rectangle(5, 5);
		gamePane.getChildren().add(r);
		if (player == null) {
			player = new PlayerShip(r, gamePane.getWidth(), gamePane.getHeight());
		}
		player.draw();
	}

	private void setUpEnemy() {
		if (enemyObjects.isEmpty()) {
			newEnemies();
		} else {
			oldEnemies();
		}

	}

	private void oldEnemies() {
		for (Ship each : enemyObjects) {
			each.draw();
		}
	}

	private void newEnemies() {
		for (int i = 0; i < enemyColumns; i++) {
			for (int i2 = 0; i2 < enemyRows; i2++) {
				double xLength = ((gamePane.getWidth() * 0.5) / (enemyColumns * 2));
				double yLength = ((gamePane.getHeight() * 0.5) / (enemyRows * 2));
				double xCord = (xLength * 2) * i + (gamePane.getWidth() * 0.25);
				double yCord = (yLength * 2) * i2 + (gamePane.getHeight() * 0.1);

				Rectangle r = new Rectangle(5, 5);
				gamePane.getChildren().add(r);
				EnemyShip baddy = new EnemyShip(r, xLength, yLength, xCord, yCord);
				enemyObjects.add(baddy);
				baddy.draw();
			}
		}
	}

	@FXML
	private void pause() {
		clock.stop();
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.getStage());
		VBox dialogVbox = new VBox(20);

		Button resume = new Button("Resume");
		resume.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
				clock.start();
			}
		});
		dialogVbox.getChildren().add(resume);

		dialogVbox.getChildren().add(new Text("Enter a name for your saved game:"));
		TextField tf = new TextField();
		dialogVbox.getChildren().add(tf);
		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String saveName = tf.getText();
				try {
					StartController.db.saveShips(enemyObjects, player, saveName);
					StartController.db.saveBullets(enemyBullets, playerBullets, saveName);
					StartController.db.insertGameInfo(saveName, lives, score);
				} catch (SQLException e) {
					error = new BadNews("Error in saving game state.");
					e.printStackTrace();
				}
			}
		});
		dialogVbox.getChildren().add(save);
		
		Button backToMain = new Button("Back to Main Screen");
		backToMain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeScene("StartScreen.fxml");
				dialog.close();
			}
		});
		dialogVbox.getChildren().add(backToMain);

		Scene dialogScene = new Scene(dialogVbox, 500, 300);
		dialog.setScene(dialogScene);
		dialog.show();
		dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				dialog.close();
				clock.start();
			}
		});
	}

	private void saveHighScore() { // TODO

		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.getStage());
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text("Enter your name to save your high score"));
		TextField tf = new TextField();
		dialogVbox.getChildren().add(tf);

		Button b = new Button("Save Score");
		b.setOnAction((event) -> {
			String name = tf.getText();
			try {
				StartController.db.insertHighscore(name, score);
			} catch (SQLException e) {
				error = new BadNews("Error in saving highscore.");
				e.printStackTrace();
			}
			dialog.close();
		});
	
		dialogVbox.getChildren().add(b);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();

	}

	private ArrayList<ArrayList<Ship>> detectCollision() {
		// check friendly collision

		ArrayList<Ship> enemiesToRemove = new ArrayList<>();
		ArrayList<Ship> playerBulletsToRemove = new ArrayList<>();
		ArrayList<Ship> enemyBulletsToRemove = new ArrayList<>();
		// if (playerBullets.isEmpty() || enemyObjects.isEmpty()) {
		// ArrayList<ArrayList<Ship>> toReturn = new ArrayList<ArrayList<Ship>>();
		// toReturn.add(new ArrayList<>());
		// toReturn.add(new ArrayList<>());
		// return toReturn;
		// }
		for (Ship b : playerBullets) {
			for (Ship s : enemyObjects) {
				if (detectCollisionHelper(s, b)) {
					enemiesToRemove.add(s);
					playerBulletsToRemove.add(b);
					score += 100;
				}
			}
			if (detectOutOfBoundBullets(b)) {
				playerBulletsToRemove.add(b);
			}

		}
		for (Ship b : enemyBullets) {
			if (detectCollisionHelper(player, b)) {
				takeDamage();
				enemyBulletsToRemove.addAll(enemyBullets);
			}
			if (detectOutOfBoundBullets(b)) {
				enemyBulletsToRemove.add(b);
			}
		}

		ArrayList<ArrayList<Ship>> toReturn = new ArrayList<>();
		toReturn.add(enemiesToRemove);
		toReturn.add(playerBulletsToRemove);
		toReturn.add(enemyBulletsToRemove);
		return toReturn;

		// check enemy collision
	}

	private void takeDamage() {
		if (lives > 0) {
			lives -= 1;

			player.setXCord(gamePane.getWidth() / 2);
		} else {
			clock.stop();
			saveHighScore();
			changeScene("StartScreen.fxml");
		}

	}

	public boolean detectCollisionHelper(Ship b, Ship s) {
		if (s.getRect().getBoundsInParent().intersects(b.getRect().getBoundsInParent())) {
			return true;
		}
		return false;

		// if ((s.getXCord() >= b.getXCord() && s.getXCord() <= b.getXCord() +
		// b.getXSize()) || (s.getXCord() + s.getXSize() >= b.getXCord() && s.getXCord()
		// + s.getXSize() <= b.getXCord() + b.getXSize())) {
		// if ((s.getYCord() >= b.getYCord() && s.getYCord() <= b.getYCord() +
		// b.getYSize()) || (s.getYCord() + s.getYSize() >= b.getYCord() && s.getYCord()
		// + s.getYSize() <= b.getYCord() + b.getYSize())) {
		// return true;
		// }
		// }
		// return false;
	}

	public boolean detectOutOfBoundBullets(Ship b) {
		if (b.getXCord() < 0 || b.getYCord() < 0 || b.getXCord() > gamePane.getWidth()
				|| b.getYCord() > gamePane.getHeight()) {
			return true;
		}
		return false;
	}

	private void enemyFire(Ship e) {
		if (Math.random() * (enemyObjects.size() + enemyBullets.size()) * 8 <= 1) {
			spawnEnemyBullet(e);
			// System.out.println(enemyBullets.size() + " " + enemyObjects.size());
		}
	}

	private void spawnEnemyBullet(Ship e) {
		Rectangle r = new Rectangle();
		gamePane.getChildren().add(r);

		double xDis = player.getXCord() - e.getXCord();
		double yDis = player.getYCord() - e.getYCord();
		double dy = 10;
		double dx = (dy / yDis) * xDis;

		Bullet bullet = new Bullet(r, 5, 12, e.getXCord() + (e.getXSize() / 2), e.getYCord() + e.getYSize(), dx, dy);
		enemyBullets.add(bullet);
		bullet.draw();
	}

	private class Movement extends AnimationTimer {
		private long lastGameInterval = 0;
		private long lastBGInterval = 0;
		private long lastBulletInterval = 0;

		@Override
		public void handle(long now) {
			if (now - lastGameInterval >= interval / 4) {
				lastGameInterval = now;

				Platform.runLater(() -> {
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
						s.draw();
					}
					for (Ship e : enemyBullets) {
						if (!gamePane.getChildren().contains(e.getRect())) {
							gamePane.getChildren().add(e.getRect());
						}
						e.draw();
					}

				});

			}
			if (now - lastBulletInterval >= interval * 10) {
				lastBulletInterval = now;
				gamePane.getChildren().clear();
				gamePane.getChildren().add(player.getRect());
				player.draw();
				handleInteractions();
			}
			/*
			 * if (now - lastBGInterval >= bgInterval) { lastBGInterval = now;
			 * 
			 * 
			 * //checkBG(); //System.out.println("1 " + bg1.getLayoutY());
			 * //System.out.println("2 " + bg2.getLayoutY()); }
			 */
		}
	}

	private void handleInteractions() {
		ArrayList<ArrayList<Ship>> toRemove = detectCollision();
		enemyObjects.removeAll(toRemove.get(0));
		playerBullets.removeAll(toRemove.get(1));
		enemyBullets.removeAll(toRemove.get(2));
		redraw();
		updateScore();
		updateLives();
	}

	private void redraw() {

		if (count < 100) {
			count++;
		} else {
			count = 0;
			dir *= -1;
		}

		for (Ship e : enemyObjects) {
			gamePane.getChildren().add(e.getRect());
			enemyFire(e);
			e.move(dir);
			e.draw();
		}
		for (Ship s : playerBullets) {
			gamePane.getChildren().add(s.getRect());
			s.move();
			s.draw();
		}
		for (Ship e : enemyBullets) {
			if (!gamePane.getChildren().contains(e.getRect())) {
				gamePane.getChildren().add(e.getRect());
			}
			e.move();
			e.draw();
		}

	}

	private void checkBG() { // BG = background
		if (Double.compare(bg1.getLayoutY(), 0.0) > 0) {
			bg2.relocate(0, -bg2.getImage().getHeight() + backgroundPane2.getHeight());
		} else if (Double.compare(bg2.getLayoutY(), 0.0) > 0) {
			bg1.relocate(0, -bg1.getImage().getHeight() + backgroundPane1.getHeight());

		}
		System.out.println("1 " + bg1.getLayoutY() + " " + backgroundPane1.getHeight());
		System.out.println("2 " + bg2.getLayoutX() + " " + backgroundPane2.getHeight());

		if (Double.compare(bg1.getLayoutY(), backgroundPane1.getHeight()) <= 0) {
			bg1.setLayoutY(bg1.getLayoutY() + bgSpeed);
		}
		if (Double.compare(bg2.getLayoutY(), backgroundPane2.getHeight()) <= 0) {
			bg2.setLayoutY(bg2.getLayoutY() + bgSpeed);
		}

	}

	private void changeScene(String fxml) {
		try {
			Parent pane = FXMLLoader.load(getClass().getResource(fxml));
			Main.getStage().getScene().setRoot(pane);

		} catch (IOException e) {
			error = new BadNews("Couldn't go back to Main Screen");
			e.printStackTrace();
		}
	}
}
