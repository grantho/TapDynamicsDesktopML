import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.javaml.FeatureUtils;
import com.javaml.UserML;
import com.javaml.data.Database;
import com.javaml.data.Person;

public class DesktopML {
	private Database db;
	private UserML userML;
	
	public DesktopML(Database db) {
		this.db = db;
		userML = new UserML();
	}
	
	/**
	 * Creates a classifier for every user in the database and stores the classifier + training normalizer
	 * in a userML object for each user.
	 * @throws SQLException 
	 */
	public void createClassifiers() throws SQLException {
		List<Person> people = db.getPeople(1111);
		for (Person user : people) {
			String username = user.getUsername();
			
			userML = new UserML();
			userML.trainClassifier(user);
			FeatureUtils.evaluateTestError(userML.getClassifier(), userML.getTestSet(), username);
			FeatureUtils.evaluateTrainError(userML.getClassifier(), userML.getTrainSet(), username);
			//Classifier rf = classifierSet.makeRandomForest(p);
		}
	}
}
