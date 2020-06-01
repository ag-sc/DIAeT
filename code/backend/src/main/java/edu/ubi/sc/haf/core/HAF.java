package edu.ubi.sc.haf.core;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.ubi.sc.haf.BackendInput;
import edu.ubi.sc.haf.ClinicalTrial;
import edu.ubi.sc.haf.DiabetesBasicArgumentFactory;
import edu.ubi.sc.haf.GlaucomaBasicArgumentFactory;
import edu.ubi.sc.haf.MedicalBasicArgument;


public class HAF<T> implements HAF_Node{

    DimensionNode Node;
	
	Map<String,Double> Weights;
	
	HashMap<String,RangeFilter> RangeFilters;
	
	List<ClinicalTrial> trials;
	
	BasicArgumentFactory Factory;
	
	BackendInput backendInput;
	
	public HAF(DimensionNode node)
	{
		Weights = new HashMap<String,Double>();
		RangeFilters = new HashMap<String,RangeFilter>();	
		Node = node;
		
		List<String> dimensions = node.getAllDimensions();
		
		for (String dimension: dimensions)
		{
			// System.out.println("I have found dimension "+dimension);
			Weights.put(dimension, 1.0);
		}
		
	}
	
	public HAF(DimensionNode node, Map<String,Double> weights, HashMap<String,RangeFilter> RangeFilters, 
			BasicArgumentFactory factory, List<ClinicalTrial> trials, BackendInput backendInput)
	{
		Weights = weights;
		this.RangeFilters = RangeFilters;
		Factory = factory;
		Node = node;
		this.backendInput = backendInput;
		
		if(trials == null) {
			this.trials = factory.getTrials();
		} else {
			this.trials = trials;
		}
	}
	
	public List<String> getComparables() {
		return null;
	}
	
