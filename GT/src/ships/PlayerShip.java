package ships;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class PlayerShip extends Ship {

	public PlayerShip(Rectangle ship, Pane space, double screenWidth, double screenHeight) {
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
		this.space = space;
		ship.setFill(new ImagePattern(new Image("/assets/player.png",ratioWidth,ratioHeight,false,false)));

	}

	public void fire(boolean fire) {
		firing = fire;
	}

	public void draw() {
		ship.setWidth(xSize);
		ship.setHeight(ySize);
		ship.setTranslateX(xCord);
		ship.setTranslateY(yCord);
	}

	public void update() {
		xCord += (dx * moving);
	}
}
