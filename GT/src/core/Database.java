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
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores (name String, score int);");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_ships (saveName String, x int, y int, xSize int, ySize int, type String);");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_bullets (saveName String, x int, y int, dx int, dy int);");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS saveState_gameInfo (saveName String, lives int, score int, active int);");
		} catch (Exception e) {
			e.printStackTrace();
			news = new BadNews("Could not access/create databases");
		}
	}

	private void refresh() {
		
		try {
			if (stat != null) {
				stat.close();
			}
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:GalaliteTwo");
			stat = con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			news = new BadNews("Couldn't connect to the database");
			e.printStackTrace();
		}
	}

	// interacting with the highscore table
	public void insertHighscore(String name, int score) {
		refresh();
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
			ResultSet info = stat.executeQuery("SELECT name, score FROM highscores ORDER BY score DESC;");
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
			ResultSet middleMan = stat.executeQuery("SELECT MAX(score) FROM highscores;");
			score = middleMan.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			news = new BadNews("Couldn't get highscore");

		}
		return Integer.toString(score);
	}

	// interacting with saveState_gameInfo
	public void insertGameInfo(String name, int lives, int score) {
		refresh();
		try {
			stat.executeUpdate("DELETE FROM saveState_gameInfo WHERE saveName='" + name + "';");
			stat.executeUpdate("INSERT INTO saveState_gameInfo (saveName, lives, score, active) VALUES ('" + name + "', "
					+ lives + ", " + score + ", 0);");

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
	
	public void setActiveLoad(String saveName) {
		refresh();
		try { 
			stat.executeUpdate("UPDATE saveState_gameInfo SET active=1 WHERE saveName='" + saveName +"';");	
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Sorry: You can't set that load");
		}
	}
	
	public String getActiveLoad() {
		refresh();
		String gameName = "";
		try {
			ResultSet results = stat.executeQuery("SELECT saveName FROM saveState_gameInfo WHERE active=1;");
			if (!results.isClosed()) {
				gameName = results.getString(1);
			}
			System.out.println(gameName);
			stat.executeUpdate("UPDATE saveState_gameInfo SET active=0 WHERE saveName='" + gameName +"';");
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Sorry: You can't get that load");
		}
		return gameName;
	}
	
	public int getAspectofGameState(String name, String what) {
		refresh();
		int returning = 0;
		try {
			ResultSet results = stat.executeQuery("SELECT "+ what+ " FROM saveState_gameInfo WHERE saveName='" + name + "';");
			returning = results.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Sorry: You can't get your lives");
		}
		return returning;
	}
	

	//interacting with saveState_ships
	public void saveShips(ArrayList<Ship> enemyShips, PlayerShip myShip, String saveName) {
		refresh();
		try {
			stat.executeUpdate("DELETE FROM saveState_ships WHERE saveName='" + saveName + "';");
			for (Ship each : enemyShips) {
				stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('"
						+ saveName + "', " + each.getXCord() + ", " + each.getYCord() + ", " + each.getXSize() + ", "
						+ each.getYSize() + ", " + "'enemy'" + ");");
			}
			System.out.println("myShip: " + myShip.getScreenWidth());
			stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, xSize, ySize, type) VALUES " + "('"
					+ saveName + "', " + myShip.getXCord() + ", " + myShip.getYCord() + ", " + myShip.getScreenWidth() + ", "
					+ myShip.getScreenHeight() + ", " + "'me'" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't save");
		}
	}

	public ArrayList<Ship> getEnemyShips(String saveName) { // Player Ship in last
		refresh();
		ArrayList<Ship> ships = new ArrayList<Ship>();
		Ship shipBuilder;
		try {
			ResultSet results = stat.executeQuery(
					"SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'enemy';");
			while (results.next()) {
				Rectangle r = new Rectangle(5, 5);
				shipBuilder = new EnemyShip(r, results.getDouble(5), results.getDouble(6), results.getDouble(2),
						results.getDouble(3));
				ships.add(shipBuilder);
			}
		} catch (SQLException e) {
			news = new BadNews("Couldn't find enemy ships in save");
			e.printStackTrace();
		}
		return ships;
	}

	public PlayerShip getPlayerShip(String saveName) { //saveName, x (2), y (3), xSize (4), ySize (5), type
		refresh();
		PlayerShip shipBuilder = null;
		try {
			ResultSet result = stat
					.executeQuery("SELECT * FROM saveState_ships WHERE saveName='" + saveName + "' AND type = 'me';");
			Rectangle r = new Rectangle(5, 5);	
			//Rectangle ship, double screenWidth, double screenHeight, double xCord, double yCord
			shipBuilder = new PlayerShip(r, result.getDouble(5), result.getDouble(6), result.getDouble(2),
					result.getDouble(3));
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't find player ship in save");
		}
		return shipBuilder;
	}

	//interacting with saveState_bullets
	public void saveBullets(ArrayList<Ship> enemyBullets, ArrayList<Ship> playerBullets, String saveName) { // TODO
		refresh();
		try {
			stat.executeUpdate("DELETE FROM saveState_bullets WHERE saveName='" + saveName + "';");
			for (Ship each : enemyBullets) {
				stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy) VALUES " + "('" + saveName
						+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + ((Bullet) each).getDX() + ", " + ((Bullet) each).getDY()
						+ ");");
			}
			for (Ship each : playerBullets) {
				stat.executeUpdate("INSERT INTO saveState_bullets (saveName, x, y, dx, dy) VALUES " + "('" + saveName
						+ "', " + each.getXCord() + ", " + each.getYCord() + ", " + ((Bullet) each).getDX() + ", " + ((Bullet) each).getDY()
						+ ");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't save bullets");
		}
	}

}
