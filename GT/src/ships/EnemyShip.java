package ships;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
		this.ship.setFill(new ImagePattern(new Image("/assets/Enemy.png",xSize,ySize,false,false)));
	}

	@Override
	public void move(int dir) {
		xCord += dx * dir;
	}
}
