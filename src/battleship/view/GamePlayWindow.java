package battleship.view;

import battleship.controller.Player;

import javax.swing.*;

public class GamePlayWindow extends JFrame {

    Board userBoard;
    Board enemyBoard;

    public GamePlayWindow(){

    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

}
