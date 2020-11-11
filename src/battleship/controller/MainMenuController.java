package battleship.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import battleship.view.GamePlayWindow;
import battleship.view.MainMenu;
import battleship.view.ViewManager;

/**
 * Initializes the main menu and game
 */
public class MainMenuController {

	private ViewManager viewManager;
	private HumanPlayer humanPlayer;
	private ComputerPlayer computerPlayer;
	private SwingWorker <Void, Void> hostConnectionWorker;
	private boolean initialStart = true;

    public MainMenuController(){
        viewManager = new ViewManager();
        setMainMenuActionListeners();
        setRulesWindowActionListener();
		setNetworkingClientActionListener();
		Player.setButtonSize(GamePlayWindow.buttonSize);
	}

	/*
	 * Set up all the action listeners for buttons on the main menu screen
	 */
	private void setMainMenuActionListeners(){
		MainMenu menu = viewManager.getMainMenu();

		menu.getRulesButton().addActionListener(e->{
			viewManager.getRulesWindow().setVisible(true);
		});

		//Networking Button Action Listener
		menu.getNetworkButton().addActionListener(e->{
			humanPlayer = new HumanPlayer(viewManager);
			int userAnswer = JOptionPane.showConfirmDialog(null, "Are you going to be hosting the game?", "Networking Dialog", JOptionPane.YES_NO_OPTION);

			if(userAnswer == JOptionPane.YES_OPTION) { 			//User Selected YES
				System.out.println("User Selected Yes: they are the host");
				setUpHostWindow();
				viewManager.getNetworkingHostWindow().setVisible(true);
				humanPlayer.setTurn(true);
				humanPlayer.setComputerGame(false);
				menu.getNetworkButton().setEnabled(false);
				menu.getOnePlayerButton().setEnabled(false);
			}else if (userAnswer == JOptionPane.NO_OPTION) { 	//User Selected NO
				System.out.println("User Selected No: they are client");
				viewManager.getNetworkingClientWindow().setVisible(true);
				humanPlayer.setTurn(false);
				menu.getNetworkButton().setEnabled(false);
				menu.getOnePlayerButton().setEnabled(false);
			}else{
				menu.getNetworkButton().setEnabled(true);
				menu.getOnePlayerButton().setEnabled(true);
			}
		});
		
		//If the player closes the host window without connecting this closes the socket
		viewManager.getNetworkingHostWindow().addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if(hostConnectionWorker != null) {
					hostConnectionWorker.cancel(true);
					if(humanPlayer != null) {
						humanPlayer.disconnect();
					}
					System.out.println("hostConnectionWorker canceled");
					menu.getNetworkButton().setEnabled(true);
					menu.getOnePlayerButton().setEnabled(true);
				}
			}
		});

		//one player button action listener
		menu.getOnePlayerButton().addActionListener(e->{
			computerPlayer = new ComputerPlayer(null);
			computerPlayer.setTurn(false);
			computerPlayer.placeComputerShips();

			if(initialStart) {
				humanPlayer = new HumanPlayer(viewManager);
				initialStart = false;
			}

			humanPlayer.setTurn(true);
			humanPlayer.setComputerGame(true);

			computerPlayer.setOpponent(humanPlayer);
			humanPlayer.setOpponent(computerPlayer);

			viewManager.getGameScreen().setVisible(true);
			menu.setVisible(false);
		});
	}

	/*
	 * Initializes and populates the host networking window
	 */
	private void setUpHostWindow(){
		String getIpLocal = humanPlayer.getNetworking().getIpLocal();;
		String getIpExternal = humanPlayer.getNetworking().getIpExternal();
		viewManager.getNetworkingHostWindow().getLocalIPAddress().setText(getIpLocal);
		viewManager.getNetworkingHostWindow().getOuterIPAddress().setText(getIpExternal);
		viewManager.getNetworkingHostWindow().getConnectionStatusLabel().setText("Connection Status: Waiting for Client...");
		hostConnectionWorker = new SwingWorker <Void, Void>(){
			
			@Override
			protected Void doInBackground() {
				humanPlayer.connectAsHost();
				return null;
			}

			public void done() {
				if(humanPlayer.getNetworking().isConnected()) {
					viewManager.getGameScreen().setVisible(true);
					viewManager.getNetworkingClientWindow().setVisible(false);
					viewManager.getNetworkingHostWindow().setVisible(false);
					viewManager.getMainMenu().setVisible(false);
					JOptionPane.showMessageDialog(null, "Connection Successful!");
					System.out.println("Connection Made!");
				}
			}
		};//end swing worker
		hostConnectionWorker.execute();
	}

	/*
	 * Close the rules window when close button is clicked
	 */
	private void setRulesWindowActionListener(){
		viewManager.getRulesWindow().getCloseButton().addActionListener(e->{
			viewManager.getRulesWindow().setVisible(false);
		});
	}

	/*
	 * Initialize the networking client window with input fields
	 */
	private void setNetworkingClientActionListener() {
		viewManager.getNetworkingClientWindow().getConnectButton().addActionListener(e->{
			boolean isConnected = humanPlayer.connectAsClient(viewManager.getNetworkingClientWindow().getIPInput().getText());
			System.out.println("ServerStatus: " + isConnected);
			if(!isConnected) {
				viewManager.getNetworkingClientWindow().getStatusLabel().setText("Couldn't Connect!");
			}else {
				viewManager.getGameScreen().setVisible(true);
				viewManager.getNetworkingClientWindow().setVisible(false);
				viewManager.getNetworkingHostWindow().setVisible(false);
				viewManager.getMainMenu().setVisible(false);
				JOptionPane.showMessageDialog(null, "Connection Successful!");
			}
		});
	}
}
