package battleship.view;

import  javax.swing.*;
import java.awt.*;

/**
 * Displays the Main Menu and handles interactions with the user until they leave the main menu
 */
public class MainMenu extends JFrame {

    //for sizing the frames and panels based on the screen size
    private final int frameSize;

    private JMenuItem rulesMenu;
    private JMenuItem howToPlay;
    private JMenuItem networkingItem;
    private JMenuItem computerItem;

    public MainMenu() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = (int) (screenSize.getHeight()*.9);

        ImageIcon background = new ImageIcon(new ImageIcon(this.getClass().getResource("/shipMainWindow.png")).getImage()
                .getScaledInstance(frameSize, frameSize, Image.SCALE_SMOOTH));

        JPanel backgroundPanel = new PanelWithBackgroundImage(background.getImage());
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        JPanel outerPanel = new JPanel(new BorderLayout());

        outerPanel.add(backgroundPanel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setJMenuBar(setupMenuBar());

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

    private JMenuBar setupMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu playMenu = new JMenu("Play Game");
        computerItem = new JMenuItem("Against Computer");
        networkingItem = new JMenuItem("Through Networking");
        playMenu.add(computerItem);
        playMenu.add(networkingItem);
        menuBar.add(playMenu);

        JMenu helpMenu = new JMenu("Help");
        rulesMenu = new JMenuItem("Rules");
        howToPlay = new JMenuItem("How to Play");
        helpMenu.add(rulesMenu);
        helpMenu.add(howToPlay);
        menuBar.add(helpMenu);

        JMenu settingsMenu = new JMenu("Settings");

        JMenu computerDifficultyMenu = new JMenu("Computer Difficulty");
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem hardComputer = new JRadioButtonMenuItem("Hard");
        hardComputer.setSelected(true);
        group.add(hardComputer);
        JRadioButtonMenuItem easyComputer = new JRadioButtonMenuItem("Easy");
        group.add(easyComputer);
        computerDifficultyMenu.add(hardComputer);
        computerDifficultyMenu.add(easyComputer);
        settingsMenu.add(computerDifficultyMenu);

        JCheckBoxMenuItem soundsItem = new JCheckBoxMenuItem("Sound Effects");
        settingsMenu.add(soundsItem);
        soundsItem.setSelected(true);
        menuBar.add(settingsMenu);

        return menuBar;
    }
    
    public JMenuItem getNetworkingItem() {
    	return networkingItem;
    }
    
    public JMenuItem getComputerItem() {
    	return computerItem;
    }
    
    public JMenuItem getRulesItem() {
    	return rulesMenu;
    }

    public JMenuItem getHowToPlayItem(){
        return howToPlay;
    }

    public int getFrameSize(){
        return frameSize;
    }

    public void reset(){
        networkingItem.setEnabled(true);
        computerItem.setEnabled(true);
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
