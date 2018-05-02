


import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ships.PlayerShip;
import ships.Ship;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest extends MainController {

    @Test
    public void testCollision(){
        this.gamePane = new Pane();
        gamePane.setMinWidth(100);
        gamePane.setMinHeight(200);
        Ship colliding1 = new PlayerShip(new Rectangle(0,0,10,10),100,200);
        Ship colliding2 = new PlayerShip(new Rectangle(0,5,10,10),100,200);
        Ship outOfBounds = new PlayerShip(new Rectangle(0,210,10,10),100,200);
        Ship notColliding = new PlayerShip(new Rectangle(50,50,10,10),100,200);
        Assertions.assertEquals(this.detectCollisionHelper(colliding1, colliding2), true);
        Assertions.assertEquals(this.detectCollisionHelper(colliding1, notColliding), false);
        Assertions.assertEquals(this.detectOutOfBoundBullets(outOfBounds), true);
    }
}
