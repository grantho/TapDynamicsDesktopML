package com.javaml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import libsvm.LibSVM;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.j256.ormlite.dao.Dao;
import com.javaml.analysis.Utility;
import com.javaml.data.AccelerometerData;
import com.javaml.data.Attempt;
import com.javaml.data.Person;
import com.javaml.data.Tap;

public class FeatureUtils {
	// For features we have latency, duration, size, and mean accel (x,y,z)
	//public static final int FEATURES_PER_TAP = 2; 
	//public static final int NUM_ACCEL_FEATURES = 0;
	public static final int FEATURES_PER_TAP = 3; 
	public static final int NUM_ACCEL_FEATURES = 15;
	public static final int NUM_NEG_EXAMPLES = 30;
	public static final int NUM_TAPS = 5;
	private static Dao<Person, Integer> personDao;

	public static void initFeatureUtils(Dao<Person, Integer> pDao){
		personDao = pDao;
	}

	private static int tp, fp, tn, fn;
	public static void clearResults() {
		tp = 0;
		fp = 0;
		tn = 0;
		fn = 0;
	}
	
	public static void countPositives(){
		
	}
	
	public static void countNegatives() {
		
	}

	public static void evaluateDataset(Classifier trainedClassifier, Dataset testData, String username) {
		clearResults();
		//LibSVM svm = (LibSVM) trainedClassifier;
        /* Classify all instances and check with the correct class values */
        for (Instance inst : testData) {
            Object predictedClassValue = trainedClassifier.classify(inst);
            Object realClassValue = inst.classValue();
            
            int realClass = (Integer) realClassValue;
            if (predictedClassValue.equals(realClassValue)) {
            	if (realClass == 1) {
            		tp++;
            	} else if (realClass == -1) {
            		tn++;
            	}
            }
            else {
            	if (realClass == 1) {
            		fn++;
            	} else if (realClass == -1) {
            		fp++;
            	}
            }
        }

		Utility.writeToFile(Evaluator.FILENAME, "True positive: " + tp + "\r\n");
		Utility.writeToFile(Evaluator.FILENAME, "False positive: " + fp + "\r\n");
		Utility.writeToFile(Evaluator.FILENAME, "True NEGATIVE: " + tn + "\r\n");
		Utility.writeToFile(Evaluator.FILENAME, "False NEGATIVE: " + fn + "\r\n");
		Utility.writeToFile(Evaluator.FILENAME, "False Rejection (User) Rate: " + (double)fn/(tp+fn) + "\r\n");
		Utility.writeToFile(Evaluator.FILENAME, "False Acceptane Rate: " + (double)fp/(tn+fp) + "\r\n");
	}
	
	public static void evaluateTestError(Classifier trainedClassifier, Dataset testData, String username) {
		clearResults();
		
		//Map<Object, PerformanceMeasure> pm = EvaluateDataset.testDataset(trainedClassifier, testData);
		String header = "\r\nEVALUATE_DATASET:: Test dataset results for person p = " + username + "\r\n";
		Utility.writeToFile(Evaluator.FILENAME, header);
		evaluateDataset(trainedClassifier, testData, username);
		
		//Evaluator.printPerformance(pm);
		/*Map<Object, PerformanceMeasure> pm2 = Evaluator.crossValidate(testData, trainedClassifier);
		Log.i("eval", "CrossValidate:: Test dataset results for person p = " + p.getUsername());
		Evaluator.printPerformance(pm2);*/
		//for(Object o:pm.keySet()) Log.d("evaluate", o+": "+pm.get(o).getAccuracy());
	}

	public static void evaluateTrainError(Classifier trainedClassifier, Dataset trainData, String username) {
		clearResults();
		//Map<Object, PerformanceMeasure> pm = EvaluateDataset.testDataset(trainedClassifier, trainData);
		String header = "TRAINING dataset results for person p = " + username + "\r\n";
		Utility.writeToFile(Evaluator.FILENAME, header);
		evaluateDataset(trainedClassifier, trainData, username);
		//Evaluator.printPerformance(pm);
		/*Map<Object, PerformanceMeasure> pm2 = Evaluator.crossValidate(trainData, trainedClassifier);
		Log.i("eval", "CrossValidate:: TRAIN results for person p = " + p.getUsername());
		Evaluator.printPerformance(pm2);*/
	}

	public static Dataset makeTrainSet(Person p) throws SQLException {
		return getDataSet(p, true);
	}

	public static Dataset makeTestSet(Person p) throws SQLException {
		return getDataSet(p, false);
	}

	private static Dataset getDataSet(Person user, boolean trainSet) throws SQLException {
		Dataset dataset = new DefaultDataset();

		makePosSet(user, trainSet, dataset);
		makeNegSet(user, trainSet, dataset);	

		return dataset;
	}

