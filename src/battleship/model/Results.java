package battleship.model;

import battleship.model.Ship.ShipType;

public class Results {
    private boolean tileHit;
    private boolean playerWon;
    private ShipType sunkShip;

    public Results(boolean tileHit, boolean playerWon, ShipType sunkShip){
        this.tileHit = tileHit;
        this.playerWon = playerWon;
        this.sunkShip = sunkShip;
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
