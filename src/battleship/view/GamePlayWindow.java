package battleship.view;

import javax.swing.*;

public class GamePlayWindow extends JFrame {

    private Board userBoard;
    private Board enemyBoard;
    private JPanel log;
    private JPanel key;
    private JPanel options;

    public GamePlayWindow(){
        userBoard = new Board();
        enemyBoard = new Board();

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //might change later


    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public void writeToLog(){
    }

    public void placeShips(){
    }

    private void selectStart(){
    }

    private void selectEnd(){
    }

    private void placeAShip(){
    }


}
