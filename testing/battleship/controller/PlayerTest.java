package battleship.controller;

import battleship.model.Ship;
import battleship.model.Ship.ShipType;
import battleship.view.ViewManager;
import javafx.scene.input.PickResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player = new ComputerPlayer(new ViewManager());

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
        player.getGameState().setTile(Player.Tile.SHIP, 3, 4);
        Assertions.assertTrue(player.checkPlaceLegal(new Point(4,4)));
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(3,4)));

        player.getGameState().setTile(Player.Tile.SHIP, 9,9);
        Assertions.assertTrue(player.checkPlaceLegal(new Point(9,8)));

        player.getGameState().setTile(Player.Tile.SHIP, 0, 4);
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

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(7,0));
        ship2.setEnd(new Point(7,2));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        //both horizontal, different rows
        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(0,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(4,9));
        ship2.setEnd(new Point(6,9));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        //both vertical, same column
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,2));
        ship2.setEnd(new Point(3,4));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,3));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));

        //both horizontal same row
        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(4,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(6,5));
        ship2.setEnd(new Point(4,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));

        //one horizontal, one vertical
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(2,9));
        ship2.setEnd(new Point(0,9));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));

        //intersect at start
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,7));
        ship2.setEnd(new Point(2,7));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));

        //intersect in middle
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,6));
        ship2.setEnd(new Point(2,6));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));

        //on top of each other
        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,7));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));

        //one ship inside another
        ship1 = new Ship(ShipType.DESTROYER);
        ship2 = new Ship(ShipType.CARRIER);

        ship1.setStart(new Point(6,6));
        ship1.setEnd(new Point(6, 5));
        ship2.setStart(new Point(6,7));
        ship2.setEnd(new Point(6, 3));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testAddShipToGameState(){
        try {
            Ship ship = new Ship(ShipType.DESTROYER);
            ship.setStart(new Point(0, 0));
            ship.setEnd(new Point(0, 1));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(0,0), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(0,1), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(0,2), "GameState not updated correctly");
            player.resetStoredShips();

            ship = new Ship(ShipType.CRUISER);
            ship.setStart(new Point(3, 2));
            ship.setEnd(new Point(5,2));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(3,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(4,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(5,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(2,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(6,2), "GameState not updated correctly");
            player.resetStoredShips();

            ship = new Ship(ShipType.BATTLESHIP);
            ship.setStart(new Point(9,7));
            ship.setEnd(new Point(9,4));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(9,7), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(9,4), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(9,6), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(9,5), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(9,8), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(9,3), "GameState not updated correctly");
            player.resetStoredShips();

            ship = new Ship(ShipType.CARRIER);
            ship.setStart(new Point(0,9));
            ship.setEnd(new Point(4,9));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(0,9), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(4,9), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(1,9), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(2,9), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(3,9), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(5,9), "GameState not updated correctly");
            player.resetStoredShips();

        } catch (ShipPlacementException e){
            Assertions.fail();
        }
    }

    @Test
    void testAddShipToGameStateFail(){
        try {
            Ship ship = new Ship(ShipType.SUBMARINE);
            ship.setStart(new Point(2, 3));
            ship.setEnd(new Point(2, 3));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.fail("Exception should have been thrown");
        } catch (ShipPlacementException e){
            //Do nothing
        }
    }
}