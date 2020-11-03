package battleship.controller;

import battleship.model.Ship;
import battleship.model.Ship.ShipType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    private Player player = new ComputerPlayer(null);
    private static final int ROWS = Player.ROWS;
    private static final int COLUMNS = Player.COLUMNS;

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
    void testCheckPlaceLegalNextToShip(){
        List<Ship> ships = new ArrayList<>();
        Ship destroyer = new Ship(ShipType.DESTROYER);
        destroyer.setStart(new Point(1,1));
        destroyer.setEnd(new Point(1,2));
        ships.add(destroyer);

        Ship battleship = new Ship(ShipType.BATTLESHIP);
        ships.add(battleship);

        Ship carrier = new Ship(ShipType.CARRIER);
        carrier.setStart(new Point(5,5));
        carrier.setEnd(new Point(5,9));
        ships.add(carrier);
        Player.PlayerTesting.setShips(player, ships);

        try{
            player.addShipToGameState(destroyer);
            player.addShipToGameState(carrier);
        }catch(ShipPlacementException e){
            Assertions.fail();
        }

        Assertions.assertTrue(player.checkPlaceLegal(new Point(5,4)));
        Assertions.assertTrue(player.checkPlaceLegal(new Point(0,1)));
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(5,8)));
        Assertions.assertTrue(!player.checkPlaceLegal(new Point(1,1)));
    }

    @Test
    void testShipsHorizontalAdjacentIntersect() {
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3, 7));
        ship1.setEnd(new Point(3, 5));
        ship2.setStart(new Point(4, 7));
        ship2.setEnd(new Point(4, 5));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1, ship2));
    }

    @Test
    void testShipsHorizontalDistantIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(7,0));
        ship2.setEnd(new Point(7,2));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsVerticalAdjacentIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(0,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsVerticalDistantIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(2,4));
        ship1.setEnd(new Point(0,4));
        ship2.setStart(new Point(4,9));
        ship2.setEnd(new Point(6,9));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsHorizontalSameRowIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,2));
        ship2.setEnd(new Point(3,4));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsVerticalSameColumnIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(4,5));
        ship2.setEnd(new Point(2,5));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsHorizontalOverlapPartiallyIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,3));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsVerticalOverlapPartiallyIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(5,5));
        ship1.setEnd(new Point(7,5));
        ship2.setStart(new Point(6,5));
        ship2.setEnd(new Point(4,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsVerticalAndHorizontalNotIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(2,9));
        ship2.setEnd(new Point(0,9));

        Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsStartAndMiddleIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,7));
        ship2.setEnd(new Point(2,7));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsMiddlesIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(4,6));
        ship2.setEnd(new Point(2,6));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsSameLocationIntersect(){
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.CRUISER);

        ship1.setStart(new Point(3,7));
        ship1.setEnd(new Point(3,5));
        ship2.setStart(new Point(3,7));
        ship2.setEnd(new Point(3,5));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testShipsInsideAnotherIntersect(){
        Ship ship1 = new Ship(ShipType.DESTROYER);
        Ship ship2 = new Ship(ShipType.CARRIER);

        ship1.setStart(new Point(6,6));
        ship1.setEnd(new Point(6, 5));
        ship2.setStart(new Point(6,7));
        ship2.setEnd(new Point(6, 3));

        Assertions.assertTrue(Player.PlayerTesting.intersect(player, ship1,ship2));
    }

    @Test
    void testMultipleShipsDoNotIntersect(){
        List<Ship> ships = new ArrayList<>();

        Ship destroyer = new Ship(ShipType.DESTROYER);
        destroyer.setStart(new Point(1,5));
        destroyer.setEnd(new Point(2,5));
        ships.add(destroyer);

        Ship submarine = new Ship(ShipType.SUBMARINE);
        submarine.setStart(new Point(7,8));
        submarine.setEnd(new Point(9,8));
        ships.add(submarine);

        Ship cruiser = new Ship(ShipType.CRUISER);
        cruiser.setStart(new Point(5,2));
        cruiser.setEnd(new Point(3,2));
        ships.add(cruiser);

        Ship battleShip = new Ship(ShipType.BATTLESHIP);
        battleShip.setStart(new Point(8,6));
        battleShip.setEnd(new Point(5,6));
        ships.add(battleShip);

        Ship carrier = new Ship(ShipType.CARRIER);
        carrier.setStart(new Point(6,7));
        carrier.setEnd(new Point(2,7));
        ships.add(carrier);


        for(int i = 0; i < ships.size(); i++){
            for(int j = i+1; j < ships.size(); j++) {
                Ship ship1 = ships.get(i);
                Ship ship2 = ships.get(j);
                boolean test = Player.PlayerTesting.intersect(player, ship1, ship2);
                Assertions.assertTrue(!test);
            }
        }
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
            player.getGameState().reset();

            ship = new Ship(ShipType.CRUISER);
            ship.setStart(new Point(3, 2));
            ship.setEnd(new Point(5,2));
            Player.PlayerTesting.addShipToGameState(player, ship);
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(3,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(4,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.SHIP, player.getGameState().getTile(5,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(2,2), "GameState not updated correctly");
            Assertions.assertEquals(Player.Tile.WATER, player.getGameState().getTile(6,2), "GameState not updated correctly");
            player.getGameState().reset();

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
            player.getGameState().reset();

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
            player.getGameState().reset();

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

    @RepeatedTest(10000)
    void testRandomShipPlacement(){
        player.randomShipPlacement();
        List<Ship> ships = Player.PlayerTesting.getShips(player);
            for (Ship ship: ships){
                Assertions.assertTrue(!(ship.getStart().x < 0 || ship.getStart().x >= ROWS || ship.getStart().y < 0 || ship.getStart().y >= COLUMNS));
                Assertions.assertTrue(!(ship.getEnd().x < 0 || ship.getEnd().x >= ROWS || ship.getEnd().y < 0 || ship.getEnd().y >= COLUMNS));
            }

            for(int i = 0; i < ships.size(); i++){
                for(int j = i+1; j < ships.size(); j++){
                    Ship ship1 = ships.get(i);
            Ship ship2 = ships.get(j);
            Assertions.assertTrue(!Player.PlayerTesting.intersect(player, ship1, ship2));
            }
        }
    }

    @Test
    void testCheckHitMiss(){
        List<Ship> ships = new ArrayList<>();

        Ship destroyer = new Ship(ShipType.DESTROYER);
        destroyer.setStart(new Point(3,2));
        destroyer.setEnd(new Point(2,2));
        ships.add(destroyer);

        Ship carrier = new Ship(ShipType.CARRIER);
        carrier.setStart(new Point(4,5));
        carrier.setEnd(new Point(0,5));
        ships.add(carrier);

        Player.PlayerTesting.setShips(player, ships);

        Assertions.assertTrue(player.checkHitMiss(new Point(3,2)));
        Assertions.assertTrue(player.checkHitMiss(new Point(2,5)));
        Assertions.assertTrue(!player.checkHitMiss(new Point(9,9)));
        Assertions.assertTrue(!player.checkHitMiss(new Point(5,5)));
    }
}