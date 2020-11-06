package battleship.controller;

import java.awt.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

import battleship.model.Results;
import battleship.model.Ship;
import battleship.view.Board;

import battleship.view.CoordinateButton;
import battleship.view.ViewManager;

public class HumanPlayer extends Player {

	private static final Color LEGAL_ENDPOINT = new Color(49,192,234);

	private Networking networking = null;
	private Thread messageListener;
	private Point startPositionPoint;
	private Point endPositionPoint;
	private boolean isComputerGame;
	private boolean isHost = false;

	public HumanPlayer(ViewManager viewManager){
		super(viewManager);
		if (!isComputerGame){
			networking = new Networking();
		}
		setUserBoardActionListeners();
	}


	/**
	 * Listens on the socket on a separate thread so that the GUI does not freeze if this takes longer than expected
	 *
	 */
	private class MessageListener extends Thread {
		@Override
		public void run() {
			while(networking.isConnected()) {
				String message = networking.receiveMessage();
				System.out.println("Message Received: " + message);
				SwingUtilities.invokeLater(new MessageDispatcher(message));
			}
			System.err.println("MessageListener: Connection Ended!");
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
			if(message.startsWith("GUESS")){
				String[] parts = message.split(" ");
				int row = Integer.parseInt(parts[1]);
				int column = Integer.parseInt(parts[2]);
				Results  result = processGuess(row, column); //What to do with this info
			}
		}
	}

	public void listenForNewMessages() {
		
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
		listenForNewMessages();
    }

    @Override
	public void resetGame(){
		super.resetGame();
		startPositionPoint = null;
		endPositionPoint = null;
		Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
		while (shipButtons.hasMoreElements()) {
			AbstractButton shipButton = shipButtons.nextElement();
			shipButton.setEnabled(true);
		}
		viewManager.getGameScreen().getShipButtonGroup().getElements().nextElement().setSelected(true);
		enableBoard(this.getGameState(), viewManager.getGameScreen().getUserBoard());
	}

    public void placeShip(CoordinateButton clickedButton) throws ShipPlacementException{
		Ship shipToPlace = findSelectedShip();

		if(startPositionPoint == null){
			startPositionPoint = clickedButton.getLocation();
			setShipStart(shipToPlace);
		}else{
			endPositionPoint = clickedButton.getLocation();
			if(checkPlaceLegal(startPositionPoint) && checkPlaceLegal(endPositionPoint)){
					setShipEnd(shipToPlace);
					String logMessage = shipToPlace.getName() + " was placed:\nStart: " + (char)(shipToPlace.getStart().y + 'A')+ (shipToPlace.getStart().x + 1)   +  "\nEnd:  " + (char)(shipToPlace.getEnd().y + 'A') + (shipToPlace.getEnd().x + 1);
					logMessage(logMessage);
			}else{
				throw new ShipPlacementException("Illegal Position Selected");
			}
		}
		if(allShipsPlaced()){
			viewManager.getGameScreen().getPlayGameButton().setEnabled(true);
		}

    }

    private void setShipStart(Ship ship){
		ship.setStart(startPositionPoint);

		List<Point> legalEndPoints = findLegalEndPoints(ship);
		if(legalEndPoints.size() != 0) {
			disableBoard(viewManager.getGameScreen().getUserBoard());
			for (Point current : legalEndPoints) {
				viewManager.getGameScreen().getUserBoard().getButton(current.x, current.y).setEnabled(true);
				viewManager.getGameScreen().getUserBoard().getButton(current.x, current.y).setBackground(LEGAL_ENDPOINT);
			}
		} else{
			startPositionPoint = null;
			ship.reset();
		}
	}

	private void setShipEnd(Ship ship) throws ShipPlacementException{
		ship.setEnd(endPositionPoint);

		addShipToGameState(ship);

		updateAllBoards();
		viewManager.getGameScreen().getShipButtonGroup().getSelection().setEnabled(false);
		viewManager.getGameScreen().getShipButtonGroup().clearSelection();

		boolean allShipsPlaced = true;
		Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
		while (shipButtons.hasMoreElements()) {
			AbstractButton shipButton = shipButtons.nextElement();
			if(shipButton.isEnabled()) {
				allShipsPlaced = false;
				shipButton.setSelected(true);
			}
		}

		//reset first and second click counter
		startPositionPoint = null;
		endPositionPoint = null;
		if (allShipsPlaced){
			disableBoard(viewManager.getGameScreen().getUserBoard());
		}else{
			enableBoard(getGameState(), viewManager.getGameScreen().getUserBoard());
		}
	}

    private Ship findSelectedShip(){
		String shipType = "";
		ButtonModel buttonModel = viewManager.getGameScreen().getShipButtonGroup().getSelection();
		Ship shipObject = null;

		if(buttonModel != null){
			shipType = buttonModel.getActionCommand();

			for (Ship ship : ships) {
				if (ship.getName().equals(shipType)) {
					shipObject = ship;
				}
			}

			if(shipObject != null) {
				System.out.println("shipObject = " + shipObject.toString());
			} else {
				System.err.println("HumanPlayer: ShipObject Not Initialized");
			}
		}else{
			System.err.println("HumanPlayer: Could not get ship type selected when trying to place ships");
		}

		System.out.println("Human Player current selected ship getActionCommand: " + shipType);

		return shipObject;
	}

    private void setUserBoardActionListeners(){
		Board userBoard = viewManager.getGameScreen().getUserBoard();

		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				CoordinateButton button = userBoard.getButton(i,j);
				button.addActionListener(e ->{
					try {
						placeShip(button);
					} catch (ShipPlacementException shipPlacementException) {
						shipPlacementException.printStackTrace();
					}
				});
			}
		}
	}

    @Override
    public Results makeGuess(int row, int column) {
		if (isComputerGame){
			 return opponent.processGuess(row, column);
		}else{
			//networking.printwriter.println("GUESS")
			//send the ints
			//use scanner to read sunk, hit/miss, win
			//convert into a Results object

			networking.sendMessage("GUESS " + row + " " + column);
			String message = networking.receiveMessage();
			return new Results(message);

		}
    }

    @Override
	public void sendResults(Results results){
		if(!isComputerGame){
			networking.sendMessage(results.toString());
		}
	}

	public void cleanup() {
    	if(messageListener != null) {
        	messageListener.interrupt();
    	}
    }

	public void setComputerGame(boolean isComputerGame){
		this.isComputerGame = isComputerGame;
	}
}
