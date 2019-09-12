package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import model.DatabaseHandler;
import model.Survey;

// class that acts as a client randomly generating survey and submits it
public class ClientSimulator {
	static DatabaseHandler db = new DatabaseHandler();
	final static int PORT = 2000;

	public static void main(String[] args) {
		new FakeClient(PORT, db).start();
	}
}

class FakeClient extends Thread {
	private int port;
	private DatabaseHandler db;
	private String[] genders = { "f", "m" };
	
	FakeClient(int p, DatabaseHandler d) { port = p; db = d; }
		
	@SuppressWarnings("static-access")
	public void run() {
		int age;
		String gender;
		String brand;
		String model;
		String laptop;
		Survey survey;

		while (true) {
			System.out.println("FakeClient: running");
			// sleep length
			int sleepTime;

			// generate random data
			age = (int) (Math.random()*9)*10;
			gender = genders[(int) (Math.random()*1)];
			brand = db.randomMake();
			model = db.randomModel(brand);
			
			System.out.println("age: "+age+", gender: "+gender+", brand: "+brand+", model: "+model);
			
			// create a fake survey
			survey = new Survey();
			survey.setAge(age);
			survey.setGender(gender);
			survey.setLaptop(model);
			
			// send to the receiver
			try {
				Socket socket = new Socket(InetAddress.getLocalHost(), port);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				DataInputStream in = new DataInputStream(socket.getInputStream());
				
				// write code
				out.writeByte(6);
				
				// write size
				out.writeByte(survey.toString().length());
				
				// write bytes
				out.write(survey.toString().getBytes());
				System.out.println("FakeClient.run(): bytes written: "+survey.toString());
				
				Byte b;
				int size = in.readInt();
				System.out.println(size);
				while((b = (byte) in.read()) != -1) {
					System.out.println(b);
				}


				// close
				out.close();
				out.flush();
				

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// wait for random second
			sleepTime = (int) (Math.random()*15+15)*1000;
			System.out.println("FakeClient sleeps "+sleepTime/1000+" sec");
			try {
				FakeClient.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
