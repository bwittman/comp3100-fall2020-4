package battleship.controller;

import battleship.model.Ship;
import battleship.model.Ship.ShipType;
import javafx.scene.input.PickResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player = new ComputerPlayer();

    @BeforeEach
    void setup(){
        player.resetGame();
    }

    @Test
    void testCheckPlaceLegalOnEdges(){
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(10,1)));//x is too large
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(1, 10)));//y is too large
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(-1,1)));//x is too small
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(1,-1)));//y is too small

        Assertions.assertTrue(player.checkPlaceLegal(new Point(0,4)));//smallest possible x
        Assertions.assertTrue(player.checkPlaceLegal(new Point(6,0)));//smallest possible y
        Assertions.assertTrue(player.checkPlaceLegal(new Point(9,5)));//largest possible x
        Assertions.assertTrue(player.checkPlaceLegal(new Point(2,9)));//largest possible y
    }

    @Test
    void testCheckPlaceLegalOnMiddle(){
        Assertions.assertTrue(player.checkPlaceLegal(new Point(5,5)));
        Assertions.assertTrue(player.checkPlaceLegal(new Point(2,3)));
        Assertions.assertTrue(player.checkPlaceLegal(new Point(7,8)));
    }

    @Test
    void testCheckPlaceLegalSideBySide(){
        player.getMyGameState().setTile(Player.Tile.SHIP, 3, 4);
        Assertions.assertTrue(player.checkPlaceLegal(new Point(4,4)));
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(3,4)));

        player.getMyGameState().setTile(Player.Tile.SHIP, 9,9);
        Assertions.assertTrue(player.checkPlaceLegal(new Point(9,8)));

        player.getMyGameState().setTile(Player.Tile.SHIP, 0, 4);
        Assertions.assertTrue(player.checkPlaceLegal(new Point(1,4)));
    }

    @Test
    void testShipsIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        //both vertical, different columns
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,7));
        ship2.setEnd(new Point(4,5));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(7,0));
        ship2.setEnd(new Point(7,2));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        //both horizontal, different rows
        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(0,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(4,9));
        ship2.setEnd(new Point(6,9));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        //both vertical, same column
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,2));
        ship2.setEnd(new Point(3,4));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,3));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(player.intersect(ship1,ship2));

        //both horizontal same row
        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(4,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(6,5));
        ship2.setEnd(new Point(4,5));

        Assertions.assertTrue(player.intersect(ship1,ship2));

        //one horizontal, one vertical
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(2,9));
        ship2.setEnd(new Point(0,9));

        Assertions.assertTrue(!player.intersect(ship1,ship2));

        //intersect at start
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,7));
        ship2.setEnd(new Point(2,7));

        Assertions.assertTrue(player.intersect(ship1,ship2));

        //intersect in middle
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,6));
        ship2.setEnd(new Point(2,6));

        Assertions.assertTrue(player.intersect(ship1,ship2));

        //on top of each other
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,7));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(player.intersect(ship1,ship2));

        //one ship inside another
        ship1 = new Ship(ShipType.DESTROYER);
        ship2 = new Ship(ShipType.CARRIER);

        ship1.setStart(new Point(6,6));
        ship1.setEnd(new Point(6, 5));
        ship2.setStart(new Point(6,7));
        ship2.setEnd(new Point(6, 3));

        Assertions.assertTrue(player.intersect(ship1,ship2));
    }
}