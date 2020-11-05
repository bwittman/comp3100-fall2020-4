package battleship.controller;

import battleship.model.GameState;
import battleship.model.Results;
import battleship.model.Ship;
import battleship.view.*;
import battleship.model.Ship.ShipType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public abstract class Player {
    public static final int ROWS = 10;
    public static final int COLUMNS = 10;
    private static final int BUTTON_SIDE = 50;
    private static final Color WATER = new Color(16,129,160);
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

    protected List<Ship> ships = new ArrayList<>();
    private GameState gameState;
    private GameState enemyGameState;
    private boolean isMyTurn;
    protected ViewManager viewManager;
    protected Player opponent = null;

    protected Player(ViewManager viewManager) {
        this.viewManager = viewManager;
        createShips();
        initGameStates();
        if(viewManager != null){
            setUpView();
            updateAllBoards();
        }
    }

    void updateAllBoards(){
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
        setResetActionListener();
        setRandomActionListener();
        setPlayGameActionListener();
        disableBoard(viewManager.getGameScreen().getEnemyBoard());
    }

    private void setEnemyActionListeners(){
        Board board = viewManager.getGameScreen().getEnemyBoard();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                CoordinateButton button = board.getButton(i,j);
                button.addActionListener(e -> onEnemyButtonClicked(button));
            }
        }
    }

    private void setResetActionListener(){
        viewManager.getGameScreen().getResetButton().addActionListener(e ->{
            resetGame();
            updateAllBoards();
            viewManager.getGameScreen().getPlayGameButton().setEnabled(false);
        });
    }

    private void setRandomActionListener(){
        viewManager.getGameScreen().getRandomButton().addActionListener(e -> {
            resetGame();
            randomShipPlacement();
            updateAllBoards();
            viewManager.getGameScreen().getPlayGameButton().setEnabled(true);
        });
    }

    private void setPlayGameActionListener(){
        viewManager.getGameScreen().getPlayGameButton().addActionListener(e -> {
            onPlayGameClicked();
        });
    }

    private void onPlayGameClicked(){
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you satisfied with this ship placement?", "Confirm Final Ship Placement", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            disableBoard(viewManager.getGameScreen().getUserBoard());

            //disable ship buttons
            Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
            while (shipButtons.hasMoreElements()) {
                AbstractButton shipButton = shipButtons.nextElement();
                shipButton.setEnabled(false);
            }

            viewManager.getGameScreen().getOptionButtons().setVisible(false);

            if (isMyTurn) {
                enableBoard(enemyGameState, viewManager.getGameScreen().getEnemyBoard());
            }
        }
    }

    //we need to be sending the message to the enemy to check if it is hit or missed
    private void onEnemyButtonClicked(CoordinateButton button){
        Results result = makeGuess(button.getLocation().x, button.getLocation().y);//sending the enemy what our guess is
        processResults(result);
    }

    private void updateBoard(GameState gameState, Board board) {
        for (int i =0; i<ROWS; i++ ){
            for(int j=0;j < COLUMNS; j++){
                Tile currentTile = gameState.getTile(i,j);
                JButton currentButton = board.getButton(i,j);
                switch(currentTile){
                    case WATER:
                        currentButton.setIcon(null);
                        currentButton.setBackground(WATER);
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
            }
        }
    }

    public void enableBoard(GameState gameState, Board board){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                JButton currentButton = board.getButton(i,j);
                if (gameState.getTile(i,j) == Tile.WATER){
                    currentButton.setEnabled(true);
                }else{
                    currentButton.setEnabled(false);
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

    protected boolean allShipsPlaced(){
        for (Ship ship: ships){
            if (ship.getStart() == null || ship.getEnd() == null){
                return false;
            }
        }
        return true;
    }

    //Assumes start and end have been checked for legal
    protected void addShipToGameState(Ship ship) throws ShipPlacementException {
        if (ship.getLength() == 2){
            gameState.setTile(Tile.SHIP, ship.getStart().x, ship.getStart().y);
            gameState.setTile(Tile.SHIP, ship.getEnd().x, ship.getEnd().y);
        //then it is horizontal
        }else if(ship.getStart().x - ship.getEnd().x == 0){
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
        //then it is vertical
        }else{
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

    public void randomShipPlacement(){
        Random random = new Random();

        int i = 0;
        while(i < ships.size()){
            Ship ship = ships.get(i);
            //keep going until we have a legal starting point and at least one legal ending point
            List<Point> legalEndPoints;
            boolean placed = false;
            while(!placed){
                Point start = new Point();
                do {
                    start.x = random.nextInt(ROWS);
                    start.y = random.nextInt(COLUMNS);
                } while (!checkPlaceLegal(start));

                ship.setStart(start);

                legalEndPoints = findLegalEndPoints(ship);
                if(legalEndPoints.size() != 0) {
                    Point endPoint = legalEndPoints.get(random.nextInt(legalEndPoints.size()));
                    ship.setEnd(endPoint);

                    placed = true;

                    for (int j = 0; j < i && placed; j++) {
                        Ship ship2 = ships.get(j);
                        if (intersect(ship, ship2)) {
                            placed = false;
                        }
                    }
                }
            }

            try {
                addShipToGameState(ship);
                i++;
            }catch (ShipPlacementException e){
                ship.reset();
            }
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

    protected List<Point> findLegalEndPoints(Ship ship){
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

        //Checks to make sure no ships intersect during ship placement
        List<Point> result = new ArrayList<>();
        if(endPoints.size() > 0) {
            for (Point currentEndPoint : endPoints) {
                ship.setEnd(currentEndPoint);
                boolean intersects = false;
                for (Ship shipTest : ships) {
                    if (shipTest.getEnd() != null && !(shipTest.getShipType() == ship.getShipType())){
                        if (intersect(ship, shipTest)) {
                            intersects = true;
                        }
                    }
                }
                if (!intersects){
                    result.add(currentEndPoint);
                }
            }
        }
        ship.setEnd(null);
        return result;
    }

    private static boolean checkPlaceInBounds(Point place){
        return (place.x >= 0 && place.x < ROWS && place.y >= 0 && place.y < COLUMNS);
    }

    public boolean checkPlaceLegal(Point place){
        if(!checkPlaceInBounds(place)){
            return false;
        }else {
            for(Ship currentShip : ships){
                if(currentShip.getEnd() != null){
                    if(pointIntersectsShip(place, currentShip)){
                        return false;
                    }
                }
            }
            return true;
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

    /**
     * Checks for intersection of ship point
     * @param point the point on the board you want to check
     * @param ship1 The ship that your point may intersect
     * @return true = the ship does intersect with the point
     */
    boolean pointIntersectsShip(Point point, Ship ship1){
        //first ship is vertical
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

    public GameState getEnemyGameState(){
        return enemyGameState;
    }
    public void setTurn(boolean isMyTurn){
        this.isMyTurn = isMyTurn;
    }

    public void setOpponent(Player opponent){
        this.opponent = opponent;
    }

    public Results processGuess(int row, int column){
        isMyTurn = true;
        boolean hit = checkHitMiss(new Point(row, column));
        if (hit){
            gameState.setTile(Tile.HIT, row, column);
        }else{
            gameState.setTile(Tile.MISS, row, column);
        }
        ShipType sunkShip = null;
        for (Ship ship: ships){
            if (ship.checkForSunk()){
                //write to the log
                sunkShip = ship.getShipType();
            }
        }

        boolean opponentWon = checkForWin();
        if (opponentWon){
            disableBoard(viewManager.getGameScreen().getUserBoard());
            disableBoard(viewManager.getGameScreen().getEnemyBoard());
            //show end screen
        }else{
            enableBoard(enemyGameState,viewManager.getGameScreen().getEnemyBoard());
        }
        return new Results(new Point(row, column), hit, opponentWon, sunkShip);
    }

    public void processResults(Results results){
        if (results.isTileHit()){
            enemyGameState.setTile(Tile.HIT, results.getGuessedTile().x, results.getGuessedTile().y);
        }else{
            enemyGameState.setTile(Tile.MISS, results.getGuessedTile().x, results.getGuessedTile().y);
        }

        if (results.getSunkShip() != null){
            //write to the log
        }

        if (results.hasPlayerWon()){
            disableBoard(viewManager.getGameScreen().getUserBoard());
            disableBoard(viewManager.getGameScreen().getEnemyBoard());
            //show end screen
        }

        if(opponent != null && opponent instanceof ComputerPlayer && !results.hasPlayerWon()){
            ComputerPlayer computer = (ComputerPlayer) opponent;
            computer.playTurn();
        }

        if (viewManager != null){
            updateAllBoards();
        }

        isMyTurn = false;
    }

    public abstract Results makeGuess(int row, int column);
        //send the guess to the opponent
        //wait for response
    public abstract void sendResults(Results results);
        //send the results to the opponent

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

        public static List<Ship> getShips(Player player){
            return player.ships;
        }
    }

    public static void main(String[]args){
        MainMenuController menuController = new MainMenuController();
    }
}
