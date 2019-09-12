package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.mysql.jdbc.PreparedStatement;

public class DatabaseHandler {
	
	// define a list of attributes used for connecting to the database
	private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final static String DATABASE_NAME = "whatech";
	private final static String HOST_NAME = "localhost";
	private final static String PORT = "3306"; // default port 
	private final static String OPTIONS = "?verifyServerCertificate=false&useSSL=true";
	private final static String URL = "jdbc:mysql://"+HOST_NAME+":"+PORT+"/"+DATABASE_NAME+OPTIONS;
	private final static String username = "whatech_admin";
	private final static String password = "517316651a";
	
	// sql for checking database's state
	private final String checkStateSql = "SELECT COUNT(*) FROM users_temp";

	// statement objects
	private java.sql.PreparedStatement pst;
	private Statement st;
	
	// integer stores number of record in users table
	private int records = 0;
	
	// connection reference
	private Connection conn;

	// list that is reused as a returned list in methods like get..()
	ArrayList<HashMap<String, Object>> sample = new ArrayList<HashMap<String, Object>>();
	ArrayList<String> tempList = new ArrayList<String>();
	
	public DatabaseHandler() {
		
		System.out.println("Connecting database...");
		try {
			/*
			 * the database will keep open when it creates until the close() calls
			 */
			
			// load the driver
			Class.forName(JDBC_DRIVER);
			System.out.println("Database driver loaded");
			
			// connect
			conn = DriverManager.getConnection(URL, username, password);
			System.out.println("Database connected");
			
			st = null;
			pst = null;
			
			// initialise the number of records
			updateRecords();
		}
		catch (SQLException e) { System.out.println("Cannot connect to the database"); }
		catch (ClassNotFoundException e) { System.out.println("Unable to load the driver: "+e); }
	}
	
	// close the connection
	public void close() {
		try { 
			conn.close();
			System.out.println("Connection closed");
		} 
		catch (SQLException e) { System.out.println("Error in closing the connection: "+e); }
	}
	
