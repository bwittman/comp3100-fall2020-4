package battleship.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetworkingHost extends JFrame {
	private JLabel ipAddressInnerLabel ;
	private JLabel ipAddressOutsideLabel ;
	private JLabel connectionStatusLabel;
	
	public NetworkingHost () {
		setTitle("Battleship - Host");
		setSize(400,200);
        setMinimumSize(new Dimension(400,200));
		setResizable(false);
		
		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new GridBagLayout());
		JPanel middlePanel = new JPanel(new GridBagLayout());
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		ipAddressInnerLabel = new JLabel("Your Local IP Address is: 192.168.X.X");
		ipAddressOutsideLabel = new JLabel("Your Outside IP Address is: XXX.XXX.X.X"); //stand in label to tell if it is working
		connectionStatusLabel = new JLabel("Connection Status: Initialized"); // once socket is open this can be changed to waiting for connection
		topPanel.add(ipAddressInnerLabel);
		middlePanel.add(ipAddressOutsideLabel);
		bottomPanel.add(connectionStatusLabel);
		outerPanel.add(topPanel, BorderLayout.NORTH);
		outerPanel.add(middlePanel, BorderLayout.CENTER);
		outerPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		add(outerPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);
	}
	
	public JLabel getIPAddressInnerLabel() {
		return ipAddressInnerLabel;
	}
	
	public JLabel getIPAddressOutsideLabel() {
		return ipAddressOutsideLabel;
	}
	
	public JLabel getConnectionStatusLabel() {
		return connectionStatusLabel;
	}
}

