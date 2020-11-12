package battleship.controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.*;

import battleship.model.Results;
import battleship.model.Ship;
import battleship.model.Ship.ShipType;
import battleship.view.Board;
import battleship.view.CoordinateButton;
import battleship.view.ViewManager;

/**
 * Manages all human interactions with the graphical user interface as well as
 * all game play actions and networking if a two player game
 */
public class HumanPlayer extends Player {

	private static final Color LEGAL_ENDPOINT = new Color(49,192,234);

	private Networking networking = null;
	private Point startPositionPoint;
	private Point endPositionPoint;
	private List<ShipType> placedShips = new ArrayList<>(5);
	private boolean isComputerGame;
	protected volatile Results results = null;

	public HumanPlayer(ViewManager viewManager){
		super(viewManager);
		if (!isComputerGame){
			networking = new Networking();
		}
		setUserBoardActionListeners();
	}

	/*
	 * Listens on the socket on a separate thread so that the GUI does not freeze
	 * if this takes longer than expected
	 */
	private class MessageListener extends Thread {
		@Override
		public void run() {
			while(networking.isConnected()) {
				System.out.println("IsConnected: " + networking.isConnected());
				String message = networking.receiveMessage();
				if(!message.equals("")) {
					SwingUtilities.invokeLater(new MessageDispatcher(message));
				}
			}
			System.err.println("MessageListener: Connection Ended!");
		}
	}
	
	/*
	 * Notifies responsible classes with the correct messages
	 * EXAMPLE: If the MessageListener receives a win game message then it will notify gameState
	 */
	private class MessageDispatcher extends Thread{	
		String message;
		
		public MessageDispatcher(String message) {
			this.message = message;
			System.out.println("Message Received: " + message);
		}
		
		@Override
		public void run() {
			if(message.startsWith("GUESS")) {
				String[] parts = message.split(" ");
				int row = Integer.parseInt(parts[1]);
				int column = Integer.parseInt(parts[2]);
				Results result = processGuess(row, column);
				synchronized (HumanPlayer.this){		//Updates a volatile instance of Result
					//we should be sending the results back not updating our own copy of them???
					HumanPlayer.this.results = result;
					HumanPlayer.this.notifyAll();
				}
				updateAllBoards();
			} else if(message.startsWith("LOG: ")){
				String[] parts = message.split(": ");
				logMessage(parts[1]);
			}else if(message.startsWith("START")){
				opponentPlacedShips = true;
				if (playerStarted && isMyTurn){
					enableBoard(getEnemyGameState(), viewManager.getGameScreen().getEnemyBoard());
				}
			}
		}
	}

	/**
	 * Listen over the network for messages being sent
	 */
	public void listenForNewMessages() {
		MessageListener messageListener = new MessageListener();
		messageListener.start();
	}

	/**
	 * Disconnect from the opponent
	 */
    public void disconnect() {
    	networking.cleanUp();
    }

	/**
	 * Connect to the opponent as a client
	 * @param IP the IP Address of the host
	 * @return if the connection was successful
	 */
	public boolean connectAsClient(String IP) {
    	networking.connect(false, IP);
    	if(networking.isConnected()) {
    		listenForNewMessages();
    		viewManager.getGameScreen().setTitle("Battleship: Game Board [Client]");
    		return true;
    	}
    	return false;
    }

	/**
	 * Connect to the opponent as a host
	 */
	public void connectAsHost() {
		networking.connect(true, "");
		listenForNewMessages();
		viewManager.getGameScreen().setTitle("Battleship: Game Board [Host]");
    }

	/**
	 * Reset the entire game including the view
	 */
	@Override
	public void resetGame(){
		results = null;
		super.resetGame();
		startPositionPoint = null;
		endPositionPoint = null;
		placedShips.clear();
		Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();

		while (shipButtons.hasMoreElements()) {
			AbstractButton shipButton = shipButtons.nextElement();
			shipButton.setEnabled(true);
		}
		viewManager.getGameScreen().getLog().setText("");
		viewManager.getGameScreen().getShipButtonGroup().getElements().nextElement().setSelected(true);
		enableBoard(this.getGameState(), viewManager.getGameScreen().getUserBoard());
	}

