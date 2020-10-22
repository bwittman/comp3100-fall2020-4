package battleship.view;

public class ViewManager {
    private MainMenu mainMenu;
    private GamePlayWindow gameScreen;
    //make rules, networkingHost, networkingClient, gameEndScreen classes and members

    public ViewManager(){
        mainMenu = new MainMenu();
        gameScreen = new GamePlayWindow();
    }

    public GamePlayWindow getGameScreen() {
        return gameScreen;
    }
    //make getters and setters for all

}
//Every individual class has its own getters for members