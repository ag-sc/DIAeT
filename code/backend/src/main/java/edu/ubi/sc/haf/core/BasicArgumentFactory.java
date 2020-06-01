package edu.ubi.sc.haf.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.ubi.sc.haf.ClinicalTrial;


public interface BasicArgumentFactory {
	
	public void createBasicArguments();
	
	public List<ClinicalTrial> getTrials();
	
	public List<BasicArgument> getBasicArguments(String dimension, HashMap<String,RangeFilter> filters, 
			List<ClinicalTrial> trials, boolean filtersModified);
}
