package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Board extends JPanel {

    protected static final int ROWS = 10;
    protected static final int COLUMNS = 10;
    private static final int BUTTON_SIDE = 50;
    private static final Color WATER_BLUE = new Color(16,129,160);
    private static final Icon MISS_ICON = new ImageIcon(((new ImageIcon("src\\pictures\\blueX.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE,java.awt.Image.SCALE_SMOOTH))));
    private static final Icon HIT_ICON = new ImageIcon(((new ImageIcon("src\\pictures\\redX.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE,java.awt.Image.SCALE_SMOOTH))));

    private GameState gameState;
    private JButton[][] buttonArray = new JButton[ROWS][COLUMNS];
    private Player player;

    public Board(GameState gameState) {
        this.gameState = gameState;
        this.setLayout(new GridLayout(ROWS,COLUMNS));
        setUpButtons();
    }

    private void setUpButtons(){
        for (int i = 0; i < ROWS; i++ ) {
            for (int j = 0; j < COLUMNS; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIDE, BUTTON_SIDE));

                button.addActionListener(e -> onButtonClicked(e, button));

                buttonArray[i][j] = button;
                this.add(button);
            }
        }
        updateBoard();
    }

    private void onButtonClicked(ActionEvent e, JButton button){
        //loop through out button array to find the location of the button which was clicked
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (buttonArray[row][col] == e.getSource()){
                    if(checkHitMiss(new Point(row,col))){
                        gameState.setTile(Player.Tile.HIT, row, col);
                    }else{
                        gameState.setTile(Player.Tile.MISS, row, col);
                    }
                }
            }
        }
        button.setEnabled(false);
        updateBoard();
    }

    private boolean checkHitMiss(Point tile) {
        return player.checkHitMiss(tile);
    }

    public void updateBoard() {
        for (int i =0; i<ROWS; i++ ){
            for(int j=0;j < COLUMNS; j++){
                Player.Tile current = gameState.getTile(i,j);
                switch(current){
                    case WATER:
                        buttonArray[i][j].setIcon(null);
                        buttonArray[i][j].setBackground(WATER_BLUE);
                        break;
                    case HIT:
                        buttonArray[i][j].setIcon(HIT_ICON);
                        buttonArray[i][j].setDisabledIcon(HIT_ICON);
                        break;
                    case MISS:
                        buttonArray[i][j].setIcon(MISS_ICON);
                        buttonArray[i][j].setDisabledIcon(MISS_ICON);
                        break;
                }
                enableBoard();
            }
        }
    }

    public void disableBoard(){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                buttonArray[i][j].setEnabled(false);
            }
        }
    }

    //may need to update this logic to include ships and sunk ships
    public void enableBoard(){
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLUMNS; j++){
                if (!(gameState.getTile(i,j) == Player.Tile.HIT || gameState.getTile(i,j) == Player.Tile.MISS)){
                    buttonArray[i][j].setEnabled(true);
                }
            }
        }
    }
}