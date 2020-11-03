package battleship.model;

import battleship.model.Ship.ShipType;

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
}
