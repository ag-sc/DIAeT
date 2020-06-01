package edu.ubi.sc.haf.core;
import java.util.ArrayList;
import java.util.List;


public class DimensionNode {

	String Dimension;
	
	List<DimensionNode> children;
	
	public DimensionNode(String dimension)
	{
		Dimension = dimension;
		children = new ArrayList<DimensionNode>();
	}
	
	public String getDimension()
	{
		return Dimension;
	}
	
		
	public List<DimensionNode> getSubDimensions()
	{
		return children;
	}

	public void addChild(DimensionNode node) {
		children.add(node);
		
	}

	public List<String> getAllDimensions() {
		
		// System.out.println("Calling getAllDimensions");
		
		List<String> dimensions = new ArrayList<String>();
		
		dimensions.add(Dimension);
		
		for (DimensionNode dimension: children)
		{
			dimensions.addAll(dimension.getAllDimensions());
		}
		
		return dimensions;
	}
	
	
}
