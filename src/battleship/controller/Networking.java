package battleship.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Networking {
	
	private String ipAddress;
	
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
                ipAddress = ip;
	        }
	    } catch (SocketException e) {
	        ipAddress = "0.0.0.0";
	    }
	}
	
	public String getIPAddress() {
		//TODO: Needs test case to check IP Address
		return ipAddress;
	}
	
}
