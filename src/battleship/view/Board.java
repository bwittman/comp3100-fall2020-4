package battleship.view;

import javax.swing.*;
import java.awt.*;

import battleship.controller.Player;

/**
 * Visual representation of the current state of a user or enemy's board
 */
public class Board extends JPanel {

    private static final int ROWS = Player.ROWS;
    private static final int COLUMNS = Player.COLUMNS;
    private static int buttonSize;

    private CoordinateButton[][] buttonArray = new CoordinateButton[ROWS][COLUMNS];



    public Board() {
        this.setLayout(new GridLayout(ROWS + 1 ,COLUMNS + 1));
        setUpLabels();
        setUpButtons();
    }

    /*
     * Sets up the labels along the top row, these are letters
     */
    private void setUpLabels(){
        JLabel first = new JLabel();
        this.add(first);//the top left corner has no visual label
        for (int i = 0; i < COLUMNS; ++i){
            JLabel label = new JLabel();
            label.setText(Character.toString((char) ( i + 'A')));
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);
        }
    }

    /*
     * Sets up the labels at the beginning of each row and adds all the buttons,
     * note that these labels are numbers
     * x is stored as columns, y is stored as rows
     */
    private void setUpButtons(){
        for (int i = 0; i < ROWS; i++ ) {
            JLabel label = new JLabel();
            label.setText(Integer.toString(i + 1));
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);
            for (int j = 0; j < COLUMNS; j++) {
                CoordinateButton button = new CoordinateButton(new Point(j,i));
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                buttonArray[i][j] = button;
                this.add(button);
            }
        }
    }

    public CoordinateButton getButton(int x, int y){
        return buttonArray[y][x];
    }

    public static void setButtonSize(int buttonSize){
        Board.buttonSize = buttonSize;
    }
}