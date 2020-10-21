package battleship.controller;

import battleship.model.GameState;
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
    //test for right next to a ship
    //test on a ship

}