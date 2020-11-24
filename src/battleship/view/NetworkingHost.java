package battleship.view;

import java.awt.*;
import javax.swing.*;

/**
 * Networking screen for a user to host a game
 */
public class NetworkingHost extends JFrame {
	private JLabel localIPAddressLabel;
	private JLabel outsideIPAddressLabel;
	private JLabel connectionStatusLabel;
	private JTextField localIPAddress;
	private JTextField outerIPAddress;
	
	public NetworkingHost () {
		setTitle("Battleship - Host");
		setSize(400,200);
        setMinimumSize(new Dimension(400,200));
		setResizable(false);
		
		localIPAddressLabel = new JLabel("Your Local IP Address is:");
		outsideIPAddressLabel = new JLabel("Your Outside IP Address is:");
		localIPAddress = new JTextField("0.0.0.0");
		outerIPAddress = new JTextField("0.0.0.0");
		localIPAddress.setEditable(false);
		outerIPAddress.setEditable(false);
		//once socket is open this can be changed to waiting for connection
		connectionStatusLabel = new JLabel("Connection Status: Initialized",JLabel.CENTER);

		JPanel ipPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		ipPanel.add(outsideIPAddressLabel,c);
		c.gridy = 1;
		ipPanel.add(outerIPAddress, c);
		outerIPAddress.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
		c.gridy = 2;
		ipPanel.add(localIPAddressLabel,c);
		localIPAddressLabel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
		c.gridy = 3;
		ipPanel.add(localIPAddress,c);
		localIPAddress.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

		JPanel outerPanel = new JPanel(new BorderLayout());
		outerPanel.add(ipPanel, BorderLayout.CENTER);
		outerPanel.add(connectionStatusLabel, BorderLayout.SOUTH);
		connectionStatusLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

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

