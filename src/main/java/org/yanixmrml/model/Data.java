package org.yanixmrml.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="data")
@Table(name="data")
public class Data {
	
	@Id
	@Column(name="key")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int key;
	
	@Column(name="number")
	private double number;
	
	public Data() {
		super();
	}

	public Data(double number) {
		super();
		this.number = number;
	}
	
	public Data(int key, double number) {
		super();
		this.key = key;
		this.number = number;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

}
