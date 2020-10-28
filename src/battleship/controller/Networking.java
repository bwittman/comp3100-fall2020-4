package battleship.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

public class Networking {
	
	private String hostIpAddress;
	private ServerSocket serverSocket;
	private Socket socket;
	private static final int PORT = 7777;
	private boolean isHost;
	Scanner input;
	OutputStream output;
	
	
	
	public Networking() {
		String ip;
		serverSocket = null;
		socket = null;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp()) {
					continue;
				}

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
                InetAddress addr = addresses.nextElement();
                ip = addr.getHostAddress();
                System.out.println(iface.getDisplayName() + " " + ip);
                hostIpAddress = ip;
	        }
	    } catch (SocketException e) {
	    	hostIpAddress = "0.0.0.0";
	    }
	    
	}
	
	/**
	 * Attempts to connect client to host
	 * @param isHost tells class if it is the host or client
	 * @param ipAddress if the class is not a host it needs to know the IP it should connect to
	 * @return returns true if connected and false if not connects
	 */
	public boolean connect(boolean isHost, String ipAddress) {
		
		if(isHost) {
	    	try {
				serverSocket = new ServerSocket(PORT);
				socket = serverSocket.accept();
				InputStream inputStream = socket.getInputStream();
				input = new Scanner(inputStream);
				output = socket.getOutputStream();
			} catch (IOException e) {
				System.out.println("IOException thrown in Networking Class Constructor (isHost = true)");
				return false;
			}
	    }else {
	    	try {
				socket = new Socket(ipAddress, PORT);
				
			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException thrown in Networking Class Constructor");
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				System.out.println("IOException thrown in Networking Class Constructor (isHost = false)");
				return false;
			}
	    }
		
		return true;
	}
	
	/**
	 * Getter for the IPAddress
	 * @return a string representation of the IPAddress
	 */
	public String getIP() {
		//TODO: Needs test case to check IP Address
		return hostIpAddress;
	}
	
	/**
	 * Tries to send a message over the socket
	 * @param message
	 */
	public void sendMessage(String message) {
		if(socket != null && socket.isConnected()) {
			try {
				output.write(message.getBytes());
			} catch (IOException e) {
				System.out.println("Could not send Message IOException thrown");
				e.printStackTrace();
			};
		}else {
			System.out.println("Could not send message. Socket could be null or not connected");
		}
	}
	
	/**
	 * Tries to get a message from the socket
	 * @return returns the string from the socket if possible. Otherwise returns an empty string.
	 */
	public String recieveMessage() {
		if(socket != null && socket.isConnected()) {
			return input.nextLine();
		}else {
			return "";
		}
	}
	
	/**
	 * Closes all input and output streams and sockets.
	 */
	public void cleanUp() {
		try {
			if(output != null) {
				output.close();
			}
		}catch (IOException e) {
			System.out.println("Could not clean up output IOException thrown");
			e.printStackTrace();
		}
		if(input != null) {
			input.close();
		}		
		try {
			if(socket != null) {
				socket.close();
			}
		}catch (IOException e) {
			System.out.println("Could not clean up socket IOException thrown");
			e.printStackTrace();
		}
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Could not clean up serverSocket IOException thrown");
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		if(socket != null) {
			return socket.isConnected();
		}else {
			return false;
		}

	}
}
