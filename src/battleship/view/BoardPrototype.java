package battleship.view;

import java.awt.*;

import javax.swing.*;

import battleship.controller.Player;
import battleship.model.GameState;

public class BoardPrototype extends JPanel {
	
	protected static final int ROWS = 10;
	protected static final int COLUMNS = 10;
	private static final int BUTTON_SIDE = 60;
	private static final Color WATER_BLUE = new Color(16,129,160);
	private static final Icon MISS_ICON = new ImageIcon(((new ImageIcon("resources/blueX.png").getImage()
            .getScaledInstance(BUTTON_SIDE, BUTTON_SIDE,java.awt.Image.SCALE_SMOOTH))));
	private static final Icon HIT_ICON = new ImageIcon(((new ImageIcon("resources/redX.png").getImage()
			.getScaledInstance(BUTTON_SIDE, BUTTON_SIDE,java.awt.Image.SCALE_SMOOTH))));

	private GameState gameState;
	private JButton[][] buttonArray = new JButton[ROWS][COLUMNS];

	public BoardPrototype(GameState gameState) {
		this.gameState = gameState;
		this.setLayout(new BorderLayout());
		this.add(setUpButtons(), BorderLayout.CENTER);
		this.add(setUpPrototypeOptions(), BorderLayout.SOUTH);
	}

	private JPanel setUpPrototypeOptions(){
		JButton resetButton = new JButton("Reset Board");
		resetButton.addActionListener(e -> {
			gameState.reset();
			updateBoard();
		});

		JButton enableButton = new JButton("Enable Buttons");
		enableButton.addActionListener(e -> enableBoard());

		JButton disableButton = new JButton("Disable Board");
		disableButton.addActionListener(e -> disableBoard());

		JPanel panel = new JPanel(new GridLayout(1,3));
		panel.add(resetButton);
		panel.add(enableButton);
		panel.add(disableButton);

		return panel;
	}

	private JPanel setUpButtons(){
		JPanel panel = new JPanel(new GridLayout(ROWS,COLUMNS));

		for (int i = 0; i < ROWS; i++ ) {
			for (int j = 0; j < COLUMNS; j++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(BUTTON_SIDE, BUTTON_SIDE));

				button.addActionListener(e -> {
					//loop through out button array to find the location of the button which was clicked
					for (int row = 0; row < ROWS; row++) {
						for (int col = 0; col < COLUMNS; col++) {
							if (buttonArray[row][col] == e.getSource()){
								if(checkForHit()){
									gameState.setTile(Player.Tile.HIT, row, col);
								}else{
									gameState.setTile(Player.Tile.MISS, row, col);
								}
							}
						}
					}
					button.setEnabled(false);
					updateBoard();
				});//end listener

				buttonArray[i][j] = button;
				panel.add(button);
			}
		}
		updateBoard();
		return panel;
	}

	private boolean checkForHit() {
		return true;
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
