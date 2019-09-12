package model;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Semaphore;

/* class that represents how users use their laptops in number level
 * 
 * 
 */
public class Sample extends SurveyDataSample {
	
	public final static String COLUMN_BRAND = "l.brand";
	public final static String COLUMN_AGE = "u.age";
	
	// semaphore
	private Semaphore sem;
	
	private List<HashMap<String, Object>> sample;
	private DatabaseHandler db;
	
	// options used for queries
	public ArrayList<Integer> age;
	public String gender;
	public ArrayList<String> bs;
	
	// list to be reused as a result list in methods like get...list()
	private ArrayList<Object> tempList = new ArrayList<Object>();
	
	//list of elements in order
	private ArrayList<Object> brandListForMale = new ArrayList<Object>();
	private ArrayList<Object> modelListForMale = new ArrayList<Object>();
	private ArrayList<Object> cpuBrandListForMale = new ArrayList<Object>();
	private ArrayList<Object> cpuModelListForMale = new ArrayList<Object>();
	private ArrayList<Object> gpuBrandListForMale = new ArrayList<Object>();
	private ArrayList<Object> gpuModelListForMale = new ArrayList<Object>();
	private ArrayList<Object> displaySizeListForMale = new ArrayList<Object>();
	private ArrayList<Object> osListForMale = new ArrayList<Object>();
	
	private ArrayList<Object> brandListForFemale = new ArrayList<Object>();
	private ArrayList<Object> modelListForFemale = new ArrayList<Object>();
	private ArrayList<Object> cpuBrandListForFemale = new ArrayList<Object>();
	private ArrayList<Object> cpuModelListForFemale = new ArrayList<Object>();
	private ArrayList<Object> gpuBrandListForFemale = new ArrayList<Object>();
	private ArrayList<Object> gpuModelListForFemale = new ArrayList<Object>();
	private ArrayList<Object> displaySizeListForFemale = new ArrayList<Object>();
	private ArrayList<Object> osListForFemale = new ArrayList<Object>();

	private ArrayList<String> ageLabelList = new ArrayList<String>();

	public Sample(DatabaseHandler d) {
		super();
		db = d;
		sem = new Semaphore(1);
		
		// get the data from db for age under 10
		// the result shown as List of HashMap<String, Object>
		// each of hashmap in the list includes laptop model, laptop brand etc
		sample = db.getSampleData(null, "All", null);

		// init data list with two genders.."All"
		dataListInit();
	}
	
	public void lock() {
		try { sem.acquire(); }
		catch (InterruptedException e) {}
	}
	
	public void unLock() {
		sem.release();
	}

	// retrieve required data from database
	public void retrieveData(ArrayList<Integer> age, String gender, ArrayList<String> bs) {
		this.age = age;
		this.gender = gender;
		this.bs = bs;
		
		sample = db.getSampleData(age, gender, bs);
		
		// update the data list
		dataListInit();
	}
	
