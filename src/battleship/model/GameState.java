package battleship.model;

import battleship.controller.Player;
import battleship.view.Board;

public class GameState {

	private Player.Tile[][] board;
	private static final int ROWS = Player.ROWS;
	private static final int COLUMNS = Player.COLUMNS;

	public GameState() {
		board = new Player.Tile[10][10];
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLUMNS; j++) {
				board[i][j] = Player.Tile.WATER;
			}
		}		
	}
	
	public Player.Tile getTile (int x, int y) {
		return board[x][y];
	}
	
	public void setTile (Player.Tile tile, int x, int y) {
		board[x][y] = tile;
	}

	public void reset(){
		for(int i = 0; i< ROWS; i++){
			for (int j = 0; j< COLUMNS; j++){
				board[i][j] = Player.Tile.WATER;
			}
		}
	}
}