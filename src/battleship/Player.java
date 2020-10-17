package battleship;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Player {

    private String playerName;
    private List<Ship> ships;
    private GameState myGameState;
    private GameState enemyGameState;
    private boolean isMyTurn;

    protected void createShips(){
        Ship destroyer = new Ship("Destroyer", 2);
        Ship submarine = new Ship("Submarine", 3);
        Ship cruiser = new Ship("Cruiser", 3);
        Ship battleship = new Ship("Battleship", 4);
        Ship carrier = new Ship("Carrier", 5);
        ships.add(destroyer);
        ships.add(submarine);
        ships.add(cruiser);
        ships.add(battleship);
        ships.add(carrier);
    }

    public void randomShipPlacement(){

    }

    public boolean checkPlaceLegal(Point place){
        return false;
    }

    public void resetStoredShips(){
        myGameState.reset();
    }

    public void resetGame(){
        myGameState.reset();
        enemyGameState.reset();
        for (Ship ship: ships){
            ship.reset();
        }
        //clear log
    }

    public boolean checkHitMiss(Point tile){
        return false;
    }

    public boolean checkForWin(){
        for (Ship ship: ships){
            if (!ship.checkForSunk()){
                return false;
            }
        }
        return true;
    }

    public abstract void placeShips();
    public abstract void guess();
    public abstract void sendMessage();
    public abstract void receiveMessage();

    public static void main(String[]args){
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(625,625);
        frame.setMinimumSize(new Dimension(625,625));
        frame.setResizable(false);

        //BoardPrototype board = new BoardPrototype(new GameState());
        MainWindow board = new MainWindow();
        
        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
