package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import model.*;

/* this class is used when the a first launches its application
 * and requires initial data. This class with the help of DatabaseHandler
 * sends initial data source to the client.
 */
public class InitialDataWorker extends Thread {
	
	private Socket inSocket;
	private DatabaseHandler db;
	
	public InitialDataWorker(Socket s, DatabaseHandler d) {
		inSocket = s; db = d;
	}
	
	public void run() {
		
		try {

			DataInputStream in  = new DataInputStream(inSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(inSocket.getOutputStream());
			
			// read the first byte indicating what data needed to send
			int code = in.readByte();

			// read the special code if there it is
			if (code == 7) {
				System.out.println("InitialDataWorker: special code "+7+" received");

				String brandList = db.getLaptopBrandAsList().toString();
				System.out.println("size: "+brandList.length());
				out.writeInt(brandList.length());
				out.write(brandList.getBytes());
				System.out.println("InitialDataWorker: message sent by 7");
		
				// if the firstCode received the secondCode must be received
				// but just in case to check if a special code 8 received
				code = in.readByte();

				if (code == 8) {
					System.out.println("InitialDataWorker: special code "+8+" received");

					String modelList = db.getLaptopModelAsList().toString();
					System.out.println("size: "+modelList.length());
					out.writeInt(modelList.length());
					out.write(modelList.getBytes());
					out.flush();
					System.out.println("InitialDataWorker: message sent by 8");
				}
			}
			
			// close the connection
			in.close();
			System.out.println("InitialDataWorker: in.close()");
			out.flush();
			System.out.println("InitialDataWorker: out.flush()");
			out.close();
			System.out.println("InitialDataWorker: out.close()");
			
		} catch (IOException e) {
			System.out.println("InitialDataWorker - Interrupted");
			e.printStackTrace();
		}
		
	}
}
