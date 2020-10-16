package battleship;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {

    public MainWindow() {
        JLabel background = new JLabel(new ImageIcon("mainWindow.png"));
        JPanel panel = new JPanel(new GridLayout(1,3));

        JButton rulesButton = new JButton("Rules");
        JButton networkButton = new JButton("Networking");
        JButton onePlayerButton = new JButton("Play Against Computer");

        panel.add(onePlayerButton);
        panel.add(rulesButton);
        panel.add(networkButton);

        this.add(background, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
    }
}
