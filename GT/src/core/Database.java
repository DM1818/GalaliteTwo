package core;

import java.sql.*;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

public class Database {
	private Statement stat;

	public Database() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:Galalite");
			stat = con.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores (name String, score int)");
			stat.executeUpdate("DELETE FROM highscores");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS saveState_entities (saveName String, x int, y int, type String)");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS saveState_gameInfo (saveName String, lives int, score int)");		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertHighscore(String name, int score) {
		try {
			stat.executeUpdate("INSERT INTO highscores (name, score) VALUES ('" + name + "', " + score + ");");
		} catch (SQLException e) {
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
				temp.setLength(0); //clears Stringbuilder for new row
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		}
		return Integer.toString(score);
	}
	// loading: grab row -> construct object -> list of objects -> populate screen

}
