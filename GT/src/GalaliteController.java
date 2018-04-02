
import core.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class GalaliteController {

	@FXML
	private Button start, load, save;
	
	@FXML
	private Label mainTitle, score, lives, currentHighscore; 
	
	@FXML
	private ListView<String> highscoreList;

	Database db = new Database();
	
	@FXML
	private void initialize() {
		db.insertHighscore("Fish", 1200);
		db.insertHighscore("Anna", 400000);
		db.insertHighscore("Jacob", 420);


		highscoreList.setItems(db.getAllHighscores());
		
	}
	
	public void clickStart() {
		displayGameScreen();
	}
	
	public void displayGameScreen() {
		mainTitle.setVisible(false);
		start.setVisible(false);
		load.setVisible(false);
		highscoreList.setVisible(false);
		score.setText("Score: 0");
		lives.setText("Lives: 3");
		currentHighscore.setText("Highscore: " + db.getHighscore());
	}



}
