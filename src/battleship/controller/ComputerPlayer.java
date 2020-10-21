package battleship.controller;

public class ComputerPlayer extends Player {

    public ComputerPlayer(){
        createShips();
        initGameStates();
        setUpView();
        //updateBoard();
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

    @Override
    public void receiveMessage() {

    }
}
