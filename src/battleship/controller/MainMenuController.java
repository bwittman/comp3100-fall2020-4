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
	private MainMenu menu;

    public MainMenuController(){
    	setLookAndFeel();
        viewManager = new ViewManager();
        setMainMenuActionListeners();
		setNetworkingClientActionListener();
		viewManager.getMainMenu().startIntroSound();
	}

	/*
	 * Set the look and feel for all the windows
	 */
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
		menu = viewManager.getMainMenu();

		menu.getRulesItem().addActionListener(e->{
			viewManager.getRulesWindow().setVisible(true);
		});

		menu.getHowToPlayItem().addActionListener(e->{
			viewManager.getHowToPlayWindow().setVisible(true);
		});

		//Networking Button Action Listener
		menu.getNetworkingItem().addActionListener(e->{
			viewManager.getMainMenu().stopIntroSound();
			if(initialStart) {
				humanPlayer = new HumanPlayer(viewManager, this);
				initialStart = false;
			}

			int hostingDecision = JOptionPane.showOptionDialog(null,"Will you be hosting or joining the game?", "Host or Join Game",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, hostingOptions, null);

			if(hostingDecision == 0) {//User Selected host
				System.out.println("User Selected host");
				setUpHostWindow();
				viewManager.getNetworkingHostWindow().setVisible(true);
				humanPlayer.setTurn(true);
				menu.getNetworkingItem().setEnabled(false);
				menu.getComputerItem().setEnabled(false);
			}else if (hostingDecision == 1) {//User Selected Join
				System.out.println("User Selected client");
				viewManager.getNetworkingClientWindow().setVisible(true);
				humanPlayer.setTurn(false);
				menu.getNetworkingItem().setEnabled(false);
				menu.getComputerItem().setEnabled(false);
			}else{
				menu.getNetworkingItem().setEnabled(true);
				menu.getComputerItem().setEnabled(true);
				viewManager.getMainMenu().startIntroSound();
			}
		});
		
		//If the player closes the host window without connecting this closes the socket
		viewManager.getNetworkingHostWindow().addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				resetMainMenu();
				viewManager.getMainMenu().startIntroSound();
			}
		});

		viewManager.getNetworkingClientWindow().addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				resetMainMenu();
				viewManager.getMainMenu().startIntroSound();
			}
		});

		//one player button action listener
		menu.getComputerItem().addActionListener(e->{
			viewManager.getMainMenu().stopIntroSound();
			computerPlayer = new ComputerPlayer(null);
			computerPlayer.setTurn(false);

			if(initialStart) {
				humanPlayer = new HumanPlayer(viewManager, this);
				initialStart = false;
			}

			humanPlayer.setTurn(true);

			computerPlayer.setOpponent(humanPlayer);
			humanPlayer.setOpponent(computerPlayer);

			computerPlayer.placeComputerShips();

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

	/**
	 * Reset the main menu screen and disconnect appropriately
	 */
	public void resetMainMenu(){
		if(hostConnectionWorker != null) {
			hostConnectionWorker.cancel(true);
			if(humanPlayer != null) {
				humanPlayer.disconnect();
				System.out.println("Human Player Disconnected called");
			}
			System.out.println("hostConnectionWorker canceled");
		}
		menu.reset();
	}
}
