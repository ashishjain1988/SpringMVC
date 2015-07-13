package com.spring.common.spinner;

public class MyLink {
	private double weight;
	private int id;
	public MyLink(double weight,int edgeIndex) {
		super();
		this.weight = weight;
		this.id= edgeIndex;
	}
	@Override
	public String toString() {
		return String.valueOf(weight);
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
