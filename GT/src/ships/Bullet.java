package ships;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Ship {
	public Bullet(Rectangle ship, Pane space, double xSize, double ySize, double xCord, double yCord, double dx, double dy) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.xCord = xCord;
		this.yCord = yCord;
		this.dx    = dx;
		this.dy    = dy;
		this.ship  = ship;
		this.space = space;
		this.ship.setFill(Color.BLACK);
	}
}