	// initialise the data list
	private void dataListInit() {
		
		// clear the SampleUnits
		brandSampleUnitForFemale.clear();
		modelSampleUnitForFemale.clear();
		cpuBrandSampleUnitForFemale.clear();
		cpuModelSampleUnitForFemale.clear();
		gpuBrandSampleUnitForFemale.clear();
		gpuModelSampleUnitForFemale.clear();
		displaySizeSampleUnitForFemale.clear();
		osSampleUnitForFemale.clear();

		brandSampleUnitForMale.clear();
		modelSampleUnitForMale.clear();
		cpuBrandSampleUnitForMale.clear();
		cpuModelSampleUnitForMale.clear();
		gpuBrandSampleUnitForMale.clear();
		gpuModelSampleUnitForMale.clear();
		displaySizeSampleUnitForMale.clear();
		osSampleUnitForMale.clear();
		
		ageSampleUnit.clear();
		
		// reset ageSampleUnit
		ageSampleUnit.resetAge();
		
		// iterate the sample and add them to brand, model , etc
		for (HashMap<String, Object> s : sample) {
			if (s.get("userGender").equals("f")) {
				brandSampleUnitForFemale.add(s.get("laptopBrand"));
				modelSampleUnitForFemale.add(s.get("laptopModel"));
				cpuBrandSampleUnitForFemale.add(s.get("cpuBrand"));
				cpuModelSampleUnitForFemale.add(s.get("cpuModel"));
				gpuBrandSampleUnitForFemale.add(s.get("gpuBrand"));
				gpuModelSampleUnitForFemale.add(s.get("gpuModel"));
				displaySizeSampleUnitForFemale.add(s.get("displaySize"));
				osSampleUnitForFemale.add(s.get("os"));

			} else {
				brandSampleUnitForMale.add(s.get("laptopBrand"));
				modelSampleUnitForMale.add(s.get("laptopModel"));
				cpuBrandSampleUnitForMale.add(s.get("cpuBrand"));
				cpuModelSampleUnitForMale.add(s.get("cpuModel"));
				gpuBrandSampleUnitForMale.add(s.get("gpuBrand"));
				gpuModelSampleUnitForMale.add(s.get("gpuModel"));
				displaySizeSampleUnitForMale.add(s.get("displaySize"));
				osSampleUnitForMale.add(s.get("os"));
			}
			
			ageSampleUnit.addAge((int) s.get("userAge"));
			
		}
		
		// set key list then we can have sorted component list
		brandSampleUnitForFemale.setKeyList();
		modelSampleUnitForFemale.setKeyList();
		cpuBrandSampleUnitForFemale.setKeyList();
		cpuModelSampleUnitForFemale.setKeyList();
		gpuBrandSampleUnitForFemale.setKeyList();
		gpuModelSampleUnitForFemale.setKeyList();
		displaySizeSampleUnitForFemale.setKeyList();
		osSampleUnitForFemale.setKeyList();
		
		brandSampleUnitForMale.setKeyList();
		modelSampleUnitForMale.setKeyList();
		cpuBrandSampleUnitForMale.setKeyList();
		cpuModelSampleUnitForMale.setKeyList();
		gpuBrandSampleUnitForMale.setKeyList();
		gpuModelSampleUnitForMale.setKeyList();
		displaySizeSampleUnitForMale.setKeyList();
		osSampleUnitForMale.setKeyList();

		// the following code are similar, what they do is to 
		// add each object of the component to the componentList shown above 
		// from line 15 to line 20, in order.
		// meaning the first element in the, for example, brandList is the brand that 
		// most people use
		// laptop brand
		Object obj;
		brandListForFemale.clear();
		while ((obj = brandSampleUnitForFemale.next()) != null)
			brandListForFemale.add(obj);
		
		// laptop model
		modelListForFemale.clear();
		while ((obj = modelSampleUnitForFemale.next()) != null)
			modelListForFemale.add(obj);
		
		// cpu brand
		cpuBrandListForFemale.clear();
		while ((obj = cpuBrandSampleUnitForFemale.next()) != null)
			cpuBrandListForFemale.add(obj);
		
		// cpu model
		cpuModelListForFemale.clear();
		while((obj = cpuModelSampleUnitForFemale.next()) != null) 
			cpuModelListForFemale.add(obj);
		
		// gpu brand
		gpuBrandListForFemale.clear();
		while ((obj = gpuBrandSampleUnitForFemale.next()) != null)
			gpuBrandListForFemale.add(obj);
		
		// gpu model
		gpuModelListForFemale.clear();
		while ((obj = gpuModelSampleUnitForFemale.next()) != null)
			gpuModelListForFemale.add(obj);
		
		// display size
		displaySizeListForFemale.clear();
		while ((obj = displaySizeSampleUnitForFemale.next()) != null)
			displaySizeListForFemale.add(obj);
		
		// os
		osListForFemale.clear();
		while ((obj = osSampleUnitForFemale.next()) != null)
			osListForFemale.add(obj);

		// laptop brand
		brandListForMale.clear();
		while ((obj = brandSampleUnitForMale.next()) != null)
			brandListForMale.add(obj);
		
		// laptop model
		modelListForMale.clear();
		while ((obj = modelSampleUnitForMale.next()) != null)
			modelListForMale.add(obj);
		
		// cpu brand
		cpuBrandListForMale.clear();
		while ((obj = cpuBrandSampleUnitForMale.next()) != null)
			cpuBrandListForMale.add(obj);
		
		// cpu model
		cpuModelListForMale.clear();
		while((obj = cpuModelSampleUnitForMale.next()) != null) 
			cpuModelListForMale.add(obj);
		
		// gpu brand
		gpuBrandListForMale.clear();
		while ((obj = gpuBrandSampleUnitForMale.next()) != null)
			gpuBrandListForMale.add(obj);
		
		// gpu model
		gpuModelListForMale.clear();
		while ((obj = gpuModelSampleUnitForMale.next()) != null)
			gpuModelListForMale.add(obj);
		
		// display size
		displaySizeListForMale.clear();
		while ((obj = displaySizeSampleUnitForMale.next()) != null)
			displaySizeListForMale.add(obj);
		
		// os
		osListForMale.clear();
		while ((obj = osSampleUnitForMale.next()) != null)
			osListForMale.add(obj);
			
		// age
		ageLabelList.clear();
		ageLabelList = ageSampleUnit.getAgeLabelList();
		
	}
	
