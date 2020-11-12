package battleship.view;

/**
 * Manages all of the view windows
 */
public class ViewManager {
    private MainMenu mainMenu;
    private GamePlayWindow gameScreen;
    private RulesWindow rulesWindow;
    private NetworkingClient networkingClientWindow;
    private NetworkingHost networkingHostWindow;

    public ViewManager(){
        mainMenu = new MainMenu();//must be set up first because of the frame sizing
        gameScreen = new GamePlayWindow(mainMenu.getFrameSize());
        rulesWindow = new RulesWindow();
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