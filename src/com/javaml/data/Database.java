package com.javaml.data;

import java.util.Collection;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.javaml.FeatureUtils;

public class Database {
	// the DAO objects we use to access the taps table
	private Dao<Tap, Integer> tapDao;
	private Dao<Attempt, Integer> attemptDao;
	private Dao<Person, Integer> personDao;
	private Dao<AccelerometerData, Integer> accelDao;

	private final static String DATABASE_LOCATION = "jdbc:sqlite:C:/Users/Grant/Desktop/main.db";
	private ConnectionSource connectionSource;

	public Database() throws Exception {
		Class.forName("org.sqlite.JDBC");
		connectionSource = new JdbcConnectionSource(DATABASE_LOCATION);

		tapDao = DaoManager.createDao(connectionSource, Tap.class);
		attemptDao = DaoManager.createDao(connectionSource, Attempt.class);
		personDao = DaoManager.createDao(connectionSource, Person.class);
		accelDao = DaoManager.createDao(connectionSource, AccelerometerData.class);

		TableUtils.createTableIfNotExists(connectionSource, Person.class);
		TableUtils.createTableIfNotExists(connectionSource, Attempt.class);
		TableUtils.createTableIfNotExists(connectionSource, Tap.class);
		TableUtils.createTableIfNotExists(connectionSource, AccelerometerData.class);

		FeatureUtils.initFeatureUtils(personDao);
	}

	public void printDatabase() throws SQLException{
		//check out DB
		List<Person> people = personDao.queryForAll();
		for(Person p : people){

			System.out.println("Person username: "+ p.getUsername());
			System.out.println("  pin: "+ p.getPin());
			System.out.println("  Has made " + p.getAttempts().size() + " attempts.");

			/*
			for(Attempt a : p.getAttempts()){
				System.out.println("    Attempt " + a.getId());
				System.out.println("    Mode:" + a.getMode());
				for(Tap t : a.getTaps()){
					Log.i("database","      Tap " + t.getId());
					Log.i("database","      duration: " + t.getDuration());
					Log.i("database","      latency: " + t.getLatency());
					Log.i("database","      number pressed: " + t.getNumberPressed());
					Log.i("database","      pressure: " + t.getPressure());
					Log.i("database","      size: " + t.getSize());
					Log.i("database","        Accelerometer Data");
					for(AccelerometerData d : t.getAccelerometerData()){
						System.out.println("        <"+d.getX()+","+d.getY()+","+d.getZ()+">");
					}
				}
			} */
		} 
		System.out.println("There are *" + people.size() + "* users in the database.");
	}

	public void addPerson(String user, int pin) throws SQLException {
		Person p = new Person(user, pin);
		personDao.createOrUpdate(p);
	}

	public int getNumUsers() throws SQLException {
		List<Person> people = personDao.queryForAll();
		return people.size();
	}

	public List<Person> getPeople(int pin) throws SQLException {
		return personDao.queryForEq("pin", pin);
	}

	public Person getPerson(String username) throws SQLException{
		List<Person> results = personDao.queryForEq("username", username);
		if(results.size() == 1) return results.get(0);
		return null;
	}

	public int getPin(String username) throws SQLException {
		Person p = getPerson(username);
		if(p == null) return 0;
		else return p.getPin();
	}

	public void deleteIncomplete() throws SQLException {
		List<Person> people = personDao.queryForAll();
		for(Person p : people){
			if((p.getPin() + "").length() < 4 || p.getAttempts().size() < 20 || p.getUsername().contains("DEMO")) {
				for(Attempt a : p.getAttempts()) {
					for(Tap t : a.getTaps()){
						tapDao.delete(t);
					}	
					for(AccelerometerData d : a.getAccelerometerData()){
						accelDao.delete(d);
					}
					attemptDao.delete(a);
				}
				personDao.delete(p);
			}
		}
	}

	public void deleteLatencyOutlier() throws SQLException {
		List<Person> people = personDao.queryForAll();
		for(Person p : people){
			for(Attempt a : p.getAttempts()) {
				if(!isOutlier(a.getTaps())) {
					continue;
				}

				for(Tap t : a.getTaps()){
					tapDao.delete(t);
				}	
				for(AccelerometerData d : a.getAccelerometerData()){
					accelDao.delete(d);
				}
				attemptDao.delete(a);
			}
		}
	}

	public boolean isOutlier(Collection<Tap> taps) {
		for(Tap t : taps){
			if(t.getLatency() > 1900) {
				return true;
			}
		}
		return false;
	}

	public void closeDatabase() throws SQLException {
		if (connectionSource != null) {
			connectionSource.close();
		}
	}
}