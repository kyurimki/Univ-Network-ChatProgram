package networkAssignment;

import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.*;

public class MultiServerThread implements Runnable {
	private Socket socket;
	private MultiServer ms;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	Timestamp ts;
	
	String b4message;

	public MultiServerThread(MultiServer ms) {
		this.ms = ms;
	}

	public synchronized void run() {
		boolean isStop = false;
		try {
			socket = ms.getSocket();
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			String message = null;
			while (!isStop) {
				message = (String) ois.readObject();
				String[] str = message.split("#");
				
				String name = "list"+"#";
				
				for(int i=0; i<ms.getClientList().size(); i++){
					name += ms.getClientList().get(i)+"#";
				}
				
				if (str[1].equals("exit")) {
					broadCasting(message);
					isStop = true;
				} else {
					broadCasting(message);
				}
			}		
			ms.getClientList().remove(this);
			TimeStamp();
			System.out.println("--------- [" + ts + "] " + socket.getInetAddress() + " 종료");
			System.out.println("          LEFT PARTICIPANTS: " + ms.getClientList().size() + " ---------");
		} catch (Exception e) {
			ms.getClientList().remove(this);
			TimeStamp();
			System.out.println("--------- [" + ts + "] " + socket.getInetAddress() + " 종료");
			System.out.println("          LEFT PARTICIPANTS: " + ms.getClientList().size() + " ---------");
		}
	}

	public void broadCasting(String message) throws IOException {
		for (MultiServerThread ct : ms.getClientList()) {
			ct.send(message);
		}
	}

	public void send(String message) throws IOException {
		oos.writeObject(message);
		System.out.println(message);
	}
	
	public void TimeStamp() {
		Date date = new Date();
		ts = new Timestamp(date.getTime());
	}
}
