package battleship.controller;

public class HumanPlayer extends Player {
	
	private Networking networkingClass;

    public HumanPlayer(){
        createShips();
        initGameStates();
        setUpView();
        //updateBoard();
        networkingClass = new Networking();
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
