package battleship.model;

import java.awt.*;

/**
 * Represents a ship, its placement, and how many hits it has received
 */
public class Ship {

    /**
     * The types of ships a player may have. Each player has one of each.
     */
    public enum ShipType{
        DESTROYER("Destroyer", 2),
        SUBMARINE("Submarine", 3),
        CRUISER("Cruiser", 3),
        BATTLESHIP("Battleship", 4),
        CARRIER("Carrier", 5);

        private String name;
        private int length;

        private ShipType(String name, int length){
            this.name = name;
            this.length = length;
        }
    }

    private ShipType shipType;
    private int hits;
    private Point start;
    private Point end;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        this.hits = 0;
    }

    public void setEnd(Point end){
        this.end = end;
    }

    public Point getEnd(){
        return end;
    }

    public void setStart(Point start){
        this.start = start;
    }

    public Point getStart(){
        return start;
    }

    public int getLength() {
        return shipType.length;
    }

    public void updateHits() {
        hits++;
    }

    public String getName() {
        return shipType.name;
    }

    public ShipType getShipType(){
        return shipType;
    }

    public void reset(){
        hits = 0;
        end = null;
        start = null;
    }

    /**
     * Check if this ship is sunk
     * @return if this ship is sunk
     */
    public boolean checkForSunk(){
        return hits == shipType.length;
    }

    //for testing only
    public boolean equals(Ship ship){
        return this.hits == ship.hits && this.shipType.name.equals(ship.shipType.name) && this.shipType.length == ship.shipType.length &&
                ((this.start == null && ship.start == null) || this.start.equals(ship.start)) &&
                ((this.end == null && ship.end == null) || this.end.equals(ship.end));
    }
}
