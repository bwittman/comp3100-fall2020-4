package battleship.controller;

import battleship.model.GameState;
import battleship.model.Results;
import battleship.model.Ship;
import battleship.view.*;
import battleship.model.Ship.ShipType;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

/**
 * Controls interactions between the view and the model of a user's game.
 * Game play functionality that is shared between all types of Players.
 */
public abstract class Player {
    public static final int ROWS = 10;
    public static final int COLUMNS = 10;
    private static final Object[] endOptions = {"Play Again", "Quit"};

    protected ImageIcon CARRIER_ICON = new ImageIcon(this.getClass().getResource("/carrier.png"));
    protected ImageIcon SUBMARINE_ICON = new ImageIcon(this.getClass().getResource("/submarine.png"));
    protected ImageIcon DESTROYER_ICON = new ImageIcon(this.getClass().getResource("/destroyer.png"));
    protected ImageIcon BATTLESHIP_ICON = new ImageIcon(this.getClass().getResource("/battleship.png"));
    protected ImageIcon CRUISER_ICON = new ImageIcon(this.getClass().getResource("/cruiser.png"));
    private ImageIcon WATER_ICON = new ImageIcon(this.getClass().getResource("/water.png"));
    protected ImageIcon LEGAL_ENDPOINT_ICON = new ImageIcon(this.getClass().getResource("/legalEndpoint.png"));
    private ImageIcon MISS_ICON = new ImageIcon(this.getClass().getResource("/blueX.png"));
    private ImageIcon HIT_ICON = new ImageIcon(this.getClass().getResource("/redX.png"));

    /**
     * The types of tiles that we have encounter
     */
    public enum Tile {
        CARRIER,
        BATTLESHIP,
        CRUISER,
        SUBMARINE,
        DESTROYER,
        HIT,
        MISS,
        WATER
    }

    protected List<Ship> ships = new ArrayList<>();
    private List<ShipType> previousShipsSunk = new ArrayList<>();
    private GameState gameState;
    private GameState enemyGameState;
    protected boolean isMyTurn;
    protected ViewManager viewManager;
    protected Player opponent = null;
    protected boolean opponentPlacedShips = false;
    protected boolean playerStarted = false;
    protected boolean opponentPlayAgain = false;

    protected Player(ViewManager viewManager) {
        this.viewManager = viewManager;
        createShips();
        initGameStates();
        if(viewManager != null){//the view manager will be null for a computerPlayer
            setupIcons(viewManager.getGameScreen().getButtonSize());
            setUpView();
            updateAllBoards();
        }
    }

    private void setupMenuBarActionListeners(){
        viewManager.getGameScreen().getRulesItem().addActionListener(e->{
            viewManager.getRulesWindow().setVisible(true);
        });

        viewManager.getGameScreen().getHowToPlayItem().addActionListener(e->{
            viewManager.getHowToPlayWindow().setVisible(true);
        });
    }

