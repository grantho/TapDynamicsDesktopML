package com.javaml;

import libsvm.LibSVM;
import libsvm.svm_parameter;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.weka.WekaClassifier;

public class TapClassifiers {
	private svm_parameter param;
	
	/*public Classifier makeLogistic(Dataset trainSet) {
		Logistic logistic = new Logistic();
		Classifier logisticRegression = new WekaClassifier(logistic);
		logisticRegression.buildClassifier(trainSet);
		return logisticRegression;
	}*/
	public TapClassifiers() {
		param = new svm_parameter();
		
		// Default params from JavaML LibSVM's source
		param.svm_type = svm_parameter.C_SVC;
		param.C = 1;
		param.kernel_type = svm_parameter.LINEAR;
		param.degree = 1;
		param.gamma = 0; // 1/k
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[2];
		param.weight_label[0] = 1;
		param.weight_label[1] = -1;
		param.weight = new double[2];
		param.weight[0] = 3;
		param.weight[1] = 1;
	}
	
	public LibSVM makeSvm(Dataset trainSet) {
		LibSVM svm = new LibSVM();
		svm.setParameters(param);
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
