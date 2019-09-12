package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import org.json.JSONObject;

import model.*;

import org.json.JSONException;

public class SurveyReceiveWorker implements Runnable {
	private Socket inSocket;
	private Survey survey;
	private DatabaseHandler db;
	
	private int sendPort; // 2001 used to interconnect to the analyser
	
	SurveyReceiveWorker(Socket s, DatabaseHandler d) {
		inSocket = s; db = d; survey = new Survey();
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		System.out.println("SurveyReceiverWorker: runing");
		try {
			DataInputStream in = new DataInputStream(inSocket.getInputStream());
			DataOutputStream outForCode = new DataOutputStream(inSocket.getOutputStream());
			
			// first special code read
			// if the fist special code is 7 then send laptop info to the client
			// else go ahead to receive the survey
			int firstCode = in.readByte();
			
			// read the special code if there it is
			if (firstCode == 7) {
				System.out.println("SurveyReceiverWorker: special code "+7+" received");

				String brandList = db.getLaptopBrandAsList().toString();
				System.out.println("size: "+brandList.length());
				outForCode.writeInt(brandList.length());
				outForCode.write(brandList.getBytes());
				System.out.println("SurveyReceiverWorker: message sent by 7");
		
				// if the firstCode received the secondCode must be received
				// but just in case to check if a special code 8 received
				int secondCode = in.readByte();
				if (secondCode == 8) {
					System.out.println("SurveyReceiverWorker: special code "+8+" received");

					String modelList = db.getLaptopModelAsList().toString();
					System.out.println("size: "+modelList.length());
					outForCode.writeInt(modelList.length());
					outForCode.write(modelList.getBytes());
					outForCode.flush();
					System.out.println("SurveyReceiverWorker: message sent by 8");
				}
				return;
			}
			
			// read the incoming massage
			int i = 0;
			int size = in.readByte();
			System.out.println("SurveyReceiverWorker: incoming message size: "+size);
			byte b;
			byte[] data = new byte[size];
      
			// read the data
      do {
      	b = in.readByte();
      	data[i] = b;
      	i++;
      } while (i < data.length);

      // form a new string from the read data
      String s = new String(data);
      System.out.println("SurveyReceiverWorker: read String: "+s);
			
			try {
				// create a JSON object to parse the received data
				JSONObject obj = new JSONObject(s);
				
				/* create a Consumer object to be used as the argument inside
				 * the obj.keys().forEachRemaining()
				 * this insertData basically assign value associated with key to techData
				 */
				Consumer<String> toBeParsed = x -> {
					try {
						switch (x) {
						case "gender":
							survey.setGender(obj.getString(x).toLowerCase().trim());
							break;
						case "age":
							survey.setAge(obj.getInt(x));
							break;
						case "laptop":
							// laptop can be empty
							survey.setLaptop(obj.getString(x).trim());
							break;
						default:
							break;
						}
					} catch (JSONException e) {}
				};
				
				// assign values for techData
				obj.keys().forEachRemaining(toBeParsed);
				
			} catch (JSONException e) { 
				System.out.println("in-valid JSON object retrieved: "+e); 
			}

			System.out.println("SurveyReceiveWorker: survey: "+survey);

			// if the user specified the laptop then insert the data
 			if (survey.laptop() != null)
				if(db.insertUser(survey.age(), survey.gender(), survey.laptop()) != 0) {
					System.out.println("SurveyReceiverWorker: new uesr inserted...");
					
					// indicate client the data has been stored
					outForCode.writeInt("received".length());
					outForCode.write("received".getBytes());

				} else {
					System.out.println("SurveyReceiverWorker: new uesr not inserted...");
					// indicate client the data has not been stored
					outForCode.writeInt("notreceived".length());
					outForCode.write("notreceived".getBytes());
				}
 			
 			// close the socket
			in.close(); // closing inputStream closes the whole stream and socket
 			outForCode.flush();
 			outForCode.close();
 			inSocket.close();
 			
			// then send the survey data to Analyser through sendPort 2001.
//			Socket outSocket = new Socket(InetAddress.getLocalHost(), sendPort);
//			ObjectOutputStream out = new ObjectOutputStream(outSocket.getOutputStream());
			
//			out.writeObject(survey);
//			System.out.println("SurveyReceiveWorker.run(): send survey object to AnalyserDataUpdateWorker");
			
//			out.flush();
			
		}
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public Survey getTectData() { return survey; }
}
