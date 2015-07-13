package com.spring.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.common.dao.PatientDAO;
import com.spring.common.dto.CancerDTO;

@Controller
public class HelloWorldController /*extends AbstractController*/ {

	@Autowired
	private PatientDAO patientDAO;
	
	@RequestMapping("/home")
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//List<TestSet> patients = patientDAO.selectAll();
		ModelAndView model = new ModelAndView("bootstrap-template");
		model.addObject("msg", "hello world");
		CancerDTO cancerDTO = new CancerDTO();
		model.addObject("CancerDTO", cancerDTO);
		return model;
	}
	
	/*@RequestMapping("/getCancerName")
	CancerDTO getCancerName(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//List<TestSet> patients = patientDAO.selectAll();
		ModelAndView model = new ModelAndView("bootstrap-template");
		model.addObject("msg", "hello world");
		return cancerDTO;
	}*/

	public PatientDAO getPatientDAO() {
		return patientDAO;
	}

	public void setPatientDAO(PatientDAO patientDAO) {
		this.patientDAO = patientDAO;
	}
}