package battleship.view;

import javax.swing.*;
import java.awt.*;

public class GamePlayWindow extends JFrame {

    private Board userBoard;
    private Board enemyBoard;
    private JTextArea log;
    private JScrollPane scrollPane;
    private ButtonGroup radioButtonGroup;
    private JPanel logPanel;
    private JPanel shipPanel;
    private JPanel boardPanel;
    private JPanel key;
    private JPanel options;

    public GamePlayWindow(){
        userBoard = new Board();
        enemyBoard = new Board();

        log = new JTextArea(20, 20);
        log.setEditable(false);

        scrollPane = new JScrollPane(log);
        scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        logPanel = new JPanel(new BorderLayout());
        logPanel.add(scrollPane, BorderLayout.NORTH);

        boardPanel = new JPanel(new BorderLayout());
        boardPanel.add(userBoard, BorderLayout.NORTH);
        boardPanel.add(enemyBoard, BorderLayout.SOUTH);

        JRadioButton carrierButton = new JRadioButton("Carrier: 5 Tiles");
        JRadioButton battleshipButton = new JRadioButton("Battleship: 4 Tiles");
        JRadioButton cruiserButton = new JRadioButton("Cruiser: 3 Tiles");
        JRadioButton submarineButton = new JRadioButton("Submarine: 3 Tiles");
        JRadioButton destroyerButton = new JRadioButton("Destroyer: 2 Tiles");

        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(carrierButton);
        radioButtonGroup.add(battleshipButton);
        radioButtonGroup.add(cruiserButton);
        radioButtonGroup.add(submarineButton);
        radioButtonGroup.add(destroyerButton);

        shipPanel = new JPanel(new GridLayout(5, 1));

        shipPanel.add(carrierButton);
        shipPanel.add(battleshipButton);
        shipPanel.add(cruiserButton);
        shipPanel.add(submarineButton);
        shipPanel.add(destroyerButton);

        logPanel.add(shipPanel, BorderLayout.SOUTH);

        setTitle("Battleship: Game Board");
        setSize(625,700);
        setMinimumSize(new Dimension(625,700));
        setResizable(true);
        setVisible(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(logPanel, BorderLayout.EAST);
        add(boardPanel, BorderLayout.WEST);

    }

    public Board getUserBoard() {
        return userBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public void writeToLog(){
    }

    public void placeShips(){
    }

    private void selectStart(){
    }

    private void selectEnd(){
    }

    private void placeAShip(){
    }


}
