package battleship.controller;

import battleship.model.Results;
import battleship.view.ViewManager;

import java.awt.*;
import java.util.Random;

public class ComputerPlayer extends Player {

    public ComputerPlayer(ViewManager viewManager){
    	super(viewManager);
    }

    public void placeComputerShips(){
       randomShipPlacement();
    }

    public void playTurn(){
        Point guess = generateGuess();
        Results results = makeGuess(guess.x, guess.y);
        processResults(results);
    }

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

    @Override
    public Results makeGuess(int row, int column) {
        return opponent.processGuess(row, column);
    }

    @Override
    public void sendResults(Results results){
        opponent.processResults(results);
    }
}
