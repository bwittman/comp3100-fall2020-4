package battleship.view;

import javax.swing.*;
import java.awt.*;

import battleship.controller.Player;

public class Board extends JPanel {

    private static final int ROWS = Player.ROWS;
    private static final int COLUMNS = Player.COLUMNS;
    private static final int BUTTON_SIZE = Toolkit.getDefaultToolkit().getScreenResolution() / 4;
    ;

    private CoordinateButton[][] buttonArray = new CoordinateButton[ROWS][COLUMNS];

    public Board() {
        this.setLayout(new GridLayout(ROWS + 1 ,COLUMNS + 1));
        setUpLabels();
        setUpButtons();
    }

    public CoordinateButton getButton(int i, int j){
        return buttonArray[i][j];
    }

    private void setUpLabels(){
        JLabel first = new JLabel();
        this.add(first);
        for (int i = 0; i < COLUMNS; ++i){
            JLabel label = new JLabel();
            label.setText(Character.toString((char) ( i + 'A')));
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);
        }
    }


    private void setUpButtons(){
        for (int i = 0; i < ROWS; i++ ) {
            JLabel label = new JLabel();
            label.setText(Integer.toString(i + 1));
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);
            for (int j = 0; j < COLUMNS; j++) {
                CoordinateButton button = new CoordinateButton(new Point(i,j));
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                buttonArray[i][j] = button;
                this.add(button);
            }
        }
    }
}