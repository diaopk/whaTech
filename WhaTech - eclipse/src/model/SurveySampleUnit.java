package model;

import java.util.*;
import java.util.function.Consumer;

/*
 * class extending HashMap with additional functions defined
 * SampleUnit is for handling an individual laptop component and make it as a 
 * arranged and mapped structure. For example, a laptop has a brand component and 
 * brands can be unique key in the Map then for this brand SampleUnit store brands
 * as keys and their frequency as the values in the Map, as seen in the method defined
 * below, add(key) is for adding a new brand or incrementing the brand's frequency. 
 * 
 * The reason for making HashMap's key an Object is because a key can be either 
 * String or Integer. For brands the key is String but for prices the key is Integer
 */
public class SurveySampleUnit extends HashMap<Object, Integer> {
	private static final long serialVersionUID = 1L;
	private Object current = null;
	
	// keyList is the ArrayList that store the keys
	// by calling the next() to return the key with most value 
	// and remove it from here keyList
	private ArrayList<Object> keyList;

	// increment to the count of the given key
	public void add(Object key) {
		if (containsKey(key))
			put(key, get(key)+1);
		else {
			put(key, 1);
		}
	}
	
	// reset all value to 0 for all keys
	public void resetAge() {
		put("10>", 0);
		put("10~20", 0);
		put("20~30", 0);
		put("30~40", 0);
		put("40<", 0);
	}
	
	public void addAge(int key) {
		if (key > 0 && key <= 10)
			put("10>", get("10>")+1);
		else if (key <= 20)
			put("10~20", get("10~20")+1);
		else if (key <= 30)
			put("20~30", get("20~30")+1);
		else if (key <= 40)
			put("30~40", get("30~40")+1);
		else
			put("40<", get("40<")+1);
	}

	// return the key with most frequency
	public Object getMostKey() {
		int count = 0;
		Object obj = null;
		for (Object key : keySet()) {
			if (count < get(key)) {
				count = get(key);
				obj = key;
			}
		}
		return obj;
	}
	
	// return the key with most frequent value
	public int getKeyWithMostValue() {
		return get(getMostKey());
	}
	
	// return the value with most frequency in percentage
	public double getMostValueInPrecentage() {
		return getKeyWithMostValue() / totalValue();
	}
	
	// return the value in percentage for a given key
	public double getValueInPrecentage(Object key) {
		return (double)Math.round(((double)get(key) / totalValue())*100)/100;
	}
	
	// values in total
	// values() from HashMap<,> saying values for each key shown as a collection
	// which means values can be iterated
	public int totalValue() {
		int count = 0;
		for (Integer i : values())
			count += i;
		return count;
	}
	
	// return the key with most values
	public Object next() {
		return recuNext(0, 1, keyList);
	}
	
	// recursive return the key with most values
	// return one key then remove it from the keyList
	public Object recuNext(int currIndex, int tempIndex, ArrayList<Object> list) {
		Object obj;
		if (list.size() == 0) {
			return null;
		}	else if (list.size() == 1) {
			obj = list.get(0);
			list.remove(0);
			return obj;
		}
		// check if the tempIndex reaches the last index meaning the comparison 
		// almost finishes. the last step is to compare last two indexes and return 
		else if (tempIndex == list.size()-1) {
			if (get(list.get(currIndex)) >= get(list.get(tempIndex))) {
				obj = list.get(currIndex);
				list.remove(obj);
				tempIndex--;
				return obj;
			} else {
				obj = list.get(tempIndex);
				list.remove(tempIndex);
				tempIndex--;
				return obj;
			}
		}	

		// check if the current index >= tempIndex, if so then keep the current index
		// and increment the tempIndex. the tempIndex always increments until
		// it reaches the last index of the list
		else if (get(list.get(currIndex)) >= get(list.get(tempIndex))) {
			return recuNext(currIndex, tempIndex+1, list);
		} 
		
		// check if the current index < tempIndex, if so increment the current Index 
		// to the next index, which means use the larger one to compare the rest of
		// the list
		else {
			currIndex = tempIndex;
			return recuNext(currIndex, tempIndex+1, list);
		}
	}

	public ArrayList<String> getAgeLabelList() {
		ArrayList<String> list = new ArrayList<String>();
		
		for (Object age : this.keySet())
			if (get(age) != 0)
				list.add((String) age);
		
		// sort the result list
		Collections.sort(list);
		return list;
	}
	
	// method that returns a list of integer for each age
	public ArrayList<Integer> getAgeNumList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for (String age : getAgeLabelList())
			if (get(age) != 0)
			list.add(this.get(age));
		
		return list;
	}
	
	// return a list of double values representing the percentage of each age
	public ArrayList<Double> getAgeDoubleList() {
		ArrayList<Double> list = new ArrayList<Double>();
		
		for (String age : getAgeLabelList())
			if (get(age) != 0)
				list.add(this.getValueInPrecentage(age));
		
		return list;
	}

	public void setKeyList() {
		keyList = new ArrayList<Object>(Arrays.asList(keySet().toArray()));
	}
	public ArrayList<Object> getTemp() { return keyList; }
}
