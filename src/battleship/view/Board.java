package battleship.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import battleship.model.GameState;
import battleship.controller.Player;
import battleship.controller.Player.Tile;

public class Board extends JPanel {

    private static final int ROWS = 10;
    private static final int COLUMNS = 10;
    private static final int BUTTON_SIDE = 50;

    private GameState gameState;
    private JButton[][] buttonArray = new JButton[ROWS][COLUMNS];
    private Player player;

    public Board(GameState gameState) {
        this.gameState = gameState;
        this.setLayout(new GridLayout(ROWS,COLUMNS));
        setUpButtons();
    }

    public JButton getButton(int i, int j){
        return buttonArray[i][j];
    }

    private void setUpButtons(){
        for (int i = 0; i < ROWS; i++ ) {
            for (int j = 0; j < COLUMNS; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIDE, BUTTON_SIDE));
                buttonArray[i][j] = button;
                this.add(button);
            }
        }
    }
}