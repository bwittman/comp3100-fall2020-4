package battleship.controller;

import battleship.model.Results;
import battleship.view.ViewManager;

public class ComputerPlayer extends Player {

    public ComputerPlayer(ViewManager viewManager){
    	super(viewManager);
    }

    @Override
    public void placeShips() throws ShipPlacementException{

    }

    @Override
    public Results makeGuess(int row, int column) {
        return null;
    }

    @Override
    public void sendResults(Results results){

    }
}
