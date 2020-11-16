package battleship.view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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

        JTextPane rulesText = new JTextPane();
        rulesText.setEditable(false);
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
            StyledDocument style  = rulesText.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            style.setParagraphAttributes(0, style.getLength(), center, false);
            add(rulesText, BorderLayout.CENTER);
            rulesText.setBorder(BorderFactory.createEmptyBorder(20, 20,20,20));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public JButton getCloseButton(){
        return closeButton;
    }
}
