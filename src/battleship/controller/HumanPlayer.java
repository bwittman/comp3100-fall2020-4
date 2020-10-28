package battleship.controller;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import battleship.view.ViewManager;

public class HumanPlayer extends Player {
	
	private Networking networking;
	private Thread messageListener;
	
	private class MessageListener extends Thread {
		@Override
		public void run() {
			Scanner scanner = networking.getScanner();
			while(networking.isConnected()) {
				String message = scanner.nextLine();
				SwingUtilities.invokeLater(new MessageDispatcher(message));
			}
		}
	}
	
	private class MessageDispatcher extends Thread{
		String message;
		
		public MessageDispatcher(String message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			//TODO: add if to check messages incoming
			
		}
	}
	
	private void listenForNewMessages() {
		
		messageListener = new MessageListener();
		messageListener.start();
	}
	
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
    
    public String getIpAdrressLocal() {
    	return networking.getIpLocal();
    }
    
    public String getIpAddressExternal() {
    	return networking.getIpExternal();
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
    
    public void cleanup() {
    	if(messageListener != null) {
        	messageListener.interrupt();
    	}
    }
    
}
