package ships;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Ship {
	protected double xSize;
	protected double ySize;
	protected double xCord;
	protected double yCord;
	protected double dx;
	protected double dy;
	protected Rectangle ship;
	protected Pane space;

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
		xCord += (dx * dir);
		yCord += (dy * dir);
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


