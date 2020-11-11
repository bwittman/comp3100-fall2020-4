package battleship.controller;

import battleship.model.GameState;
import battleship.model.Results;
import battleship.view.ViewManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Computer opponent for a single player game
 */
public class ComputerPlayer extends Player {

   private Point previousGuess;
   private List<Point> toSearch = new ArrayList<>();
   private boolean shipSunk;

    public ComputerPlayer(ViewManager viewManager){
    	super(viewManager);
    }

    /**
     * Computer places its ships
     */
    public void placeComputerShips(){
       randomShipPlacement();
    }

    /**
     * Computer plays its turn
     */
    public void playTurn(){
        Point guess = generateGuess();
        Results results = makeGuess(guess.y, guess.x);
        previousGuess = results.getGuessedTile();
        shipSunk = results.getSunkShip() != null;
        processResults(results);
    }

    private void findNextGuesses(Point previousGuess){
        findNextGuessesOnEdgeColumns(previousGuess);
        findNextGuessesOnEdgeRows(previousGuess);
        findNextGuessesInMiddle(previousGuess);
    }

    private void findNextGuessesInMiddle(Point previousGuess){
        GameState enemyState = getEnemyGameState();
        //if its in the middle
        if (previousGuess.x != 0 && previousGuess.x != COLUMNS-1 && previousGuess.y != 0 && previousGuess.y != ROWS-1) {
            //search around this tile to see if any are hits and the opposite of them are water
            //these are clearly the better guesses that we should be choosing first
            if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.HIT &&
                    enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER) {
                toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
            } else if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.HIT &&
                    enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER) {
                toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
            }

            if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.HIT &&
                    enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER) {
                toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
            } else if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.HIT &&
                    enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER) {
                toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
            }

            if (toSearch.size()==0){
                //then just add all the surrounding points
                if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }

                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }

                if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }

                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                }
            }
        }
    }
    private void findNextGuessesOnEdgeColumns(Point previousGuess){
        GameState enemyState = getEnemyGameState();
        //if its on one of the edge columns
        if (previousGuess.x == 0){
            //if its one of the corners
            if(previousGuess.y == 0){
                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                }

                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }
            }else if(previousGuess.y == ROWS-1){
                if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }

                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }
                //if its in the middle of this column
            }else {
                //search for if there is a hit on either side of this tile
                //vertically and water on the other side
                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }else if(enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER) {
                    toSearch.add(new Point(previousGuess.x, previousGuess.y + 1));
                }else{
                    if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                    }

                    if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                    }
                }

                //always check horizontally from here
                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }
            }
        }else if (previousGuess.x == COLUMNS-1){
            //if its one of the corners
            if(previousGuess.y == 0){
                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                }

                if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }
            }else if(previousGuess.y == ROWS-1){
                if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }

                if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }
                //if its in the middle of this column
            }else {
                //search for if there is a hit on either side of this tile
                //vertically and water on the other side
                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }else if(enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER) {
                    toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                }else{
                    if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                    }

                    if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                    }
                }

                //always check horizontally from here
                if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }
            }
        }
    }

    private void findNextGuessesOnEdgeRows(Point previousGuess){
        GameState enemyState = getEnemyGameState();
        //if its on one of the edge rows
        if (previousGuess.y == 0){
            //if its in the middle of this row
            if(previousGuess.x != 0 && previousGuess.x != COLUMNS-1){
                //search for if there is a hit on either side of this tile
                //horizontally and water on the other side
                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }else if(enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER) {
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }else{
                    if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                    }

                    if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                    }
                }

                //always check vertically from here
                if (enemyState.getTile(previousGuess.x, previousGuess.y+1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y+1));
                }
            }
        }else if (previousGuess.y == COLUMNS-1){
            //if its in the middle of this column
            if(previousGuess.x != 0 && previousGuess.x != ROWS-1){
                //search for if there is a hit on either side of this tile
                //vertically and water on the other side
                if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                }else if(enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.HIT &&
                        enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER) {
                    toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                }else{
                    if (enemyState.getTile(previousGuess.x-1, previousGuess.y) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x-1, previousGuess.y));
                    }

                    if (enemyState.getTile(previousGuess.x+1, previousGuess.y) == Tile.WATER){
                        toSearch.add(new Point(previousGuess.x+1, previousGuess.y));
                    }
                }

                //always check vertically from here
                if (enemyState.getTile(previousGuess.x, previousGuess.y-1) == Tile.WATER){
                    toSearch.add(new Point(previousGuess.x, previousGuess.y-1));
                }
            }
        }
    }

    /*
     * Strategy of generating the next tile that the computer will guess
     */
    private Point generateGuess(){
        //if we hit a ship and did not sink it find the next best guess
        if (previousGuess != null) {
            if (getEnemyGameState().getTile(previousGuess.x, previousGuess.y) == Tile.HIT) {
                if (!shipSunk) {
                    //toSearch.clear();
                    findNextGuesses(previousGuess);
                    //if we sunk a ship start over guessing randomly
                } else {
                    toSearch.clear();
                }
            }
        }

        //if there not more tiles to check then guess randomly again
        if (toSearch.size() == 0) {
            shipSunk = false;
            int x;
            int y;
            do {
                Random random = new Random();
                x = random.nextInt(COLUMNS);
                y = random.nextInt(ROWS);
            } while (getEnemyGameState().getTile(x,y) != Tile.WATER);

            return new Point(x,y);
        //if there are more tiles we deemed earlier that we need to check then take the next one
        }else{
            Point point;
            do {
                point = toSearch.remove(0);
            } while (getEnemyGameState().getTile(point.x, point.y) != Tile.WATER);
            return point;
        }
    }

    /**
     * Make a guess and wait for the results of that guess from the opponent
     * @param row the row of the guessed tile
     * @param column the column of the guessed tile
     * @return the results of our guess
     */
    @Override
    public Results makeGuess(int row, int column) {
        return opponent.processGuess(row, column);
    }

    /**
     * Send the results of an opponent's guess to them
     * @param results the results of an opponent's guess
     */
    @Override
    public void sendResults(Results results){
        opponent.processResults(results);
    }
}
