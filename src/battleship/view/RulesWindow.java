package battleship.view;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RulesWindow extends JFrame {

    JButton closeButton;

    public RulesWindow(){
        JTextArea rulesText = new JTextArea();
        closeButton = new JButton("Close");

        add(closeButton, BorderLayout.SOUTH);

        setTitle("Battleship: Rules");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);

        try {
            FileReader reader = new FileReader(new File("resources/rules.txt"));
            BufferedReader buffer = new BufferedReader(reader);

            rulesText.read(buffer, null);

            add(rulesText, BorderLayout.CENTER);

        }catch(Exception e){

        }
    }

    public JButton getCloseButton(){
        return closeButton;
    }

}
