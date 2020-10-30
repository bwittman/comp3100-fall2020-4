package battleship.view;

public class ViewManager {
    private MainMenu mainMenu;
    private GamePlayWindow gameScreen;
    private RulesWindow rulesWindow;
    private NetworkingClient networkingClientWindow;
    private NetworkingHost networkingHostWindow;
    
    //make gameEndScreen class and member

    public ViewManager(){
        gameScreen = new GamePlayWindow();
        rulesWindow = new RulesWindow();
        mainMenu = new MainMenu();
        networkingClientWindow = new NetworkingClient();
        networkingHostWindow = new NetworkingHost();
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

    public NetworkingClient getNetworkingClientWindow() {
		return networkingClientWindow;
    }

    public NetworkingHost getNetworkingHostWindow() {
		return networkingHostWindow;
    }
}
//Every individual class has its own getters for members