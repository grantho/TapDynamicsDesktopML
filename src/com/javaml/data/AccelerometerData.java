package com.javaml.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="accelerometer_data")
public class AccelerometerData {

	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField
	private int readingNumber;

	@DatabaseField
	private float x;

	@DatabaseField
	private float y;

	@DatabaseField
	private float z;

	// New Accelerometer
	@DatabaseField(foreign=true, foreignAutoRefresh = true)
	private Attempt attempt;
	//@DatabaseField(foreign = true, foreignAutoRefresh = true)
	//private Tap tap;

	public AccelerometerData() {}

	public AccelerometerData(float x, float y, float z,int readingNumber, Attempt attempt){
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setReadingNumber(readingNumber);
		this.setAttempt(attempt);
		//this.setTap(tap);
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public long getId() {
		return id;
	}

	public int getReadingNumber() {
		return readingNumber;
	}

	public void setReadingNumber(int readingNumber) {
		this.readingNumber = readingNumber;
	}

	public Attempt getAttempt() {
		return attempt;
	}
	
	public void setAttempt(Attempt a) {
		this.attempt = a;
	}
	
//	public Tap getTap() {
//		return tap;
//	}
//
//	public void setTap(Tap tap) {
//		this.tap = tap;
//	}

	public float round(float roundMe) {
		long multipled = Math.round(roundMe * 100000);
		return (float) (multipled/100000.0);
	}

	public boolean equals(AccelerometerData previous) {
		return false;
		//return (x - previous.getX() < 0.0001) && (y - previous.getY() < 0.0001) && (z - previous.getZ() < 0.0001);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public String toString() {
		return "These are the accelerometer stats: x = " + x + ", y = " + y + ", z = " + z;
	}
}
