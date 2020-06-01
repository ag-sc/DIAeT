package edu.ubi.sc.haf;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sys.JenaSystem;
import org.apache.jena.util.FileManager;



public class MachineReadingMain {
	public static List<ExperimentalEvidence> getEvidence() {
		org.apache.jena.query.ARQ.init();
		JenaSystem.init();
		
		String queryString = "PREFIX nlpeval: <http://example.org/nlpeval/>\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
				"SELECT\n" + 
				"  ?model_name\n" + 
				"  ?em_squad\n" + 
				"  ?em_squad_date\n" + 
				"  ?f1_squad\n" + 
				"  ?f1_squad_date\n" + 
				"  ?race_accuracy\n" + 
				"  ?race_accuracy_date\n" + 
				"  ?race_m_accuracy\n" + 
				"  ?race_m_accuracy_date\n" + 
				"  ?race_h_accuracy\n" + 
				"  ?race_h_accuracy_date\n" + 
				"  ?dream_accuracy\n" + 
				"  ?dream_accuracy_date\n" + 
				"  ?link\n" + 
				"WHERE {\n" + 
				"  ?model rdf:type nlpeval:Model ;\n" + 
				"    nlpeval:modelname ?model_name .\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:SQuAD ;\n" + 
				"        nlpeval:metric nlpeval:EM ;\n" + 
				"        nlpeval:value ?em_squad ;\n" + 
				"        nlpeval:date ?em_squad_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:SQuAD ;\n" + 
				"        nlpeval:metric nlpeval:F1 ;\n" + 
				"        nlpeval:value ?f1_squad ;\n" + 
				"        nlpeval:date ?f1_squad_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:RACE ;\n" + 
				"        nlpeval:metric nlpeval:accuracy ;\n" + 
				"        nlpeval:value ?race_accuracy ;\n" + 
				"        nlpeval:date ?race_accuracy_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:RACE-M ;\n" + 
				"        nlpeval:metric nlpeval:accuracy ;\n" + 
				"        nlpeval:value ?race_m_accuracy ;\n" + 
				"        nlpeval:date ?race_m_accuracy_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:RACE-H ;\n" + 
				"        nlpeval:metric nlpeval:accuracy ;\n" + 
				"        nlpeval:value ?race_h_accuracy ;\n" + 
				"        nlpeval:date ?race_h_accuracy_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:result [\n" + 
				"        nlpeval:dataset nlpeval:DREAM ;\n" + 
				"        nlpeval:metric nlpeval:accuracy ;\n" + 
				"        nlpeval:value ?dream_accuracy ;\n" + 
				"        nlpeval:date ?dream_accuracy_date\n" + 
				"    ]\n" + 
				"  }\n" + 
				"\n" + 
				"  OPTIONAL {\n" + 
				"    ?model nlpeval:publication [\n" + 
				"      rdf:type nlpeval:Publication ;\n" + 
				"      nlpeval:link ?link\n" + 
				"    ]\n" + 
				"  }\n" + 
				"} limit 1000";
		
		Model model= FileManager.get().loadModel("data_machine_reading.ttl");
		
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		  	 	  
		ResultSet resultSet = qexec.execSelect();		  
		
		/*
		List<String> varNames = resultSet.getResultVars();
		for(String s : varNames) {
			System.out.println(s);
		}
		*/
		List<ExperimentalEvidence> evidenceList = (List<ExperimentalEvidence>) new ArrayList();
		
		while(resultSet.hasNext()) {
			QuerySolution row = resultSet.next();
			ExperimentalEvidence evidence = new ExperimentalEvidence();
			
			String model_name = row.get("model_name").toString();
			model_name = model_name.substring(0, model_name.indexOf("@en"));
			evidence.model_name = model_name;
			
			Object obj = row.get("em_squad");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.em_squad = str;
			}
			
			obj = row.get("f1_squad");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.f1_squad = str;
			}
			
