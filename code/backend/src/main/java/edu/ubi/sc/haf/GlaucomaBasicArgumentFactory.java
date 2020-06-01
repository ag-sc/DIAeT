package edu.ubi.sc.haf;




import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.ResultSetMetaData;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sys.JenaSystem;
import org.apache.jena.util.FileManager;

import edu.ubi.sc.haf.core.BasicArgumentFactory;
import edu.ubi.sc.haf.core.BasicArgument;
import edu.ubi.sc.haf.core.Filter;
import edu.ubi.sc.haf.core.RangeFilter;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

public class GlaucomaBasicArgumentFactory implements BasicArgumentFactory {
    public static String glaucomaEndpointDesc = "Diurnal_IOP";
    
    public static String glaucomaAdvEffName = "Conjunctival_hyperemia";

	List<MedicalBasicArgument> basicArguments;

	String file;
	
	public GlaucomaBasicArgumentFactory()
	{
		basicArguments = new ArrayList<MedicalBasicArgument>();
	}
	
	public void setFile(String RDF_file)
	{
		file = RDF_file;
	}
	
	public String createQuery()
	{
		
		//String d1 ="Timolol";
		ParameterizedSparqlString pss = new ParameterizedSparqlString();	   	   
		   
		pss.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		pss.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		pss.setNsPrefix("","http://www.semanticweb.org/root/ontologies/2018/6/ctro#");
		//pss.setLiteral("drug1", "Timolol");
		//pss.setLiteral("drug2", "Latanoprost");
		//pss.setLiteral("drug1", "Glargine_Insuline");
		//pss.setLiteral("drug2", "NHP_Insuline");
		
	     //String drug1 = "Glargine_Insulin";
	     //String drug2 = "NPH_Insulin";
	     //String EndpointDesc = "HbA1c";
	     //String AdvEff = "Nocturnal_hypoglycemia";
	     
	     String drug1 = "Latanoprost";
	     String drug2 = "Timolol";
	     String EndpointDesc = "Diurnal_IOP";
	     String AdvEff = "Conjunctival_hyperemia";
		  
		String queryDyn="";	
			
		pss.setCommandText("SELECT DISTINCT ?ct ?pmid ?title ?year ?author ?country ?duration ?numPatients ?avgAge ?drugName1 ?drugName2 ?endpointDesc ?reduction1 ?reduction2 ?AEName ?numAffected1 ?numAffected2 ?sponsor ?confint ?affil ?biasS ?abs ?CTdesigns ?arm1_desc_g ?arm2_desc_g\n" + 
				"\n" + 
				"WHERE{\n" + 
				"  ?medic1 :hasDrug :Latanoprost.\n" + 
				"  ?medic2 :hasDrug :Timolol. \n" + 
				"  ?interv1 :hasMedication ?medic1. \n" + 
				"  ?interv2 :hasMedication ?medic2.\n" + 
				"  ?medic1 :hasDrug ?drugName1. \n" + 
				"  ?medic2 :hasDrug ?drugName2. \n" + 
				"  ?arm1 :hasIntervention ?interv1. \n" + 
				"  ?arm2 :hasIntervention ?interv2.\n" + 
				"  ?ct :hasArm ?arm1 .\n" + 
				"  ?ct :hasArm ?arm2 .\n" + 
				"  ?pub :describes ?ct. \n" + 
				"  ?pub :hasPMID ?pmid.  \n" + 
				"  ?pub :hasTitle ?title.  \n" + 
				"  ?pub rdfs:label ?author. \n" + 
				"  ?pub :hasPublicationYear ?year .\n" + 
				"  ?pub :hasAbstract ?abs . \n" + 
				"  ?ct :hasPopulation ?population .  \n" + 
				"  ?population :hasCountry ?country .\n" + 
				"  ?population :hasAvgAge ?avgAge .\n" + 
				"  ?ct :hasNumberPatientsCT ?numPatients . \n" + 
				"  ?ct :hasCTduration ?duration .  \n" + 
				"  ?ct :hasQualityIndicator ?qi.\n" + 
				"  ?qi :hasSponsor ?sponsor. \n" + 
				"  ?qi :hasConflictInterest ?confint. \n" + 
				"  ?qi :hasAuthorAffilations ?affil.\n" + 
				"  ?qi :hasBiasSponsor ?biasS.\n" + 
				"  ?arm1 :hasOutcome ?outcome1. \n" + 
				"  ?arm2 :hasOutcome ?outcome2.  \n" + 
				"  ?outcome1 :hasEndPoint ?endpoint1. \n" + 
				"  ?outcome2 :hasEndPoint ?endpoint2.\n" + 
				"  ?endpoint1 :hasEndpointDescription :Diurnal_IOP. \n" + 
				"  ?endpoint2 :hasEndpointDescription :Diurnal_IOP.\n" + 
				"  ?endpoint2 :hasEndpointDescription ?endpointDesc . \n" + 
				"  ?outcome1 :hasResult ?res1.  \n" + 
				"  ?outcome2 :hasResult ?res2.\n" + 
				"  ?res1 :hasAbsoluteValue ?result1.\n" + 
				"  ?res2 :hasAbsoluteValue ?result2.\n" + 
				"  bind(str(?result1) as ?reduction1) \n" + 
				"  bind(str(?result2) as ?reduction2)\n" + 
				"  ?arm1 :hasAdverseEffect ?AEff1 .\n" + 
				"  ?AEff1 :hasAEname :Conjunctival_hyperemia.    \n" + 
				"  ?AEff1 :hasAEname ?AEName .\n" + 
				"  ?AEff1 :hasNumAffectedAE ?numAffected1. \n" + 
				"  ?arm2 :hasAdverseEffect ?AEff2 .\n" + 
				"  ?AEff2 :hasAEname :Conjunctival_hyperemia. \n" + 
				"  ?AEff2 :hasAEname ?AEName .\n" + 
				"  ?AEff2 :hasNumAffectedAE ?numAffected2.  \n" + 
				"  { SELECT distinct ?ct (group_concat(?CTdes_l;separator=\",\") as ?CTdesigns)  \n" + 
				"    WHERE { \n" + 
				"       ?ct :hasCTDesign  ?CTdes .  \n" + 
				"       ?CTdes rdfs:label ?CTdes_l\n" + 
				"  } group by ?ct  } \n" + 
				"{SELECT  distinct ?arm1 ?interv1 (group_concat(?arm1_desc;separator=\"/\") as ?arm1_desc_g) \n" + 
				"WHERE{ \n" + 
				"  ?interv1 :hasMedication ?medic1. \n" + 
				"  ?arm1 :hasIntervention ?interv1.\n" + 
				"  ?interv1 :hasFrequency  ?freq.\n" + 
				"  ?medic1 :hasDrug ?dr.\n" + 
				"  ?dr rdfs:label ?drug_l.\n" + 
				"  ?medic1 :hasDoseValue ?dose.\n" + 
				"  optional{?medic1 :hasDoseUnit ?unit.}\n" + 
				"  optional{?unit rdfs:label ?unit_l.}\n" + 
				"  bind (concat(?drug_l,\" \", str(?dose),\" \", str(?unit_l),\" \", str(?freq)) as ?arm1_desc)\n" + 
				"} group by ?arm1 ?interv1 }" +
				"{SELECT  distinct ?arm2 ?interv2 (group_concat(?arm2_desc;separator=\"/\") as ?arm2_desc_g) \n" + 
				"WHERE{ \n" + 
				"  ?interv2 :hasMedication ?medic2. \n" + 
				"  ?arm2 :hasIntervention ?interv2.\n" + 
				"  ?interv2 :hasFrequency  ?freq.\n" + 
				"  ?medic2 :hasDrug ?dr.\n" + 
				"  ?dr rdfs:label ?drug_l.\n" + 
				"  ?medic2 :hasDoseValue ?dose.\n" + 
				"  optional{?medic2 :hasDoseUnit ?unit.}\n" + 
				"  optional{?unit rdfs:label ?unit_l.}\n" + 
				"  bind (concat(?drug_l,\" \", str(?dose),\" \", str(?unit_l),\" \", str(?freq)) as ?arm2_desc)\n" + 
				"} group by ?arm2 ?interv2 }" +
				"} order by ?ct");
		 
		
		/*pss.setCommandText("SELECT DISTINCT ?ct ?drug1 ?drug2 ?result1 ?result2 WHERE{\n" +
		   "?drug1 rdfs:label ?drug1Name. ?drug2 rdfs:label ?drug2Name. \n" + 
		   "?medic1 :hasDrug ?drug1. ?medic2 :hasDrug ?drug2. \n"+
	       "?interv1 :hasMedication ?medic1. ?interv2 :hasMedication ?medic2. \n"+
	       "?arm1 :hasIntervention ?interv1. ?arm2 :hasIntervention ?interv2. \n" +
	       "?ct :hasArm ?arm1. ?ct :hasArm ?arm2.");*/
	                			
		//pss.append("?ct :hasCTDesign ?design. \n"); 
		   //pss.append("?ct :hasCTDesign ?design. \n");
		   //pss.append("?design rdfs:label ?designDesc. \n");
		  		
		   //pss.append("}");
	            
		   queryDyn = pss.toString();
		
		return queryDyn;
	}
	
