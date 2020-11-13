package battleship.view;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Frame for displaying the background and rules of the game
 */
public class RulesWindow extends JFrame {

    JButton closeButton;

    public RulesWindow(){
        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        closeButton = new JButton("Close");

        add(closeButton, BorderLayout.SOUTH);

        setTitle("Battleship: Rules");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);

        try {
            //read in the rules from the text file
            FileReader reader = new FileReader(new File(this.getClass().getResource("/rules.txt").toURI()));
            BufferedReader buffer = new BufferedReader(reader);

            rulesText.read(buffer, null);
            add(rulesText, BorderLayout.CENTER);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public JButton getCloseButton(){
        return closeButton;
    }
}
