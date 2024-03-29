package com.javaml;

import java.sql.SQLException;

import libsvm.LibSVM;

import com.javaml.data.Attempt;
import com.javaml.data.Person;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.filter.normalize.NormalizeMean;

public class UserML {
	private Dataset trainSet;
	private Dataset testSet;
	private NormalizeMean normalizer;
	private Classifier classifier;
	private TapClassifiers classifierSet;
	private Person self;
	
	public UserML(){
		trainSet = null;
		testSet = null;
		normalizer = new NormalizeMean();
		classifierSet = new TapClassifiers();
	}

	private void normalizeInstance(Instance inst) {
		normalizer.filter(inst);
	}
	
	public void trainClassifier(Person user) throws SQLException {
		self = user;
		
		trainSet = FeatureUtils.makeTrainSet(self);
		normalizer.filter(trainSet);
		
		testSet = new DefaultDataset();
		Dataset rawTestSet = FeatureUtils.makeTestSet(self);
		for (Instance inst : rawTestSet) {
			normalizer.filter(inst);
			testSet.add(inst);
		}

		Evaluator.writeToFile(Evaluator.FILENAME, "\nTraining set size = " + trainSet.size());
		classifier = classifierSet.makeRandomForest(trainSet);
	}
	
	public boolean classifyAttempt(Attempt attempt) {
		Instance instance = FeatureUtils.instanceForAttempt(attempt);
		normalizeInstance(instance);
		
		Integer result = (Integer)classifier.classify(instance);
		String label;
		if(result == 1) {
			label = "pos";
		} else if(result == 0) {
			label = "neg";
		} else {
			label = "wtf";
		}
		
		//Log.i("ml_debug", "Classifier result = " + label);
		return result == 1;
	}
	
	public Dataset getTrainSet() {
		return trainSet;
	}

	public Dataset getTestSet() {
		return testSet;
	}

	public void setTestSet(Dataset testSet) {
		this.testSet = testSet;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(LibSVM classifier) {
		this.classifier = classifier;
	}
}
