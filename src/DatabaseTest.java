import static org.junit.Assert.*;

import org.junit.Test;

import com.javaml.data.Database;


public class DatabaseTest {

	@Test
	public void test() {
		try {
			Database db = new Database();
			//db.addPerson("bobby", 1111);
			//assert(db.getPin("bobby") == 1111);
			db.closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail("Exception thrown");
		}
	}
	
	@Test
	public void test2() {
		try {
			Database db = new Database();
			assert(db.getNumUsers() > 10);
			db.closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail("Exception thrown");
		}
	}

}
