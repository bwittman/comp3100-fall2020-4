package battleship.view;

import javax.swing.*;
import java.awt.*;

public class GamePlayWindow extends JFrame {

    private Board userBoard;
    private Board enemyBoard;
    private JPanel log;
    private JPanel key;
    private JPanel options;

    public GamePlayWindow(){
        userBoard = new Board();
        enemyBoard = new Board();

        setTitle("Battleship: Main Menu");
        setSize(625,625);
        setMinimumSize(new Dimension(625,625));
        setResizable(true);
        setVisible(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //might change later

        add(userBoard, BorderLayout.NORTH);
        add(enemyBoard, BorderLayout.SOUTH);
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
