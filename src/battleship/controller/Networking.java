package battleship.controller;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Holds all the Networking Components for a two player game
 */
public class Networking {
	
	private String hostIpAddressLocal;
	private String hostIpAddressExternal;
	private ServerSocket serverSocket;
	private Socket socket;
	private static final int PORT = 7777;
	private Scanner input;
	private PrintWriter output;

	public Networking() {
		String ip;
		serverSocket = null;
		socket = null;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface networkingInterface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
				if (!(networkingInterface.isLoopback() || !networkingInterface.isUp())) {
					Enumeration<InetAddress> addresses = networkingInterface.getInetAddresses(); //Gets the local IP address
					InetAddress address = addresses.nextElement();
					ip = address.getHostAddress();
					System.out.println(networkingInterface.getDisplayName() + " " + ip);
					hostIpAddressLocal = ip;
				}
	        }
	    } catch (SocketException e) {
	    	hostIpAddressLocal = "0.0.0.0";
	    }
	    
	    // Gets External IP Address from AWS website
	    try {
	    URL whatIsMyIp = new URL("http://checkip.amazonaws.com"); //Asks this address for our IP
	    BufferedReader in = new BufferedReader(new InputStreamReader(
	                    whatIsMyIp.openStream()));
	    hostIpAddressExternal = in.readLine(); //you get the IP as a String
	    System.out.println(hostIpAddressExternal);
	    }catch(MalformedURLException e) {
	    	System.err.println("MalformedURLException thrown in Networking Constructor");
	    }
	    catch(IOException e) {
	    	System.err.println("IOException Thrown in Networking Constructor (trying to get external IP)");
	    }
	}
	
	/**
	 * Attempts to connect client to host
	 * @param isHost tells class if it is the host or client
	 * @param ipAddress if the class is not a host it needs to know the IP it should connect to
	 */
	public void connect(boolean isHost, String ipAddress) {
		if(isHost) {
	    	try {
				serverSocket = new ServerSocket(PORT);
				socket = serverSocket.accept();
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("IOException thrown in Networking Class Constructor (isHost = true)");
			}
	    }else {
	    	try {
				socket = new Socket(ipAddress, PORT);
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream());
			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException thrown in Networking Class Constructor");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException thrown in Networking Class Constructor (isHost = false)");
			}
	    }
	}
	
	/**
	 * Tries to send a message over the socket
	 * @param message the string to be sent over the socket
	 */
	public void sendMessage(String message) {
		if(socket != null && socket.isConnected() && output != null) {
			output.println(message);
			output.flush();
		}else {
			System.out.println("Could not send message: "+ message +"\nSocket could be null or not connected");
		}
	}
	
	/**
	 * Tries to get a message from the socket
	 * @return returns the string from the socket if possible. Otherwise returns an empty string.
	 */
	public String receiveMessage() {
		return input.nextLine();
	}
	
	/**
	 * Closes all input and output streams and sockets.
	 */
	public void cleanUp() {
		if(output != null) {
			output.close();
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
		output = null;
		input = null;
		socket = null;
		serverSocket = null;
	}
	
	/**
	 * Checks if the socket is connected
	 * @return true if the socket is connected, otherwise false
	 */
	public boolean isConnected() {
		if(socket != null) {
			return !socket.isClosed() && socket.isConnected();
		}else {
			return false;
		}
	}

	public String getIpLocal() {
		return hostIpAddressLocal;
	}

	public String getIpExternal() {
		return hostIpAddressExternal;
	}
}
