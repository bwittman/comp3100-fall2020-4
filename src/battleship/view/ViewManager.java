package battleship.view;

public class ViewManager {
    private MainMenu mainMenu;
    private GamePlayWindow gameScreen;
    private RulesWindow rulesWindow;
    //make rules, networkingHost, networkingClient, gameEndScreen classes and members

    public ViewManager(){
        mainMenu = new MainMenu();
        gameScreen = new GamePlayWindow();
        rulesWindow= new RulesWindow();
    }

    public GamePlayWindow getGameScreen() {
        return gameScreen;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public RulesWindow getRulesWindow() {
        return rulesWindow;
    }
    //make getters and setters for all

}
//Every individual class has its own getters for members