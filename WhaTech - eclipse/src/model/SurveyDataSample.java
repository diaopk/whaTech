package model;

import java.util.*;

/* 
 * this class provide comprehensive data sample for dataParser
 * basically every data comes into the database SurveyDataSample gets data analysed
 * and update its figure in the class
 * for example, a SurveyDataSample is going to calculate how many acer's laptops are used in
 * age group of 10 ~ 20 and throws out a popular model of this laptop brand
 */
public abstract class SurveyDataSample {
	
	// a list of surveySampleUnit that helps to analyse the SurveyDataSample
	// for a particular component
	// check out SurveySampleUnit methods and see what it can do
	public SurveySampleUnit brandSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit modelSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit cpuBrandSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit cpuModelSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit gpuBrandSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit gpuModelSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit displaySizeSampleUnitForMale = new SurveySampleUnit();
	public SurveySampleUnit osSampleUnitForMale = new SurveySampleUnit();

	public SurveySampleUnit brandSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit modelSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit cpuBrandSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit cpuModelSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit gpuBrandSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit gpuModelSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit displaySizeSampleUnitForFemale = new SurveySampleUnit();
	public SurveySampleUnit osSampleUnitForFemale = new SurveySampleUnit();

	public SurveySampleUnit ageSampleUnit = new SurveySampleUnit();
	
	// a list of getter methods that return a particular component in order
	public abstract ArrayList<Object> getLaptopBrandListForFemale();
	public abstract ArrayList<Object> getLaptopModelListForFemale();
	public abstract ArrayList<Object> getCPUBrandListForFemale();
	public abstract ArrayList<Object> getCPUModelListForFemale();
	public abstract ArrayList<Object> getGPUBrandListForFemale();
	public abstract ArrayList<Object> getGPUModelListForFemale();
	public abstract ArrayList<Object> getDisplaySizeListForFemale();
	public abstract ArrayList<Object> getDisplayResListForFemale();
	public abstract ArrayList<Object> getPriceListForFemale();

	public abstract ArrayList<Object> getLaptopBrandListForMale();
	public abstract ArrayList<Object> getLaptopModelListForMale();
	public abstract ArrayList<Object> getCPUBrandListForMale();
	public abstract ArrayList<Object> getCPUModelListForMale();
	public abstract ArrayList<Object> getGPUBrandListForMale();
	public abstract ArrayList<Object> getGPUModelListForMale();
	public abstract ArrayList<Object> getDisplaySizeListForMale();
	public abstract ArrayList<Object> getDisplayResListForMale();
	public abstract ArrayList<Object> getPriceListForMale();

	// update the getXXList() whenever a new data comes in
	// a new data is from submitted survey by a user
	public abstract void update();
	
	// a list of getter methods that return a particular component in percentage
	public abstract ArrayList<Object> getLaptopBrandListForMaleInPercentage();
	public abstract ArrayList<Object> getLaptopModelListForMaleInPercentage();
	public abstract ArrayList<Object> getCPUBrandListForMaleInPercentage();
	public abstract ArrayList<Object> getCPUModelListForMaleInPercentage();
	public abstract ArrayList<Object> getGPUBrandListForMaleInPercentage();
	public abstract ArrayList<Object> getGPUModelListForMaleInPercentage();
	public abstract ArrayList<Object> getDisplaySizeListForMaleInPercentage();
	public abstract ArrayList<Object> getDisplayResListForMaleInPercentage();
	public abstract ArrayList<Object> getPriceListForMaleInPercentage();

	public abstract ArrayList<Object> getLaptopBrandListForFemaleInPercentage();
	public abstract ArrayList<Object> getLaptopModelListForFemaleInPercentage();
	public abstract ArrayList<Object> getCPUBrandListForFemaleInPercentage();
	public abstract ArrayList<Object> getCPUModelListForFemaleInPercentage();
	public abstract ArrayList<Object> getGPUBrandListForFemaleInPercentage();
	public abstract ArrayList<Object> getGPUModelListForFemaleInPercentage();
	public abstract ArrayList<Object> getDisplaySizeLisForFemaleInPercentage();
	public abstract ArrayList<Object> getDisplayResListForFemaleInPercentage();
	public abstract ArrayList<Object> getPriceListForFemaleInPercentage();
	
	// a list of getter methods that returns how many records a particular component has
	public abstract ArrayList<Object> getLaptopBrandNumListForMale();
	public abstract ArrayList<Object> getLaptopModelNumListForMale();
	public abstract ArrayList<Object> getCPUBrandNumListForMale();
	public abstract ArrayList<Object> getCPUModelNumListForMale();
	public abstract ArrayList<Object> getGPUBrandNumListForMale();
	public abstract ArrayList<Object> getGPUModelNumListForMale();
	public abstract ArrayList<Object> getDisplaySizeNumListForMale();
	public abstract ArrayList<Object> getDisplayResNumListForMale();
	public abstract ArrayList<Object> getPriceNumListForMale();

	public abstract ArrayList<Object> getLaptopBrandNumListForFemale();
	public abstract ArrayList<Object> getLaptopModelNumListForFemale();
	public abstract ArrayList<Object> getCPUBrandNumListForFemale();
	public abstract ArrayList<Object> getCPUModelNumListForFemale();
	public abstract ArrayList<Object> getGPUBrandNumListForFemale();
	public abstract ArrayList<Object> getGPUModelNumListForFemale();
	public abstract ArrayList<Object> getDisplaySizeNumListForFemale();
	public abstract ArrayList<Object> getDisplayResNumListForFemale();
	public abstract ArrayList<Object> getPriceNumListForFemale();
	
}
