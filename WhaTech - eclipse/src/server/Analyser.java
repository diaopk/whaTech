package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.DatabaseHandler;

public class Analyser {
	// port used to receive survey from SurveyReceiveWorker
	final static int INIT_DATA_RECV_PORT = 2001; 
	
	// port used to receive request from client
	final static int REQUEST_RECV_PORT = 2002;
	final static int PULL_RECV_PORT = 2003;
	final static int numThreads = Runtime.getRuntime().availableProcessors();
	static ExecutorService pool = Executors.newFixedThreadPool(numThreads);
	static DatabaseHandler db = new DatabaseHandler();

	public static void main(String[] args) {

		// three listeners that listen on two different ports 
		new ServerListener(INIT_DATA_RECV_PORT, db).start();
		new ServerListener(REQUEST_RECV_PORT, db).start();
		new ServerListener(PULL_RECV_PORT, db).start();
	}
}