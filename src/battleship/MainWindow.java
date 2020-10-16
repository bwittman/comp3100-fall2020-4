package battleship;

import javax.swing.*;
import java.awt.*;

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

        panel.add(onePlayerButton);
        panel.add(rulesButton);
        panel.add(networkButton);

        this.add(panel, BorderLayout.SOUTH);
    }
}
