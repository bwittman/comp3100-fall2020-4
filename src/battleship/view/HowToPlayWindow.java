package battleship.view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HowToPlayWindow extends JFrame{
    JButton closeButton;

    public HowToPlayWindow(){

        JTextPane howToPlayText = new JTextPane();
        howToPlayText.setEditable(false);
        closeButton = new JButton("Close");

        add(closeButton, BorderLayout.SOUTH);

        setTitle("Battleship: How to Play");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);

        try {
            //read in the how to play from the text file
            FileReader reader = new FileReader(new File(this.getClass().getResource("/howToPlay.txt").toURI()));
            BufferedReader buffer = new BufferedReader(reader);
            howToPlayText.read(buffer, null);
            StyledDocument style  = howToPlayText.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            style.setParagraphAttributes(0, style.getLength(), center, false);
            add(howToPlayText, BorderLayout.CENTER);
            howToPlayText.setBorder(BorderFactory.createEmptyBorder(20, 20,20,20));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public JButton getCloseButton(){
        return closeButton;
    }
}

