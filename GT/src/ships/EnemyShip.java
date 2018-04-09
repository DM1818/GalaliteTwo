package ships;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyShip extends Ship {

	public EnemyShip(Rectangle ship, double xSize, double ySize, double xCord, double yCord) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.xCord = xCord;
		this.yCord = yCord;
		dx = 1;
		dy = 0;
		this.ship  = ship;
		this.ship.setFill(Color.RED);
	}

}
