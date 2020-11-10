package battleship.view;

import  javax.swing.*;
import java.awt.*;

/**
 * Displays the Main Menu and handles interactions with the user until they leave the main menu
 */
public class MainMenu extends JFrame {

    //for sizing the frames and panels based on the screen size
    public static int frameSize;

    private ImageIcon background;
    private JButton rulesButton;
    private JButton networkButton;
    private JButton onePlayerButton;

    public MainMenu() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = (int) (screenSize.getHeight()*.9);

    	background = new ImageIcon(new ImageIcon("resources/shipMainWindow.png").getImage()
                .getScaledInstance(frameSize,frameSize, Image.SCALE_SMOOTH));

        JPanel backgroundPanel = new PanelWithBackgroundImage(background.getImage());
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        JPanel outerPanel = new JPanel(new BorderLayout());

        rulesButton = new JButton("Rules");
        networkButton = new JButton("Networking");
        onePlayerButton = new JButton("Play Against Computer");
        onePlayerButton.setFocusPainted(false);

        buttonPanel.add(onePlayerButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(networkButton);

        outerPanel.add(backgroundPanel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Battleship: Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameSize,frameSize);
        setMinimumSize(new Dimension(frameSize,frameSize));
        setResizable(false);

        add(outerPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public JButton getNetworkButton() {
    	return networkButton;
    }
    
    public JButton getOnePlayerButton() {
    	return onePlayerButton;
    }
    
    public JButton getRulesButton() {
    	return rulesButton;
    }
}

/**
 * Inner class for displaying the background image on the Main Menu
 */
class PanelWithBackgroundImage extends JPanel {

	private static final long serialVersionUID = 1L;
	Image background;
	
	public PanelWithBackgroundImage (Image bg) {
		this.background = bg;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }
}
