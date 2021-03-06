package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import ships.Bullet;
import ships.EnemyShip;
import ships.PlayerShip;
import ships.Ship;

public class Database {
	private Statement stat;
	private BadNews news;
	private Connection con;

	public Database() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:GalaliteTwo");
		stat = con.createStatement();
		stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores (name String, score int);");
		stat.executeUpdate(
				"CREATE TABLE IF NOT EXISTS saveState_ships (saveName String, x int, y int, xSize int, ySize int, type String);");
		stat.executeUpdate(
				"CREATE TABLE IF NOT EXISTS saveState_bullets (saveName String, x int, y int, dx int, dy int, type String);");
		stat.executeUpdate(
				"CREATE TABLE IF NOT EXISTS saveState_gameInfo (saveName String, lives int, score int, active int);");

	}

	// interacting with the highscore table
	public void insertHighscore(String name, int score) throws SQLException {
		stat.executeUpdate("INSERT INTO highscores (name, score) VALUES ('" + name + "', " + score + ");");

	}

	public ObservableList<String> getAllHighscores() throws SQLException {

		ObservableList<String> scores = FXCollections.observableArrayList();
		scores.add("Highscores: ");
		StringBuilder temp = new StringBuilder();
		ResultSet info = stat.executeQuery("SELECT name, score FROM highscores ORDER BY score DESC;");
		while (info.next()) {
			temp.append(info.getString("name"));
			temp.append(": ");
			temp.append(info.getLong("score"));
			scores.add(temp.toString());
			temp.setLength(0); // clears Stringbuilder for new row
		}

		return scores;
	}

	public String getHighscore() throws SQLException {

		int score = 0;
		ResultSet middleMan = stat.executeQuery("SELECT MAX(score) FROM highscores;");
		score = middleMan.getInt(1);
		return Integer.toString(score);
	}

	// interacting with saveState_gameInfo
	public void insertGameInfo(String name, int lives, int score) throws SQLException {

		stat.executeUpdate("DELETE FROM saveState_gameInfo WHERE saveName='" + name + "';");
		stat.executeUpdate("INSERT INTO saveState_gameInfo (saveName, lives, score, active) VALUES ('" + name + "', "
				+ lives + ", " + score + ", 0);");

	}

	public ArrayList<String> getSaveNames() throws SQLException {

		ArrayList<String> names = new ArrayList<String>();

		ResultSet results = stat.executeQuery("SELECT saveName FROM saveState_gameInfo;");
		while (results.next()) {
			names.add(results.getString(1));
		}

		return names;
	}

	public void setActiveLoad(String saveName) throws SQLException {
		stat.executeUpdate("UPDATE saveState_gameInfo SET active=1 WHERE saveName='" + saveName + "';");
	}

	public void deactivateLoad() throws SQLException {
		stat.executeUpdate("UPDATE saveState_gameInfo SET active=0 WHERE active=1;");
	}

	public String getActiveLoad() throws SQLException {
		String gameName = "";
		ResultSet results = stat.executeQuery("SELECT saveName FROM saveState_gameInfo WHERE active=1;");
		if (!results.isClosed()) {
			gameName = results.getString(1);
		}
		if (!gameName.isEmpty()) {
			stat.executeUpdate("UPDATE saveState_gameInfo SET active=0 WHERE saveName='" + gameName + "';");
		}

		return gameName;
	}

	public int getAspectofGameState(String name, String what) throws SQLException {
		int returning = 0;
		ResultSet results = stat
				.executeQuery("SELECT " + what + " FROM saveState_gameInfo WHERE saveName='" + name + "';");
		returning = results.getInt(1);

		return returning;
	}

	// interacting with saveState_ships
	public void saveShips(ArrayList<Ship> enemyShips, PlayerShip myShip, String saveName) throws SQLException {
		stat.executeUpdate("DELETE FROM saveState_ships WHERE saveName='" + saveName + "';");
		for (Ship each : enemyShips) {
			stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('"
					+ saveName + "', " + each.getXCord() + ", " + each.getYCord() + ", " + each.getXSize() + ", "
					+ each.getYSize() + ", " + "'enemy'" + ");");
		}
		stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('" + saveName
				+ "', " + myShip.getXCord() + ", " + myShip.getYCord() + ", " + myShip.getScreenWidth() + ", "
				+ myShip.getScreenHeight() + ", " + "'me'" + ");");

	}

	public ArrayList<Ship> getEnemyShips(String saveName) throws SQLException { // Player Ship in last
		ArrayList<Ship> ships = new ArrayList<Ship>();
		Ship shipBuilder;
		ResultSet results = stat
				.executeQuery("SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'enemy';");
		while (results.next()) {
			Rectangle r = new Rectangle(5, 5);
			shipBuilder = new EnemyShip(r, results.getDouble(4), results.getDouble(5), results.getDouble(2),
					results.getDouble(3));
			ships.add(shipBuilder);
		}

		return ships;
	}

	public PlayerShip getPlayerShip(String saveName) throws SQLException { // saveName, x (2), y (3), xSize (4), ySize
																			// (5), type
		PlayerShip shipBuilder = null;
		ResultSet result = stat
				.executeQuery("SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'me';");
		Rectangle r = new Rectangle(5, 5);
		// Rectangle ship, double screenWidth, double screenHeight, double xCord, double
		// yCord
		shipBuilder = new PlayerShip(r, result.getDouble(4), result.getDouble(5), result.getDouble(2),
				result.getDouble(3));

		return shipBuilder;
	}

	// interacting with saveState_bullets
	public void saveBullets(ArrayList<Ship> enemyBullets, ArrayList<Ship> playerBullets, String saveName)
			throws SQLException { // TODO
		stat.executeUpdate("DELETE FROM saveState_bullets WHERE saveName='" + saveName + "';");
		for (Ship each : enemyBullets) {
			stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy, type) VALUES " + "('" + saveName
					+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + ((Bullet) each).getDX() + ", "
					+ ((Bullet) each).getDY() + ", 'enemy" + "');");
		}
		for (Ship each : playerBullets) {
			stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy, type) VALUES " + "('" + saveName
					+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + ((Bullet) each).getDX() + ", "
					+ ((Bullet) each).getDY() + ", 'me'" + ");");
		}

	}

	public ArrayList<Bullet> getEnemyBullets(String saveName) throws SQLException { // syntax: Rectangle ship, double
																					// xSize, double ySize, double
																					// xCord, double yCord, double dx,
																					// double dy
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		Bullet b;

		ResultSet results = stat
				.executeQuery("SELECT * FROM saveState_bullets WHERE saveName='" + saveName + "' AND type='enemy';");
		while (results.next()) {
			Rectangle r = new Rectangle();
			b = new Bullet(r, 5, 12, results.getDouble(2), results.getDouble(3), results.getDouble(4),
					results.getDouble(5));
			bullets.add(b);
		}
		return bullets;
	}

	public ArrayList<Bullet> getMyBullets(String saveName) throws SQLException { // syntax: Rectangle ship, double
																					// xSize, double ySize, double
																					// xCord, double yCord, double dx,
																					// double dy
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		Bullet b;
		ResultSet results = stat
				.executeQuery("SELECT * FROM saveState_bullets WHERE saveName='" + saveName + "' AND type='me';");
		while (results.next()) {
			Rectangle r = new Rectangle();
			b = new Bullet(r, 5, 12, results.getDouble(2), results.getDouble(3), results.getDouble(4),
					results.getDouble(5));
			bullets.add(b);
		}
		return bullets;
	}

}
