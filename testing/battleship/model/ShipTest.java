package battleship.model;

import battleship.model.Ship;
import org.junit.jupiter.api.*;
import java.awt.*;

class ShipTest {
    private Ship ship;

    @BeforeEach
    void setup(){
        ship = new Ship(Ship.ShipType.DESTROYER);
    }

    @Test
    void resetTest() {
        ship.updateHits();
        ship.setStart(new Point(1,1));
        ship.setEnd(new Point(1,2));
        ship.reset();

        Assertions.assertTrue(ship.equals(new Ship(Ship.ShipType.DESTROYER)));
    }

    @Test
    void testShipSunk() {
        ship.updateHits();
        ship.updateHits();
        Assertions.assertTrue(ship.checkForSunk());
    }

    @Test
    void testShipNotSunk() {
        ship.updateHits();
        Assertions.assertTrue(!ship.checkForSunk());
    }
}