	public Map<String, Object> getTextualArgument(String superior, String inferior)
	{
		Map<String,Object> verbalization = new HashMap<String,Object>();
		
		List<Map<String, Object>> subarguments = new ArrayList<Map<String, Object>>();
		
		String Top = Node.getDimension();
		
		System.out.println("Top: "+Top);
		
		List<DimensionNode> subdimensions = Node.getSubDimensions();
		
		HAF<T> haf;
		
		List<HAF_Node> subnodes = new ArrayList<HAF_Node>();
		
		// generate textual argument for current node ////////////////////////////////
		double score = this.evaluate(superior, inferior);
		double weight;
		String superiorityString;
		String textualArgument = null;
		String superiorString;
		
		Locale locale  = new Locale("en", "UK");
		String pattern = "###.##";
		DecimalFormat df = (DecimalFormat)
		        NumberFormat.getNumberInstance(locale);
		df.applyPattern(pattern);
		
		// check is basic arguments exist
		if(Factory.getBasicArguments(GlaucomaBasicArgumentFactory.glaucomaEndpointDesc, RangeFilters, trials, backendInput.filtersModified).size() == 0
				&& Factory.getBasicArguments(DiabetesBasicArgumentFactory.diabetesEndpointDesc, RangeFilters, trials, backendInput.filtersModified).size() == 0) {;
			verbalization.put("text", "No basic arguments left!");
			verbalization.put("children", null);
			return verbalization;
		}
		
		// generate subarguments for all children of the current node
		if (subdimensions.size() > 0)
		{
			
			for (DimensionNode node: subdimensions)
			{
				System.out.println("Subdimension text: "+node.getDimension());
		
				haf = new HAF<T>(node,Weights,RangeFilters,Factory,trials, backendInput);		
				subnodes.add(haf);
			}
			System.out.println(subnodes);
			
			for (HAF_Node subnode: subnodes)
			{
				System.out.println("subnode text: "+subnode.getDimension());
			}
			
			for (HAF_Node subnode: subnodes)
			{
				subarguments.add(subnode.getTextualArgument(superior, inferior));
			}
			
			verbalization.put("children", subarguments);		
		} else {
			verbalization.put("children", null);
		}
		
		// generate verbalization for current node ////////////////////
		if (subdimensions.size() > 0)
		{	
			if(score > 0.5) {
				superiorityString = "shown to be superior";
			} else {
				superiorityString = "not shown to be superior";
			}
			
			if(this.getDimension().equalsIgnoreCase("top")) {
				// header
				verbalization.put("header", "Overall");
				
				// total number of trials
				int numTrials = trials.size();
				
				// superiority string
				if(score > 0.5) {
					superiorityString = "can be concluded";
				} else {
					superiorityString = "cannot be concluded";
				}
				
				// text
				textualArgument = "Taking into account the evidence from " + String.valueOf(numTrials)
									+ " clinical studies comparing " + superior.replace("_", " ") + " to " + inferior.replace("_", " ")
									+ ", it " + superiorityString + " that " + superior.replace("_", " ") + " is superior "
									+ " to " + inferior.replace("_", " ") + " (weight of " + subdimensions.get(0).getDimension().replace("_", " ")
									+ ": " + df.format(Weights.get(subdimensions.get(0).getDimension()))
									+ "; weight of " + subdimensions.get(1).getDimension().replace("_", " ")
									+ ": " + df.format(Weights.get(subdimensions.get(1).getDimension())) + ")";
				
				verbalization.put("text", textualArgument);
			} else if(this.getDimension().equalsIgnoreCase("efficacy")) {
				// header
				verbalization.put("header", "Efficacy");
				
				// total number of trials
				int numTrials = trials.size();
				
				// superiority string
				if(score > 0.5) {
					superiorityString = "shows";
				} else {
					superiorityString = "does not show";
				}
				
				// text
				textualArgument = "With respect to efficacy, the evidence in " + String.valueOf(numTrials)
				+ " clinical studies " + superiorityString + " that overall " + superior.replace("_", " ") + " is superior to " + inferior.replace("_", " ")
				+ " in terms of efficacy (weight of "
				+ subdimensions.get(0).getDimension().replace("_", " ") + ": " 
				+ df.format(Weights.get(subdimensions.get(0).getDimension()))
				+ ").";
				
				// save text
				verbalization.put("text", textualArgument);
			} else if(this.getDimension().equalsIgnoreCase("safety")) {
				// header
				verbalization.put("header", "Safety");
				
				// superiority string
				if(score > 0.5) {
					superiorityString = "shows";
				} else {
					superiorityString = "does not show";
				}
				
				// total number of trials
				int numTrials = trials.size();
				
				// text
				textualArgument = "With respect to safety, the evidence in " + String.valueOf(numTrials)
				+ " clinical studies " + superiorityString + " that overall " + superior.replace("_", " ") + " is superior to " + inferior.replace("_", " ")
				+ " in terms of safety (weight of "
				+ subdimensions.get(0).getDimension().replace("_", " ") + ": " 
				+ df.format(Weights.get(subdimensions.get(0).getDimension()))
				+ ").";
				
				// save text
				verbalization.put("text", textualArgument);
			}
		} else { // textual argument for leaf node
			// header
			verbalization.put("header", this.getDimension().replace("_", " "));
			
			// compute score
			List<BasicArgument> bas = Factory.getBasicArguments(Top, RangeFilters, trials, backendInput.filtersModified);
			double numBasicArguments = bas.size();
			int count = 0;
						
			for (BasicArgument ba:bas)
			{
				if (ba.evaluate(superior, inferior) == 1.0)
				{
					count=count+1;
				}
			}
			
			score = count / numBasicArguments;	
			
			// variables
			String title = "";
			String leafDimension = this.getDimension();
			String summary = "";
			
			// summary
			if(leafDimension.equalsIgnoreCase(GlaucomaBasicArgumentFactory.glaucomaEndpointDesc)) {
				title = "Reduction of IOP (mmHg)";
				
				summary = "With respect to lowering IOP, " + count + " out of " + bas.size()
						+ " clinical studies meausing" + 
						" IOP (mmHg) have shown superiority of " + superior.replace("_", " ")  +" compared to " + inferior.replace("_", " ") + ".";
			}
			else if(leafDimension.equalsIgnoreCase(GlaucomaBasicArgumentFactory.glaucomaAdvEffName)) {
				title = "Number of patients affected by " + leafDimension.replace("_", " ");
				
				summary = "With respect to the number of people affected by " + leafDimension.replace("_", " ") + ", "
						 + count + " out of " + bas.size()
				+ " clinical studies meausing " + leafDimension.replace("_", " ")
				+ " have shown superiority of " + superior.replace("_", " ")  +" compared to " + inferior.replace("_", " ") + ".";
			}
			else if(leafDimension.equalsIgnoreCase(DiabetesBasicArgumentFactory.diabetesEndpointDesc)) {
				title = "Reduction of HbA1c (%)";
				
				summary = "With respect to lowering Hb1Ac, " + count + " out of " + bas.size()
						+ " clinical studies measuring " + 
						" Hb1AC (%) have shown superiority of " + superior.replace("_", " ")  +" compared to " + inferior.replace("_", " ") + ".";
			}
			else if(leafDimension.equalsIgnoreCase(DiabetesBasicArgumentFactory.diabetesAdvEffName)) {
				title = "Number of patients affected by " + leafDimension.replace("_", " ");
				
				summary = "With respect to the number of people affected by " + leafDimension.replace("_", " ") + ", "
						 + count + " out of " + bas.size()
				+ " clinical studies meausing " + leafDimension.replace("_", " ")
				+ " have shown superiority of " + superior.replace("_", " ")  +" compared to " + inferior.replace("_", " ") + ".";
			}
			
			verbalization.put("text_summary", summary);	
			
			// text header
			verbalization.put("text_header", title);
			
			// text table
			String table = "<table><thead><tr><td>Trial ID</td><td>Reference</td><td>"
					+ superior.replace("_", " ") + "</td><td>" + inferior.replace("_", " ")
					+ "</td></tr></thead><tbody>";
			
			for (BasicArgument ba:bas)
			{
				MedicalBasicArgument mba = (MedicalBasicArgument) ba;
				double evidence_superior = mba.getEvidence(superior);
				double evidence_inferior = mba.getEvidence(inferior);
				
				if(evidence_superior == 0  &&  evidence_inferior == 0) {
					continue;
				}
				
				table += "<tr>";
				
				// trial id
				table += "<td>" + mba.getTrialId() + "</td>";
						
				// reference
				String firstAuthor = mba.getAuthors();
				int commaIndex = firstAuthor.indexOf(",");
				if(commaIndex != -1) {
					firstAuthor = firstAuthor.substring(0, commaIndex);
					firstAuthor += " et al.";
				}
				
				String reference = firstAuthor + ", " + (int)mba.getYear();
				table += "<td>" + reference + "</td>";
				
				if(mba.getComparator().equals(">")) {
					if(evidence_superior > evidence_inferior) {
						table += "<td><b>" + evidence_superior + "</b></td>";
						table += "<td>" + evidence_inferior + "</td>";		
					} else {
						table += "<td>" + evidence_superior + "</td>";
						table += "<td><b>" + evidence_inferior + "</b></td>";	
					}
				} else {
					if(evidence_superior < evidence_inferior) {
						table += "<td><b>" + evidence_superior + "</b></td>";
						table += "<td>" + evidence_inferior + "</td>";		
					} else {
						table += "<td>" + evidence_superior + "</td>";
						table += "<td><b>" + evidence_inferior + "</b></td>";	
					}
				}
				
				table += "</tr>\n";
			}
			
			table += "</tbody></table>";
			
			verbalization.put("text_table", table);	
		}
		
		return verbalization;
	}
	
