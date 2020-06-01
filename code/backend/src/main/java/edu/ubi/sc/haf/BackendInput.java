package edu.ubi.sc.haf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ubi.sc.haf.core.RangeFilter;

public class BackendInput {
	public static String EFFICACY_STR = "efficacy";
	
	public static String SAFETY_STR = "safety";
	
	public static String DURATION_STR = "duration";
	
	public static String NUM_PATIENTS_STR = "numPatients";
	
	public static String AVG_AGE_STR = "avgAge";
	
	public static String PUBLICATION_YEAR_STR = "publicationYear";
	
	public String disease;
	
	public String drug1;
	
	public String drug2;
	
	public HashMap<String, RangeFilter> filters; // duration,numPatients,avgAge
	
	public HashMap<String,Double> weights;
	
	public List<ClinicalTrial> evidence;
	
	public boolean filtersModified;
}

