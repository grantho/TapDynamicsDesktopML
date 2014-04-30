package com.javaml.data;

import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class Database {
	// the DAO objects we use to access the taps table
	private Dao<Tap, Integer> tapDao;
	private Dao<Attempt, Integer> attemptDao;
	private Dao<Person, Integer> personDao;
	private Dao<AccelerometerData, Integer> accelDao;
	
	private final static String DATABASE_LOCATION = "jdbc:sqlite:test.db";
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
	}
	
	/**
	 * Reads sqlite database from file and initializes ORM/DAO objects
	 * to access the database
	 * @param path: path to sqlite db IN JAVA PROJECT FOLDER
	 */
	public void readDatabaseFromFile(String path) {
		
	}
	
	public void addPerson(String user, int pin) throws SQLException {
		Person p = new Person(user, pin);
		personDao.createOrUpdate(p);
	}
	
	public int getNumUsers() throws SQLException {
		List<Person> people = personDao.queryForAll();
		return people.size();
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
}