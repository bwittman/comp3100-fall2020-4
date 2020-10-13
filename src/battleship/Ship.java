package battleship;

import java.awt.*;

public class Ship {

    private String name;
    private int length;
    private int hits;
    private Point start;
    private Point end;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
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

    public void setLength(int length) {
        this.length = length;
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
}
