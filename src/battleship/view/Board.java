package battleship.view;

import javax.swing.*;
import java.awt.*;

import battleship.controller.Player;

public class Board extends JPanel {

    private static final int ROWS = 10;
    private static final int COLUMNS = 10;
    private static final int BUTTON_SIZE = 30;

    private JButton[][] buttonArray = new JButton[ROWS][COLUMNS];
    private Player player;

    public Board() {
        this.setLayout(new GridLayout(ROWS + 1,COLUMNS + 1));
        setUpLabels();
        setUpButtons();
    }

    public JButton getButton(int i, int j){
        return buttonArray[i][j];
    }

    private void setUpLabels(){
        for (int i = 0; i < COLUMNS; ++i){
            JLabel label = new JLabel();
            label.setText(Character.toString((char) i + (int) 'A'));
            this.add(label);
        }
    }

    private void setUpButtons(){
        for (int i = 0; i < ROWS; i++ ) {
            JLabel label = new JLabel();
            label.setText(Integer.toString(i + 1));
            this.add(label);
            for (int j = 0; j < COLUMNS; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                buttonArray[i][j] = button;
                this.add(button);
            }
        }
    }
}