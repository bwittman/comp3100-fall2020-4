package battleship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    protected enum Tile {
        SHIP,
        HIT,
        MISS,
        WATER
    }

    private String playerName;
    private List<Ship> ships = new ArrayList<>();
    private GameState myGameState;
    private GameState enemyGameState;
    private boolean isMyTurn;

    protected void createShips(){
        Ship destroyer = new Ship(Ship.ShipType.DESTROYER);
        Ship submarine = new Ship(Ship.ShipType.SUBMARINE);
        Ship cruiser = new Ship(Ship.ShipType.CRUISER);
        Ship battleship = new Ship(Ship.ShipType.BATTLESHIP);
        Ship carrier = new Ship(Ship.ShipType.CARRIER);
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
