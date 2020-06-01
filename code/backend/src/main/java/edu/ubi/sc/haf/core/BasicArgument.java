package edu.ubi.sc.haf.core;

import edu.ubi.sc.haf.ClinicalTrial;

public interface BasicArgument extends HAF_Node {

	public Double evaluate(String superior, String inferior);
	
	public String getSuperiorOption();
	public String getInferiorOption();
	
	public boolean matches(String dimension);
	
	public String getDimension();
	
	public ClinicalTrial getTrial();
}
