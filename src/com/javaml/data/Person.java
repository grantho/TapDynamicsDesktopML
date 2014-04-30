package com.javaml.data;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName= "people")
public class Person {

	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField
	private String username;

	@DatabaseField
	private int pin;

	// hand = 0 for one-handed or 1 for two-handed
	@DatabaseField	
	private int hand;

	// gender = 0 -> male
	// gender = 1 -> female
	@DatabaseField
	private int gender;

	// age = 0 -> 0 - 17
	// age = 1 -> 18 - 24
	// age = 2 -> 25 - 49
	// age = 3 -> 51+
	@DatabaseField
	private int age;

	@ForeignCollectionField
	private Collection<Attempt> attempts;

	public Person(String username, int pin){
		this.username = username;
		this.pin = pin;
		if(this.attempts == null) this.attempts = new ArrayList<Attempt>();
	}

	public Person(){
		if(this.attempts == null) this.attempts = new ArrayList<Attempt>();
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getHand() {
		return hand;
	}

	public void setHand(int hand) {
		this.hand = hand;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public Collection<Attempt> getAttempts() {  
		return attempts;
	}

	public void setAttempts(Collection<Attempt> attempts){
		this.attempts = attempts;
	}

	public boolean addAttempt(Attempt attempt){
		return this.attempts.add(attempt);
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return this.age;
	}
}