	// check if there is a change in users table
	public boolean dbStateIsChanged() {
		
		int num = 0;
		
		try {

			st = conn.createStatement();
			ResultSet rs = st.executeQuery(checkStateSql);
			
			while (rs.next()) {
				num = rs.getInt(1);
			}
			
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// if there is no change in db return false otherwise return true
		return !(num == records);
	}
	
	// update number of records
	public void updateRecords() {
		try {
			
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(checkStateSql);
			
			while (rs.next()) {
				records = rs.getInt(1);
			}
			
			rs.close();
			st.close();
			
			System.out.println("records: "+records);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// insert new user with given age, gender and laptop model
	public int insertUser(int age, String gender, String laptopModel) {
		
		int laptopId;
//		String[] brandAndModel = laptop.split(" ");
//		String brand = brandAndModel[0];
//		String model = brandAndModel[1];

		String insertSql = "INSERT INTO users_temp (gender, age, laptop_id) VALUES (?, ?, ?)";
		String searchSql = "SELECT id FROM laptops WHERE model='"+laptopModel+"'";
		System.out.println(searchSql);
		try {

			st = conn.createStatement();
			ResultSet rs = st.executeQuery(searchSql);
			
			// get the laptop id if exist
			if (rs.next()) {
				laptopId = rs.getInt("id");
				System.out.println("laptop id: "+laptopId);
				
				// insert the new user
				pst = conn.prepareStatement(insertSql);
				pst.setString(1, gender);
				pst.setInt(2, age);
				pst.setInt(3, laptopId);
				
				// return number of rows affected
				return pst.executeUpdate();
			}	
		} catch (SQLException e) { e.printStackTrace(); }
		
		return 0;
	}
	
	// simulate the users into database
	public void simulateUser(int age, int numAge) {
		
	// define sql
			String sql = "INSERT INTO users_temp (gender, age, laptop_id) VALUES (?, ?, ?)";
			String[] randomGender = new String[] {
				"f", "m", "m",
			};
			int randomGenderIndex;
			int randomAge;
			int randomLaptopId;
			//int randomLaptopIdOutside;
			int laptopId;
			ArrayList<Integer> randomId = new ArrayList<Integer>();
			ArrayList<Integer> randomId2 = new ArrayList<Integer>();
			ArrayList<Integer> randomId3 = new ArrayList<Integer>();
			
			try {
				
				// determine the user age
				if (age <= 10) {
					// age < 10
					// numId: 80%, numIdOuside: 15%, numIdOutside2: 5%
					
					String subsql = "SELECT COUNT(*) FROM laptops WHERE price != 0 and price < 400";
					String subsql2 = "SELECT COUNT(*) FROM laptops WHERE price >= 400 and price < 1000";
					String subsql3 = "SELECT COUNT(*) FROM laptops WHERE price >= 1000";
					// get how many laptops to choose
					ResultSet numIdRs = conn.createStatement().executeQuery(subsql);
					ResultSet numIdRs2 = conn.createStatement().executeQuery(subsql2);
					ResultSet numIdRs3 = conn.createStatement().executeQuery(subsql3);
					numIdRs.next();
					int numId = numIdRs.getInt("COUNT(*)"); // how many rows data inside the required scope
					numIdRs.close();
					numIdRs2.next();
					int numIdOutside = numIdRs2.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs2.close();
					numIdRs3.next();
					int numIdOutside2 = numIdRs3.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs3.close();
					
					// collect all required laptop id
					String laptopsql = "SELECT id FROM laptops WHERE price != 0 and price < 400";
					String laptopsqlOutside = "SELECT id FROM laptops WHERE price >= 400 and price < 1000";
					String laptopsqlOutside2 = "SELECT id FROM laptops WHERE price >= 1000";
					ResultSet laptopRs = conn.createStatement().executeQuery(laptopsql); // retrieve laptop id with price less than 350
					ResultSet laptopRsOutside = conn.createStatement().executeQuery(laptopsqlOutside); // retrieve laptop id with price >350 and <1000
					ResultSet laptopRsOutside2 = conn.createStatement().executeQuery(laptopsqlOutside2); // retrieve laptop id with price > 1000
					
					// add laptop id to arraylist randomId
					while (laptopRs.next()) {
						randomId.add(laptopRs.getInt("id"));
					}
					laptopRs.close();
					
					int i = 0;
					int numOutsideScope = numId*3/16;
					while(i < numOutsideScope) {
						laptopRsOutside.absolute((int) (Math.random()*numIdOutside));
						randomId.add(laptopRsOutside.getInt("id"));
						i++;
					}
					laptopRsOutside.close();
					
					i = 0;
					int numOutsideScope2 = numId*1/16;
					while(i < numOutsideScope2) {
						laptopRsOutside2.absolute((int) (Math.random()*numIdOutside2));
						randomId.add(laptopRsOutside2.getInt("id"));
						i++;
					}
					laptopRsOutside2.close();
					
					i = 0;
					while (i < numAge) {
						// randomly pick up one
						randomAge = (int) (Math.random()*5 + 5);
						randomGenderIndex = (int) (Math.random()*3);
						randomLaptopId = (int) (Math.random()*(numId+numOutsideScope+numOutsideScope2)-1);
						laptopId = randomId.get(randomLaptopId);

						// insert data
						pst = conn.prepareStatement(sql);
						pst.setString(1, randomGender[randomGenderIndex]);
						pst.setInt(2, randomAge);
						pst.setInt(3, laptopId);

						pst.executeUpdate();
						i++;
					}

					pst.close();
					
				} else if (age <= 20) {
					// age: 10 ~ 20
					// numId: 70%, numIdOuside: 20%, numIdOutside2: 10%
					
					String subsql = "SELECT COUNT(*) FROM laptops WHERE price != 0 and price < 400";
					String subsql2 = "SELECT COUNT(*) FROM laptops WHERE price >= 400 and price < 1000";
					String subsql3 = "SELECT COUNT(*) FROM laptops WHERE price >= 1000";
					// get how many laptops to choose
					ResultSet numIdRs = conn.createStatement().executeQuery(subsql);
					ResultSet numIdRs2 = conn.createStatement().executeQuery(subsql2);
					ResultSet numIdRs3 = conn.createStatement().executeQuery(subsql3);
					numIdRs.next();
					int numId = numIdRs.getInt("COUNT(*)"); // how many rows data inside the required scope
					numIdRs.close();
					numIdRs2.next();
					int numIdOutside = numIdRs2.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs2.close();
					numIdRs3.next();
					int numIdOutside2 = numIdRs3.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs3.close();
					
					// collect all required laptop id
					String laptopsql = "SELECT id FROM laptops WHERE price != 0 and price < 400";
					String laptopsqlOutside = "SELECT id FROM laptops WHERE price >= 400 and price < 1000";
					String laptopsqlOutside2 = "SELECT id FROM laptops WHERE price >= 1000";
					ResultSet laptopRs = conn.createStatement().executeQuery(laptopsql); // retrieve laptop id with price less than 350
					ResultSet laptopRsOutside = conn.createStatement().executeQuery(laptopsqlOutside); // retrieve laptop id with price >350 and <1000
					ResultSet laptopRsOutside2 = conn.createStatement().executeQuery(laptopsqlOutside2); // retrieve laptop id with price > 1000
					
					// add laptop id to arraylist randomId
					while (laptopRs.next()) {
						randomId.add(laptopRs.getInt("id"));
					}
					laptopRs.close();
					
					int i = 0;
					int numOutsideScope = numId*2/7;
					while(i < numOutsideScope) {
						laptopRsOutside.absolute((int) (Math.random()*numIdOutside));
						randomId.add(laptopRsOutside.getInt("id"));
						i++;
					}
					laptopRsOutside.close();
					
					i = 0;
					int numOutsideScope2 = numId*1/7;
					while(i < numOutsideScope2) {
						laptopRsOutside2.absolute((int) (Math.random()*numIdOutside2));
						randomId.add(laptopRsOutside2.getInt("id"));
						i++;
					}
					laptopRsOutside2.close();
					
					i = 0;
					while (i < numAge) {
						// randomly pick up one
						randomAge = (int) (Math.random()*10 + 10);
						randomGenderIndex = (int) (Math.random()*3);
						randomLaptopId = (int) (Math.random()*(numId+numOutsideScope+numOutsideScope2)-1);
						laptopId = randomId.get(randomLaptopId);

						// insert data
						pst = conn.prepareStatement(sql);
						pst.setString(1, randomGender[randomGenderIndex]);
						pst.setInt(2, randomAge);
						pst.setInt(3, laptopId);

						pst.executeUpdate();
						i++;
					}
					pst.close();
					
				} else if (age <= 30) {
					// age: 20 ~ 30
					// numId: 50%, numIdOuside: 15%, numIdOutside2: 35%
					//select l.id, l.brand, l.model, p.model from (laptops as l join processors as p on l.processor_id=p.id) where p.model like '%i5%' or p.model like '%i7%'
					
					String subsql = "select COUNT(*) from (select l.id, l.memory_gb from "
							+ "(laptops as l join processors as p on l.processor_id=p.id) "
							+ "where (p.model like '%i5%' or p.model like '%i7%') and l.price != 0) as a "
							+ "where a.memory_gb >=8 and a.memory_gb <= 16;";
					String subsql2 = "SELECT COUNT(*) FROM laptops WHERE brand='Apple'";
					String subsql3 = "SELECT COUNT(*) FROM laptops WHERE price != 0";
					// get how many laptops to choose
					ResultSet numIdRs = conn.createStatement().executeQuery(subsql);
					ResultSet numIdRs2 = conn.createStatement().executeQuery(subsql2);
					ResultSet numIdRs3 = conn.createStatement().executeQuery(subsql3);
					numIdRs.next();
					int numId = numIdRs.getInt("COUNT(*)"); // how many rows data inside the required scope
					numIdRs.close();
					numIdRs2.next();
					int numIdOutside2 = numIdRs2.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs2.close();
					numIdRs3.next();
					int numIdOutside3 = numIdRs3.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs3.close();
					
					// collect all required laptop id
					String laptopsql = "select id from (select l.id, l.memory_gb from "
							+ "(laptops as l join processors as p on l.processor_id=p.id) "
							+ "where (p.model like '%i5%' or p.model like '%i7%') and l.price != 0) as a "
							+ "where a.memory_gb >=8 and a.memory_gb <= 16;";
					String laptopsql2 = "SELECT id FROM laptops WHERE brand = 'Apple'";
					String laptopsql3 = "SELECT id FROM laptops WHERE price != 0";
					ResultSet laptopRs = conn.createStatement().executeQuery(laptopsql); // retrieve laptop id with price less than 350
					ResultSet laptopRsOutside2 = conn.createStatement().executeQuery(laptopsql2); // retrieve laptop id with price >350 and <1000
					ResultSet laptopRsOutside3 = conn.createStatement().executeQuery(laptopsql3); // retrieve laptop id with price > 1000
					
					// add laptop id to arraylist randomId
					while (laptopRs.next()) {
						randomId.add(laptopRs.getInt("id"));
					}
					laptopRs.close();
					
					// add element to randomId2, which is an arraylist storing element about laptopsql2
					while(laptopRsOutside2.next())
						randomId2.add(laptopRsOutside2.getInt("id"));
						
					laptopRsOutside2.close();
					
					// generate random from randomId2 to randomId
					int i = 0;
					int numOutsideScope2 = numId*3/10;
					int j;
					while (i < numOutsideScope2) {
						j = (int) (Math.random()*numIdOutside2);
						randomId.add(randomId2.get(j));
						i++;
					}
					
					// add element to randomId3, which is an arraylist storing element about laptopsql3
					while(laptopRsOutside3.next())
						randomId3.add(laptopRsOutside3.getInt("id"));

					laptopRsOutside3.close();
					
					// generate random from randomId3 to randomId
					i = 0;
					int numOutsideScope3 = numId*7/10;
					while (i < numOutsideScope3) {
						j = (int) (Math.random()*numIdOutside3);
						randomId.add(randomId3.get(j));
						i++;
					}
					
					i = 0;
					while (i < numAge) {
						// randomly pick up one
						randomAge = (int) (Math.random()*10+20);
						randomGenderIndex = (int) (Math.random()*3);
						randomLaptopId = (int) (Math.random()*(numId+numOutsideScope2+numOutsideScope3)-1);
						laptopId = randomId.get(randomLaptopId);

						// insert data
						pst = conn.prepareStatement(sql);
						pst.setString(1, randomGender[randomGenderIndex]);
						pst.setInt(2, randomAge);
						pst.setInt(3, laptopId);

						pst.executeUpdate();
						i++;
					}
					pst.close();
					
				} else if (age <= 40) {
					// age: 30 ~ 40
					// numId: 40%, numIdOuside: 20%, numIdOutside2: 40%
					
					String subsql = "select COUNT(*) from (select l.id, l.memory_gb from "
							+ "(laptops as l join processors as p on l.processor_id=p.id) "
							+ "where (p.model like '%i5%' or p.model like '%i7%') and l.price != 0) as a "
							+ "where a.memory_gb >=8 and a.memory_gb <= 16;";
					String subsql2 = "SELECT COUNT(*) FROM laptops WHERE brand='Apple'";
					String subsql3 = "SELECT COUNT(*) FROM laptops WHERE price != 0";
					// get how many laptops to choose
					ResultSet numIdRs = conn.createStatement().executeQuery(subsql);
					ResultSet numIdRs2 = conn.createStatement().executeQuery(subsql2);
					ResultSet numIdRs3 = conn.createStatement().executeQuery(subsql3);
					numIdRs.next();
					int numId = numIdRs.getInt("COUNT(*)"); // how many rows data inside the required scope
					numIdRs.close();
					numIdRs2.next();
					int numIdOutside2 = numIdRs2.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs2.close();
					numIdRs3.next();
					int numIdOutside3 = numIdRs3.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs3.close();
					
					// collect all required laptop id
					String laptopsql = "select id from (select l.id, l.memory_gb from "
							+ "(laptops as l join processors as p on l.processor_id=p.id) "
							+ "where (p.model like '%i5%' or p.model like '%i7%' or p.model like 'Xeon%') and l.price != 0) as a "
							+ "where a.memory_gb >=8";
					String laptopsql2 = "SELECT id FROM laptops WHERE brand = 'Apple'";
					String laptopsql3 = "SELECT id FROM laptops WHERE price != 0";
					ResultSet laptopRs = conn.createStatement().executeQuery(laptopsql); // retrieve laptop id with price less than 350
					ResultSet laptopRsOutside2 = conn.createStatement().executeQuery(laptopsql2); // retrieve laptop id with price >350 and <1000
					ResultSet laptopRsOutside3 = conn.createStatement().executeQuery(laptopsql3); // retrieve laptop id with price > 1000
					
					// add laptop id to arraylist randomId
					while (laptopRs.next()) {
						randomId.add(laptopRs.getInt("id"));
					}
					laptopRs.close();
					
					// add element to randomId2, which is an arraylist storing element about laptopsql2
					while(laptopRsOutside2.next())
						randomId2.add(laptopRsOutside2.getInt("id"));
						
					laptopRsOutside2.close();
					
					// generate random from randomId2 to randomId
					int i = 0;
					int numOutsideScope2 = numId*1/2;
					int j;
					while (i < numOutsideScope2) {
						j = (int) (Math.random()*numIdOutside2);
						randomId.add(randomId2.get(j));
						i++;
					}
					
					// add element to randomId3, which is an arraylist storing element about laptopsql3
					while(laptopRsOutside3.next())
						randomId3.add(laptopRsOutside3.getInt("id"));

					laptopRsOutside3.close();
					
					// generate random from randomId3 to randomId
					i = 0;
					int numOutsideScope3 = numId;
					while (i < numOutsideScope3) {
						j = (int) (Math.random()*numIdOutside3);
						randomId.add(randomId3.get(j));
						i++;
					}
					
					i = 0;
					while (i < numAge) {
						// randomly pick up one
						randomAge = (int) (Math.random()*10+30);
						randomGenderIndex = (int) (Math.random()*3);
						randomLaptopId = (int) (Math.random()*(numId+numOutsideScope2+numOutsideScope3)-1);
						laptopId = randomId.get(randomLaptopId);

						// insert data
						pst = conn.prepareStatement(sql);
						pst.setString(1, randomGender[randomGenderIndex]);
						pst.setInt(2, randomAge);
						pst.setInt(3, laptopId);

						pst.executeUpdate();
						i++;
					}
					pst.close();
					
				} else if (age >40) {
					// age > 40
					// numId: 80%, numIdOuside: 20%
					
					String subsql = "select COUNT(*) from"
							+ " (laptops as l join processors as p on l.processor_id=p.id) join graphics as g on l.graphic_id=g.id "
							+ "where (p.model like '%i3%' or p.model like '%i5%') "
							+ "and l.price != 0 and l.price <=1500 "
							+ "and l.memory_gb <=8 "
							+ "and g.brand='Intel'";
					//String subsql2 = "SELECT COUNT(*) FROM laptops WHERE brand='Apple'";
					String subsql3 = "SELECT COUNT(*) FROM laptops WHERE price != 0";
					// get how many laptops to choose
					ResultSet numIdRs = conn.createStatement().executeQuery(subsql);
					//ResultSet numIdRs2 = conn.createStatement().executeQuery(subsql2);
					ResultSet numIdRs3 = conn.createStatement().executeQuery(subsql3);
					numIdRs.next();
					int numId = numIdRs.getInt("COUNT(*)"); // how many rows data inside the required scope
					numIdRs.close();
//					numIdRs2.next();
//					int numIdOutside2 = numIdRs2.getInt("COUNT(*)"); // how many rows data outside the required scope
//					numIdRs2.close();
					numIdRs3.next();
					int numIdOutside3 = numIdRs3.getInt("COUNT(*)"); // how many rows data outside the required scope
					numIdRs3.close();
					
					// collect all required laptop id
					String laptopsql = "select l.id from"
							+ " (laptops as l join processors as p on l.processor_id=p.id) join graphics as g on l.graphic_id=g.id "
							+ "where (p.model like '%i3%' or p.model like '%i5%') "
							+ "and l.price != 0 and l.price <=1500 "
							+ "and l.memory_gb <=8 "
							+ "and g.brand='Intel'";
					//String laptopsql2 = "SELECT id FROM laptops WHERE brand = 'Apple'";
					String laptopsql3 = "SELECT id FROM laptops WHERE price != 0";
					ResultSet laptopRs = conn.createStatement().executeQuery(laptopsql); // retrieve laptop id with price less than 350
					//ResultSet laptopRsOutside2 = conn.createStatement().executeQuery(laptopsql2); // retrieve laptop id with price >350 and <1000
					ResultSet laptopRsOutside3 = conn.createStatement().executeQuery(laptopsql3); // retrieve laptop id with price > 1000
					
					// add laptop id to arraylist randomId
					while (laptopRs.next()) {
						randomId.add(laptopRs.getInt("id"));
					}
					laptopRs.close();
					
					// add element to randomId2, which is an arraylist storing element about laptopsql2
//					while(laptopRsOutside2.next())
//						randomId2.add(laptopRsOutside2.getInt("id"));
//						
//					laptopRsOutside2.close();
//					
//					// generate random from randomId2 to randomId
//					int i = 0;
//					int numOutsideScope2 = numId*3/10;
//					int j;
//					while (i < numOutsideScope2) {
//						j = (int) (Math.random()*numIdOutside2);
//						randomId.add(randomId2.get(j));
//						i++;
//					}
					
					// add element to randomId3, which is an arraylist storing element about laptopsql3
					while(laptopRsOutside3.next())
						randomId3.add(laptopRsOutside3.getInt("id"));

					laptopRsOutside3.close();
					
					// generate random from randomId3 to randomId
					int i = 0; int j;
					int numOutsideScope3 = numId/5;
					while (i < numOutsideScope3) {
						j = (int) (Math.random()*numIdOutside3);
						randomId.add(randomId3.get(j));
						i++;
					}
					
					i = 0;
					while (i < numAge) {
						// randomly pick up one
						randomAge = (int) (Math.random()*10+50);
						randomGenderIndex = (int) (Math.random()*3);
						randomLaptopId = (int) (Math.random()*(numId+numOutsideScope3)-1);
						laptopId = randomId.get(randomLaptopId);

						// insert data
						pst = conn.prepareStatement(sql);
						pst.setString(1, randomGender[randomGenderIndex]);
						pst.setInt(2, randomAge);
						pst.setInt(3, laptopId);

						pst.executeUpdate();
						i++;
					}
					pst.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	// return list of HashMap<String, Object> as a sample for a given age range
	// each of HashMap<String, Object> object contains the following info:
	// user id, user age, user gender, laptop brand, laptop model, laptop moemry
	// laptop storage, laptop os, laptop price, cpu brand, cpu model, gpu brand, 
	// gpu model and display size.
	public ArrayList<HashMap<String, Object>> getSampleData(ArrayList<Integer> age, String gender, ArrayList<String> brands ) {

		// a sample to be returned
		sample.clear();
		
		// define a sql that searches particular age range
		String sql = "SELECT * from users_temp as u, laptops as l, processors as p, graphics as g, display as d "
				+ "WHERE u.laptop_id=l.id AND l.processor_id=p.id AND l.graphic_id=g.id AND l.display_id=d.id ";
			
		// if the age is specified
		if (age != null && age.size() > 0 && !age.contains(0)) {
			sql += " AND (";
			for (int i = 0; i < age.size(); i++) {
				if (i != age.size()-1) 
					sql += " (u.age>"+String.valueOf(age.get(i)-9)+" AND u.age<"+String.valueOf(age.get(i))+") OR";
				else {
					if (age.get(i) > 40)
						sql += " u.age>"+String.valueOf(age.get(i)-9)+")";
					else 
						sql += " (u.age>"+String.valueOf(age.get(i)-9)+" AND u.age<"+String.valueOf(age.get(i))+") )";
				}
			}
		}

		// if a gender is specified
		if (gender != "All")
			sql += " AND u.gender='"+gender+"'";
			
		// if brands is specified
		// brands need to be not null, size is greater than 0 and "all" option selected
		// then do not care about the rest of options
		if (brands != null) System.out.println("in the getData(): check brands: "+brands.toString());
		if (brands != null && brands.size() > 0 && !brands.contains("All")) {
			sql += " AND (";
			for (int i = 0; i < brands.size(); i++) {
				if (i != brands.size()-1)
					sql += " l.brand='"+brands.get(i)+"' OR";
				else 
					sql += " l.brand='"+brands.get(i)+"')";
			}
		}
			
		try {
			st = conn.createStatement();
				
			System.out.println("getdata sql: "+sql);
			ResultSet rs = st.executeQuery(sql);
			
			HashMap<String, Object> temp;
				
			while (rs.next()) {
				temp = new HashMap<String, Object>();
					
				// each temp of HashMap<String, Object> contains the following info
				temp.put("userId", rs.getInt("u.id"));
				temp.put("userAge", rs.getInt("u.age"));
				temp.put("userGender", rs.getString("u.gender"));
				temp.put("laptopBrand", rs.getString("l.brand"));
				temp.put("laptopModel", rs.getString("l.model"));
				temp.put("laptopMemory", rs.getInt("l.memory_gb"));
				temp.put("laptopStorage", rs.getInt("l.storage_gb"));
				temp.put("laptopOS", rs.getString("l.os"));
				temp.put("laptopPrice", rs.getDouble("l.price"));
				temp.put("cpuBrand", rs.getString("p.brand"));
				temp.put("cpuModel", rs.getString("p.model"));
				temp.put("gpuBrand", rs.getString("g.brand"));
				temp.put("gpuModel", rs.getString("g.model"));
				temp.put("displaySize", rs.getString("d.size"));
					
				sample.add(temp);
			}
				
			// finally close the statement and resultset
			rs.close();
			st.close();
				
			return sample;
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return null;
	}

	public ArrayList<String> getLabelsForFilterView(String component, ArrayList<Integer> age, String gender) {
		
		// clear before appending
		tempList.clear();
		
		String sql = "SELECT DISTINCT "+component+" from users_temp as u, laptops as l, processors as p, graphics as g, display as d "
				+ "WHERE u.laptop_id=l.id AND l.processor_id=p.id AND l.graphic_id=g.id AND l.display_id=d.id ";
		
		// if the age is specified
		if (age != null && age.size() > 0 && !age.contains(0)) {
			sql += " AND (";
			for (int i = 0; i < age.size(); i++) {
				if (i != age.size()-1) 
					sql += " (u.age>"+String.valueOf(age.get(i)-9)+" AND u.age<"+String.valueOf(age.get(i))+") OR";
				else {
					if (age.get(i) > 40)
						sql += " u.age>"+String.valueOf(age.get(i)-9)+")";
					else 
						sql += " (u.age>"+String.valueOf(age.get(i)-9)+" AND u.age<"+String.valueOf(age.get(i))+") )";
				}
			}
		}
		
		System.out.println("DatabaseHandler.getLabelsForFilterView(): sql "+sql);
		
		try {
			
			// run the statement
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				tempList.add(rs.getString("l.brand"));
			}
			
			rs.close();
			st.close();
			
			return tempList;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	// return list of hashmap objects that is associated with a specific gender
	public ArrayList<HashMap<String, Object>> getDataForGender(String gender) {
	// a sample to be returned
				ArrayList<HashMap<String, Object>> sample = new ArrayList<HashMap<String, Object>>();
			
				// define a sql that searches particular age range
				String sql = "SELECT * from users_temp as u, laptops as l, processors as p, graphics as g, display as d "
						+ "WHERE u.laptop_id=l.id AND l.processor_id=p.id AND l.graphic_id=g.id AND l.display_id=d.id "
						+ "AND u.gender=?";
				
				try {
					pst = conn.prepareStatement(sql);
					pst.setString(1, gender);
					ResultSet rs = pst.executeQuery();
					
					HashMap<String, Object> temp;
					
					while (rs.next()) {
						temp = new HashMap<String, Object>();
						
						// each temp of HashMap<String, Object> contains the following info
						temp.put("userId", rs.getInt("u.id"));
						temp.put("userAge", rs.getInt("u.age"));
						temp.put("userGender", rs.getString("u.gender"));
						temp.put("laptopBrand", rs.getString("l.brand"));
						temp.put("laptopModel", rs.getString("l.model"));
						temp.put("laptopMemory", rs.getInt("l.memory_gb"));
						temp.put("laptopStorage", rs.getInt("l.storage_gb"));
						temp.put("laptopOS", rs.getString("l.os"));
						temp.put("laptopPrice", rs.getDouble("l.price"));
						temp.put("cpuBrand", rs.getString("p.brand"));
						temp.put("cpuModel", rs.getString("p.model"));
						temp.put("gpuBrand", rs.getString("g.brand"));
						temp.put("gpuModel", rs.getString("g.model"));
						temp.put("displaySize", rs.getString("d.size"));
						
						sample.add(temp);
					}
					
					// finally close the statement and resultset
					rs.close();
					st.close();
					
					return sample;
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		return null;
	}
	
	public int insertLaptop(int id, 
			String brand, 
			String model, 
			String os, 
			int processor_id,
			int graphic_id, 
			int display_id, 
			int storage_gb,
			int memory_gb,
			double battery_wh,
			double weight_kg,
			double price) {
		
		String sql = "INSERT INTO laptops (id, brand, model, os, processor_id, graphic_id, display_id, storage_gb, memory_gb, battery_wh, weight_kg, price)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.setString(2, brand);
			pst.setString(3, model);
			pst.setString(4, os);
			pst.setInt(5, processor_id);
			pst.setInt(6, graphic_id);
			pst.setInt(7, display_id);
			pst.setInt(8, storage_gb);
			pst.setInt(9, memory_gb);
			pst.setDouble(10, battery_wh);
			pst.setDouble(11, weight_kg);
			pst.setDouble(12, price);
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int insertGPU(int id, String brand, String model, int memory, int speed) {
		System.out.println(model);
		String sql = "INSERT INTO graphics (id, brand, model, memory_mb, speed_MHz) VALUES (?, ?, ?, ?, ?)";

		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.setString(2, brand);
			pst.setString(3, model);
			pst.setInt(4, memory);
			pst.setInt(5, speed);
			
			return pst.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

	public int insertCPU(int id, String brand, String model, int count, int base_speed_Mhz, int max_speed_Mhz) {
		String sql = "INSERT INTO processors (id, brand, count, base_speed_Mhz, max_speed_MHz, model) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.setString(2, brand);
			pst.setInt(3, count);
			pst.setInt(4, base_speed_Mhz);
			pst.setInt(5, max_speed_Mhz);
			pst.setString(6, model);
			
			return pst.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	public int insertDisplay(int id, String type, float size, String res) {
		String sql = "INSERT TABLE display (id, type, size, ressolution) VALUES (?, ?, ?, ?)";
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.setString(2, type);
			pst.setFloat(3, size);
			pst.setString(4, res);
			
			return pst.executeUpdate();
			
		} catch (SQLException e) { e.printStackTrace(); }
		
		return 0;
	}
	
	public int getDisplay(String res, double size) {
		String sql = "SELECT id FROM display WHERE resolution = ? AND size = ?";
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, res);
			pst.setDouble(2, size);
			ResultSet rs = pst.executeQuery();
			rs.next();
			
			return rs.getInt("id");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int getCPU(String brand, String model) {
		String sql = "SELECT id FROM processors WHERE brand = ? AND model = ?";
		
		try {
			pst = (PreparedStatement) conn.prepareStatement(sql);
			pst.setString(1, brand);
			pst.setString(2, model);
			ResultSet rs = pst.executeQuery();
			rs.next();
			return rs.getInt("id");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int getGPU(String brand, String model) {
		String sql = "SELECT id FROM graphics WHERE brand = ? AND model = ?";
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, brand);
			pst.setString(2, model);
			ResultSet rs = pst.executeQuery();
			rs.next();
			return rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	// retrieve laptop brands as a list
	public ArrayList<String> getLaptopBrandAsList() {
		String sql = "SELECT brand FROM laptops GROUP BY brand";
		ArrayList<String> brands = new ArrayList<String>();
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				brands.add(rs.getString("brand"));
			}
			
			rs.close();
			st.close();
			
		} catch (SQLException e) { e.printStackTrace(); }
		
		return brands;
	}
	
	// Retrieve laptop models associated with their brand
	public ArrayList<HashMap<String, String>> getLaptopModelAsList() {

		String sql = "SELECT brand, model FROM laptops";
		ArrayList<HashMap<String, String>> models = new ArrayList<HashMap<String, String>>();
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				String model = rs.getString("model");
				String brand = rs.getString("brand");
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(model, brand);
				models.add(temp);
			}
			
			rs.close();
			st.close();
			
		} catch (SQLException e) { e.printStackTrace(); }
		
		return models;
	}
	
	// method that randomly generates laptop brand and model
	public String randomMake() {
		// reuse the tempList
		tempList.clear();
		
		String sql = "SELECT DISTINCT brand FROM laptops";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				tempList.add(rs.getString("brand"));
			}

			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// random index
		int index = (int) (Math.random()*tempList.size());
		System.out.println("DatabaseHandler.randomMake(): index: "+index);
		
		// return a random index of makes from tempList
		return tempList.get(index);
	}

	public String randomModel(String brand) {
		// reuse the tempList
		tempList.clear();
		
		String sql = "SELECT DISTINCT model FROM laptops WHERE brand='"+brand+"'";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				tempList.add(rs.getString("model"));
			}

			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// random index
		int index = (int) (Math.random()*tempList.size());
		System.out.println("DatabaseHandler.randomMake(): index: "+index);
		
		// return a random index of makes from tempList
		return tempList.get(index);
	}

	public ArrayList<String> getInfoData() {
		ArrayList<String> result = new ArrayList<String>();
		
		String sql = "SELECT COUNT(*) FROM users_temp WHERE TIMESTAMPDIFF(SECOND, date, NOW())/(3600*24) < ?";
		
		try {

			pst = conn.prepareStatement(sql);
			ResultSet rs;
			
			pst.setInt(1, 90);
			rs = pst.executeQuery();
			if (rs.next()) {
				result.add(String.valueOf(rs.getInt("COUNT(*)")));
			}
			rs.close();
			
			pst.setInt(1, 30);
			rs = pst.executeQuery();
			if (rs.next()) {
				result.add(String.valueOf(rs.getInt("COUNT(*)")));
			}
			rs.close();
			
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			if (rs.next()) {
				result.add(String.valueOf(rs.getInt("COUNT(*)")));
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}