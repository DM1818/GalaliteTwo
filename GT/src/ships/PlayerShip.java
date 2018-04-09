package ships;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class PlayerShip extends Ship {

	public PlayerShip(Rectangle ship, double screenWidth, double screenHeight) {
		this.screenWidth = screenWidth;
		double ratioWidth = screenWidth / 15.0;
		double ratioHeight = screenHeight / 12.0;
		this.screenHeight = screenHeight;
		System.out.println(screenWidth);
		ySize = ratioHeight;
		xSize = ratioWidth;
		xCord = screenWidth / 2.0;
		yCord = screenHeight - (screenHeight / 10.0);
		dx = xSize;
		firing = false;
		dy = 0;
		this.ship  = ship;
		ship.setFill(new ImagePattern(new Image("/assets/player.png",ratioWidth,ratioHeight,false,false)));

	}

	public void fire(boolean fire) {
		firing = fire;
	}

	public void update() {
		double temp = xCord + (dx * moving);
		if (!(temp < 0 || temp + xSize > screenWidth)) {
			xCord += (dx * moving);
		}
	}
}
