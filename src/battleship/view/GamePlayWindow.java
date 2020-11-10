package battleship.view;

import battleship.controller.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Graphical interface for placing ships and playing the game
 */
public class GamePlayWindow extends JFrame {

    //variables used for sizing the frames and panels based on the screen size
    private static int frameSize;
    public static int boardPanelHeight;
    public static int buttonSize;

    private Board userBoard;
    private Board enemyBoard;
    private JTextArea log;
    private JScrollPane scrollPane;
    private ButtonGroup shipButtonGroup;
    private JPanel logPanel;
    private JPanel shipPanel;
    private JPanel boardPanel;
    private JPanel optionButtons;
    private JButton resetButton;
    private JButton randomButton;
    private JButton playGameButton;

    public GamePlayWindow(){
        frameSize = MainMenu.frameSize;
        boardPanelHeight = (int) (frameSize*.9);

        setupBoardPanel();
        setupLogPanel();
        setupShipPlacementOptionsPanel();

        setTitle("Battleship: Game Board");
        setSize(frameSize,frameSize);
        setMinimumSize(new Dimension(frameSize,frameSize));
        setResizable(true);
        setVisible(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(logPanel, BorderLayout.CENTER);
        add(boardPanel, BorderLayout.WEST);
        add(optionButtons, BorderLayout.SOUTH);
    }

    /*
     * Set up the user and game boards
     */
    private void setupBoardPanel(){
        boardPanel = new JPanel(new BorderLayout());
        int boardPanelWidth = boardPanelHeight/2;
        boardPanel.setSize(new Dimension(boardPanelWidth, boardPanelHeight));

        buttonSize = boardPanelWidth/11;
        Board.setButtonSize(buttonSize);

        userBoard = new Board();
        enemyBoard = new Board();

        boardPanel.add(enemyBoard, BorderLayout.NORTH);
        boardPanel.add(userBoard, BorderLayout.SOUTH);

        boardPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,5,0));
    }

    /*
     * Set up the log
     */
    private void setupLogPanel(){
        log = new JTextArea(20,20);
        log.setEditable(false);

        scrollPane = new JScrollPane(log);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setSize(frameSize/2, frameSize/2);

        logPanel = new JPanel(new BorderLayout());
        logPanel.add(scrollPane, BorderLayout.NORTH);

        logPanel.setBorder(BorderFactory.createEmptyBorder(5, (int) (frameSize *.1),5,0));

        setupShipPanel();
    }

    /*
     * Set up all the ship buttons for ship placement
     */
    private void setupShipPanel(){
        JRadioButton carrierButton = new JRadioButton("Carrier: 5 Tiles");
        JRadioButton battleshipButton = new JRadioButton("Battleship: 4 Tiles");
        JRadioButton cruiserButton = new JRadioButton("Cruiser: 3 Tiles");
        JRadioButton submarineButton = new JRadioButton("Submarine: 3 Tiles");
        JRadioButton destroyerButton = new JRadioButton("Destroyer: 2 Tiles");

        carrierButton.setActionCommand("Carrier");
        battleshipButton.setActionCommand("Battleship");
        cruiserButton.setActionCommand("Cruiser");
        submarineButton.setActionCommand("Submarine");
        destroyerButton.setActionCommand("Destroyer");

        shipButtonGroup = new ButtonGroup();
        shipButtonGroup.add(carrierButton);
        shipButtonGroup.add(battleshipButton);
        shipButtonGroup.add(cruiserButton);
        shipButtonGroup.add(submarineButton);
        shipButtonGroup.add(destroyerButton);

        shipButtonGroup.setSelected(carrierButton.getModel(), true);

        shipPanel = new JPanel(new GridLayout(5, 1));

        shipPanel.add(carrierButton);
        shipPanel.add(battleshipButton);
        shipPanel.add(cruiserButton);
        shipPanel.add(submarineButton);
        shipPanel.add(destroyerButton);

        logPanel.add(shipPanel, BorderLayout.CENTER);
    }

    /*
     * Set up the options that are available only during the ship placement phase
     */
    private void setupShipPlacementOptionsPanel(){
        optionButtons = new JPanel(new GridLayout(1, 3));

        resetButton = new JButton("Reset");
        randomButton = new JButton("Random");
        playGameButton = new JButton("Play Game");
        playGameButton.setEnabled(false);

        optionButtons.add(resetButton);
        optionButtons.add(randomButton);
        optionButtons.add(playGameButton);
    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public JButton getResetButton(){
        return resetButton;
    }

    public JButton getRandomButton(){
        return randomButton;
    }

    public JButton getPlayGameButton(){
        return playGameButton;
    }

    public ButtonGroup getShipButtonGroup(){
        return shipButtonGroup;
    }

    public JPanel getOptionButtons(){
        return optionButtons;
    }

    public JTextArea getLog() {return log;}
}
