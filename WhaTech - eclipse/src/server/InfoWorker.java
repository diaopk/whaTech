package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import model.DatabaseHandler;

public class InfoWorker extends Thread {
	private DatabaseHandler db;
	private Socket inSocket;
	
	InfoWorker(Socket socket, DatabaseHandler d) {
		inSocket = socket; db = d;
	}
	public void run() {
		try {
			DataOutputStream out = new DataOutputStream(inSocket.getOutputStream());
			
			out.write(db.getInfoData().toString().getBytes());
			System.out.println("InfoWorker: wrote");

			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
