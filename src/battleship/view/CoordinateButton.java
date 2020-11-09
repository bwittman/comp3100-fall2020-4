package battleship.view;

import javax.swing.*;
import java.awt.*;

/**
 * Buttons that know their location for use in the Board class
 */
public class CoordinateButton extends JButton {
    private Point location;

    public CoordinateButton(Point location){
        this.location = location;
    }

    public Point getLocation(){
        return location;
    }
}
