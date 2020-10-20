package battleship;

public class GameState {

	private Player.Tile [][] board;

	public GameState() {
		board = new Player.Tile[10][10];
		
		for(int i = 0; i < Board.ROWS; i++) {
			for(int j = 0; j < Board.COLUMNS; j++) {
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
		for(int i = 0; i< Board.ROWS; i++){
			for (int j = 0; j< Board.COLUMNS; j++){
				board[i][j] = Player.Tile.WATER;
			}
		}
	}
}