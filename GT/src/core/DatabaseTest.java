package core;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
	Database db;

	@BeforeEach
	void init() throws SQLException, ClassNotFoundException {
		db = new Database();
		db.insertHighscore("one", 1);
		db.insertHighscore("one", 1);
		db.insertHighscore("two", 2);
		db.insertHighscore("three", 3);
	}

	@Test
	void testHighestScore() throws SQLException {
		assertEquals(db.getHighscore(), "3");
	}

	@Test
	void testAllHighscores() throws SQLException {
		ObservableList<String> scores = db.getAllHighscores();
		assertEquals("three: 3", scores.get(1));
		assertEquals("two: 2", scores.get(2));
		assertEquals("one: 1", scores.get(3));
		assertEquals("one: 1", scores.get(4));
	}
}