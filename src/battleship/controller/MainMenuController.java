package battleship.controller;

import javax.swing.JOptionPane;

import battleship.view.MainMenu;
import battleship.view.ViewManager;

public class MainMenuController {

    ViewManager viewManager;

    public MainMenuController(){
        setMainMenuActionListeners();
        viewManager = new ViewManager();
        setRulesWindowActionListener();
    }

    private void setMainMenuActionListeners(){
        MainMenu menu = new MainMenu();
        menu.getRulesButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(true);
        });
        
        //Networking Button Action Listener
        viewManager.getMainMenu().getNetworkButton().addActionListener(e->{
    		int userAnswer = JOptionPane.showConfirmDialog(null, "Are you going to be hosting the game?", "Networking Dialog", JOptionPane.YES_NO_OPTION);
    		
    		if(userAnswer == 0) { 			//User Selected YES
    			System.out.println("User Selected Yes: they are the host");
    			viewManager.createNetworkingHostWindow();
    		}else if (userAnswer == 1) { 	//User Selected NO
    			System.out.println("User Selected No: they are client");
    			viewManager.createNetworkingHostWindow();
    		}
    	});
    }

    private void setRulesWindowActionListener(){
        viewManager.getRulesWindow().getCloseButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(false);
        });

    }
    
}
