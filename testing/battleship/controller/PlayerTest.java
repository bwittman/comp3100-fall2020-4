package battleship.controller;

import battleship.controller.Player.Tile;
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

    //checkPlaceLegalOnMiddle()
    @Test
    void testCheckPlaceLegalOnMiddle(){
        Assertions.assertTrue(player.checkPlaceLegal(new Point(5,5)));
        Assertions.assertTrue(player.checkPlaceLegal(new Point(2,3)));
        Assertions.assertTrue(player.checkPlaceLegal(new Point(7,8)));
    }

    //test for right next to a ship and on a ship
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
}