	public List<ClinicalTrial> getTrials() {
		List<ClinicalTrial> trials = new ArrayList<ClinicalTrial>();
		
		String queryString = createQuery();
		
		Model model= FileManager.get().loadModel("ctro_v4.ttl");
		
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		  	 	  
		ResultSet resultSet = qexec.execSelect();		  
		
		List<String> varNames = resultSet.getResultVars();
		for(String s : varNames) {
			System.out.println(s);
		}
		
		while(resultSet.hasNext()) {
			QuerySolution row = resultSet.next();
			
			ClinicalTrial trial = new ClinicalTrial();
			
			String id = row.get("ct").toString();
			trial.id = id.substring(id.lastIndexOf("#")+1);
			
			String pmidStr = row.get("pmid").toString();
			if(!pmidStr.startsWith("https://")) {
				pmidStr = pmidStr.substring(0, pmidStr.indexOf("^^"));
				int pmidInt = Integer.valueOf(pmidStr);
				trial.link = "https://www.ncbi.nlm.nih.gov/pubmed/" + pmidInt;
			} else {
				trial.link = pmidStr;
			}
			
			String title = row.get("title").toString();
			
			String authors = row.get("author").toString();
			
			trial.title = title;
			trial.authors = authors;
			trial.include = true;
			
			// year
			String str = row.get("year").toString();
			str = str.substring(0, str.indexOf("^^"));
			trial.year = Double.valueOf(str);
			
			// duration
			str = row.get("duration").toString();
			str = str.substring(0, str.indexOf(" "));
			trial.duration = Double.valueOf(str);
			
			// avg age
			str = row.get("avgAge").toString();
			str = str.substring(0, str.indexOf("^^"));				
			trial.avgAge = Double.valueOf(str);
			
			// num participants
			str = row.get("numPatients").toString();
			str = str.substring(0, str.indexOf("^^"));
			trial.numParticipants = Double.valueOf(str);
			
			// sponsors
			str = row.get("sponsor").toString(); 
			trial.sponsors = str;
			
			// conflict of interest
			str = row.get("confint").toString(); 
			trial.conflictOfInterest = str;
			
			// affiliation
			str = row.get("affil").toString(); 
			trial.affiliation = str;
			
			// bias
			str = row.get("biasS").toString(); 
			trial.reasonOfBias = str;
			
			// abstract
			str = row.get("abs").toString(); 
			trial.abstractFullText = str;
			
			// trial designs
			str = row.get("CTdesigns").toString(); 
			trial.studyDesign = str;
			
			// description of arm1
			str = row.get("arm1_desc_g").toString(); 
			trial.arm1Description = str;
			
			// description of arm2
			str = row.get("arm2_desc_g").toString(); 
			trial.arm2Description = str;
			System.out.println(str);
			
			trials.add(trial);
		}
		
		return trials;
	}
	
		
	public void createBasicArguments()
	{
		String queryString = createQuery();
		Model model= FileManager.get().loadModel("ctro_v4.ttl");
		
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		  	 	  
		ResultSet resultSet = qexec.execSelect();		  
		
		List<String> varNames = resultSet.getResultVars();
		for(String s : varNames) {
			System.out.println(s);
		}
		
		while(resultSet.hasNext()) {
			QuerySolution row = resultSet.next();
			
			// basic information about trial
			String trialId = row.get("ct").toString();
			trialId = trialId.substring(trialId.lastIndexOf('#')+1);
			
			String authors = row.get("author").toString();
			authors = authors.substring(authors.lastIndexOf('#')+1);
			
			String durationStr = row.get("duration").toString();
			durationStr = durationStr.substring(0, durationStr.indexOf(' '));
			double duration = Double.parseDouble(durationStr);
			
			String avgAgeStr = row.get("avgAge").toString();
			avgAgeStr = avgAgeStr.substring(0, avgAgeStr.indexOf("^^"));
			double avgAge = Double.parseDouble(avgAgeStr);
			
			String numPatientsStr = row.get("numPatients").toString();
			numPatientsStr = numPatientsStr.substring(0, numPatientsStr.indexOf("^^"));
			double numPatients = Double.parseDouble(numPatientsStr);
			
			String yearStr = row.get("year").toString();
			yearStr = yearStr.substring(0, yearStr.indexOf("^^"));
			double year = Double.parseDouble(yearStr);
			
			// efficacy //////////////////////////////////////////////////
			String dimension = GlaucomaBasicArgumentFactory.glaucomaEndpointDesc; //row.get("endpointDesc").toString();
			String disease = "Glaucoma";
			String comparator = ">";
			
			MedicalBasicArgument basicArgumentEfficacy = new MedicalBasicArgument(disease, dimension, comparator);
			basicArgumentEfficacy.setTrialId(trialId);
			basicArgumentEfficacy.setAvgAge(avgAge);
			basicArgumentEfficacy.setNumPatients(numPatients);
			basicArgumentEfficacy.setStudyDuration(duration);
			basicArgumentEfficacy.setYear(year);
			basicArgumentEfficacy.setAuthors(authors);
			
			String drugName1 = row.get("drugName1").toString();
			drugName1 = drugName1.substring(drugName1.lastIndexOf('#')+1);
			double reduction1 = Double.valueOf(row.get("reduction1").toString());
			basicArgumentEfficacy.addEvidence(drugName1, reduction1);
			
			String drugName2 = row.get("drugName2").toString();
			drugName2 = drugName2.substring(drugName2.lastIndexOf('#')+1);
			double reduction2 = Double.valueOf(row.get("reduction2").toString());
			basicArgumentEfficacy.addEvidence(drugName2, reduction2);
			
			this.basicArguments.add(basicArgumentEfficacy);
			
			// debugging ///////////
			System.out.println("duration: " + duration);
			System.out.println("avgAge: " + avgAge);
			System.out.println("numPatients: " + numPatients);
			
			
			// safety //////////////////////////////////////////////////
			dimension = GlaucomaBasicArgumentFactory.glaucomaAdvEffName; //row.get("AEName").toString();
			disease = "Glaucoma";
			comparator = "<";
			
			MedicalBasicArgument basicArgumentSafety = new MedicalBasicArgument(disease, dimension, comparator);
			basicArgumentSafety.setTrialId(trialId);
			basicArgumentSafety.setAvgAge(avgAge);
			basicArgumentSafety.setNumPatients(numPatients);
			basicArgumentSafety.setStudyDuration(duration);
			basicArgumentSafety.setYear(year);
			basicArgumentSafety.setAuthors(authors);
			
			double numAffectedAdverseEvent1 = Double.valueOf(row.get("numAffected1").toString());
			basicArgumentSafety.addEvidence(drugName1, numAffectedAdverseEvent1);
						
			double numAffectedAdverseEvent2 = Double.valueOf(row.get("numAffected2").toString());
			basicArgumentSafety.addEvidence(drugName2, numAffectedAdverseEvent2);
			
			if(numAffectedAdverseEvent1 == 0  &&  numAffectedAdverseEvent2 == 0) {
				continue;
			}
			
			this.basicArguments.add(basicArgumentSafety);
			
			
			System.out.println("drugName1: " + drugName1);
			System.out.println("redcution1: " + reduction1);
			System.out.println("numAffected1: " + numAffectedAdverseEvent1);
			System.out.println("drugName2: " + drugName2);
			System.out.println("redcution2: " + reduction2);
			System.out.println("numAffected2: " + numAffectedAdverseEvent2);
		}
	
	    ResultSetFormatter.out(resultSet);
	    qexec.close();	
	}