	private static void makePosSet(Person user, boolean trainSet, Dataset dataset) {
		ArrayList<Attempt> userAttempts = new ArrayList<Attempt>(user.getAttempts());

		/*int start, end;
		if(trainSet) {
			start = 0;
			end = userAttempts.size()/2;
		} else {
			start = userAttempts.size()/2;
			end = userAttempts.size();
		}*/

		// Even numbers for test set, odd numbers for training set
		for(int i = 0; i < userAttempts.size(); i++) {
			// Skip even for training set
			if (i % 2 == 0 && trainSet) {
				continue;
			} 
			//Skip odd for test set (not training)
			if (i % 2 != 0 && !trainSet) {
				//Log.i("feature", "Attempte #" + i);
				continue;
			}


			Attempt a = userAttempts.get(i);
			if(a.getTaps().size() != 5) continue;
			double[] featureVector = vectorForAttempt(a);
			Instance instance = new DenseInstance(featureVector, 1);
			//Log.i("feature", "Feature vector: " + Arrays.toString(featureVector));
			dataset.add(instance);
		}

		//normalizer.filter(dataset);
		//Log.i("eval", "Num positive samples" + counter);
	}

	private static void makeNegSet(Person user, boolean trainSet, Dataset dataset) throws SQLException {
		// get others with the same pin
		List<Person> people = personDao.queryForEq("pin", user.getPin());

		//Log.w("ml","Found "+people.size() + " with the same pin.");
		for(Person other : people){			
			if(other.getId() == user.getId()) continue;
			//if(other.getUsername().contains("DEMO")) continue;

			ArrayList<Attempt> otherAttempts = new ArrayList<Attempt>(other.getAttempts());
			if(otherAttempts.size() < 1) {
				continue;
			}

			int start, end;
			int trainStart = 4;
			if(trainSet) {
				start = trainStart;
				end = 5;
				//end = otherAttempts.size()/2;
			} else {
				start = 5;
				//start = otherAttempts.size()/2;
				end = otherAttempts.size();
			}
			for(int i = start; i < end; i++){
				Attempt a = otherAttempts.get(i);
				if(a.getTaps().size() != 5) continue;
				if(a.getMode() == Attempt.USER_MODE) continue;

				Instance instance = new DenseInstance(vectorForAttempt(a), -1);
				dataset.add(instance);
			}
			for (int i = 0; i < trainStart; i++) {
				Attempt a = otherAttempts.get(i);
				if(a.getTaps().size() != 5) continue;
				if(a.getMode() == Attempt.USER_MODE) continue;

				Instance instance = new DenseInstance(vectorForAttempt(a), -1);
				dataset.add(instance);
			}
		}

		//normalizer.filter(dataset);
		//Log.i("eval", "Num negative samples" + counter);
	}

	public Dataset fullDataset(Person user) throws SQLException {
		Dataset dataset = new DefaultDataset();
		String pin = "" + user.getPin();
		int pinLength = pin.length() ;

		for(Attempt a: user.getAttempts()){
			if(a.getTaps().size() != pinLength + 1) continue;

			//Log.i("ml_debug", "POS feature vector contains: " + Arrays.toString(vectorForAttempt(a)));
			Instance instance = new DenseInstance(vectorForAttempt(a));
			instance.setClassValue("pos");
			//Log.i("ml_debug", "POS Instance has attribute num = " + instance.noAttributes());
			dataset.add(instance);
		}

		// get others with the same pin
		List<Person> people = personDao.queryForEq("pin", user.getPin());
		for(Person other : people){			
			if(other.getId() == user.getId()) continue;


			for(Attempt a : other.getAttempts()) {
				if(a.getTaps().size() != pinLength + 1) continue;

				//Log.i("ml_debug", "NEG feature vector contains: " + Arrays.toString(vectorForAttempt(a)));
				Instance instance = new DenseInstance(vectorForAttempt(a));
				instance.setClassValue("neg");
				//Log.i("ml_debug", "NEG Instance has attribute num = " + instance.noAttributes());
				dataset.add(instance);
			}
		}

		return dataset;
	}

	public static Instance instanceForAttempt(Attempt attempt) {
		double[] featureVector = vectorForAttempt(attempt);
		//Log.i("feature", "Feature vector: " + Arrays.toString(featureVector));
		Instance inst = new DenseInstance(featureVector);
		//normalizer.filter(inst);

		return inst;
	}

	public static double[] vectorForAttempt(Attempt attempt){
		int nonAccelCount = attempt.getTaps().size() * FEATURES_PER_TAP;
		double[] features = new double[nonAccelCount + NUM_ACCEL_FEATURES];
		int tapNum = 0;
		for(Tap t : attempt.getTaps()){
			features[tapNum * FEATURES_PER_TAP] = (double) t.getLatency();
			features[tapNum * FEATURES_PER_TAP + 1] = (double) t.getDuration();
			features[tapNum * FEATURES_PER_TAP + 2] = t.getSize();
			//features[tapNum * FEATURES_PER_TAP + 3] = t.getPressure();
			tapNum++;
		}

		ArrayList<Double> accelStats = getAccelerometerFeatures(attempt.getAccelerometerData());
		for(int i = 0; i < accelStats.size();i++){
			features[nonAccelCount + i] = accelStats.get(i);
		}
		
		features[0] = 1; // hack to make sure first latency of zero doesn't become NaN w/ normalization

		return features;
	}

