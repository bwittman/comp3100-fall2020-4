package battleship.controller;

/**
 * Exception thrown when something wrong happens in the ship placement phase
 */
public class ShipPlacementException extends Exception{
    public ShipPlacementException(String message){
        super(message);
    }
}