	@Override
	public List<BasicArgument> getBasicArguments(String dimension, 
			HashMap<String,RangeFilter> filters, List<ClinicalTrial> trials, boolean filtersModified)
	{
		List<BasicArgument> arguments = new ArrayList<BasicArgument>();
		
		for (MedicalBasicArgument argument: basicArguments)
		{
		    if (argument.matches(dimension))
		    {
		    	// filtering
		    	boolean filterSuccess = true;
		    	String positiveFilters = "";
		    	
		    	if(filters.containsKey(BackendInput.AVG_AGE_STR)) {
		    		RangeFilter filter = (RangeFilter) filters.get(BackendInput.AVG_AGE_STR);
		    		double avgAge = argument.getAvgAge();
		    		if(avgAge != 0  &&  !(avgAge >= filter.getMin() && avgAge <= filter.getMax()) ) {
		    			filterSuccess = false;

		    			if(positiveFilters.length() == 0)
		    				positiveFilters = "age";
		    			else
		    				positiveFilters = positiveFilters + "," + "age";
		    		}
		    	}
		    	
		    	if(filters.containsKey(BackendInput.DURATION_STR)) {
		    		RangeFilter filter = (RangeFilter) filters.get(BackendInput.DURATION_STR);
		    		double duration = argument.getStudyDuration();
		    		if( !(duration >= filter.getMin() && duration <= filter.getMax()) ) {
		    			filterSuccess = false;

		    			if(positiveFilters.length() == 0)
		    				positiveFilters = "study duration";
		    			else
		    				positiveFilters = positiveFilters + ", " + "study duration";
		    		}
		    	}
		    	
		    	if(filters.containsKey(BackendInput.NUM_PATIENTS_STR)) {
		    		RangeFilter filter = (RangeFilter) filters.get(BackendInput.NUM_PATIENTS_STR);
		    		double numPatients = argument.getNumPatients();
		    		if( !(numPatients >= filter.getMin() && numPatients <= filter.getMax()) ) {
		    			filterSuccess = false;

		    			if(positiveFilters.length() == 0)
		    				positiveFilters = "no. participants";
		    			else
		    				positiveFilters = positiveFilters + ", " + "no. participants";
		    		}
	    		}
		    	
		    	
		    	if(filters.containsKey(BackendInput.PUBLICATION_YEAR_STR)) {
		    	 RangeFilter filter = (RangeFilter) filters.get(BackendInput.PUBLICATION_YEAR_STR);
		    		double year = argument.getYear();
		    		if( !(year >= filter.getMin() && year <= filter.getMax()) ) {
		    			filterSuccess = false;

		    			if(positiveFilters.length() == 0)
		    				positiveFilters = "year of publication";
		    			else
		    				positiveFilters = positiveFilters + ", " + "year of publication";
		    		}
	    		}
		    	
		    	
		    	for(ClinicalTrial trial : trials) {
		    		if(argument.getTrialId().equalsIgnoreCase(trial.id)) {
		    			if(filtersModified) {
		    				if(!filterSuccess) {
		    					trial.include = false;
		    					trial.exclusionReason = "excluded by the following filters: " + positiveFilters;
		    				} else {
		    					trial.include = true;
		    					trial.exclusionReason = "-";
		    				}
		    			} else if(!filterSuccess) {
			    			if(!trial.include) {
			    				filterSuccess = false;
			    				trial.exclusionReason = "excluded by the following filters: " + positiveFilters;
			    			} else {
			    				filterSuccess = true;
			    				trial.exclusionReason = "would be excluded by the following filters: " + positiveFilters;
			    			}
		    			} else if(!trial.include) {
		    				filterSuccess = false;
		    			}
		    			
		    			break;
		    		}
		    	}
		    	
		    	if(filterSuccess) {
		    		arguments.add(argument);
		    	}
		    }
		}
		return arguments;
	}
	
	
	public static void main(String[] args) {
		org.apache.jena.query.ARQ.init();
		JenaSystem.init();
		
		
		GlaucomaBasicArgumentFactory factory = new GlaucomaBasicArgumentFactory();
		List<ClinicalTrial> trials = factory.getTrials();
		factory.createBasicArguments();
		for(ClinicalTrial trial : trials) {
			System.out.println(trial.id);
			System.out.println(trial.title);
			System.out.println(trial.authors);
			System.out.println("----------");
		}
	}
}

