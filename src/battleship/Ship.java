package battleship;

import java.awt.*;

public class Ship {

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

        public String getName(){
            return name;
        }

        public int getLength(){
            return length;
        }
    }

    private String name;
    private int length;
    private int hits;
    private Point start;
    private Point end;

    public Ship(ShipType shipType) {
        this.name = shipType.getName();
        this.length = shipType.getLength();
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
        return length;
    }

    public void updateHits() {
        hits++;
    }

    public String getName() {
        return name;
    }

    public void reset(){
        hits = 0;
        end = null;
        start = null;
    }

    public boolean checkForSunk(){
        return hits == length;
    }

    //for testing only
    public boolean equals(Ship ship){
        return this.hits == ship.hits && this.name.equals(ship.name) && this.length == ship.length &&
                ((this.start == null && ship.start == null) || this.start.equals(ship.start)) &&
                ((this.end == null && ship.end == null) || this.end.equals(ship.end));
    }
}
