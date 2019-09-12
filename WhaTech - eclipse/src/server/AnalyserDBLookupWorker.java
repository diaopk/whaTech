package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import model.DatabaseHandler;
import model.Sample;

public class AnalyserDBLookupWorker extends Thread {
	private Socket socket;
	private Sample sample;
	
	private String[] genders;
	
	// attributes for retrieving sample data from db
	private ArrayList<Integer> ages;
	private String gender;
	private ArrayList<String> brands;
	
	public AnalyserDBLookupWorker (Socket s, DatabaseHandler d, Sample resultSample) {
		socket = s; sample = resultSample;
		
		genders = new String[] {
			"All", "m", "f",	
		};
		
		ages = new ArrayList<Integer>();
		brands = new ArrayList<String>();
	}
	
	public void run() {
		System.out.println("AnalyserDBLookupWorker: runing");
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			// how many bytes to receive
			byte[] sizeBytes = new byte[] {
					in.readByte(),
					in.readByte(),
			};
			int sizeInt = Integer.valueOf(new String(sizeBytes));

			// skip the 'return' '\n'
			in.skipBytes(1);
			
			// array of bytes
			byte[] bs = new byte[sizeInt];
			
			// read bytes
			for (int i = 0; i < bs.length; i++)
				bs[i] = in.readByte();

			String s = new String(bs);
			System.out.println(s);
			
			// create a JSON object
			JSONObject obj = new JSONObject(s);
			System.out.println("json: "+obj);
			System.out.println("getArray(): "+obj.getJSONArray("Brand")); 
			
			// retrieve data from client and run a query
			if (obj.has("Brand")) {
				for (int i = 0; i < obj.getJSONArray("Brand").length(); i++)
					brands.add(obj.getJSONArray("Brand").getString(i));
			}
			
			if (obj.has("Age")) {
				String tempAge;
				for (int i = 0; i < obj.getJSONArray("Age").length(); i++) {
					tempAge = obj.getJSONArray("Age").getString(i);
					
					if (tempAge.startsWith("A")) {
						ages.add(0);
						break;
					} else if (tempAge.startsWith("5")) {
						ages.add(10);
					} else if (tempAge.startsWith("1")) {
						ages.add(20);
					} else if (tempAge.startsWith("2")) {
						ages.add(30);
					} else if (tempAge.startsWith("3")) {
						ages.add(40);
					} else {
						ages.add(50);
					}
				}
			}
			
			if (obj.has("Gender"))
				gender = genders[(int) obj.get("Gender")];
			
			// retrieve new data sample
			sample.lock();
			sample.retrieveData(ages, gender, brands);
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			String delimiter = "|";

			// write response to the client
			String dataToSend = 
					// brand list
					sample.getLaptopBrandLabelList().toString() + delimiter
					
					// number list
					+ sample.getLaptopBrandNumList().toString() + delimiter
					
					// double list
					+ sample.getLaptopBrandDoubleList().toString() + delimiter
					
					// age lists
					+ sample.getAgeLabelList() + delimiter
					+ sample.getAgeNumList() + delimiter
					+ sample.getAgeDoubleList() + delimiter

					// brand label for filter view
					+ sample.getLaptopBrandLabelListForFiterView(Sample.COLUMN_BRAND) + delimiter
					;
			
			
			out.write(dataToSend.getBytes());
			
			System.out.println("AnalyserDBLookupWorker: "+dataToSend);
			sample.unLock();
			System.out.println("AnalyserDBLookupWorker: out sent");
			
			out.flush();
			socket.close();
			
		} 
		catch (IOException e) {e.printStackTrace();}
		catch (JSONException e) { e.printStackTrace(); }
	}
}
