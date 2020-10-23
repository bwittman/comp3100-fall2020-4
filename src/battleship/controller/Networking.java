package battleship.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Networking {
	
	private String hostIpAddress;
	private ServerSocket serverSocket;
	private Socket socket;
	private static final int PORT = 7777;
	
	public Networking() {
		String ip;
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
	
	public String getIPAddress() {
		//TODO: Needs test case to check IP Address
		return hostIpAddress;
	}
	
	public void sendMessage(String message) {
		//TODO: implement
	}
	
	public String recieveMessage() {
		//TODO: implement
		return "";
	}
	
	
	
}
