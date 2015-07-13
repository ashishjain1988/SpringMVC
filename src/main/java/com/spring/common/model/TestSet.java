package com.spring.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TESTSET")
public class TestSet {

	@Id
	@Column(name="LAB_TEST_GROUP_ID")
	Long testSetId;
	@Column(name="TEST_GROUP_NAME")
	String testSetName;
	public Long getTestSetId() {
		return testSetId;
	}
	public void setTestSetId(Long testSetId) {
		this.testSetId = testSetId;
	}
	public String getTestSetName() {
		return testSetName;
	}
	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}
	
	
}
