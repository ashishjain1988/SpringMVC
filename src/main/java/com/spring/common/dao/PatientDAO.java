package com.spring.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.common.model.Patient;
import com.spring.common.model.TestSet;

@Repository("PatientDAO")
@Transactional
public class PatientDAO {
	 
	@PersistenceContext
	private EntityManager entityManager;
	 
	public EntityManager getEntityManager() {
	return entityManager;
	}
	 
	public void setEntityManager(EntityManager entityManager) {
	this.entityManager = entityManager;
	}
	 
	public void insert(Patient person) {
	entityManager.persist(person);
	}
	 
	public List<TestSet> selectAll() {
	CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
	Query query = entityManager.createNativeQuery("select * from testset", TestSet.class);
	List<TestSet> persons = query.getResultList();
	return persons;
	}
}
