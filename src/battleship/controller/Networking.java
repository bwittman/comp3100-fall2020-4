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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Holds all the Networking Components
 * @author poiu2
 *
 */
public class Networking {
	
	private String hostIpAddressLocal;
	private String hostIpAddressExternal;
	private ServerSocket serverSocket;
	private Socket socket;
	private static final int PORT = 7777;
	private boolean isHost;
	private Scanner input;
	private PrintWriter output;

	/**
	 * Constructor for Networking Class. Gets local and
	 */
	public Networking() {
		String ip;
		serverSocket = null;
		socket = null;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
				if (!(iface.isLoopback() || !iface.isUp())) {
					Enumeration<InetAddress> addresses = iface.getInetAddresses(); //Gets the local IP address
					InetAddress addr = addresses.nextElement();
					ip = addr.getHostAddress();
					System.out.println(iface.getDisplayName() + " " + ip);
					hostIpAddressLocal = ip;
				}


	        }
	    } catch (SocketException e) {
	    	hostIpAddressLocal = "0.0.0.0";
	    }
	    
	    // Gets External IP Address from AWS website
	    try {
	    	//TODO: Double check that this doesn't need to happen separate from main execution thread
	    URL whatismyip = new URL("http://checkip.amazonaws.com"); //Asks this address for our IP
	    BufferedReader in = new BufferedReader(new InputStreamReader(
	                    whatismyip.openStream()));
	    hostIpAddressExternal = in.readLine(); //you get the IP as a String
	    System.out.println(hostIpAddressExternal);
	    }catch(MalformedURLException e) {
	    	System.err.println("MalformedURLException thrown in Networking Contructor");
	    }
	    catch(IOException e) {
	    	System.err.println("IOException Thrown in Networking Constructor (trying to get external IP)");
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
				output = new PrintWriter(socket.getOutputStream());
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
	public String getIpLocal() {
		//TODO: Needs test case to check IP Address
		return hostIpAddressLocal;
	}
	
	public String getIpExternal() {
		return hostIpAddressExternal;
	}
	
	/**
	 * Tries to send a message over the socket
	 * @param message
	 */
	public void sendMessage(String message) {
		if(socket != null && socket.isConnected()) {
			output.println(Arrays.toString(message.getBytes()));
		}else {
			System.out.println("Could not send message. Socket could be null or not connected");
		}
	}
	
	/**
	 * Tries to get a message from the socket
	 * @return returns the string from the socket if possible. Otherwise returns an empty string.
	 */
	public String receiveMessage() {
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
	}
	
	/**
	 * Checks if the socket is connected
	 * @return true if the socket is connected, otherwise false
	 */
	public boolean isConnected() {
		if(socket != null) {
			return socket.isConnected();
		}else {
			return false;
		}

	}
}
