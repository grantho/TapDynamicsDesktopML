package com.javaml.data;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="attempts")
public class Attempt {
	//public static final int TAPS_PER_ATTEMPT = 5;
	//public static final int FEATURES_PER_TAP = 4;

	@DatabaseField(generatedId = true)
	private long id;

	@ForeignCollectionField
	private Collection<Tap> taps;

	// New Accelerometer
	@ForeignCollectionField
	private Collection<AccelerometerData> accelerometerData;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Person person; 

	@DatabaseField
	private int mode;

	public static final int TRAINING_MODE = 1;
	public static final int USER_MODE = 0;

	public String toString() {
		String me = "Mode = " + mode;
		me += ", number of taps = " + taps.size();
		me += ", id = " + id;
		return me;
	}

	/*public double[] getFeatureVector(){
		double[] vector = new double[this.taps.size() * FEATURES_PER_TAP];
		int tapNum = 0;
		for(Tap t : this.getTaps()){
			vector[tapNum * FEATURES_PER_TAP] = (double) t.getLatency();
			vector[tapNum * FEATURES_PER_TAP + 1] = (double) t.getDuration();
			vector[tapNum * FEATURES_PER_TAP + 2] = t.getSize();
			vector[tapNum * FEATURES_PER_TAP + 3] = t.getPressure();
			ArrayList<Double> accelStats = getAccelerometerStats(t);
			for(int i = 0; i < accelStats.size();i++){
				vector[(tapNum * FEATURES_PER_TAP) + 4 + i] = accelStats.get(i);
			}
			tapNum++;
		}
		return vector;
	}*/

	public long getId() {
		return id;
	}

	public Attempt(){
		if(this.taps == null) this.taps = new ArrayList<Tap>();
		this.accelerometerData = new ArrayList<AccelerometerData>();
	}

	public Collection<Tap> getTaps() {
		return taps;
	}

	public void addTap(Tap t) {
		taps.add(t);
	}

	public void setTaps(Collection<Tap> taps) {
		this.taps = taps;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void addAccelerometer(AccelerometerData accel){
		accelerometerData.add(accel);
	}
	
	public Collection<AccelerometerData> getAccelerometerData() {
		return accelerometerData;
	}

	public void setAccelerometerData(Collection<AccelerometerData> readings) {
		this.accelerometerData = readings;
	}
}
