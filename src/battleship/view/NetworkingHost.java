package battleship.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.*;

/**
 * Networking screen for a user to host a game
 */
public class NetworkingHost extends JFrame {
	private JLabel ipAddressInnerLabel ;
	private JLabel ipAddressOutsideLabel ;
	private JLabel connectionStatusLabel;
	private JTextField localIPAddress;
	private JTextField outerIPAddress;
	
	public NetworkingHost () {
		setTitle("Battleship - Host");
		setSize(400,200);
        setMinimumSize(new Dimension(400,200));
		setResizable(false);
		
		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel middlePanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel(new BorderLayout());
		JPanel ipAddressInnerLabelPanel = new JPanel(new GridBagLayout());
		JPanel ipAddressOutsideLabelPanel = new JPanel(new GridBagLayout());
		JPanel connectionStatusLabelPanel = new JPanel(new GridBagLayout());
		JPanel localIPPanel = new JPanel(new GridBagLayout());
		JPanel outerIPPanel = new JPanel(new GridBagLayout());
		ipAddressInnerLabel = new JLabel("Your Local IP Address is:");
		ipAddressOutsideLabel = new JLabel("Your Outside IP Address is:");
		localIPAddress = new JTextField("0.0.0.0");
		outerIPAddress = new JTextField("0.0.0.0");
		localIPAddress.setEditable(false);
		outerIPAddress.setEditable(false);
		connectionStatusLabel = new JLabel("Connection Status: Initialized"); // once socket is open this can be changed to waiting for connection
		localIPPanel.add(localIPAddress);
		outerIPPanel.add(outerIPAddress);
		ipAddressInnerLabelPanel.add(ipAddressInnerLabel);
		ipAddressOutsideLabelPanel.add(ipAddressOutsideLabel);
		connectionStatusLabelPanel.add(connectionStatusLabel);
		topPanel.add(ipAddressOutsideLabelPanel, BorderLayout.NORTH);
		topPanel.add(outerIPPanel, BorderLayout.SOUTH);
		middlePanel.add(ipAddressInnerLabelPanel, BorderLayout.NORTH);
		middlePanel.add(localIPPanel, BorderLayout.SOUTH);
		bottomPanel.add(connectionStatusLabelPanel, BorderLayout.NORTH);
		outerPanel.add(topPanel, BorderLayout.NORTH);
		outerPanel.add(middlePanel, BorderLayout.CENTER);
		outerPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		add(outerPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);
	}
	
	public JLabel getConnectionStatusLabel() {
		return connectionStatusLabel;
	}

	public JTextField getLocalIPAddress(){ return localIPAddress;}

	public JTextField getOuterIPAddress(){ return outerIPAddress;}
}

