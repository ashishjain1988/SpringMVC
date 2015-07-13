package com.spring.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PATIENT")
public class Patient {

	@Id
	@Column(name = "PATIENT_ID")
	int patientId;
	@Column(name = "GENDER")
	String GENDER;
	@Column(name = "CALCULATED_BIRTH_YEAR")
	int calculateBirthYear;
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getGENDER() {
		return GENDER;
	}
	public void setGENDER(String gENDER) {
		GENDER = gENDER;
	}
	public int getCalculateBirthYear() {
		return calculateBirthYear;
	}
	public void setCalculateBirthYear(int calculateBirthYear) {
		this.calculateBirthYear = calculateBirthYear;
	}
	
	
}
