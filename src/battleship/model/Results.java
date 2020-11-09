package battleship.model;

import battleship.model.Ship.ShipType;

import java.awt.*;

/**
 * Stores the results of a tile being guessed
 */
public class Results {

    private Point guessedTile;
    private boolean tileHit;
    private boolean playerWon;
    private ShipType sunkShip;

    /**
     * Constructor for creating an object to send to the opponent
     * @param guessedTile the tile that was guessed
     * @param tileHit if the tile was a hit or miss
     * @param playerWon if the player who guessed has won
     * @param sunkShip which ship wa sunk if any
     */
    public Results(Point guessedTile, boolean tileHit, boolean playerWon, ShipType sunkShip){
        this.guessedTile = guessedTile;
        this.tileHit = tileHit;
        this.playerWon = playerWon;
        this.sunkShip = sunkShip;
    }

    /**
     * Constructor for creating an object from a message received via networking
     * @param string the string representation of this object received via networking
     */
    public Results (String string){
        String[] parts = string.split(" ");
        this.guessedTile = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        this.tileHit = Boolean.parseBoolean(parts[2]);
        this.playerWon = Boolean.parseBoolean(parts[3]);
        if(parts.length > 4){
            for(ShipType ship : ShipType.values()){
                if(ship.name().equals(parts[4])){
                    this.sunkShip = ship;
                }
            }
        }
    }

    public Point getGuessedTile() {
        return guessedTile;
    }

    public boolean isTileHit() {
        return tileHit;
    }

    public boolean hasPlayerWon() {
        return playerWon;
    }

    public ShipType getSunkShip() {
        return sunkShip;
    }

    @Override
    public String toString(){
        if(sunkShip != null) {
            return guessedTile.x + " " + guessedTile.y + " " + tileHit + " " + playerWon + " " + sunkShip.name();
        }else{
            return guessedTile.x + " " + guessedTile.y + " " + tileHit + " " + playerWon;
        }
    }
}
