package core;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BadNews {

	BadNews(String what) { // Dr. Ferrer 352 sockDemo
		Alert badNum = new Alert(AlertType.ERROR);
		badNum.setContentText(what);
		badNum.show();
	}
}
