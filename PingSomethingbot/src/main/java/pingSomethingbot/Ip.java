package pingSomethingbot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ip {
	String ipAddress = "";
	boolean isReachable = false;
	String ipAnswer = "";

	public Ip(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
	}

	public String getIpAnswer() {
		this.pingIp(ipAddress);
		return ipAnswer;
	}

	public void setIpAnswer(String ipAnswer) {
		this.ipAnswer = ipAnswer;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean pingIp(String ipText) {
		try {
			InetAddress inet = InetAddress.getByName(ipAddress);
			System.out.println("Sending Ping Request to " + ipAddress);
			try {
				if (inet.isReachable(5000)) {
					this.setIpAnswer("Host: " + ipText + " is reachable \ud83d\ude0a");
					isReachable = true;
				} else {
					this.setIpAnswer("Host: " + ipText + "  is NOT reachable"  + "\ud83d\udca9");
					isReachable = false;
				}
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
			e.printStackTrace();
		}
		System.out.println(ipAnswer);

		return isReachable;
	}

}
