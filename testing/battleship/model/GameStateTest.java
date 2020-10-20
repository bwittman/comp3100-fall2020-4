package battleship.model;

import battleship.model.GameState;
import battleship.controller.Player.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setup(){
        gameState = new GameState();
    }

    @Test
    void testPartialBoardReset(){
        gameState.setTile(Tile.HIT, 3,6);
        gameState.setTile(Tile.MISS, 4,2);
        gameState.setTile(Tile.SHIP, 9,9);
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