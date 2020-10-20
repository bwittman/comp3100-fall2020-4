package battleship.controller;

import battleship.model.GameState;
import battleship.model.Ship;
import battleship.view.Board;
import battleship.view.BoardPrototype;
import battleship.view.ViewManager;
import battleship.model.Ship.ShipType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private static final int ROWS = 10;
    private static final int COLUMNS = 10;
    private static final int BUTTON_SIDE = 50;
    private static final Color WATER_BLUE = new Color(16,129,160);
    private static final Icon MISS_ICON = new ImageIcon(((new ImageIcon("resources/blueX.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE, Image.SCALE_SMOOTH))));
    private static final Icon HIT_ICON = new ImageIcon(((new ImageIcon("resources/redX.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE, Image.SCALE_SMOOTH))));

    public enum Tile {
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
    private ViewManager viewManager;

    protected void createShips(){
        Ship destroyer = new Ship(ShipType.DESTROYER);
        Ship submarine = new Ship(ShipType.SUBMARINE);
        Ship cruiser = new Ship(ShipType.CRUISER);
        Ship battleship = new Ship(ShipType.BATTLESHIP);
        Ship carrier = new Ship(ShipType.CARRIER);
        ships.add(destroyer);
        ships.add(submarine);
        ships.add(cruiser);
        ships.add(battleship);
        ships.add(carrier);
    }

    protected void setEnemyActionListeners(){
        Board board = viewManager.getGameScreen().getEnemyBoard();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                board.getButton(i, j).addActionListener(e -> onEnemyButtonClicked(e, board));
            }
        }
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

    public void updateBoard(GameState gameState, Board board) {
        for (int i =0; i<ROWS; i++ ){
            for(int j=0;j < COLUMNS; j++){
                Tile currentTile = gameState.getTile(i,j);
                JButton currentButton = board.getButton(i,j);
                switch(currentTile){
                    case WATER:
                        currentButton.setIcon(null);
                        currentButton.setBackground(WATER_BLUE);
                        break;
                    case HIT:
                        currentButton.setIcon(HIT_ICON);
                        currentButton.setDisabledIcon(HIT_ICON);
                        break;
                    case MISS:
                        currentButton.setIcon(MISS_ICON);
                        currentButton.setDisabledIcon(MISS_ICON);
                        break;
                }
                enableBoard(gameState, board);
            }
        }
    }

    public void enableBoard(GameState gameState, Board board){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                JButton currentButton = board.getButton(i,j);
                if (!(gameState.getTile(i,j) == Tile.HIT || gameState.getTile(i,j) == Tile.MISS)){
                    currentButton.setEnabled(true);
                }
            }
        }
    }

    public void disableBoard(Board board){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                JButton currentButton = board.getButton(i,j);
                currentButton.setEnabled(false);
            }
        }
    }

    private void onEnemyButtonClicked(ActionEvent e, Board board){
        //loop through out button array to find the location of the button which was clicked
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton currentButton = board.getButton(row, col);
                if (currentButton == e.getSource()){
                    if(checkHitMiss(new Point(row,col))){
                        enemyGameState.setTile(Tile.HIT, row, col);
                        currentButton.setEnabled(false);
                    }else{
                        enemyGameState.setTile(Tile.MISS, row, col);
                        currentButton.setEnabled(false);
                    }
                }
            }
        }
        updateBoard(enemyGameState, board);
    }

    public static void main(String[]args){
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(625,625);
        frame.setMinimumSize(new Dimension(625,625));
        frame.setResizable(false);

        BoardPrototype board = new BoardPrototype(new GameState());

        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
