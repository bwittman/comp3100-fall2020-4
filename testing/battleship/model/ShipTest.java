package battleship.model;

import battleship.model.Ship.ShipType;

import org.junit.jupiter.api.*;
import java.awt.*;

/**
 * All tests associated with the Ship class
 */
class ShipTest {
    private Ship ship = new Ship(ShipType.DESTROYER);

    @Test
    void resetTest() {
        ship.updateHits();
        ship.setStart(new Point(1,1));
        ship.setEnd(new Point(1,2));
        ship.reset();

        Assertions.assertTrue(ship.equals(new Ship(ShipType.DESTROYER)));
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