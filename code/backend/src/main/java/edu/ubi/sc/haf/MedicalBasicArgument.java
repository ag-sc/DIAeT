package edu.ubi.sc.haf;
import java.util.HashMap;
import java.util.Map;

import edu.ubi.sc.haf.core.BasicArgument;

public class MedicalBasicArgument implements BasicArgument {

	String dimension;
	
	double studyDuration;
	
	double numPatients;
	
	double avgAge;
	
	double year;
	
	String trialId;
	
	HashMap<String, Double> evidence;
	
	String comparator; // only '<' or '>'
	
	String disease;
	
	String authors;
	
	ClinicalTrial trial; // not yet used
	
	public MedicalBasicArgument(String disease, String dimension, String comparator) {
		this.disease = disease;
		
		this.dimension = dimension;
		
		this.comparator = comparator;
		
		evidence = new HashMap<String, Double>();
	}
	
	public String getComparator() {
		return this.comparator;
	}
	
	public double getStudyDuration() {
		return this.studyDuration;
	}
	
	public void setStudyDuration(double studyDuration) {
		this.studyDuration = studyDuration;
	}
	
	public double getNumPatients() {
		return this.numPatients;
	}
	
	public void setNumPatients(double numPatients) {
		this.numPatients = numPatients;
	}
	
	public double getAvgAge() {
		return this.avgAge;
	}
	
	public void setAvgAge(double avgAge) {
		this.avgAge = avgAge;
	}
	
	public String getTrialId() {
		return this.trialId;
	}
	
	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}
	
	public double getYear() {
		return this.year;
	}
	
	public void setYear(double year) {
		this.year = year;
	}
	
	public ClinicalTrial getTrial() {
		return trial;
	}
	
	public String getAuthors() {
		return this.authors;
	}
	
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public void setTrial(ClinicalTrial trial) {
		this.trial = trial;
	}
	
	public void addEvidence(String evidenceName, double evidenceValue) {
		evidence.put(evidenceName, evidenceValue);
	}
	
	public double getEvidence(String evidenceName) {
		return evidence.get(evidenceName);
	}

	public String getSuperiorOption() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInferiorOption() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean matches(HashMap<String, String> filters) {
		// TODO Auto-generated method stub
		return false;
	}

	public Double evaluate(String superior, String inferior) {
		System.out.println(this.dimension);
		if(comparator.equals(">")) {
			if(evidence.get(superior) > evidence.get(inferior))
				return 1.0;
			else
				return 0.0;
		} else if(comparator.equals("<")) {
			System.out.println("E " + evidence.get(superior) + " ; " + evidence.get(inferior));
			if(evidence.get(superior) < evidence.get(inferior))
				return 1.0;
			else
				return 0.0;
		}
		
		return 0.0;
	}

	public String getTextualArgument(String superior, String inferior, String indent) {
		return indent+ superior + "has been shown to be superior to" + inferior + "in study X  with respect to" + dimension;
	}


	public boolean matches(String dimension) {
		return this.dimension.equals(dimension);
	}

	public String getDimension() {
		return dimension;
	}

	public Map<String, Object> getTextualArgument(String superior, String inferior) {
		// TODO Auto-generated method stub
		return null;
	}

}
