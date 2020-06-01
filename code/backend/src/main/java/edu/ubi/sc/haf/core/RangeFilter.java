package edu.ubi.sc.haf.core;

public class RangeFilter implements Filter {
	private String propertyName;
	
	private double min;
	
	private double max;
	
	public RangeFilter(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
}
