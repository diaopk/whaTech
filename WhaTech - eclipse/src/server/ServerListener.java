package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.DatabaseHandler;
import model.Sample;

public class ServerListener extends Thread {
	private final int SURVEY_UPDATE_SEND_PORT = 2010;
	private final int RESULT_UPDATE_SEND_PORT = 2011; 

	// ports stated in Analyser
	private final int INIT_DATA_RECV_PORT = 2001;
	private final int REQUEST_RECV_PORT = 2002;
	private final int PULL_RECV_PORT = 2003;
	private int port; 

	private ServerSocket serversocket; 
	private DatabaseHandler db;
	private Sample sample;
	
	ServerListener(int p, DatabaseHandler d) { 
		port = p; db = d; 
		sample = new Sample(db);
	} 
	
	public void run() {
		try {
			serversocket = new ServerSocket(port);
			while (true) {
				Socket socket = serversocket.accept();
				
				// thread will be created based on port
				if (port == INIT_DATA_RECV_PORT)
					new InitialDataWorker(socket, db).start();
				else if (port == REQUEST_RECV_PORT)
					new AnalyserDBLookupWorker(socket, db, sample).start();
				else if (port == this.PULL_RECV_PORT)
					new InfoWorker(socket, db).start();
			}
		} catch (IOException e) {}
	}
}
