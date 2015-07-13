package com.spring.common.spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 
 * @author Ashish Jain
 *
 */
public class CreateNetworkBasedRank {
	
	public static Map<Integer, Map<Integer, Integer>> createNetworkBasedRankMap(Map<Integer, Integer> NetworkDivisionMap, Map<Integer, Double> nodeScoreMap)
	{
		Map<Integer, Map<Integer, Double>> networkBasedNodeScores = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Map<Integer, Integer>> networkBasedNodeRanks = new HashMap<Integer,Map<Integer,Integer>>();
		
		Map<Integer, Double> tempMap;
		int networkId;
		Double score;
		
		// Create Network Based Group
		for(Entry<Integer,Double> entry : nodeScoreMap.entrySet())
		{
			networkId = NetworkDivisionMap.get(entry.getKey());
			//System.out.println(networkId);
			score = entry.getValue();
			if(networkBasedNodeScores.get(networkId) == null)
			{
				tempMap = new HashMap<Integer, Double>();
			}else
			{
				tempMap = networkBasedNodeScores.get(networkId);
			}
			tempMap.put(entry.getKey(), score);
			networkBasedNodeScores.put(networkId, tempMap);
		}
		
		Map<Integer, Integer> tempMap1;
		// Create Network Based Group Ranking
		for(Entry<Integer, Map<Integer, Double>> entry : networkBasedNodeScores.entrySet())
		{
			networkId = entry.getKey();
			tempMap = SpinnerModular.sortByComparator(entry.getValue());
			tempMap1 = new HashMap<Integer, Integer>();
			int count = 1;
			double previous=0;
			for (Map.Entry<Integer, Double> entry1 : tempMap.entrySet()) {
				tempMap1.put(entry1.getKey(), count);
				//if(entry1.getValue() != previous)
				//{
					count++;
					//previous = entry1.getValue();
				//}
			}
			networkBasedNodeRanks.put(networkId, tempMap1);
		}
		
		
		return networkBasedNodeRanks;
	}
	
	public static Map<Integer, Map<Integer, Integer>> createNetworkBasedRankMap(Map<Integer, Integer> NetworkDivisionMap, Map<Integer, Double> nodeScoreMap,List<String> nodesInOriginalNetwork , Map<String, Integer> proteinIndexMap,String nodeName)
	{
		Map<Integer, Map<Integer, Double>> networkBasedNodeScores = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Map<Integer, Integer>> networkBasedNodeRanks = new HashMap<Integer,Map<Integer,Integer>>();
		
		Map<Integer, Double> tempMap = new HashMap<Integer, Double>();
		int networkId;
		Double score;
		
		// Create Network Based Group
		for(Entry<Integer,Double> entry : nodeScoreMap.entrySet())
		{
			networkId = NetworkDivisionMap.get(entry.getKey());
			score = entry.getValue();
			if(networkBasedNodeScores.get(networkId) == null)
			{
				tempMap = new HashMap<Integer, Double>();
			}else
			{
				tempMap = networkBasedNodeScores.get(networkId);
			}
			tempMap.put(entry.getKey(), score);
			networkBasedNodeScores.put(networkId, tempMap);
		}
		
		Map<Integer, Integer> tempMap1 = new HashMap<Integer, Integer>();
		List<Integer> nodesNotInNewNetwork = new ArrayList<Integer>();
		for(Entry<Integer, Map<Integer, Double>> entry : networkBasedNodeScores.entrySet())
		{
			networkId = entry.getKey();
			if(networkId != 1)
			{
				for (Map.Entry<Integer, Double> entry1 : tempMap.entrySet()) {
					nodesNotInNewNetwork.add(entry1.getKey());
				}
				
			}
		}
		//System.out.println("Size of Nodes not in network"+nodesNotInNewNetwork.size());
		int count = 1;
		Map<Integer, Integer> nodesInNewNetwork1 = new HashMap<Integer, Integer>();
		// Create Group Ranking on the basis of the new networks
		// Clubbing the one node network in the original network 
		for(Entry<Integer, Map<Integer, Double>> entry : networkBasedNodeScores.entrySet())
		{
			networkId = entry.getKey();
			tempMap1 = new HashMap<Integer, Integer>();
			if(networkId == 1)
			{
				//System.out.println("Network id"+networkId);
				tempMap = SpinnerModular.sortByComparator(entry.getValue());
				for (Map.Entry<Integer, Double> entry1 : tempMap.entrySet()) {
					tempMap1.put(entry1.getKey(), count);
					count++;
				}
				nodesInNewNetwork1 = new HashMap<Integer, Integer>(tempMap1);
				networkBasedNodeRanks.put(networkId, tempMap1);
			}
		}
		
		for(String s : nodesInOriginalNetwork)
		{
			if(nodesInNewNetwork1.get(proteinIndexMap.get(s)) == null && !s.equals(nodeName))
			{
				nodesInNewNetwork1.put(proteinIndexMap.get(s), count);
				count++;
			}
		}
		//System.out.println("New Network :"+nodesInNewNetwork1.size());
		networkBasedNodeRanks.put(1, nodesInNewNetwork1);
		return networkBasedNodeRanks;
	}

}
