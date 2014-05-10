package com.javaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.featureselection.scoring.KullbackLeiblerDivergence;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;

public class Evaluator {
	private static final int FOLDS = 10;
	public static String FILENAME = "results.txt";
	
	public static void writeToFile(String fileName, String data) {
		try{
    		File file = new File(fileName);
 
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(), true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write(data);
    	        bufferWritter.close();
 
	        System.out.println("Done");
 
    	}catch(IOException e){
    		e.printStackTrace();
    	}
	}
	
	public static Map<Object, PerformanceMeasure> crossValidate(Dataset dataset, Classifier classifier){
		CrossValidation cv = new CrossValidation(classifier);
		Map<Object, PerformanceMeasure> perf = cv.crossValidation(dataset, FOLDS);
		//printPerformance(perf);
		return perf;
	}
	
	public static void printPerformance(Map<Object, PerformanceMeasure> performance){
		for(Object key : performance.keySet()){
			PerformanceMeasure p = performance.get(key);
			writeToFile(FILENAME, "Performance for class '" +key + "':\r\n");
			writeToFile(FILENAME, "  precision: " + p.getPrecision() + "\r\n");
			writeToFile(FILENAME, "  recall: " + p.getRecall() + "\r\n");
			writeToFile(FILENAME, "false negative rate: " + p.getFNRate() + "\r\n");
			writeToFile(FILENAME, "false positive rate: " + p.getFPRate()  + "\r\n");
			writeToFile(FILENAME, "  accuracy: "+p.getAccuracy() + "\r\n");
		}
	}
	
	public static void klDivergence(Dataset data){
		KullbackLeiblerDivergence kl = new KullbackLeiblerDivergence();
		System.out.println("Scoring features via KL divergence.");
		kl.build(data);
		for(int i = 0; i < kl.noAttributes(); i++){
			System.out.println("Feature "+ i + ": "+ kl.score(i));
		}
	}
	
	public static void gainRatio(Dataset data){
		GainRatio gain = new GainRatio();
		System.out.println("Scoring features via KL divergence.");
		gain.build(data);
		for(int i = 0; i < gain.noAttributes(); i++){
			System.out.println("Feature "+ i + ": "+ gain.score(i));
		}
	}
	
	public static void featureRanking(Dataset data) {
		RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
        /* Apply the algorithm to the data set */
        svmrfe.build(data);
        /* Print out the rank of each attribute */
        for (int i = 0; i < svmrfe.noAttributes(); i++)
            System.out.println("Rank for feature #" + i + " " + svmrfe.rank(i));
	}
	
	public static void feaureSelection(Dataset data) {
		GreedyForwardSelection ga = new GreedyForwardSelection(1, new PearsonCorrelationCoefficient());
        /* Apply the algorithm to the data set */
        ga.build(data);
        /* Print out the attribute that has been selected */
        System.out.println(ga.selectedAttributes().toString());
	}
	
	public static void featureScores(Dataset data) {
		GainRatio ga = new GainRatio();
		ga.build(data);
		for (int i = 0; i < ga.noAttributes(); i++) {
			System.out.println("Feature " + i + " score: " + ga.score(i));
		}
	}
}
