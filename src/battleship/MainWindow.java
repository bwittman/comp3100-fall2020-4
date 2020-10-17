package battleship;

import javax.swing.*;



import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JPanel {
    private ImageIcon background = new ImageIcon("src\\pictures\\mainWindow.png");

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, null);
    }

    public MainWindow() {


        JPanel panel = new JPanel(new GridLayout(1,3));

        setLayout(new BorderLayout());

        JButton rulesButton = new JButton("Rules");
        JButton networkButton = new JButton("Networking");
        JButton onePlayerButton = new JButton("Play Against Computer");
        
        rulesButton.addActionListener(e->{
        	Desktop myDesktop = Desktop.getDesktop();
        	if(Desktop.isDesktopSupported()) {
        		try {
                	File rules = new File("src\\pictures\\rules.txt");
                	myDesktop.open(rules);
        		}catch(IOException ex){
        			//TODO: warning window No application registered for opening .txt files
        			System.err.println("No application registered for opening .txt files");
        		}
        	}
        });
        
        panel.add(onePlayerButton);
        panel.add(rulesButton);
        panel.add(networkButton);
        
        
        this.add(panel, BorderLayout.SOUTH);
    }
}