	public Double evaluate(String superior, String inferior)
	{
		String Top = Node.getDimension();
		
		System.out.println("Top: "+Top);
		
		List<DimensionNode> subdimensions = Node.getSubDimensions();
		
		HAF<T> haf;
		
		List<HAF_Node> subnodes = new ArrayList<HAF_Node>();
		
		if (subdimensions.size() > 0)
		{
			
			for (DimensionNode node: subdimensions)
			{
				System.out.println("Subdimension: "+node.getDimension());
		
				haf = new HAF<T>(node,Weights,RangeFilters,Factory, trials, backendInput);		
				subnodes.add(haf);
			}
		
			double score = 0.0;
			double weight = 0.0;
			double sumWeights = 0.0;
			
			for (HAF_Node node: subnodes)
			{
				// System.out.println("Trying to get weight for "+node.getDimension()+"\n");
				
				if (Weights.containsKey(node.getDimension()))
					weight = Weights.get(node.getDimension());
				else {
					System.out.println("No weight for: "+node.getDimension());
					weight = 1.0;
					Weights.put(node.getDimension(), 1.0);
					
				}
				sumWeights = sumWeights + weight;
				score = score + node.evaluate(superior, inferior) * weight;
			}
			
			score = score / sumWeights;
			System.out.println("Node: " + Top + " ; Score: " + score);
			
			return score;
			
		}
		else
		{
			List<BasicArgument> bas = Factory.getBasicArguments(Top, RangeFilters, trials,
					backendInput.filtersModified);
			
			System.out.println("Getting basic arguments for superiority of "+superior+ " vs. "+inferior+" with respect to "+Top);
			
			/*
			for (BasicArgument ba:bas)
			{
				subnodes.add(ba);
			}
			
			double count = 0;
			
			for (HAF_Node node: subnodes)
			{
				if (node.evaluate(superior, inferior) == 1.0)
				{
					count=count+1.0;
				}
			}
			*/
			
			double count = 0;
			
			for (BasicArgument ba:bas)
			{
				subnodes.add(ba);
				if (ba.evaluate(superior, inferior) == 1.0)
				{
					count=count+1.0;
				}
			}
			
			return (count / (new Integer(subnodes.size()).doubleValue()));
			
		}
 
	}
	
	
	private void getBasicArguments(String top2) {
		// TODO Auto-generated method stub
		
	}



