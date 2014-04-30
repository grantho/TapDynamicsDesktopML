import java.util.HashMap;

import com.javaml.UserML;

public class Main {
	private HashMap<String, UserML> classifiers;
	
	public static void main(String[] args) {
		// Create indentical DAO structure (copy + paste) from Android
		// Read from SQLite database copied + pasted into the project
		// Use Java ML methods (copy + paste FeatureUtils + Tap Classifiers) to train and test the classifiers
	}
	
	/**
	 * Creates a classifier for every user in the database and stores the classifier + training normalizer
	 * in a userML object for each user.
	 */
	public void createClassifiers() {
		/*RuntimeExceptionDao<Person, Integer> personDao = getHelper().getPersonDao();
		List<Person> people = personDao.queryForEq("pin", pin);
		for (Person user : people) {
			String username = user.getName();
			
			userML = new UserML();
			trainClassifier();
			FeatureUtils.evaluateTestError(userML.getClassifier(), userML.getTestSet(), username);
			FeatureUtils.evaluateTrainError(userML.getClassifier(), userML.getTrainSet(), username);
			//Classifier rf = classifierSet.makeRandomForest(p);
		}*/
	}

}
