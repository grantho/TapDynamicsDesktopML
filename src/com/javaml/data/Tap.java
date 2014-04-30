package com.javaml.data;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "taps")
public class Tap {
	
	@DatabaseField(generatedId = true)
	private long id;
	
	@DatabaseField(foreign=true, foreignAutoRefresh = true)
	private Attempt attempt;
	
	// New Accelerometer
//	@ForeignCollectionField
//	private Collection<AccelerometerData> accelerometerData;
	
	/**
	 * Tap number, i.e. the order in which the taps were made
	 */
	@DatabaseField
	private int tapNumber;
	
	/**
	 * The number pressed by the user.
	 */
	@DatabaseField
	private int keyPressed;
	
	/**
	 * The amount of time elapsed before this tap
	 */
	@DatabaseField
	private long latency;
	
	/**
	 * The duration of the tap
	 */
	@DatabaseField
	private long duration;
	
	/**
	 * Surface area of the tap
	 */
	@DatabaseField
	private double size;
	
	/**
	 * Pressure that user exerted during tap
	 */
	@DatabaseField
	private double pressure;
	
	private long pressTime;
	private long releaseTime;
	
//	public Tap(){
//		if(this.accelerometerData == null) this.accelerometerData = new ArrayList<AccelerometerData>();
//	}

	public int getTapNumber() {
		return tapNumber;
	}

	public void setTapNumber(int tapNumber) {
		this.tapNumber = tapNumber;
	}

	public int getKeyPressed() {
		return keyPressed;
	}

	public void setKeyPressed(int numberPressed) {
		this.keyPressed = numberPressed;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public long getId() {
		return id;
	}

	public Attempt getAttempt() {
		return attempt;
	}

	public void setAttempt(Attempt attempt) {
		this.attempt = attempt;
	}
	
	public void addTapPressureAndSize(float pressure, float size) {
		this.pressure = pressure;
		this.size = size;
	}

	public long getPressTime() {
		return pressTime;
	}

	public void setPressTime(long pressTime) {
		this.pressTime = pressTime;
	}

	public long getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

//	public Collection<AccelerometerData> getAccelerometerData() {
//		return accelerometerData;
//	}
//	
//	public void setAccelerometerData(Collection<AccelerometerData> readings) {
//		this.accelerometerData = readings;
//	}

}