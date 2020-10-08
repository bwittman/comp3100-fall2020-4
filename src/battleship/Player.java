package battleship;

import javax.swing.*;

public class Player {
    public static void main(String[]args){
        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600,600);

        BoardPrototype board = new BoardPrototype(new GameState());

        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
