package battleship.controller;

public class HumanPlayer extends Player {

    public HumanPlayer(){
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
