package core;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;


class DatabaseTest {

	Database db;
	
	@BeforeEach
	void init() {
		db = new Database();
		db.insertHighscore("one", 1);
		db.insertHighscore("one", 1);
		db.insertHighscore("two", 2);
		db.insertHighscore("three", 3);
	}
	
	@Test 
	void testHighestScore() {
		assertEquals(db.getHighscore(), "3");
	}
	
	@Test
	void testAllHighscores() {
		ObservableList<String> scores = db.getAllHighscores();
		assertEquals("three: 3", scores.get(1));
		assertEquals("two: 2", scores.get(2));
		assertEquals("one: 1", scores.get(3));
		assertEquals("one: 1", scores.get(4));
	}

}
