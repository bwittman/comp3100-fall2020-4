package battleship.view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class HowToPlayWindow extends JFrame{


    public HowToPlayWindow(){

        JTextPane howToPlayText = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(howToPlayText);
        howToPlayText.setEditable(false);
        getContentPane().add( scrollPane );

        setTitle("Battleship: How to Play");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);

        try {
            //read in the how to play from the text file
            InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/howToPlay.txt"));
            BufferedReader buffer = new BufferedReader(reader);
            howToPlayText.read(buffer, null);
            StyledDocument style  = howToPlayText.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
            style.setParagraphAttributes(0, style.getLength(), center, false);
            add(scrollPane, BorderLayout.CENTER);
            howToPlayText.setBorder(BorderFactory.createEmptyBorder(20, 20,20,20));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}

