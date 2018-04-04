package ships;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyShip extends Ship {

	public EnemyShip(Rectangle ship, Pane space) {
		xSize = 50;
		ySize = 50;
		xCord = 250;
		yCord = 200;
		dx = 1;
		dy = 0;
		this.ship  = ship;
		this.space = space;
		this.ship.setFill(Color.RED);
	}

}
