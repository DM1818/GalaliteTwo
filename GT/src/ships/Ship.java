package ships;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Ship {
	double xSize;
	double ySize;
	double xCord;
	double yCord;
	double dx;
	double dy;
	Rectangle ship;
	public boolean firing;
	double screenHeight;
	double screenWidth;
	int moving;

	public double getXSize() {
		return xSize;
	}

	public double getYSize() {
		return ySize;
	}

	public double getXCord() {
		return xCord;
	}

	public double getYCord() {
		return yCord;
	}

	public void setXCord(double newXCord) {
		xCord = newXCord;
	}

	public void setYCord(double newYCord) {
		yCord = newYCord;
	}

	public void move(int dir) {
		moving = dir;
	}
	
	public void move() {
		xCord += dx;
		yCord += dy;
	}

	public void stop() {
		moving = 0;
	}

	public Rectangle getRect() {
		return ship;
	}
	
	public void setInvisible() {
		ship.setVisible(false);
	}

	public void setVisisble() {
		ship.setVisible(true);
	}

	public void draw() {
		ship.setWidth(xSize);
		ship.setHeight(ySize);
		ship.setTranslateX(xCord);
		ship.setTranslateY(yCord);
	}

}