	private static ArrayList<AccelerometerData> normalizeAccelData(Collection<AccelerometerData> accelerometerData) {
		ArrayList<AccelerometerData> accelFeatures = new ArrayList<AccelerometerData>();

		DescriptiveStatistics x_stats = new DescriptiveStatistics();
		DescriptiveStatistics y_stats = new DescriptiveStatistics();
		DescriptiveStatistics z_stats = new DescriptiveStatistics();

		for(AccelerometerData accel : accelerometerData){
			x_stats.addValue(accel.getX());
			y_stats.addValue(accel.getY());
			z_stats.addValue(accel.getZ());
		}

		double x_mean = x_stats.getMean();
		double y_mean = y_stats.getMean();
		double z_mean = z_stats.getMean();

		for(AccelerometerData accel : accelerometerData){
			AccelerometerData newAccel = new AccelerometerData();
			newAccel.setX((float)(accel.getX() - x_mean));
			newAccel.setY((float)(accel.getY() - y_mean));
			newAccel.setZ((float)(accel.getZ() - z_mean));

			accelFeatures.add(newAccel);
		}

		return accelFeatures;
	}

	private static ArrayList<Double> getEnergyFeatures(ArrayList<AccelerometerData> accel) {
		ArrayList<Double> energyFeatures = new ArrayList<Double>(15);
		
		int start = 0;
		int end = 0;
		
		int numPoints = accel.size() / NUM_TAPS;
		for (int i = 0; i < NUM_TAPS - 1; i++) {
			end += numPoints;
			List<AccelerometerData> points = accel.subList(start, end);
			ArrayList<Double> accelEnergies = FeatureUtils.xyzEnergy(points, numPoints);
			energyFeatures.addAll(accelEnergies);
			start = end;
		}
		
		// Get last chunk of accel values
		List<AccelerometerData> points = accel.subList(start, accel.size());
		ArrayList<Double> accelEnergies = FeatureUtils.xyzEnergy(points, accel.size() - start);
		energyFeatures.addAll(accelEnergies);
		
		return energyFeatures;
	}
	
	// Takes a list of accelerometerdata points, converts them into three lists
	// of their x, y, and z values. Finally computes the "energy under the curve" for
	// each of the x, y, z lists and returns a three-element array that contains the
	// energy values for each x, y, z curve from the list of accelerometerdata points 
	private static ArrayList<Double> xyzEnergy(List<AccelerometerData> accelPoints, int numPoints) {
		ArrayList<Double> xyz = new ArrayList<Double>(3);
		
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		ArrayList<Double> z = new ArrayList<Double>();
		
		for (AccelerometerData point : accelPoints) {
			x.add((double)point.getX());
			y.add((double)point.getY());
			z.add((double)point.getZ());
		}
		
		xyz.add(FeatureUtils.computeEnergy(x)/numPoints);
		xyz.add(FeatureUtils.computeEnergy(y)/numPoints);
		xyz.add(FeatureUtils.computeEnergy(z)/numPoints);
		
		return xyz;
	}
	
	private static double computeEnergy(Collection<Double> points) {
		double energy = 0;
		
		for(Double p : points) {
			energy += Math.pow(p, 2);
		}
		
		return energy;
	}
	
	public static ArrayList<Double> getAccelerometerFeatures(Collection<AccelerometerData> accelerometerData) {
		// Normalize accel means before computing each component features
		ArrayList<AccelerometerData> normalizedData = FeatureUtils.normalizeAccelData(accelerometerData);
		
		DescriptiveStatistics x = new DescriptiveStatistics();
		DescriptiveStatistics y = new DescriptiveStatistics();
		DescriptiveStatistics z = new DescriptiveStatistics();

		for(AccelerometerData accel : normalizedData){
			x.addValue(accel.getX());
			y.addValue(accel.getY());
			z.addValue(accel.getZ());
		}
		
		ArrayList<Double> accelFeatures = new ArrayList<Double>();

		accelFeatures.add(x.getMean());
		accelFeatures.add(x.getPercentile(50.0));
		accelFeatures.add(x.getStandardDeviation());
		//accelFeatures.add(x.getMin());
		//accelFeatures.add(x.getMax());

		accelFeatures.add(y.getPercentile(50.0));
		accelFeatures.add(y.getMean());
		accelFeatures.add(y.getStandardDeviation());
		//accelFeatures.add(y.getMin());
		//accelFeatures.add(y.getMax());

		accelFeatures.add(z.getPercentile(50.0));
		accelFeatures.add(z.getMean());
		accelFeatures.add(z.getStandardDeviation());
		//accelFeatures.add(z.getMin());
		//accelFeatures.add(z.getMax());

		ArrayList<Double> energies = FeatureUtils.getEnergyFeatures(normalizedData);

		//Log.i("accel", "Accelerometer energies: " + FeatureUtils.arrayListToString(energies));
		
		return energies;
		// return accelFeatures
	}
	
	private static String arrayListToString(ArrayList<Double> aList) {
		String str = "";
		
		for(Double value : aList) {
			str = str + value + ", ";
		}
		
		return str;
	}
}
