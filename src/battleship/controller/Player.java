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
    private static final Icon SHIP_ICON = new ImageIcon(((new ImageIcon("resources/shipTile.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE, Image.SCALE_SMOOTH))));


    public enum Tile {
        SHIP,
        HIT,
        MISS,
        WATER
    }

    private List<Ship> ships = new ArrayList<>();
    private GameState gameState;
    private GameState enemyGameState;
    private boolean isMyTurn;
    private ViewManager viewManager;

    protected Player(ViewManager viewManager) {
        this.viewManager = viewManager;
        createShips();
        initGameStates();
        if(viewManager != null){
            setUpView();
            updateAllBoards();
        }

    }

    private void updateAllBoards(){
        updateBoard(enemyGameState, viewManager.getGameScreen().getEnemyBoard());
        updateBoard(gameState, viewManager.getGameScreen().getUserBoard());
    }
    
    private void createShips(){
        Ship destroyer = new Ship(ShipType.DESTROYER);
        Ship submarine = new Ship(ShipType.SUBMARINE);
        Ship cruiser = new Ship(ShipType.CRUISER);
        Ship battleship = new Ship(ShipType.BATTLESHIP);
        Ship carrier = new Ship(ShipType.CARRIER);
        ships.add(carrier);
        ships.add(battleship);
        ships.add(cruiser);
        ships.add(submarine);
        ships.add(destroyer);
    }

    private void initGameStates(){
        gameState = new GameState();
        enemyGameState = new GameState();
    }

    private void setUpView(){
        setEnemyActionListeners();
    }

    private void setEnemyActionListeners(){
        Board board = viewManager.getGameScreen().getEnemyBoard();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                board.getButton(i, j).addActionListener(e -> onEnemyButtonClicked(e, board));
            }
        }
    }

    //we need to be sending the message to the enemy to check if it is hit or missed
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
                    case SHIP:
                        currentButton.setIcon(SHIP_ICON);
                        currentButton.setDisabledIcon(SHIP_ICON);
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
    //Assumes start and end have been checked for legal
    private void addShipToGameState(Ship ship) throws ShipPlacementException {
        if (ship.getLength() == 2){
            gameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
            gameState.setTile(Tile.SHIP, ship.getEnd().x, ship.getEnd().y);
        }else if(ship.getStart().x - ship.getEnd().x == 0){ //then it is vertical
            if (ship.getStart().y < ship.getEnd().y){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y+i);
                }
            }else if (ship.getStart().y > ship.getEnd().y){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y-i);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        }else{//then it is horizontal
            if (ship.getStart().x < ship.getEnd().x){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(Tile.SHIP, ship.getStart().x+i, ship.getStart().y);
                }
            }else if (ship.getStart().x > ship.getEnd().x){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(Tile.SHIP, ship.getStart().x-i, ship.getStart().y);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        }
    }

    public void randomShipPlacement() throws ShipPlacementException {
        Random random = new Random();
        boolean placed;
        for (int i = 0; i < ships.size(); i++){
            Ship ship = ships.get(i);
            //keep going until we have a legal starting point and at least one legal ending point
            List<Point> legalEndPoints;
            placed = false;
            while(!placed){
                Point start = new Point();
                do {
                    start.x = random.nextInt(10);
                    start.y = random.nextInt(10);
                } while (!checkPlaceLegal(start));

                ship.setStart(start);

                legalEndPoints = findInBoundsEndPoints(ship);
                Point endPoint = legalEndPoints.get(random.nextInt(legalEndPoints.size()));
                ship.setEnd(endPoint);

                placed = true;

                for (int j = 0; j < i && placed; j++) {
                    Ship ship2 = ships.get(j);
                    if (intersect(ship, ship2)){
                        placed = false;
                    }
                }
            }
            addShipToGameState(ship);
        }
    }

    private boolean intersect(Ship ship1, Ship ship2){
        if (ship2.getStart().x < ship2.getEnd().x){
            if (ship2.getStart().y < ship2.getEnd().y){
                for (int x = ship2.getStart().x; x <= ship2.getEnd().x; x++){
                    for (int y = ship2.getStart().y; y <= ship2.getEnd().y; y++){
                        if(pointIntersectsShip(new Point(x,y),ship1)){
                            return true;
                        }
                    }
                }
            }else{
                for (int x = ship2.getStart().x; x <= ship2.getEnd().x; x++){
                    for (int y = ship2.getStart().y; y >= ship2.getEnd().y; y--){
                        if(pointIntersectsShip(new Point(x,y),ship1)){
                            return true;
                        }
                    }
                }
            }
        }else{
            if (ship2.getStart().y < ship2.getEnd().y){
                for (int x = ship2.getStart().x; x >= ship2.getEnd().x; x--){
                    for (int y = ship2.getStart().y; y <= ship2.getEnd().y; y++){
                        if(pointIntersectsShip(new Point(x,y),ship1)){
                            return true;
                        }
                    }
                }
            }else{
                for (int x = ship2.getStart().x; x >= ship2.getEnd().x; x--){
                    for (int y = ship2.getStart().y; y >= ship2.getEnd().y; y--){
                        if(pointIntersectsShip(new Point(x,y),ship1)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<Point> findInBoundsEndPoints(Ship ship){
        List<Point> endPoints = new ArrayList<>();
        Point start = ship.getStart();
        int length = ship.getLength();

        Point down = new Point(start.x, start.y + length - 1);
        if (checkPlaceInBounds(down)){
            endPoints.add(down);
        }

        Point up = new Point (start.x, start.y - (length - 1));
        if(checkPlaceInBounds(up)){
            endPoints.add(up);
        }

        Point left = new Point(start.x - (length -1), start.y);
        if(checkPlaceInBounds(left)){
            endPoints.add(left);
        }

        Point right = new Point(start.x + length - 1, start.y);
        if (checkPlaceInBounds(right)){
            endPoints.add(right);
        }
        return endPoints;
    }

    private static boolean checkPlaceInBounds(Point place){
        return (place.x >= 0 && place.x < COLUMNS && place.y >= 0 && place.y < ROWS);
    }

    public boolean checkPlaceLegal(Point place){
        if(!checkPlaceInBounds(place)){
            return false;
        }else {
            return gameState.getTile(place.x, place.y) != Tile.SHIP;
        }
    }

    public boolean checkHitMiss(Point tile){
        for(Ship ship: ships){
            if (pointIntersectsShip(tile, ship)){
                ship.updateHits();
                return true;
            }
        }
        return false;
    }

    private boolean pointIntersectsShip(Point point, Ship ship1){
        //first ship is horizontal
        if (ship1.getStart().y == ship1.getEnd().y) {
            if (ship1.getStart().x > ship1.getEnd().x) {
                //the point from ship 2 intersects ship 1
                if (ship1.getStart().y == point.y && ship1.getStart().x >= point.x && point.x >= ship1.getEnd().x){
                    return true;
                }
            } else {
                if (ship1.getStart().y == point.y && ship1.getStart().x <= point.x && point.x <= ship1.getEnd().x) {
                    return true;
                }
            }
        }else{
            if (ship1.getStart().y > ship1.getEnd().y){
                if (ship1.getStart().x == point.x && ship1.getStart().y >= point.y && point.y >= ship1.getEnd().y){
                    return true;
                }
            }else{
                if (ship1.getStart().x == point.x && ship1.getStart().y <= point.y && point.y <= ship1.getEnd().y){
                    return true;
                }
            }
        }
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

    public void resetStoredShips(){
        gameState.reset();
    }

    public void resetGame(){
        gameState.reset();
        enemyGameState.reset();
        for (Ship ship: ships){
            ship.reset();
        }
        //clear log
    }

    public GameState getGameState(){
        return gameState;
    }

    public List<Ship> getShips(){
        return ships;
    }

    public abstract void placeShips() throws ShipPlacementException;
    public abstract void guess();
    public abstract void sendMessage();
    public abstract void processMessage();

    //testing class
    public static class PlayerTesting{
        public static boolean intersect(Player player, Ship ship1, Ship ship2) {
            return player.intersect(ship1, ship2);
        }

        public static void addShipToGameState(Player player, Ship ship) throws ShipPlacementException {
            player.addShipToGameState(ship);
        }

        public static void setShips(Player player, List<Ship> ships){
            player.ships = ships;
        }
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
