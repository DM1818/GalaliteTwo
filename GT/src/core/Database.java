package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import ships.PlayerShip;
import ships.Ship;

public class Database {
	private Statement stat;
	private BadNews news;

	public Database() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:Galalite");
			stat = con.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores (name String, score int)");
			stat.executeUpdate("DELETE FROM highscores");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_ships (saveName String, x int, y int, type String)");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_bullets (saveName String, x int, y int, dx int, dy int)");
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS saveState_gameInfo (saveName String, lives int, score int, level int)");
		} catch (Exception e) {
			e.printStackTrace();
			news = new BadNews("Could not access/create databases");
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
	// loading: grab row -> construct object -> list of objects -> populate screen

	public void saveShips(ArrayList<Ship> enemyShips, PlayerShip myShip, String saveName) {
		try {
			for (Ship each : enemyShips) {
				stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, type) VALUES " + "('" + saveName
						+ "', " + each.getXCord() + "', " + each.getYCord() + "', " + "enemy" + ");");
			}
			stat.executeUpdate("INSERT INTO saveState_ships (saveName, x, y, type) VALUES " + "('" + saveName + "', "
					+ myShip.getXCord() + "', " + myShip.getYCord() + "', " + "me" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
			news = new BadNews("Couldn't save");
		}
	}
	
	public ArrayList<Ship> getShips(String saveName) { //Player Ship in first
		ArrayList<Ship> ships = new ArrayList<Ship>();
		try {
			ResultSet results = stat.executeQuery("SELECT * FROM saveState_ships WHERE saveName='" + saveName +"';");
			System.out.println(results.getInt(0));
		} catch (SQLException e) {
			news = new BadNews("Couldn't find save");
		}
		return ships;
	}

}
