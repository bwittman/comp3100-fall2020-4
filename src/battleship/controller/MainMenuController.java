package battleship.controller;

import battleship.view.MainMenu;
import battleship.view.ViewManager;

public class MainMenuController {

    ViewManager viewManager;

    public MainMenuController(){
        setMainMenuActionListeners();
        viewManager = new ViewManager();
        setRulesWindowActionListener();
    }

    protected void setMainMenuActionListeners(){
        MainMenu menu = new MainMenu();
        menu.getRulesButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(true);
        });

    }

    protected void setRulesWindowActionListener(){
        viewManager.getRulesWindow().getCloseButton().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(false);
        });

    }

}
