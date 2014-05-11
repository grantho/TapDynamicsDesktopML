package com.javaml.analysis;

import java.util.List;

import com.javaml.data.Attempt;
import com.javaml.data.Person;
import com.javaml.data.Tap;

public class RawDataAnalysis {
	String filename;

	public RawDataAnalysis(String filename) {
		this.filename = filename;
	}

	public void writeDurationsToCsv(List<Person> people) {
		String[] durations = new String[5];
		for (int i = 0; i < durations.length; i++) {
			durations[i] = "";
		}
		String[] latencies = new String[5];
		for (int i = 0; i < latencies.length; i++) {
			latencies[i] = "";
		}
		String[] sizes = new String[5];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = "";
		}

		// For every person's taps, update the respective duration's list with his/her tap duration
		for (Person person : people) {
			for (Attempt attempt : person.getAttempts()) {
				for (Tap tap : attempt.getTaps()) {
					int tapNum = tap.getTapNumber();
					// Get the list for this tap number and add this tap's duration
					String durationString = durations[tapNum] + tap.getDuration() + "\r\n";
					durations[tapNum] = durationString;

					String latencyString = latencies[tapNum] + tap.getLatency() + "\r\n";
					latencies[tapNum] = latencyString;
					
					String sizeString = sizes[tapNum] + tap.getSize() + "\r\n";
					sizes[tapNum] = sizeString;
				}
			}
		}
		
		for (int i = 0; i < durations.length; i++) {
			String file = "duration" + i + ".csv";
			Utility.writeToFile(file, durations[i]);
			
			file = "latency" + i + ".csv";
			Utility.writeToFile(file, latencies[i]);
			
			file = "size" + i + ".csv";
			Utility.writeToFile(file, sizes[i]);
		}
	}
}
