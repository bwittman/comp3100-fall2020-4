package battleship.view;

import  javax.swing.*;



import java.awt.*;


/**
 * Displays the Main Menu and handles interactions with the user until they leave the main menu.
 * 
 * @author poiu2
 *
 */
public class MainMenu extends JFrame {
    private ImageIcon background;
    private JButton rulesButton;
    private JButton networkButton;
    private JButton onePlayerButton;
    
    /**
     * Constructor creates a JPanel and constructs the Main Menu on it.
     */
    public MainMenu() {
    	background = new ImageIcon("resources/shipMainWindow.png");
        JPanel backgroundPanel = new PanelWithBackgroundImage(background.getImage());
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        JPanel outerPanel = new JPanel(new BorderLayout());

        rulesButton = new JButton("Rules");
        networkButton = new JButton("Networking");
        onePlayerButton = new JButton("Play Against Computer");

        buttonPanel.add(onePlayerButton);

        buttonPanel.add(rulesButton);
        buttonPanel.add(networkButton);

        outerPanel.add(backgroundPanel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Battleship: Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(625,625);
        setMinimumSize(new Dimension(625,625));
        setResizable(true);

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
    
    public void setNetworkButton(JButton newNetworkButton) {
    	this.networkButton = newNetworkButton;
    }
    
    public void setGetOnePlayerButton(JButton newOnePlayerButton) {
    	this.onePlayerButton = newOnePlayerButton;
    }
    
    public void setRulesButton(JButton newRulesButton) {
    	this.rulesButton = newRulesButton;
    }
}

class PanelWithBackgroundImage extends JPanel {
	/**
	 * 
	 */
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
