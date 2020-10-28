package battleship.controller;

import battleship.view.ViewManager;

public class HumanPlayer extends Player {
	
	private Networking networking;

    public HumanPlayer(ViewManager viewManager){
    	super(viewManager);
        createShips();
        initGameStates();
        setUpView();
        //updateBoard();
        networking = new Networking(); 
    }
    
    public void disconnect() {
    	networking.cleanUp();
    }
    
    public boolean connectAsClient(String IP) {
    	networking.connect(false, IP);
    	if(networking.isConnected()) {
    		return true;
    	}
    	return false;
    }
    
    public String getIPAdrress() {
    	return networking.getIP();
    }
    
    public void connectAsHost() {
    	networking.connect(true, "");
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
