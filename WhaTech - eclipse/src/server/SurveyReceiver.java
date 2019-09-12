package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.DatabaseHandler;
import model.Survey;

public class SurveyReceiver {
	
	final static int OUTER_RECV_PORT = 2000; // used to send init picker data
	final static int numThreads = Runtime.getRuntime().availableProcessors(); // get the numof processes
	static ExecutorService pool = Executors.newFixedThreadPool(numThreads); // create a thread pool
	static DatabaseHandler db = new DatabaseHandler();

	public static void main(String[] args) {
		
		// a runnable object
		Runnable r;
		
		try {
			ServerSocket serverSocket = new ServerSocket(OUTER_RECV_PORT);
				
			while (true) {
				System.out.println("Survey Receiver running...");
				Socket socket = serverSocket.accept();
				System.out.println("Survey Receiver Connected...");
				r = new SurveyReceiveWorker(socket, db);
				pool.submit(r);
			}
		}
		catch (IOException e) {
			pool.shutdown();
		}
	}
}