			obj = row.get("race_accuracy");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.race_accuracy = str;
			}
			
			obj = row.get("race_m_accuracy");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.race_m_accuracy = str;
			}
			
			obj = row.get("race_h_accuracy");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.race_h_accuracy = str;
			}
			
			obj = row.get("dream_accuracy");
			if(obj != null) {
				String str = obj.toString();
				str = str.substring(0, str.indexOf("^^"));
				evidence.dream_accuracy = str;
			}
			
			obj = row.get("link");
			if(obj != null) {
				String str = obj.toString();
				//str = str.substring(0, str.indexOf("^^"));
				evidence.link = str;
				System.out.println(str);
			}
			
			evidence.authors = "";
			
			evidenceList.add(evidence);
		}
		
		return evidenceList;
	}
	
	public static BackendOutputMachineReading Main(BackendInputMachineReading backendInput) {
		double accuracy_weight = backendInput.weights.get("accuracy");
		double f1_weight = backendInput.weights.get("f1_score");
		double em_weight = backendInput.weights.get("exact_match");
		
		boolean squad_included = backendInput.included_datasets.contains("squad");	
		boolean dream_included = backendInput.included_datasets.contains("dream");
		boolean race_included = backendInput.included_datasets.contains("race");
		boolean race_m_included = backendInput.included_datasets.contains("race_m");
		boolean race_h_included = backendInput.included_datasets.contains("race_h");
		
		String text = "";
		text += "accuracy_weight=" + accuracy_weight + ";";
		text += "f1_weight=" + f1_weight + ";";
		text += "em_weight=" + em_weight + ";";
		if(squad_included)
			text += "squad_included;";
		if(dream_included)
			text += "dream_included;";
		if(race_included)
			text += "race_included;";
		if(race_m_included)
			text += "race_m_included;";
		if(race_h_included)
			text += "race_h_included;";
		
		List<ExperimentalEvidence> evidenceList = MachineReadingMain.getEvidence();
		ExperimentalEvidence evidenceModel1 = null;
		ExperimentalEvidence evidenceModel2 = null;
		
		for(ExperimentalEvidence evidence : evidenceList) {
			if(evidence.model_name.equalsIgnoreCase(backendInput.model1))
				evidenceModel1 = evidence;
			if(evidence.model_name.equalsIgnoreCase(backendInput.model2))
				evidenceModel2 = evidence;
		}
		
		// accuracy verbalization //////////////////////////////////////
		String accuracyVerbalization = "";
		double accuracyModel1 = -1;
		double accuracyModel2 = -1;
		double accuracyCountModel1 = 0;
		double accuracyCountModel2 = 0;
		boolean accuracyFound = false;
		
		if(race_included) {
			// race
			if(evidenceModel1.race_accuracy != null  &&  evidenceModel1.race_accuracy.length() > 0)
				accuracyModel1 = Double.parseDouble(evidenceModel1.race_accuracy);
			if(evidenceModel2.race_accuracy != null  &&  evidenceModel2.race_accuracy.length() > 0)
				accuracyModel2 = Double.parseDouble(evidenceModel2.race_accuracy);
			if(accuracyModel1 != -1  && accuracyModel2 != -1) {
				if(accuracyModel1 > accuracyModel2)
					accuracyCountModel1++;
				else
					accuracyCountModel2++;
				
				accuracyFound = true;
			}
		}
		
		if(race_h_included) {
			// race h
			accuracyModel1 = -1;
			accuracyModel2 = -1;
			if(evidenceModel1.race_h_accuracy != null  &&  evidenceModel1.race_h_accuracy.length() > 0)
				accuracyModel1 = Double.parseDouble(evidenceModel1.race_accuracy);
			if(evidenceModel2.race_h_accuracy != null  &&  evidenceModel2.race_h_accuracy.length() > 0)
				accuracyModel2 = Double.parseDouble(evidenceModel2.race_accuracy);
			if(accuracyModel1 != -1  && accuracyModel2 != -1) {
				if(accuracyModel1 > accuracyModel2)
					accuracyCountModel1++;
				else
					accuracyCountModel2++;
				
				accuracyFound = true;
			}
		}
		
		if(race_m_included) {
			// race m
			accuracyModel1 = -1;
			accuracyModel2 = -1;
			if(evidenceModel1.race_m_accuracy != null  &&  evidenceModel1.race_m_accuracy.length() > 0)
				accuracyModel1 = Double.parseDouble(evidenceModel1.race_accuracy);
			if(evidenceModel2.race_m_accuracy != null  &&  evidenceModel2.race_m_accuracy.length() > 0)
				accuracyModel2 = Double.parseDouble(evidenceModel2.race_accuracy);
			if(accuracyModel1 != -1  && accuracyModel2 != -1) {
				if(accuracyModel1 > accuracyModel2)
					accuracyCountModel1++;
				else
					accuracyCountModel2++;
				
				accuracyFound = true;
			}
		}
		
		if(dream_included) {
			// dream m
			accuracyModel1 = -1;
			accuracyModel2 = -1;
			if(evidenceModel1.dream_accuracy != null  &&  evidenceModel1.dream_accuracy.length() > 0)
				accuracyModel1 = Double.parseDouble(evidenceModel1.race_accuracy);
			if(evidenceModel2.dream_accuracy != null  &&  evidenceModel2.dream_accuracy.length() > 0)
				accuracyModel2 = Double.parseDouble(evidenceModel2.race_accuracy);
			if(accuracyModel1 != -1  && accuracyModel2 != -1) {
				if(accuracyModel1 > accuracyModel2)
					accuracyCountModel1++;
				else
					accuracyCountModel2++;
				
				accuracyFound = true;
			}
		}
		
			// verbalization
			if(accuracyFound) {
				if(accuracyCountModel1 > accuracyCountModel2) {
					accuracyVerbalization = "The evidence shows that " + evidenceModel1.model_name + " performs "
					+ "better than model " + evidenceModel2.model_name + " w.r.t accuracy";
				} else {
					accuracyVerbalization = "The evidence does not show that " + evidenceModel1.model_name + " performs "
							+ "better than model " + evidenceModel2.model_name + " w.r.t accuracy";
				}
			} else
				accuracyVerbalization = "At least for one model there is no evidence regarding accuracy";
	
		
		// f1 verbalization //////////////////////////////////////
		String f1Verbalization = "";
		double f1Model1 = -1;
		double f1Model2 = -1;
		double f1CountModel1 = 0;
		double f1CountModel2 = 0;
		
		if(squad_included) {
			if(evidenceModel1.f1_squad != null  &&  evidenceModel1.f1_squad.length() > 0)
				f1Model1 = Double.parseDouble(evidenceModel1.f1_squad);
			if(evidenceModel2.f1_squad != null  &&  evidenceModel2.f1_squad.length() > 0)
				f1Model2 = Double.parseDouble(evidenceModel2.f1_squad);
			if(f1Model1 != -1  && f1Model2 != -1)
				if(f1Model1 > f1Model1)
					f1CountModel1++;
				else
					f1CountModel2++;
		}
			
		if(f1Model1 != -1  && f1Model2 != -1) {	
			if(f1Model1 > f1Model2) {
				f1Verbalization = "The evidence shows that " + evidenceModel1.model_name + " performs "
				+ "better than model " + evidenceModel2.model_name + " w.r.t f1";
			} else {
				f1Verbalization = "The evidence does not show that " + evidenceModel1.model_name + " performs "
						+ "better than model " + evidenceModel2.model_name + " w.r.t f1";
			}
		} else
			f1Verbalization = "At least for one model there is no evidence regarding f1";
		
		
		// em verbalization //////////////////////////////////////
				String emVerbalization = "";
				double emModel1 = -1;
				double emModel2 = -1;
				double emCountModel1 = 0;
				double emCountModel2 = 0;
				
				if(squad_included) {
					if(evidenceModel1.em_squad != null  &&  evidenceModel1.em_squad.length() > 0)
						emModel1 = Double.parseDouble(evidenceModel1.em_squad);
					if(evidenceModel2.em_squad != null  &&  evidenceModel2.em_squad.length() > 0)
						emModel2 = Double.parseDouble(evidenceModel2.em_squad);
					if(emModel1 != -1  && emModel2 != -1)
						if(emModel1 > emModel1)
							emCountModel1++;
						else
							emCountModel2++;
				}
					
				if(emModel1 != -1  && emModel2 != -1) {	
					if(emModel1 > emModel2) {
						emVerbalization = "The evidence shows that " + evidenceModel1.model_name + " performs "
						+ "better than model " + evidenceModel2.model_name + " w.r.t exact match";
					} else {
						emVerbalization = "The evidence does not show that " + evidenceModel1.model_name + " performs "
								+ "better than model " + evidenceModel2.model_name + " w.r.t exact match";
					}
				} else
					emVerbalization = "At least for one model there is no evidence regarding exact match";
		
		// overall conclusion
		boolean overallConclusionExists = false;
		double score = 0;
		String overallConclusion = "";
		Locale locale  = new Locale("en", "UK");
		String pattern = "###.##";
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		df.applyPattern(pattern);
		
		double weights_sum = 0;
		
		// weights sum
		if(accuracyFound) {
			weights_sum += accuracy_weight;
		}
		
		if(f1Model1 != -1  && f1Model2 != -1) {
			weights_sum += f1_weight;
		}
		
		if(emModel1 != -1  && emModel2 != -1) {
			weights_sum += em_weight;
		}
		
		if(weights_sum != 0) {
			accuracy_weight /= weights_sum;
			f1_weight /= weights_sum;
			em_weight /= weights_sum;
		}
		
		// verbalization
		if(accuracyFound) {
			score += (accuracyCountModel1 / (accuracyCountModel1+accuracyCountModel2)) * accuracy_weight;
			overallConclusionExists = true;
		}
		
		if(f1Model1 != -1  && f1Model2 != -1) {
			if(f1Model1 > f1Model2)
				score += f1_weight;
			
			overallConclusionExists = true;
		}
		
		if(emModel1 != -1  && emModel2 != -1) {
			if(emModel1 > emModel2)
				score += em_weight;
			
			overallConclusionExists = true;
		}
		
		if(overallConclusionExists) {
			if(score > 0.5) {
				overallConclusion = "Overall, it has been shown that model " + evidenceModel1.model_name +" performs better than model "
						+ evidenceModel2.model_name + "  (score: " + df.format(score) + "; weight f1:"
						+ df.format(f1_weight) + "; weight accuracy:" + df.format(accuracy_weight )
						+ "; weight exact match:" + df.format(em_weight) + ")";
			} else {
				overallConclusion = "Overall, it has not been shown that model " + evidenceModel1.model_name +" performs better than model "
						+ evidenceModel2.model_name + "  (score: " + df.format(score) + "; weight f1:"
						+ df.format(f1_weight) + "; weight accuracy:" + df.format(accuracy_weight) 
						+ "; weight exact match:" + df.format(em_weight) + ")";
			}
		} else {
			overallConclusion = "Overall conclusion could not be computed";
		}
		
			
		HashMap<String,Object> verbalizationMap = new HashMap();
		HashMap<String,Object> accuracyMap = new HashMap();
		HashMap<String,Object> f1Map = new HashMap();
		HashMap<String,Object> emMap = new HashMap();
		ArrayList<HashMap<String,Object>> leafsList = new ArrayList();
		
		accuracyMap.put("text", accuracyVerbalization);
		accuracyMap.put("children", null);
		
		f1Map.put("text", f1Verbalization);
		f1Map.put("children", null);
		
		emMap.put("text", emVerbalization);
		emMap.put("children", null);
		
		leafsList.add(accuracyMap);
		leafsList.add(f1Map);
		leafsList.add(emMap);
		
		verbalizationMap.put("text", overallConclusion);
		verbalizationMap.put("children", leafsList);
		
		BackendOutputMachineReading backendOutput = new BackendOutputMachineReading();
		backendOutput.evidenceModel1 = evidenceModel1;
		backendOutput.evidenceModel2 = evidenceModel2;
		backendOutput.verbalization = verbalizationMap;
		return backendOutput;
	}
}
