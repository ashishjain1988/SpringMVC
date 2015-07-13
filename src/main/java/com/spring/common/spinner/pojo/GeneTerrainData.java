package com.spring.common.spinner.pojo;

import java.util.List;
import java.util.Map;

public class GeneTerrainData {

	private Map<Integer, String> geneMap;
	private Map<Integer, List<Integer>> PPIMatrix;
	public Map<Integer, String> getGeneMap() {
		return geneMap;
	}
	public void setGeneMap(Map<Integer, String> geneMap) {
		this.geneMap = geneMap;
	}
	public Map<Integer, List<Integer>> getPPIMatrix() {
		return PPIMatrix;
	}
	public void setPPIMatrix(Map<Integer, List<Integer>> pPIMatrix) {
		PPIMatrix = pPIMatrix;
	}
}
