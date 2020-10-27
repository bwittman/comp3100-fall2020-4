package battleship.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkingClient extends JFrame {
	
	private JTextField IPinput;
	private JButton connectButton;
	private JLabel statusLabel;
	
	
	public NetworkingClient() {
		setTitle("Battleship - Host");
		setSize(400,200);
        setMinimumSize(new Dimension(400,200));
		setResizable(false);
		
		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new GridBagLayout());
		JPanel middlePanel = new JPanel(new GridBagLayout());
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		
		statusLabel = new JLabel("Enter the host's IP address: ");
		IPinput = new JTextField(11);
		connectButton = new JButton("Connect!");
		
		topPanel.add(statusLabel);
		middlePanel.add(IPinput);
		bottomPanel.add(connectButton);
		outerPanel.add(topPanel, BorderLayout.NORTH);
		outerPanel.add(middlePanel, BorderLayout.CENTER);
		outerPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		add(outerPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public JButton getConnectButton() {
		return connectButton;
	}
	
	public JTextField getIPInput() {
		return IPinput;
	}
	
	public void setConnectButton(JButton newConnectButton) {
		this.connectButton = newConnectButton;
	}
	
	public void setIPInput(JTextField newIPInput) {
		this.IPinput = newIPInput;
	}
	
	public JLabel getStatusLabel() {
		return statusLabel;
	}
	
	public static void main(String[] args) {
		NetworkingClient test = new NetworkingClient();

	}

}
