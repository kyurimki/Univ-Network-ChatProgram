package networkAssignment;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;

public class MultiServer {
	private ArrayList<MultiServerThread> clientList;
	private Socket socket;
	
	Timestamp ts;

	public MultiServer() throws IOException {
		clientList = new ArrayList<MultiServerThread>();
		
		ServerSocket serverSocket = new ServerSocket(8791);
		TimeStamp();
		System.out.println("---------- [" + ts + "] SERVER START ----------");
		MultiServerThread mst = null;
		boolean isStop = false;
		while (!isStop) {			
			socket = serverSocket.accept();
			InetSocketAddress sockAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			TimeStamp();
			System.out.println("---------- [" + ts + "] NEW CLIENT " + sockAddress.getHostName() + " ACCEPTED ----------");
			
			mst = new MultiServerThread(this);
			clientList.add(mst);
			TimeStamp();
			System.out.println("---------- [" + ts + "] CURRENT PARTICIPANTS NUMBER: " + clientList.size() + " ----------\n");
			
			Thread t = new Thread(mst);
			t.start();
		}
	}

	public ArrayList<MultiServerThread> getClientList(){
		return clientList;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void TimeStamp() {
		Date date = new Date();
		ts = new Timestamp(date.getTime());
	}
	
}