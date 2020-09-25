package battleship;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameBoardPrototype extends JFrame {
	
	private static final int rowCount = 10;
	private static final int columnCount = 10;

	
	public GameBoardPrototype() {
		//covid sucks
		this.setTitle("Battleship");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(rowCount,columnCount));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
