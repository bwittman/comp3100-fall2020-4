package battleship.controller;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

import battleship.model.Ship;
import battleship.view.Board;

import battleship.view.ViewManager;

public class HumanPlayer extends Player {
	
	private Networking networking;
	private Thread messageListener;
	private ViewManager viewManager;
	private Point startPositionPoint;
	private Point endPositionPoint;
	private List<Ship> ships;

	public HumanPlayer(ViewManager viewManager){
		super(viewManager);
		this.viewManager = viewManager;
		networking = new Networking();
		setUserBoardActionListeners();
		ships = getShips();
	}

	@Override
	public void placeShips() throws ShipPlacementException {
		//TODO: Resolve problem of two placeShips methods
	}

	/**
	 * Listens on the socket on a separate thread so that the GUI does not freeze if this takes longer than expected
	 *
	 */
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
	
	/**
	 * Notifies responsible classes with the correct messages
	 * EXAMPLE: If the MessageListener receives a win game message then it will notify gameState
	 * 
	 */
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

    public Networking getNetworking(){
		return networking;
	}

    public void connectAsHost() {
    	networking.connect(true, "");
    }

    @Override
	public void resetGame(){
		super.resetGame();
		startPositionPoint = null;
		endPositionPoint = null;
	}

    public void placeShips(String buttonName) throws ShipPlacementException{
		String shipType = "";
		ButtonModel buttonModel = viewManager.getGameScreen().getShipButtonGroup().getSelection();
		Ship shipObject = null;

		if(buttonModel != null){
			shipType = buttonModel.getActionCommand();

			for(int i = 0; i < ships.size(); i++){
				if(ships.get(i).toString() == shipType) shipObject = ships.get(i);
				/*
				if((shipObject.getEnd()!=null) || (shipObject.getStart()!= null)) {
					throw new ShipPlacementException("Ship already Initialized...");
				}
				*/
			}
			if(shipObject != null) System.out.println("shipObject = " + shipObject.toString());
			else System.err.println("HumanPlayer: ShipObject Not Initialized");
		}else{
			System.err.println("HumanPlayer: Could not get ship type selected when trying to place ships");
		}

		System.out.println("Human Player current selected ship getActionCommand: " + shipType);

		if(startPositionPoint == null){ 	//Checks start position first
			String [] startPositionPoint = buttonName.split(" ");
			String xString = startPositionPoint[0]; //First String
			String yString = startPositionPoint[1];	//Second String
			int x = Integer.parseInt(xString);
			int y = Integer.parseInt(yString);
			this.startPositionPoint = new Point(x, y);
		}else{ 								//Checks end position next
			String [] endPositionStringList = buttonName.split(" ");
			String xString = endPositionStringList[0]; //First String
			String yString = endPositionStringList[1];	//Second String
			int x = Integer.parseInt(xString);
			int y = Integer.parseInt(yString);
			endPositionPoint = new Point(x,y);
			boolean isStartLegal = checkPlaceLegal(startPositionPoint);
			if(isStartLegal){
				boolean isEndLegal = checkPlaceLegal(endPositionPoint);
				if(isEndLegal){
					shipObject.setStart(startPositionPoint);
					shipObject.setEnd(endPositionPoint);
					System.out.println("Trying to place ships at Start: " + startPositionPoint.toString() + "\tEnd: " + endPositionPoint.toString());
					try{
						addShipToGameState(shipObject);
					}catch (ShipPlacementException e){
						System.err.println("HumanPlayer: Could not add ship to game state");
					}
					updateAllBoards();
					startPositionPoint = null;
					endPositionPoint = null;		//reset first and second click counter

				}else{
					System.err.println("Human Player: End Position is not Legal");
				}
			}else{
				System.err.println("Human Player: Start Position is not Legal");
			}


		}
    }

    private void setUserBoardActionListeners(){
		Board userBoard = viewManager.getGameScreen().getUserBoard();

		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				int finalI = i;
				int finalJ = j;
				userBoard.getButton(i, j).addActionListener(e->{
					String name = userBoard.getButton(finalI, finalJ).getName();
					try {
						System.out.println("ActionListener for UserBoard: " + name);
						placeShips(name);
					} catch (ShipPlacementException shipPlacementException) {
						shipPlacementException.printStackTrace();
					}
				});
			}
		}
	}

    @Override
    public void guess() {

    }

    @Override
    public void sendMessage() {

    }

	@Override
	public void processMessage() {

	}

	public void cleanup() {
    	if(messageListener != null) {
        	messageListener.interrupt();
    	}
    }


    
}
