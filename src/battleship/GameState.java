package battleship;


public class GameState {

	private Tile [][] board;
	
	
	public GameState() {
		board = new Tile[10][10];
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j] = Tile.WATER;
			}
		}		
	}
	
	public Tile getTile (int x, int y) {
		return board[x][y];
	}
	
	public void setTile (Tile tile, int x, int y) {
		board[x][y] = tile;
	}


}
