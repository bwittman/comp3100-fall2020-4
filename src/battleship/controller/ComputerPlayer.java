package battleship.controller;

import battleship.model.Results;
import battleship.view.ViewManager;

import java.awt.*;
import java.util.Random;

/**
 * Computer opponent for a single player game
 */
public class ComputerPlayer extends Player {

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
        Results results = makeGuess(guess.x, guess.y);
        processResults(results);
    }

    /*
     * Strategy of generating the next tile that the computer will guess
     */
    private Point generateGuess(){
        int x;
        int y;
        do {
            Random rand = new Random();
            x = rand.nextInt(ROWS);
            y = rand.nextInt(COLUMNS);
        }while(getEnemyGameState().getTile(x,y) != Tile.WATER);
        return new Point(x,y);
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