    /*
     * Scale the icons based on the size of the button
     */
    private void setupIcons(int buttonSize){
        MISS_ICON = new ImageIcon((MISS_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        HIT_ICON = new ImageIcon((HIT_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        WATER_ICON = new ImageIcon((WATER_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        LEGAL_ENDPOINT_ICON = new ImageIcon((LEGAL_ENDPOINT_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        CARRIER_ICON = new ImageIcon((CARRIER_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        SUBMARINE_ICON = new ImageIcon((SUBMARINE_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        DESTROYER_ICON = new ImageIcon((DESTROYER_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        BATTLESHIP_ICON = new ImageIcon((BATTLESHIP_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
        CRUISER_ICON = new ImageIcon((CRUISER_ICON.getImage()
                .getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
    }

    protected void playSound(String fileName)  {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }

    /**
     * Update both the user and enemy boards
     */
    protected void updateAllBoards(){
        updateBoard(enemyGameState, viewManager.getGameScreen().getEnemyBoard());
        updateBoard(gameState, viewManager.getGameScreen().getUserBoard());
    }

    /*
     * Create all the ships that a Player will have
     */
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

    /*
     * Initialize the game states
     */
    private void initGameStates(){
        gameState = new GameState();
        enemyGameState = new GameState();
    }

    /*
     * Set all the action listeners for the windows
     */
    private void setUpView(){
        setupMenuBarActionListeners();
        setEnemyActionListeners();
        setResetActionListener();
        setRandomActionListener();
        setPlayGameActionListener();
        disableBoard(viewManager.getGameScreen().getEnemyBoard());
    }

    /*
     * Set the action listener of all the buttons on the enemy board
     */
    private void setEnemyActionListeners(){
        Board board = viewManager.getGameScreen().getEnemyBoard();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                CoordinateButton button = board.getButton(j,i);
                button.addActionListener(e -> onEnemyButtonClicked(button));
            }
        }
    }

    /*
     * Set the action listener of the reset button for ship placement
     */
    private void setResetActionListener(){
        viewManager.getGameScreen().getResetButton().addActionListener(e ->{
            resetGame();
            enableBoard(gameState, viewManager.getGameScreen().getUserBoard());
            updateAllBoards();
            viewManager.getGameScreen().getPlayGameButton().setEnabled(false);
            logMessage("Game was reset.");
        });
    }

    /*
     * Generate and show a random ship placement during the ship placement phase
     */
    private void setRandomActionListener(){
        viewManager.getGameScreen().getRandomButton().addActionListener(e -> {
            resetGame();
            randomShipPlacement();
            updateAllBoards();
            viewManager.getGameScreen().getPlayGameButton().setEnabled(true);
            disableBoard(viewManager.getGameScreen().getUserBoard());
            Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
            while (shipButtons.hasMoreElements()) {
                AbstractButton shipButton = shipButtons.nextElement();
                shipButton.setEnabled(false);
            }
        });
    }

    private void setPlayGameActionListener(){
        viewManager.getGameScreen().getPlayGameButton().addActionListener(e -> {
            onPlayGameClicked();
        });
    }

    /*
     * Confirm the final ship placement of the user's board and then start the game
     */
    private void onPlayGameClicked(){
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you satisfied with this ship placement?", "Confirm Final Ship Placement", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            playerStarted = true;
            logMessage("=========== Battleship ===========");
            disableBoard(viewManager.getGameScreen().getUserBoard());
            if(this instanceof HumanPlayer && !isComputerGame()){
                ((HumanPlayer) this).getNetworking().sendMessage("LOG: Other player has placed ships.");
                ((HumanPlayer) this).getNetworking().sendMessage("START");//tell the other player we are ready to start
            }

            //disable ship buttons
            Enumeration<AbstractButton> shipButtons = viewManager.getGameScreen().getShipButtonGroup().getElements();
            while (shipButtons.hasMoreElements()) {
                AbstractButton shipButton = shipButtons.nextElement();
                shipButton.setEnabled(false);
            }

            viewManager.getGameScreen().getOptionButtons().setVisible(false);

            if (isMyTurn && opponentPlacedShips) {
                enableBoard(enemyGameState, viewManager.getGameScreen().getEnemyBoard());
            }
        }
    }

    /*
     * Initialize the action to be taken when an enemy button is clicked
     */
    private void onEnemyButtonClicked(CoordinateButton button){
        disableBoard(viewManager.getGameScreen().getEnemyBoard());
        Runnable worker = ()->makeGuess(button.getLocation().y, button.getLocation().x);//sending the enemy what our guess is
        new Thread(worker).start();
    }

    /*
     * Update the icons of the given board
     */
    private void updateBoard(GameState gameState, Board board) {
        for (int i =0; i<ROWS; i++ ){
            for(int j=0;j < COLUMNS; j++){
                Tile currentTile = gameState.getTile(j,i);
                JButton currentButton = board.getButton(j,i);
                switch(currentTile){
                    case WATER:
                        currentButton.setIcon(WATER_ICON);
                        currentButton.setDisabledIcon(WATER_ICON);
                        break;
                    case HIT:
                        currentButton.setIcon(HIT_ICON);
                        currentButton.setDisabledIcon(HIT_ICON);
                        break;
                    case MISS:
                        currentButton.setIcon(MISS_ICON);
                        currentButton.setDisabledIcon(MISS_ICON);
                        break;
                    case CARRIER:
                        currentButton.setIcon(CARRIER_ICON);
                        currentButton.setDisabledIcon(CARRIER_ICON);
                        break;
                    case BATTLESHIP:
                        currentButton.setIcon(BATTLESHIP_ICON);
                        currentButton.setDisabledIcon(BATTLESHIP_ICON);
                        break;
                    case CRUISER:
                        currentButton.setIcon(CRUISER_ICON);
                        currentButton.setDisabledIcon(CRUISER_ICON);
                        break;
                    case SUBMARINE:
                        currentButton.setIcon(SUBMARINE_ICON);
                        currentButton.setDisabledIcon(SUBMARINE_ICON);
                        break;
                    case DESTROYER:
                        currentButton.setIcon(DESTROYER_ICON);
                        currentButton.setDisabledIcon(DESTROYER_ICON);
                        break;
                }
            }
        }
    }

    /**
     * Enable the buttons of the board that are legal to click
     * @param gameState the corresponding game state to this board
     * @param board the board to be enabled
     */
    public void enableBoard(GameState gameState, Board board){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                JButton currentButton = board.getButton(j,i);
                if (gameState.getTile(j,i) == Tile.WATER){
                    currentButton.setEnabled(true);
                }else{
                    currentButton.setEnabled(false);
                }
            }
        }
    }

    /**
     * Disable all of the buttons on this board
     * @param board the board to be disabled
     */
    public void disableBoard(Board board){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                JButton currentButton = board.getButton(j,i);
                currentButton.setEnabled(false);
            }
        }
    }

    /**
     * Check if all the ships have been placed
     * @return if all the ships have been placed
     */
    protected boolean allShipsPlaced(){
        for (Ship ship: ships){
            if (ship.getStart() == null || ship.getEnd() == null){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a ship that has been placed to the game state.
     * Assumes that the start and end points have been checked to be in bounds already
     * @param ship the ships to be added
     * @throws ShipPlacementException if the start or end points are at the same point
     */
    protected void addShipToGameState(Ship ship) throws ShipPlacementException {
        Tile shipTile = null;
        if(ship.getName().equals("Carrier")){
            shipTile = Tile.CARRIER;
        }else if(ship.getName().equals("Battleship")){
            shipTile = Tile.BATTLESHIP;
        }else if(ship.getName().equals("Cruiser")){
            shipTile = Tile.CRUISER;
        }else if(ship.getName().equals("Submarine")){
            shipTile = Tile.SUBMARINE;
        }else{
            shipTile =Tile.DESTROYER;
        }
        if (ship.getLength() == 2){
            gameState.setTile(shipTile, ship.getStart().x, ship.getStart().y);
            gameState.setTile(shipTile, ship.getEnd().x, ship.getEnd().y);
        //then it is horizontal
        }else if(ship.getStart().x - ship.getEnd().x == 0){
            if (ship.getStart().y < ship.getEnd().y){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(shipTile, ship.getStart().x, ship.getStart().y+i);
                }
            }else if (ship.getStart().y > ship.getEnd().y){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(shipTile, ship.getStart().x, ship.getStart().y-i);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        //then it is vertical
        }else{
            if (ship.getStart().x < ship.getEnd().x){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(shipTile, ship.getStart().x+i, ship.getStart().y);
                }
            }else if (ship.getStart().x > ship.getEnd().x){
                for (int i = 0; i < ship.getLength(); i++){
                    gameState.setTile(shipTile, ship.getStart().x-i, ship.getStart().y);
                }
            }else {
                throw new ShipPlacementException("Start and End were the same position");
            }
        }
    }

    /**
     * Generate a random placement of ships
     */
    public void randomShipPlacement(){
        Random random = new Random();

        for(int i =0; i < ships.size();){
            Ship ship = ships.get(i);
            //keep going until we have a legal starting point and at least one legal ending point
            List<Point> legalEndPoints;
            boolean placed = false;
            while(!placed){
                Point start = new Point();
                do {
                    start.x = random.nextInt(COLUMNS);
                    start.y = random.nextInt(ROWS);
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
        logMessage("Ships were placed randomly.");
    }

    /**
     * Check if two ships intersect each other.
     * There are four cases that mirror each other,
     * the difference is if we loop from start to end or end to start for each ship
     * depending on if the start is bigger than the end or vice versa.
     */
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

    /**
     * Find all of the legal endpoints for a ship that has its starting point set
     * @param ship the ship being placed
     * @return the legal endpoints
     */
    protected List<Point> findLegalEndPoints(Ship ship){
        List<Point> endPoints = new ArrayList<>();
        Point start = ship.getStart();

        int length = ship.getLength();

        //First check if each potential endpoint would be illegal
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

        //check to make sure that each endpoint would not make the ship
        //intersect with another already placed ship
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

    /**
     * Check if the point is in the bounds of the board
     */
    private static boolean checkPlaceInBounds(Point place){
        return (place.x >= 0 && place.x < COLUMNS && place.y >= 0 && place.y < ROWS);
    }

    /**
     * Checks if this tile is a legal tile to place ships on
     * @param place the point to be checked
     * @return if this tile is a legal tile to place a ship on
     */
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

    /**
     * Check if the guessed tile hits a ship
     * @param tile the guessed tile
     * @return if tile hit a ship
     */
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
     * Checks if this point intersects this ship
     * @param point the point on the board you want to check
     * @param ship1 The ship that your point may intersect
     * @return if the ship does intersect with the point
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

    /**
     * Check if this Player has won the game
     * @return if this Player has won the game
     */
    public boolean checkForWin(){
        for (Ship ship: ships){
            if (!ship.checkForSunk()){
                return false;
            }
        }
        return true;
    }

    /**
     * Reset the game
     */
    public void resetGame(){
        gameState.reset();
        enemyGameState.reset();
        previousShipsSunk.clear();
        for (Ship ship: ships){
            ship.reset();
        }

        if (viewManager != null){
            viewManager.getMainMenu().reset();
        }
    }

    /**
     * Determines if a game is a computer game
     * @return if it computer game
     */
    public boolean isComputerGame(){
        return opponent != null;
    }

    /**
     * Processes enemy guess and returns the results of the guess
     * @param row row coordinate
     * @param column column coordinate
     * @return Results object to be processed by enemy
     */
    public Results processGuess(int row, int column){

        boolean hit = checkHitMiss(new Point(column, row));
        if (hit){
            gameState.setTile(Tile.HIT, column, row);
            logMessage("Enemy HIT: " + (char)(column+'A') + (row + 1));
        }else{
            gameState.setTile(Tile.MISS, column, row);
            logMessage("Enemy MISSED: " + (char)(column+'A') + (row + 1));
        }

        ShipType sunkShip = null;
        for (Ship ship: ships){
            if (ship.checkForSunk()){
                if(!previousShipsSunk.contains(ship.getShipType())){
                    sunkShip = ship.getShipType();
                    previousShipsSunk.add(sunkShip);
                }
            }
        }
        if(sunkShip != null) {
            logMessage("My " + sunkShip.name() + " was sunk!");
        }

        boolean opponentWon = checkForWin();
        if (viewManager != null) {
            updateAllBoards();
            enableBoard(getEnemyGameState(), viewManager.getGameScreen().getEnemyBoard());
            if (opponentWon) {
                SwingWorker <Void, Void> swingWorker = new SwingWorker <Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        opponentWon();
                        return null;
                    }
                };
                swingWorker.execute();
            }
        }

        return new Results(new Point(column, row), hit, opponentWon, sunkShip);
    }

    private void opponentWon(){
        disableBoard(viewManager.getGameScreen().getEnemyBoard());
        logMessage("Enemy has won!");
        int endDecision = JOptionPane.showOptionDialog(null,"You lost! Play again?", "Opponent won",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, endOptions, endOptions[0]);

        resetGame();
        if (isComputerGame()) {
            opponent.resetGame();
        }

        updateAllBoards();

        if (opponentPlayAgain || isComputerGame()){
            enableBoard(gameState, viewManager.getGameScreen().getUserBoard());
        }

        viewManager.getGameScreen().getOptionButtons().setVisible(true);
        opponentPlacedShips = false;
        playerStarted = false;

        if(endDecision == 0){
            if (isComputerGame()) {
                ComputerPlayer computer = (ComputerPlayer) opponent;
                computer.placeComputerShips();
            }else{
                ((HumanPlayer)this).getNetworking().sendMessage("PLAY AGAIN");
            }
        }else{
            viewManager.getGameScreen().setVisible(false);
            viewManager.getMainMenu().setVisible(true);
            if (isComputerGame()) {
                opponent.setOpponent(null);
                opponent = null;
            }
        }
    }

    /**
     * Processes results received from enemy about our guess
     * @param results the results of this Player's guess
     */
    public void processResults(Results results){
        if (results.isTileHit()){
            enemyGameState.setTile(Tile.HIT, results.getGuessedTile().x, results.getGuessedTile().y);
            String logMessage = "You HIT: " + (char)(results.getGuessedTile().x + 'A') + (results.getGuessedTile().y + 1);
            logMessage(logMessage);
        }else{
            enemyGameState.setTile(Tile.MISS, results.getGuessedTile().x, results.getGuessedTile().y);
            String logMessage = "You MISSED: " + (char)(results.getGuessedTile().x + 'A') + (results.getGuessedTile().y + 1);
            logMessage(logMessage);
        }

        if (results.getSunkShip() != null){
                logMessage("You sunk a " + results.getSunkShip().name() + "!");
        }

        if (viewManager != null){
            updateAllBoards();
            if (results.hasPlayerWon()){
                playerWon();
            }
        }

        //play the computers turn now only if we didn't just win
        if(isComputerGame() && opponent instanceof ComputerPlayer && !results.hasPlayerWon()){
            ComputerPlayer computer = (ComputerPlayer) opponent;
            computer.playTurn();
        }
    }

    private void playerWon(){
        disableBoard(viewManager.getGameScreen().getEnemyBoard());
        logMessage("You have won!");
        int endDecision = JOptionPane.showOptionDialog(null,"You win! Play Again?", "Player Won",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, endOptions, endOptions[0]);

        resetGame();
        if (isComputerGame()) {
            opponent.resetGame();
        }

        updateAllBoards();

        if (opponentPlayAgain || isComputerGame()){
            enableBoard(gameState, viewManager.getGameScreen().getUserBoard());
        }

        viewManager.getGameScreen().getOptionButtons().setVisible(true);
        opponentPlacedShips = false;
        playerStarted = false;

        if(endDecision == 0){
            if (isComputerGame()) {
                ComputerPlayer computer = (ComputerPlayer) opponent;
                computer.placeComputerShips();
            }else{
                ((HumanPlayer)this).getNetworking().sendMessage("PLAY AGAIN");
            }
        }else {
            viewManager.getGameScreen().setVisible(false);
            viewManager.getMainMenu().setVisible(true);
            if (isComputerGame()) {
                opponent.setOpponent(null);
                opponent = null;
            }else{
                ((HumanPlayer) this).getNetworking().sendMessage("QUITS");
            }
        }
    }

    /**
     * Write a message to the log
     * @param message the message to be written
     */
    protected void logMessage(String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(viewManager != null) {
                    JTextArea log = viewManager.getGameScreen().getLog();
                    log.append(message + "\n");
                }
            }
        });
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

    //abstract methods to be implemented
    public abstract void makeGuess(int row, int column);

    /**
     * Testing class
     */
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
        new MainMenuController();
    }
}