	/* list of overridden methods, check out the abstract class for the description */
	
	@Override
	public void update() {
		// do some updates when new data comes in
		// update the sample and all lists
	}

	@Override
	public ArrayList<Object> getLaptopBrandListForFemale() {
		return brandListForFemale;
	}

	@Override
	public ArrayList<Object> getLaptopModelListForFemale() {
		return modelListForFemale;
	}

	@Override
	public ArrayList<Object> getCPUBrandListForFemale() {
		return cpuBrandListForFemale;
	}

	@Override
	public ArrayList<Object> getCPUModelListForFemale() {
		return cpuModelListForFemale;
	}

	@Override
	public ArrayList<Object> getGPUBrandListForFemale() {
		return null;
	}

	@Override
	public ArrayList<Object> getGPUModelListForFemale() {
		return gpuModelListForFemale;
	}

	@Override
	public ArrayList<Object> getDisplaySizeListForFemale() {
		return displaySizeListForFemale;
	}

	@Override
	public ArrayList<Object> getDisplayResListForFemale() {
		return null;
	}

	@Override
	public ArrayList<Object> getPriceListForFemale() {
		return null;
	}

	@Override
	public ArrayList<Object> getLaptopBrandListForFemaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : brandListForFemale)
			list.add(brandSampleUnitForFemale.getValueInPrecentage(obj));

		return list;
	}

	@Override
	public ArrayList<Object> getLaptopModelListForFemaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : modelListForFemale)
			list.add(modelSampleUnitForFemale.getValueInPrecentage(obj));
		
		return list;
	}

	@Override
	public ArrayList<Object> getCPUBrandListForFemaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : cpuBrandListForFemale) 
			list.add(cpuBrandSampleUnitForFemale.getValueInPrecentage(obj));
		return list;
	}

	@Override
	public ArrayList<Object> getCPUModelListForFemaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : cpuModelListForFemale)
			list.add(cpuModelSampleUnitForFemale.getValueInPrecentage(obj));
		return list;
	}

	@Override
	public ArrayList<Object> getGPUBrandListForFemaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getGPUModelListForFemaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getDisplaySizeLisForFemaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getDisplayResListForFemaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getPriceListForFemaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* getter method to return number of record for particular component */
	@Override
	public ArrayList<Object> getLaptopBrandNumListForFemale() {
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : brandListForFemale)
			list.add(brandSampleUnitForFemale.get(k));
		return list;
	}

	@Override
	public ArrayList<Object> getLaptopModelNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : modelListForFemale)
			list.add(modelSampleUnitForFemale.get(k));
		return list;
	}

	@Override
	public ArrayList<Object> getCPUBrandNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
			for (Object k : cpuBrandListForFemale)
			list.add(cpuBrandSampleUnitForFemale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getCPUModelNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : cpuModelListForFemale)
			list.add(cpuModelSampleUnitForFemale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getGPUBrandNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : gpuBrandListForFemale)
			list.add(gpuBrandSampleUnitForFemale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getGPUModelNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : gpuModelListForFemale)
			list.add(gpuModelSampleUnitForFemale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getDisplaySizeNumListForFemale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : displaySizeListForFemale)
			list.add(displaySizeSampleUnitForFemale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getDisplayResNumListForFemale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getPriceNumListForFemale() {
		// TODO Auto-generated method stub
		return null;
	}

	/* == For male ==*/
	@Override
	public ArrayList<Object> getLaptopBrandListForMale() {
		return brandListForMale;
	}

	@Override
	public ArrayList<Object> getLaptopModelListForMale() {
		return modelListForMale;
	}

	@Override
	public ArrayList<Object> getCPUBrandListForMale() {
		return cpuBrandListForMale;
	}

	@Override
	public ArrayList<Object> getCPUModelListForMale() {
		return cpuModelListForMale;
	}

	@Override
	public ArrayList<Object> getGPUBrandListForMale() {
		return null;
	}

	@Override
	public ArrayList<Object> getGPUModelListForMale() {
		return gpuModelListForMale;
	}

	@Override
	public ArrayList<Object> getDisplaySizeListForMale() {
		return displaySizeListForMale;
	}

	@Override
	public ArrayList<Object> getDisplayResListForMale() {
		return null;
	}

	@Override
	public ArrayList<Object> getPriceListForMale() {
		return null;
	}

	@Override
	public ArrayList<Object> getLaptopBrandListForMaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : brandListForMale)
			list.add(brandSampleUnitForMale.getValueInPrecentage(obj));

		return list;
	}

	@Override
	public ArrayList<Object> getLaptopModelListForMaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : modelListForMale)
			list.add(modelSampleUnitForMale.getValueInPrecentage(obj));
		
		return list;
	}

	@Override
	public ArrayList<Object> getCPUBrandListForMaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : cpuBrandListForMale) 
			list.add(cpuBrandSampleUnitForMale.getValueInPrecentage(obj));
		return list;
	}

	@Override
	public ArrayList<Object> getCPUModelListForMaleInPercentage() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object obj : cpuModelListForMale)
			list.add(cpuModelSampleUnitForMale.getValueInPrecentage(obj));
		return list;
	}

	@Override
	public ArrayList<Object> getGPUBrandListForMaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getGPUModelListForMaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getDisplaySizeListForMaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getDisplayResListForMaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getPriceListForMaleInPercentage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* getter method to return number of record for particular component */
	@Override
	public ArrayList<Object> getLaptopBrandNumListForMale() {
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : brandListForMale)
			list.add(brandSampleUnitForMale.get(k));
		return list;
	}

	@Override
	public ArrayList<Object> getLaptopModelNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : modelListForMale)
			list.add(modelSampleUnitForMale.get(k));
		return list;
	}

	@Override
	public ArrayList<Object> getCPUBrandNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
			for (Object k : cpuBrandListForMale)
			list.add(cpuBrandSampleUnitForMale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getCPUModelNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : cpuModelListForMale)
			list.add(cpuModelSampleUnitForMale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getGPUBrandNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : gpuBrandListForMale)
			list.add(gpuBrandSampleUnitForMale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getGPUModelNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : gpuModelListForMale)
			list.add(gpuModelSampleUnitForMale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getDisplaySizeNumListForMale() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object k : displaySizeListForMale)
			list.add(displaySizeSampleUnitForMale.get(k));
		return list;

	}

	@Override
	public ArrayList<Object> getDisplayResNumListForMale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getPriceNumListForMale() {
		// TODO Auto-generated method stub
		return null;
	}

	// method that returns a brand list to be used by the filter view
	// as a list of brand options that user can choose
	public ArrayList<Object> getLaptopBrandLabelListForFiterView(String component) {
		
		// clear before appending
		tempList.clear();
		
		tempList.addAll(db.getLabelsForFilterView(component, age, gender));
		
		return tempList;
	}
	
	// method that returns laptop brand list as data source 
	public ArrayList<Object> getLaptopBrandLabelList() {

		// if either male list or female list is empty then return the non-empty one
		if (brandSampleUnitForMale.size() == 0)
			return this.getLaptopBrandListForFemale();

		if (brandSampleUnitForFemale.size() == 0) 
			return this.getLaptopBrandListForMale();

		// an arraylist to be returned
		tempList.clear();
		
		/*
		List<Map.Entry<Object, Integer>> entrysetmale = new ArrayList<>(brandSampleUnitForMale.entrySet());
		List<Map.Entry<Object, Integer>> entrysetfemale = new ArrayList<>(brandSampleUnitForFemale.entrySet());
		
		// temp object referred for finding key from another list
		Object key;
		
		// add value to the tempList
		for (int i = 0; i < entrysetmale.size(); i++) {
			key = entrysetmale.get(i).getKey();
			tempList.add(key);
			tempList.add(key);
		}
		
		// make sure every brand has been added to tempList from male and female list
		for (int i = 0; i < entrysetfemale.size(); i++) {
			key = entrysetfemale.get(i).getKey();
			if (!tempList.contains(key)) {
				tempList.add(key);
				tempList.add(key);
			}
		}
		*/

		ArrayList<Object> malesource = this.getLaptopBrandListForMale();
		ArrayList<Object> femalesource = this.getLaptopBrandListForFemale();
		
		String label;
		for (int i = 0; i < malesource.size(); i++) {
			label = (String) malesource.get(i);
			tempList.add(label);
			tempList.add(label);
			
//			if (femalesource.get(i) != null) 
//				tempList.add(femalesource.get(i));
//			else
		}
		
		return tempList;
		
	}
	
	public ArrayList<Object> getLaptopBrandNumList() {
		
		// if either male or female is empty then return the non-empty one
		if (brandSampleUnitForMale.size() == 0)
			return this.getLaptopBrandNumListForFemale();

		if (brandSampleUnitForFemale.size() == 0) 
			return this.getLaptopBrandNumListForMale();
			
		// result list cleared before appending
		tempList.clear();
		/*	
		// two arraylists created from brandSampleUnits
		List<Map.Entry<Object, Integer>> entrysetmale = new ArrayList<>(brandSampleUnitForMale.entrySet());
		List<Map.Entry<Object, Integer>> entrysetfemale = new ArrayList<>(brandSampleUnitForFemale.entrySet());
		
		// temp object as key
		Object key;
		int value;
		int value2;
		
		// iterate the arraylist and add value to the result
		for (int i = 0; i < entrysetmale.size(); i++) {
				key = entrysetmale.get(i).getKey();
				value = entrysetmale.get(i).getValue();
//				value2 = (entrysetfemale.get(i) != null) ? entrysetfemale.get(i).getValue() : 0;
				
//				for (int j = i; j < entrysetmale.size()-1; j++) {
//					if (value < entrysetmale.get(j+1).getValue()) {
//						value = entrysetmale.get(j+1).getValue();
//					}
//				}
				
				tempList.add(value);

				if (brandSampleUnitForFemale.containsKey(key))
					tempList.add(brandSampleUnitForFemale.get(key));
				else
					tempList.add(0);
		}
		
		// make sure every value from male and female lists has been added to result
		for (int i = 0; i < entrysetfemale.size(); i++) {
				key = entrysetfemale.get(i).getKey();
				value = entrysetfemale.get(i).getValue();
				
				// if male list does not have that key then add 0 to result
				if (!brandSampleUnitForMale.containsKey(key)) {
					tempList.add(0); // for male
					tempList.add(value); // for female
				}
		}
		*/
		
		/* another version */
		tempList.clear();
		ArrayList<Object> malesource = this.getLaptopBrandNumListForMale();
		ArrayList<Object> femalesource = this.getLaptopBrandNumListForFemale();
		
		for (int i = 0; i < malesource.size(); i++) {
			tempList.add(malesource.get(i));
			
			if (femalesource.get(i) != null) 
				tempList.add(femalesource.get(i));
			else
				tempList.add(0);
		}
		
		return tempList;
		
	}
	
	public ArrayList<Object> getLaptopBrandDoubleList() {
		
		ArrayList<Object> source = getLaptopBrandNumList();
		ArrayList<Object> result = new ArrayList<Object>();
		
		int totalValue = 0;

		for (Object value : source) {
			totalValue += (int) value;
		}
		System.out.println("totalValue: "+totalValue);
		
		double d;
		int v;
		double dd = 0.0;
		for (int i = 0; i < source.size(); i++) {
			v = (int) source.get(i);
//			d = (double) (Math.round((v*1.0 / totalValue)));
			d = new BigDecimal((double) (v*1.0 / totalValue)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			dd += d;
			result.add(d);
		}
		System.out.println("dd: "+dd);
		
		return result;
	}
	
	public ArrayList<Object> getLaptopBrandDoubleList1() {

		// if either male or female is empty then return the non-empty one
		if (brandSampleUnitForMale.size() == 0)
			return this.getLaptopBrandListForFemaleInPercentage();

		if (brandSampleUnitForFemale.size() == 0) 
			return this.getLaptopBrandListForMaleInPercentage();

		// an arraylist to be returned
		tempList.clear();
		
		List<Map.Entry<Object, Integer>> entrysetmale = new ArrayList<>(brandSampleUnitForMale.entrySet());
		List<Map.Entry<Object, Integer>> entrysetfemale = new ArrayList<>(brandSampleUnitForFemale.entrySet());

		// temps
		Object key;
		double value;
		
		for (int i = 0; i < entrysetmale.size(); i++) {
			key = entrysetmale.get(i).getKey();
			value = brandSampleUnitForMale.getValueInPrecentage(key);
			tempList.add(value);
				
			if (brandSampleUnitForFemale.containsKey(key))
				tempList.add(brandSampleUnitForFemale.getValueInPrecentage(key));
			else
				tempList.add(0.0);
		}
		
		// make sure every value from male and female lists has been added to tempList
		for (int i = 0; i < entrysetfemale.size(); i++) {
			key = entrysetfemale.get(i).getKey();
			value = brandSampleUnitForFemale.getValueInPrecentage(key);
			
			if (!brandSampleUnitForMale.containsKey(key)) {
				tempList.add(0.0); // for male
				tempList.add(value);
			}
		}
		
		return tempList;
		
	}
	
	public ArrayList<String> getAgeLabelList() {
		return ageLabelList;
	}
	
	public ArrayList<Integer> getAgeNumList() {
		return ageSampleUnit.getAgeNumList();
	}
	
	public ArrayList<Double> getAgeDoubleList() {
		return ageSampleUnit.getAgeDoubleList();
	}
	
	private void addMissingValue(ArrayList<Object> list, String listType) {
		System.out.println("Sample.addMissingValue()");
		if (listType.equals("label")) {
			for (String brand : this.bs)
				if (!list.contains(brand))
					list.add(brand);

		} else if (listType.equals("num")) {
			for (int i = 0; i < bs.size()-1; i++)
				list.add(0);
		
		} else if (listType.equals("double")) {
			for (int i = 0; i < bs.size()-1; i++)
				list.add(0.0);
		}
	}
}