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

	public Database() {
		try {
			refresh();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores (name String, score int)");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_ships (saveName String, x int, y int, xSize int, ySize int, type String)");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_bullets (saveName String, x int, y int, dx int, dy int)");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS saveState_gameInfo (saveName String, lives int, score int)");
		} catch (Exception e) {
			e.printStackTrace();
			news = new BadNews("Could not access/create databases");
		}
	}

	private void refresh() {

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:Galalite");
			stat = con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			news = new BadNews("Couldn't connect to the database");
			e.printStackTrace();
		}

	}

	// interacting with the highscore table
	public void insertHighscore(String name, int score) {
		try {
			stat.executeUpdate("INSERT INTO highscores (name, score) VALUES ('" + name + "', " + score + ");");

		} catch (SQLException e) {
			news = new BadNews("Couldn't insert highscore");
			e.printStackTrace();
		}
	}

	public ObservableList<String> getAllHighscores() {
		refresh();
		ObservableList<String> scores = FXCollections.observableArrayList();
		scores.add("Highscores: ");
		try {
			StringBuilder temp = new StringBuilder();
			ResultSet info = stat.executeQuery("SELECT name, score FROM highscores ORDER BY score DESC");
			while (info.next()) {
				temp.append(info.getString("name"));
				temp.append(": ");
				temp.append(info.getLong("score"));
				scores.add(temp.toString());
				temp.setLength(0); // clears Stringbuilder for new row
			}
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't get highscores");
		}

		return scores;
	}

	public String getHighscore() {
		refresh();
		int score = 0;
		try {
			ResultSet middleMan = stat.executeQuery("SELECT MAX(score) FROM highscores");
			score = middleMan.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			news = new BadNews("Couldn't get highscore");

		}
		return Integer.toString(score);
	}

	// interacting with saveState_gameInfo
	public void insertGameInfo(String name, int lives, int score, int level) {
		try {
			stat.executeUpdate("DELETE FROM saveState_gameInfo WHERE saveName='" + name + "';");
			stat.executeUpdate("INSERT INTO saveState_gameInfo (saveName, lives, score) VALUES ('" + name + "', "
					+ lives + ", " + score + ");");

		} catch (SQLException e) {
			news = new BadNews("Couldn't insert game info");
			e.printStackTrace();
		}
	}

	public ArrayList<String> getSaveNames() {
		refresh();
		ArrayList<String> names = new ArrayList<String>();
		try {
			ResultSet results = stat.executeQuery("SELECT saveName FROM saveState_gameInfo;");
			while (results.next()) {
				names.add(results.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't find any saves");
		}
		return names;
	}
	

	//interacting with saveState_ships
	public void saveShips(ArrayList<Ship> enemyShips, PlayerShip myShip, String saveName) {
		try {
			stat.executeUpdate("DELETE FROM saveState_ships WHERE saveName='" + saveName + "';");
			for (Ship each : enemyShips) {
				stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('"
						+ saveName + "', " + each.getXCord() + ", " + each.getYCord() + ", " + each.getXSize() + ", "
						+ each.getYSize() + ", " + "'enemy'" + ");");
			}
			stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('"
					+ saveName + "', " + myShip.getXCord() + ", " + myShip.getYCord() + ", " + myShip.getXSize() + ", "
					+ myShip.getYSize() + ", " + "'me'" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't save");
		}
	}

	public ArrayList<Ship> getEnemyShips(String saveName) { // Player Ship in last

		ArrayList<Ship> ships = new ArrayList<Ship>();
		Ship shipBuilder;
		try {
			ResultSet results = stat.executeQuery(
					"SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'enemy';");
			while (results.next()) {
				Rectangle r = new Rectangle();
				shipBuilder = new EnemyShip(r, results.getDouble(4), results.getDouble(5), results.getDouble(2),
						results.getDouble(3));
			}
		} catch (SQLException e) {
			news = new BadNews("Couldn't find enemy ships in save");
			e.printStackTrace();
		}
		return ships;
	}

	public Ship getPlayerShip(String saveName) {
		refresh();
		Ship shipBuilder = null;
		try {
			ResultSet result = stat
					.executeQuery("SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'me';");
			Rectangle r = new Rectangle();
			shipBuilder = new EnemyShip(r, result.getDouble(4), result.getDouble(5), result.getDouble(2),
					result.getDouble(3));
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't find player ship in save");
		}
		return shipBuilder;
	}

	//interacting with saveState_bullets
	public void saveBullets(ArrayList<Bullet> enemyBullets, ArrayList<Bullet> playerBullets, String saveName) { // TODO
		try {
			stat.executeUpdate("DELETE FROM saveState_bullets WHERE saveName='" + saveName + "';");
			for (Bullet each : enemyBullets) {
				stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy) VALUES " + "('" + saveName
						+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + each.getDX() + ", " + each.getDY()
						+ ");");
			}
			for (Bullet each : playerBullets) {
				stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy) VALUES " + "('" + saveName
						+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + each.getDX() + ", " + each.getDY()
						+ ");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't save bullets");
		}
	}

}
