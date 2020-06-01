package edu.ubi.sc.haf.core;

import java.util.Map;

public interface HAF_Node {

	public String getDimension();
	
	public Double evaluate(String superior, String inferior);
	
	public Map<String, Object> getTextualArgument(String superior, String inferior);
	
}
