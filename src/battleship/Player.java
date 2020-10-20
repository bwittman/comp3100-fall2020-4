package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    protected enum Tile {
        SHIP,
        HIT,
        MISS,
        WATER
    }

    private String playerName;
    private List<Ship> ships = new ArrayList<>();
    private GameState myGameState;
    private GameState enemyGameState;
    private boolean isMyTurn;
    private MainWindow mainWindowView;

    protected void createShips(){
        Ship destroyer = new Ship(Ship.ShipType.DESTROYER);
        Ship submarine = new Ship(Ship.ShipType.SUBMARINE);
        Ship cruiser = new Ship(Ship.ShipType.CRUISER);
        Ship battleship = new Ship(Ship.ShipType.BATTLESHIP);
        Ship carrier = new Ship(Ship.ShipType.CARRIER);
        ships.add(destroyer);
        ships.add(submarine);
        ships.add(cruiser);
        ships.add(battleship);
        ships.add(carrier);
    }

    public void randomShipPlacement(){

    }

    public boolean checkPlaceLegal(Point place){
        return false;
    }

    public void resetStoredShips(){
        myGameState.reset();
    }

    public void resetGame(){
        myGameState.reset();
        enemyGameState.reset();
        for (Ship ship: ships){
            ship.reset();
        }
        //clear log
    }

    public boolean checkHitMiss(Point tile){
        return false;
    }

    public boolean checkForWin(){
        for (Ship ship: ships){
            if (!ship.checkForSunk()){
                return false;
            }
        }
        return true;
    }
    
    /*
     * TODO: Might need to alter player class to be able to instantiate
    public Player() {
    	mainWindowView = new MainWindow();
    	//TODO: Instantiate Models
    }
    */
    

	public void initPlayer(MainWindow newMainWindowView) {
		mainWindowView = newMainWindowView;
		mainWindowView.getNetworkButton().addActionListener(e->{
        	Object[] options = {"Host", "Join"};
        	int response = JOptionPane.showOptionDialog(null, "Will you Host or Join?", "Host or Join?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        	System.out.println("Player responded: " + response);
        	if(response == 0) { //User Selected Host
        		System.out.println("User Selected Host");
        		JPanel hostPanel = new JPanel(new GridLayout(4,1));
        		hostPanel.setLayout(new BorderLayout());
        		String myAddress = Networking.getLocalIP();
        		JLabel localIP = new JLabel("Your IP Address is: " + myAddress);
        		hostPanel.add(localIP);
        		hostPanel.add(hostPanel, BorderLayout.CENTER);	 //TODO: asked dr. wittman about this and how to draw over the current JPanel
        		
        	}else if(response == 1) {				//User Selected Join 
        		System.out.println("User Selected Join");
        	}
        });
		
		mainWindowView.getRulesButton().addActionListener(e->{
        	Desktop myDesktop = Desktop.getDesktop();
        	if(Desktop.isDesktopSupported()) {
        		try {
                	File rules = new File("src\\pictures\\rules.txt");
                	myDesktop.open(rules);
        		}catch(IOException ex){
        			//TODO: warning window No application registered for opening .txt files
        			System.err.println("No application registered for opening .txt files");
        		}
        	}
        });
    	
    }

    public abstract void placeShips();
    public abstract void guess();
    public abstract void sendMessage();
    public abstract void receiveMessage();

    public static void main(String[]args){
    	
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(625,625);
        frame.setMinimumSize(new Dimension(625,625));
        frame.setResizable(false);
        

        //BoardPrototype board = new BoardPrototype(new GameState());
        MainWindow board = new MainWindow();
        

        
        /*
        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        */
        
    }

}
