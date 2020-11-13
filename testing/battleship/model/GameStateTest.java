package battleship.model;

import battleship.controller.Player.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * All tests associated with the GameState class
 */
class GameStateTest {

    private GameState gameState = new GameState();

    @Test
    void testPartialBoardReset(){
        gameState.setTile(Tile.HIT, 3,6);
        gameState.setTile(Tile.MISS, 4,2);
        gameState.setTile(Tile.CARRIER, 9,9);
        gameState.setTile(Tile.BATTLESHIP, 2,4);
        gameState.setTile(Tile.CRUISER, 1,3);
        gameState.setTile(Tile.SUBMARINE, 6,1);
        gameState.setTile(Tile.DESTROYER, 3,1);
        gameState.setTile(Tile.HIT, 1,1);

        gameState.reset();

        for (int i=0; i < 10; i++){
            for (int j=0; j < 10; j++){
                Assertions.assertEquals(gameState.getTile(i, j), Tile.WATER);
            }
        }
    }

    @Test
    void testWholeBoardReset() {
        for (int i=0; i < 10; i++){
            for (int j=0; j < 10; j++){
                gameState.setTile(Tile.HIT, i, j);
            }
        }

        gameState.reset();

        for (int i=0; i < 10; i++){
            for (int j=0; j < 10; j++){
                Assertions.assertEquals(gameState.getTile(i, j), Tile.WATER);
            }
        }
    }
}