
import java.util.HashMap;

import com.javaml.UserML;
import com.javaml.data.Database;

/**
 * Using Identical DAO/database structure to Android project
 * @author Grant
 */
public class Main {
	private HashMap<String, UserML> classifiers;
	
	public static void main(String[] args) {
		// Read from SQLite database copied + pasted into the project
		// Use Java ML methods (copy + paste FeatureUtils + Tap Classifiers) to train and test the classifiers
		try {
			Database db = new Database();
			//db.deleteLatencyOutlier();
			//db.deleteIncomplete();
			DesktopML ml = new DesktopML(db);
			
			ml.createClassifiers();
			//db.printDatabase();
			
			db.closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
