package ships;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerShip extends Ship {

	public PlayerShip(Rectangle ship, Pane space) {
		xSize = 50;
		ySize = 50;
		xCord = 250;
		yCord = 400;
		dx = 1;
		dy = 0;
		this.ship  = ship;
		this.space = space;
		this.ship.setFill(Color.GREEN);
	}

}
