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
    
    public boolean connectAsClient(String IP) {
    	networkingClass.connect(false, IP);
    	if(networkingClass.isConnected()) {
    		return true;
    	}
    	return false;
    }
    
    public String getIPAdrress() {
    	return networkingClass.getIP();
    }
    
    public void connectAsHost() {
    	networkingClass.connect(true, "");
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
