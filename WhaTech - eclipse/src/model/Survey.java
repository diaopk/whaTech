package model;

import java.io.Serializable;

public class Survey implements Serializable {
	private String gender, laptop;
	private int age;
	
	public Survey(String g, int a,String l) {
		gender = g; age = a;
		laptop = (l == null)? null : l;
	}
	
	public Survey() {
		gender = laptop = null;
		age = 0;
	}
	
	public void setGender(String g) { gender = g; }
	public void setAge(int a) { age = a; }
	public void setLaptop(String l) { laptop = l; }
	
	public String gender() { return gender; }
	public int age() { return age; }
	public String laptop() { return laptop; }
	
	public String toString() {
		return "{gender: "+gender+", age: "+age+", laptop: "+laptop+"}";
	}
	
}
