package battleship.view;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class RulesWindow extends JFrame {


    public RulesWindow(){
        JTextArea rulesText = new JTextArea();
        JButton closeButton = new JButton("Close");

        add(closeButton, BorderLayout.SOUTH);

        setTitle("Battleship: Rules");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            FileReader reader = new FileReader(new File("resources/rules.txt"));
            BufferedReader buffer = new BufferedReader(reader);

            rulesText.read(buffer, null);

            add(rulesText, BorderLayout.CENTER);
            setVisible(true);

        }catch(Exception e){

        }
    }

}
