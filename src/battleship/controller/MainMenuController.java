package battleship.controller;

import javax.swing.JOptionPane;

import battleship.view.MainMenu;
import battleship.view.ViewManager;

public class MainMenuController {

    private ViewManager viewManager;
    private HumanPlayer humanPlayer;

    public MainMenuController(){
        viewManager = new ViewManager();
        setMainMenuActionListeners();
        setRulesWindowActionListener();
    }

    private void setMainMenuActionListeners(){
        MainMenu menu = viewManager.getMainMenu();

        menu.getRulesButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(true);
        });

        //Networking Button Action Listener
        menu.getNetworkButton().addActionListener(e->{
        	this.humanPlayer = new HumanPlayer();
    		int userAnswer = JOptionPane.showConfirmDialog(null, "Are you going to be hosting the game?", "Networking Dialog", JOptionPane.YES_NO_OPTION);
    		
    		if(userAnswer == 0) { 			//User Selected YES
    			System.out.println("User Selected Yes: they are the host");
    			viewManager.createNetworkingHostWindow();
    			String hostIP = this.humanPlayer.getIPAdrress();
    			viewManager.getNetworkingHostWindow().getIPAddressOutsideLabel().setText("Your Outside IP Address is: " + hostIP);
    			//this.humanPlayer.connectAsHost();
    		}else if (userAnswer == 1) { 	//User Selected NO
    			System.out.println("User Selected No: they are client");
    			viewManager.createNetworkingClientWindow();
    			setNetworkingClientActionListener();
    		}
    	});
        
        

        menu.getOnePlayerButton().addActionListener(e->{
            //instantiate the correct players
            //set the player's view manager to this view manager
            viewManager.getGameScreen().setVisible(true);
        });
    }

    //for the closing the rules window
    private void setRulesWindowActionListener(){
        viewManager.getRulesWindow().getCloseButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(false);
        });
    }
    
    private void setNetworkingClientActionListener() {
    	viewManager.getNetworkingClientWindow().getConnectButton().addActionListener(e->{
    		boolean isConnected = humanPlayer.connectAsClient(viewManager.getNetworkingClientWindow().getIPInput().getText());
    		System.out.println("ServerStatus: " + isConnected);
    		if(isConnected == false) {
    			viewManager.getNetworkingClientWindow().getStatusLabel().setText("Couldn't Connect!");
    		}else {
    			JOptionPane.showConfirmDialog(null, "Connection Successful!");
    		}
    	});
    }
    
    public void setHostIPLabel(String ipAddress) {
    	viewManager.getNetworkingHostWindow().getIPAddressOutsideLabel().setText(ipAddress);
    }
    
    
}