	public void setRangeFilter(String property, String value)
	{
		
	}
	
	public void removeRangeFilter(String property)
	{
		
	}
	
	public void removeAllRangeFilters()
	{
		
	}
	
	
	public void voidSetFactory(BasicArgumentFactory factory)
	{
		this.Factory = factory;
	}

	public String getDimension() {
		
		return Node.getDimension();
	}

	public Map<String,Double> renderTrueSuperior(String superior, String inferior, Double step, double max) {
		
		List<Map<String,Double>> list = renderTrueSuperior(superior, inferior, Weights, Node,step,max, 0);
		
		return getMinimalWeights(list);
	}
		
	
	
	private Map<String, Double> getMinimalWeights(
			List<Map<String, Double>> list) {
		
		Map<String,Double> minimum = null;
		
		int min = Integer.MAX_VALUE;
		
		for (Map<String,Double> weights: list)
		{
			if (getSize(weights) < min)
			{
				minimum = weights;
				min = getSize(weights);
			}
		}
			
		return minimum;
	}

	private int getSize(Map<String, Double> weights) {
		int size = 0;
		
		for (String dimension: weights.keySet())
		{
			size += weights.get(dimension);
		}
		
		return size;
	}

	public List<Map<String,Double>> renderTrueSuperior(String superior, String inferior, Map<String,Double> map, DimensionNode node, Double step, Double max, int pos) {
		
		List<String> subdimensions = node.getAllDimensions();
		
		List<Map<String,Double>> list = new ArrayList<Map<String,Double>>();
		
		System.out.println(map);
		
		HAF haf;
		
		Map<String,Double> copy;
		
		if (pos < subdimensions.size())
		{
			String dimension = subdimensions.get(pos);
	
			for (int i =1; i <= max; i++)
			{
				copy = getCopy(map);
				copy.put(dimension,copy.get(dimension)+step);
			
				haf = new HAF<T>(node,copy,RangeFilters,Factory, trials, backendInput);
			
				if (haf.evaluate(superior, inferior) > 0.5)
				{
					list.add(copy);
				}
					
				list.addAll(renderTrueSuperior(superior, inferior, copy, node, step, max, pos+1));
			}
		}
		
	return list;
		
	}

	private int getMaximumDimensions(HashMap<String, Double> map, Double max) {
		
		int count = 0;
		for (String key: map.keySet())
		{
			if (map.get(key) - max == 0)
			{
				count ++;
			}
		}
		return count;
	}

	private Map<String, Double> getCopy(Map<String, Double> map) {
		
		Map<String,Double> copy = new HashMap<String,Double>();
		HAF haf;
		
		for (String key: map.keySet())
		{
			copy.put(key, map.get(key));
		}
		return copy;
	}
	
	public List<ClinicalTrial> getTrials() {
		return this.trials;
	}
}
