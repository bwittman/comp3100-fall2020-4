package battleship.controller;

import battleship.model.GameState;
import battleship.model.Ship;
import battleship.view.*;
import battleship.model.Ship.ShipType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    protected void initGameStates(){
        myGameState = new GameState();
        enemyGameState = new GameState();
    }

    protected void setUpView(){
        this.viewManager = new ViewManager();
        setEnemyActionListeners();
        //setMainMenuActionListeners();
        //setRulesWindowActionListener();
    }

    private void setEnemyActionListeners(){
        Board board = viewManager.getGameScreen().getEnemyBoard();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                board.getButton(i, j).addActionListener(e -> onEnemyButtonClicked(e, board));
            }
        }
    }


    private void addShipToGameState(Ship ship) throws ShipPlacementException {
        if (ship.getLength() == 2){
            myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
            myGameState.setTile(Tile.SHIP, ship.getEnd().x, ship.getEnd().y);
        }else if(ship.getStart().x - ship.getEnd().x == 0){ //then it is vertical
            if (ship.getStart().y < ship.getEnd().y){
                myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
                for (int i = 0; i < ship.getLength()-1; i++){
                    myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y+1);
                }
            }else if (ship.getStart().y > ship.getEnd().y){
                myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
                for (int i = 0; i < ship.getLength()-1; i++){
                    myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y-1);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        }else{//then it is horizontal
            if (ship.getStart().x < ship.getEnd().x){
                myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
                for (int i = 0; i < ship.getLength()-1; i++){
                    myGameState.setTile(Tile.SHIP, ship.getStart().x+1, ship.getStart().y);
                }
            }else if (ship.getStart().x > ship.getEnd().x){
                myGameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
                for (int i = 0; i < ship.getLength()-1; i++){
                    myGameState.setTile(Tile.SHIP, ship.getStart().x-1, ship.getStart().y);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        }
    }

    public void randomShipPlacement() throws ShipPlacementException {
        Random random = new Random();
        for (Ship ship: ships){
            //keep going until we have a legal starting point and at least one legal ending point
            List<Point> legalEndPoints = new ArrayList<>();
            while(legalEndPoints.size() == 0) {
                Point start = new Point();
                do {
                    start.x = random.nextInt(9);
                    start.y = random.nextInt(9);
                } while (!checkPlaceLegal(start));

                ship.setStart(start);

                legalEndPoints = findAllLegalEndPoints(ship);
            }

            Point endPoint = legalEndPoints.get(random.nextInt(legalEndPoints.size()));
            ship.setEnd(endPoint);
            addShipToGameState(ship);
        }
    }

    private List<Point> findAllLegalEndPoints(Ship ship){
        List<Point> endPoints = new ArrayList<>();
        Point start = ship.getStart();
        int length = ship.getLength();

        Point down = new Point(start.x, start.y + length - 1);
        if (checkPlaceLegal(down)){
            endPoints.add(down);
        }

        Point up = new Point (start.x, start.y - (length - 1));
        if(checkPlaceLegal(up)){
            endPoints.add(up);
        }

        Point left = new Point(start.x - (length -1), start.y);
        if(checkPlaceLegal(left)){
            endPoints.add(left);
        }

        Point right = new Point(start.x + length - 1, start.y);
        if (checkPlaceLegal(right)){
            endPoints.add(right);
        }
        return endPoints;
    }

    public boolean checkPlaceLegal(Point place){
        if(place.x < 0 || place.x >= ROWS || place.y < 0 || place.y >= COLUMNS){
            return false;
        }else {
            return myGameState.getTile(place.x, place.y) != Tile.SHIP;
        }
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

    public abstract void placeShips() throws ShipPlacementException;
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
                    }else{
                        enemyGameState.setTile(Tile.MISS, row, col);
                    }
                    currentButton.setEnabled(false);
                }
            }
        }
        updateBoard(enemyGameState, board);
    }

    public GameState getMyGameState(){
        return myGameState;
    }

    public static void main(String[]args){
        /*
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
        */

        MainMenuController menuController = new MainMenuController();
    }
}
