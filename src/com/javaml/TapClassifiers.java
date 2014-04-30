package com.javaml;

import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.weka.WekaClassifier;

public class TapClassifiers {

	/*public Classifier makeLogistic(Dataset trainSet) {
		Logistic logistic = new Logistic();
		Classifier logisticRegression = new WekaClassifier(logistic);
		logisticRegression.buildClassifier(trainSet);
		return logisticRegression;
	}*/
	
	public Classifier makeSvm(Dataset trainSet) {
		Classifier svm = new LibSVM();
		svm.buildClassifier(trainSet);
		return svm;
	}

	public Classifier makeRandomForest(Dataset trainSet){
		RandomForest rf = new RandomForest(30);
		rf.buildClassifier(trainSet);
		// Evaluator.crossValidate(data, new RandomForest(30));
		//Evaluator.klDivergence(data);
		//Evaluator.gainRatio(data);
		return rf;
	}

	public Classifier makeNaiveBayes(Dataset trainSet){
		//Log.w("ml", "Feature vector has "+data.get(0).size() + "features");
		//binner.build(data);
		//binner.filter(data);
		NaiveBayesClassifier nb = new NaiveBayesClassifier(true, true, false);
		nb.buildClassifier(trainSet);
		// Print out diagnostics
		//Evaluator.crossValidate(data, new NaiveBayesClassifier(true,true,false));
		//Evaluator.klDivergence(data);
		//Evaluator.gainRatio(data);
		return nb;
	}
}