	/**
	 * Place the ship that is currently selected
	 * @param clickedButton the ship that is currently selected
	 * @throws ShipPlacementException if the start or end point of the ship is illegal
	 */
    public void placeShip(CoordinateButton clickedButton) throws ShipPlacementException{
		Ship shipToPlace = findSelectedShip();

		if(startPositionPoint == null){
			startPositionPoint = clickedButton.getLocation();
			setShipStart(shipToPlace);
		}else{
			endPositionPoint = clickedButton.getLocation();
			if(checkPlaceLegal(startPositionPoint) && checkPlaceLegal(endPositionPoint)){
					setShipEnd(shipToPlace);
					String logMessage = shipToPlace.getName() + " was placed:\nStart: " + (char)(shipToPlace.getStart().x + 'A')+ (shipToPlace.getStart().y + 1)   +  "\nEnd:  " + (char)(shipToPlace.getEnd().x + 'A') + (shipToPlace.getEnd().y + 1);
					logMessage(logMessage);
			}else{
				throw new ShipPlacementException("Illegal Position Selected");
			}
		}

		if(allShipsPlaced()){
			viewManager.getGameScreen().getPlayGameButton().setEnabled(true);
		}
    }

    /*
     * Choose the starting point of the ship that is currently being placed and
     * enable only legal options for the endpoint of this ship
     */
    private void setShipStart(Ship ship){
		ship.setStart(startPositionPoint);
		placedShips.add(ship.getShipType());

		List<Point> legalEndPoints = findLegalEndPoints(ship);
		if(legalEndPoints.size() != 0) {
			disableBoard(viewManager.getGameScreen().getUserBoard());

			Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
			while (shipButtons.hasMoreElements()) {
				AbstractButton shipButton = shipButtons.nextElement();
				shipButton.setEnabled(false);
			}

			for (Point current : legalEndPoints) {
				viewManager.getGameScreen().getUserBoard().getButton(current.x, current.y).setEnabled(true);
				viewManager.getGameScreen().getUserBoard().getButton(current.x, current.y).setBackground(LEGAL_ENDPOINT);
			}
		} else{
			startPositionPoint = null;
			ship.reset();
		}
	}

	/*
	 * Choose the endpoint of the ship that is currently being placed
	 */
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
			if(shipButton.getText().contains("Carrier") && !placedShips.contains(Ship.ShipType.CARRIER)){
				shipButton.setEnabled(true);
			}
			if(shipButton.getText().contains("Battleship") && !placedShips.contains(Ship.ShipType.BATTLESHIP)){
				shipButton.setEnabled(true);
			}
			if(shipButton.getText().contains("Cruiser") && !placedShips.contains(Ship.ShipType.CRUISER)){
				shipButton.setEnabled(true);
			}
			if(shipButton.getText().contains("Submarine") && !placedShips.contains(Ship.ShipType.SUBMARINE)){
				shipButton.setEnabled(true);
			}
			if(shipButton.getText().contains("Destroyer") && !placedShips.contains(Ship.ShipType.DESTROYER)){
				shipButton.setEnabled(true);
			}
		}

		shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
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

	/*
	 * Figure out which ship is currently being placed
	 */
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

	/*
	 * Set the action listeners on the user board for ship placement
	 */
    private void setUserBoardActionListeners(){
		Board userBoard = viewManager.getGameScreen().getUserBoard();

		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++){
				CoordinateButton button = userBoard.getButton(j,i);
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

	/**
	 * Make a guess and wait for the results of that guess from the opponent
	 * @param row the row of the guessed tile
	 * @param column the column of the guessed tile
	 * @return the results of our guess
	 */
    @Override
    public Results makeGuess(int row, int column) {
		if (isComputerGame){
			disableBoard(viewManager.getGameScreen().getEnemyBoard());
			return opponent.processGuess(row, column);
		}else{
			synchronized (HumanPlayer.this){
				results = null;
			}
			networking.sendMessage("GUESS " + row + " " + column);
			synchronized (HumanPlayer.this){
				while (results == null){
					try {
						HumanPlayer.this.wait();		//wait until results received
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			disableBoard(viewManager.getGameScreen().getEnemyBoard());
			updateAllBoards();
			return results;
		}
    }

	/**
	 * Send the results of an opponent's guess to them
	 * @param results the results of the opponent's guess
	 */
	@Override
	public void sendResults(Results results){
		if(!isComputerGame){
			networking.sendMessage(results.toString());
		}
	}

	public Networking getNetworking(){
		return networking;
	}

	public void setComputerGame(boolean isComputerGame){
		this.isComputerGame = isComputerGame;
	}
}
