package battleship.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import battleship.view.MainMenu;
import battleship.view.ViewManager;

/**
 * Initializes the main menu and game
 */
public class MainMenuController {

	private static final String[] hostingOptions = {"Host", "Join"};

	private ViewManager viewManager;
	private HumanPlayer humanPlayer;
	private ComputerPlayer computerPlayer;
	private SwingWorker <Void, Void> hostConnectionWorker;
	private boolean initialStart = true;

    public MainMenuController(){
    	setLookAndFeel();
        viewManager = new ViewManager();
        setMainMenuActionListeners();
        setRulesWindowActionListener();
		setNetworkingClientActionListener();
	}

	private void setLookAndFeel(){
        try {
            //Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
	}

	/*
	 * Set up all the action listeners for buttons on the main menu screen
	 */
	private void setMainMenuActionListeners(){
		MainMenu mainMenu = viewManager.getMainMenu();

		mainMenu.getRulesItem().addActionListener(e->{
			viewManager.getRulesWindow().setVisible(true);
		});

		//Networking Button Action Listener
		mainMenu.getNetworkingItem().addActionListener(e->{
			humanPlayer = new HumanPlayer(viewManager);
			int hostingDecision = JOptionPane.showOptionDialog(null,"Will you be hosing or joining the game?", "Host or Join Game",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, hostingOptions, null);

			if(hostingDecision == 0) {//User Selected host
				System.out.println("User Selected host");
				setUpHostWindow();
				viewManager.getNetworkingHostWindow().setVisible(true);
				humanPlayer.setTurn(true);
				mainMenu.getNetworkingItem().setEnabled(false);
				mainMenu.getComputerItem().setEnabled(false);
			}else if (hostingDecision == 1) {//User Selected Join
				System.out.println("User Selected client");
				viewManager.getNetworkingClientWindow().setVisible(true);
				humanPlayer.setTurn(false);
				mainMenu.getNetworkingItem().setEnabled(false);
				mainMenu.getComputerItem().setEnabled(false);
			}else{
				mainMenu.getNetworkingItem().setEnabled(true);
				mainMenu.getComputerItem().setEnabled(true);
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
					mainMenu.getNetworkingItem().setEnabled(true);
					mainMenu.getComputerItem().setEnabled(true);
				}
			}
		});

		viewManager.getNetworkingClientWindow().addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				viewManager.getMainMenu().getComputerItem().setEnabled(true);
				viewManager.getMainMenu().getNetworkingItem().setEnabled(true);
			}
		});

		//one player button action listener
		mainMenu.getComputerItem().addActionListener(e->{
			computerPlayer = new ComputerPlayer(null);
			computerPlayer.setTurn(false);

			if(initialStart) {
				humanPlayer = new HumanPlayer(viewManager);
				initialStart = false;
			}

			humanPlayer.setTurn(true);

			computerPlayer.setOpponent(humanPlayer);
			humanPlayer.setOpponent(computerPlayer);

			computerPlayer.placeComputerShips();

			viewManager.getGameScreen().setVisible(true);
			mainMenu.setVisible(false);
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
