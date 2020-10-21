package battleship.view;

import javax.swing.*;

public class GamePlayWindow extends JFrame {

    Board userBoard;
    Board enemyBoard;

    public GamePlayWindow(){
        userBoard = new Board();
        enemyBoard = new Board();
    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

}
