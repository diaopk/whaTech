package model;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Simulator {

	public static void main(String[] args) {
			DatabaseHandler db = new DatabaseHandler();
			
			
//			db.simulateUser(5, 200);
//			db.simulateUser(15, 200);
//			db.simulateUser(25, 200);
//			db.simulateUser(35, 400);
//			db.simulateUser(50, 3);
			
			Sample sample = new Sample(db);
			
//			String data = sample.getLaptopBrandLabelListForFiterView().toString();
			
//			print(sample.getAgeList().toString());
			
//			ArrayList<Integer> age = new ArrayList<Integer>();
//			age.add(10);
//			age.add(20);
//			age.add(30);
//			age.add(40);
//			age.add(50);
			
//			sample.retrieveData(age, "All", null);
			
//			print(sample.getLaptopBrandListForFemale().toString());
//			print(sample.getLaptopBrandNumListForFemale().toString());
//			print(sample.getLaptopBrandListForMale().toString());
//			print(sample.getLaptopBrandNumListForMale().toString());
			
//			print(sample.getLaptopBrandLabelList().toString());
//			print(sample.brandSampleUnitForMale.toString());
//			print(sample.brandSampleUnitForFemale.toString());
//			print(sample.getLaptopBrandLabelList().toString());
//			print(sample.getLaptopBrandNumList().toString());
//			print(sample.getLaptopBrandDoubleList().toString());
			print(db.getInfoData().toString());
			
//			Object obj;
//			while ((obj = sample.model.testNext()) != null) {
//				print(obj.toString());
//			}
			/*
			System.out.println(db.getLaptopBrandAsList());
			ArrayList<HashMap<String, String>> temp = db.getLaptopModelAsList();
			for (HashMap<String, String> hm : temp) {
				System.out.println(hm);
			}
			*/
			//DataSampleForUnder10 sample = new DataSampleForUnder10(db);
			/* generate users */
//			int num10 = 50; // < 10
//			int num20 = 100; // 10 ~ 20
//			int num30 = 400; // 20 ~ 30
//			int num40 = 200; // 30 ~ 40
//			int numGreaterThan40 = 200; // > 40
//			
//			db.simulateUser(10, num10);
			
			/* end of generation of users */
			
			
			/* for laptops database and their relative tables */
//			JSONParser parser = new JSONParser();
//			
//			try {
//				FileReader file = new FileReader("/Users/junli/workspace/WhaTech/src/dataModel/laptop_temp.txt");
//				BufferedReader buf = new BufferedReader(file);
//				
//				int count = 0;
//				int id = 1705;
//				JSONObject obj;
//				String s;
//				while ((s = buf.readLine()) != null) {
//					obj = (JSONObject) parser.parse(s);
//					if (obj == null) continue;
//					
//					/* for cpu */
////					String brand = (String) obj.get("Manufacturer");
////					String model = (String) obj.get("Model");
////					int count = Integer.parseInt(String.valueOf(obj.get("Count")));
////					int base_speed = Integer.parseInt(String.valueOf(obj.get("Base_speed")));
////					int max_speed = Integer.parseInt(String.valueOf(obj.get("Max_speed")));
////					
////					if (db.insertCPU(id, brand, model, count, base_speed, max_speed) != 1) 
////						System.out.println(obj);
////					id++;
//					/* end of cpu */
//					
//					/* laptop elements to insert */
//					String os = "";
//					if (obj.get("OS") != null)
//						os = (String) obj.get("OS");
//					else 
//						continue;
//					if (os.contains("Android") || os.contains("Phone") || os.equals("")) {
//						continue;
//					}
//					String brand = (String) obj.get("Brand");
//					String model = (String) obj.get("Model");
//					// modify the string model
//					if (model.contains("(") && model.contains(")")) {
//						int index1 = model.indexOf('(');
//						int index2 = model.indexOf(')');
//						model = model.substring(0, index1);
//					} else if (model.contains(",")) {
//						int index = model.indexOf(',');
//						model = model.substring(0, index);
//					}
//					
//					double display_size = Double.parseDouble((String) obj.get("Display_size"));
//					if (display_size == 3.3) display_size = 13.3;
//					
//					String display_res = (String) obj.get("Display_res");
//					String display = (String) obj.get("Display");
//					String graphic = (String) obj.get("Graphic");
//					String graphic_brand = (String) obj.get("Graphic_brand");
//					if (!graphic_brand.equals("AMD") && !graphic_brand.equals("NVIDIA") && !graphic_brand.equals("Intel")) continue;
//					String graphic_model = (String) obj.get("Graphic_model");
//					String processor = (String) obj.get("Processor");
//					String processor_brand = (String) obj.get("Processor_brand");
//					if (!processor_brand.equals("AMD") && !processor_brand.equals("Intel")) continue;
//					
//					String processor_model = (String) obj.get("Processor_model");
//					int memory = Integer.parseInt(String.valueOf(obj.get("Memory")));
//					if (memory == 0) memory = 8;
//					//System.out.println(obj.get("Memory"));
//					int storage = Integer.parseInt(String.valueOf(obj.get("Storage")));
//					double weight = Double.parseDouble(String.valueOf(obj.get("Weight")));
//					double battery = Double.parseDouble(String.valueOf(obj.get("Batterty")));
//					double price = 0;
//					
//					if (!obj.get("Price").equals(""))
//						price = Double.parseDouble(String.valueOf(obj.get("Price")));
//					
//					
//					
//					 /* End of the elements defined */
//					/*
//					String brand = "Intel";//(String) obj.get("Manufacturer");
//					String model = (String) obj.get("Model");
//					int memory = 0;
//					int speed = Integer.parseInt((String) obj.get("Speed"));
//					*/
////					int count = Integer.parseInt((String) obj.get("Count"));
////					int base_speed = Integer.parseInt((String) obj.get("Base_speed"));
////					int max_speed = base_speed;
//					
////					if (!obj.get("Max_speed").equals("")) {
////						max_speed = Integer.parseInt((String) obj.get("Max_speed"));
////					}
//						
//					// insert data
//					int display_id = -1;
//					int processor_id = -1;
//					int graphic_id = -1;
//					if (db.getCPU(processor_brand, processor_model) != -1 &&
//							db.getDisplay(display_res, display_size) != -1 &&
//							db.getGPU(graphic_brand, graphic_model) != -1) { ;
//						display_id = db.getDisplay(display_res, display_size);
//						processor_id = db.getCPU(processor_brand, processor_model);
//						graphic_id = db.getGPU(graphic_brand, graphic_model);
//						
//						db.insertLaptop(id, brand, model, os, processor_id, graphic_id, display_id, storage, memory, battery, weight, price);
//					} else {
//						System.out.println(obj);
//					}
//					count++;
//					id++;
//				}
//				buf.close();
//				file.close();
//				System.out.println(count);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			}
			/* end of the laptops and their relative tables */
			
	}
	static public void print(String x) {
		System.out.println(x);
	}
	static public void print(int x) {
		System.out.println(x);
	}
	static public void print(boolean x) {
		System.out.println(x);
	}
	static public void print(double d) {
		System.out.println(d);
	}
}
