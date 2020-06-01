package edu.ubi.sc.haf;

import java.util.List;
import java.util.HashMap;

public class BackendInputMachineReading {
	public String model1;
	
	public String model2;
	
	public List<String> included_datasets; // for checkboxes; names: squad, dream, race, race_m, race_h
	
	public HashMap<String,Double> weights; // key names: accuracy, f1_score, exact_match
}
