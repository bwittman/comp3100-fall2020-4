package battleship.view;

import javax.swing.*;
import java.awt.*;

public class GamePlayWindow extends JFrame {

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
        userBoard = new Board();
        enemyBoard = new Board();

        log = new JTextArea(20,20);
        log.setEditable(false);

        scrollPane = new JScrollPane(log);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        logPanel = new JPanel(new BorderLayout());
        logPanel.add(scrollPane, BorderLayout.NORTH);

        boardPanel = new JPanel(new BorderLayout());
        boardPanel.add(enemyBoard, BorderLayout.NORTH);
        boardPanel.add(userBoard, BorderLayout.SOUTH);

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

        setUpShipPlacementOptionsPanel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //These numbers are based on the size of Dominic's laptop screen compared to a frame that is 625 px wide and 750 px tall
        int screenWidth = (int) (screenSize.getWidth() / 2.5);
        int screenHeight = (int) (screenSize.getHeight() / 1.2);

        setTitle("Battleship: Game Board");
        setSize(screenWidth,screenHeight); //~625 ~750
        setMinimumSize(new Dimension(screenWidth,screenHeight));
        setResizable(true);
        setVisible(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(logPanel, BorderLayout.EAST);
        add(boardPanel, BorderLayout.WEST);
        add(optionButtons, BorderLayout.SOUTH);

    }

    private void setUpShipPlacementOptionsPanel(){
        optionButtons = new JPanel(new GridLayout(1, 3));

        resetButton = new JButton("Reset");
        randomButton = new JButton("Random");
        playGameButton = new JButton("Play Game");
        playGameButton.setEnabled(false);

        optionButtons.add(resetButton);
        optionButtons.add(randomButton);
        optionButtons.add(playGameButton);
        optionButtons.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
    }



    public void placeShips(){
    }

    private void selectStart(){
    }

    private void selectEnd(){
    }

    private void placeAShip(){
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
