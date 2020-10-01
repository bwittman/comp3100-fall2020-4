package battleship;

import java.awt.GridLayout;

import javax.swing.*;

public class GameBoardPrototype extends JFrame {
	
	private static final int ROWS = 10;
	private static final int COLUMNS = 10;
	private GameState gameState;
	private JButton [] [] buttonArray = new JButton[ROWS][COLUMNS];


	public GameBoardPrototype(GameState gameState) {

		this.gameState = gameState;
		this.setTitle("Battleship");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(ROWS,COLUMNS));
		this.setSize(700,700);

		for (int i =0; i<ROWS; i++ ) {
			for (int j = 0; j < COLUMNS; j++) {
				JButton button = new JButton(new ImageIcon ("water.png"));
				buttonArray [i][j]=button;
				panel.add(button);
			}
		}
		this.add(panel);
		pack();
		setVisible(true);
	}

	private boolean checkForHit(){
		return false;
	}
	public void updateBoard() {
		for (int i =0; i<ROWS; i++ ){
			for(int j=0;j < COLUMNS; j++){
				Tile current = gameState.getTile(i,j);
				switch(current){
					case WATER:
						buttonArray[i][j].setIcon(new ImageIcon("water.png"));
						break;
					case HIT:
						buttonArray[i][j].setIcon(new ImageIcon("hit.png"));
						break;
					case MISS:
						buttonArray[i][j].setIcon(new ImageIcon("miss.png"));
						break;

				}
			}
		}
	}
	public static void main(String[] args) {
		GameState gameState = new GameState();
		new GameBoardPrototype(gameState);

	}

}
