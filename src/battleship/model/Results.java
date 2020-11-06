package battleship.model;

import battleship.model.Ship.ShipType;
import com.sun.org.apache.xerces.internal.xs.StringList;

import java.awt.*;

public class Results {
    private Point guessedTile;
    private boolean tileHit;
    private boolean playerWon;
    private ShipType sunkShip;

    public Results(Point guessedTile, boolean tileHit, boolean playerWon, ShipType sunkShip){
        this.guessedTile = guessedTile;
        this.tileHit = tileHit;
        this.playerWon = playerWon;
        this.sunkShip = sunkShip;
    }

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
            return guessedTile.y + " " + guessedTile.x + " " + tileHit + " " + playerWon + " " + sunkShip.name();
        }else{
            return guessedTile.y + " " + guessedTile.x + " " + tileHit + " " + playerWon;
        }
    }
}
