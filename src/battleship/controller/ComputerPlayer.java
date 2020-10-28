package battleship.controller;

import battleship.view.ViewManager;

public class ComputerPlayer extends Player {

    public ComputerPlayer(ViewManager viewManager){
    	super(viewManager);
    }

    @Override
    public void placeShips() throws ShipPlacementException{

    }

    @Override
    public void guess() {

    }

    @Override
    public void sendMessage() {

    }
}
