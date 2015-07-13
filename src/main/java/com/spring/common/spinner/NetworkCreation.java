package com.spring.common.spinner;

import java.util.List;
import java.util.Map;




import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * @author Ashish Jain
 * 
 *
 */
public class NetworkCreation {
	
	public static void networkCreation(List<String> fileData,double networkData[][],Map<String,Integer> proteinIndexMap,double a[][], Double compressionFactor,Graph<MyNode,MyLink> g)
	{
		for(int j=0;j<fileData.size();j++)
		{
			String line = fileData.get(j);
			String lineData[] = line.split("\t");
			int indexA = proteinIndexMap.get(lineData[0].trim());
			int indexB = proteinIndexMap.get(lineData[1].trim());
			MyLink edge = new MyLink(Double.parseDouble(lineData[2].trim())*compressionFactor, j);
			MyNode n1 = new MyNode(indexA, lineData[0].trim());
			MyNode n2 = new MyNode(indexB, lineData[1].trim());
			g.addEdge(edge,n1,n2);
			networkData[indexA][indexB] = Double.parseDouble(lineData[2].trim())*compressionFactor;
			networkData[indexB][indexA] = Double.parseDouble(lineData[2].trim())*compressionFactor;
			int check = Double.compare(Double.parseDouble(lineData[2].trim())*compressionFactor,0);
			if(check > 0)
			{
				a[indexA][indexB] = 1;
				a[indexB][indexA] = 1;
			}else if(check == 0)
			{
				a[indexA][indexB] = 0;
				a[indexB][indexA] = 0;
			}else
			{
				a[indexA][indexB] = -1;
				a[indexB][indexA] = -1;
			}
		}
	}
	
	public static void networkCreation(List<String> fileData,String nodeName,List<String> nodesList,double networkData[][],Map<String,Integer> proteinIndexMap,double a[][],Graph<MyNode,MyLink> g)
	{
		for(int j=0;j<fileData.size();j++)
		{
			String line = fileData.get(j);
			String lineData[] = line.split("\t");
			if(nodesList.contains(lineData[0].trim()))
			{
				if(!(lineData[0].trim().equals(nodeName) || lineData[1].trim().equals(nodeName)))
				{
					int indexA = proteinIndexMap.get(lineData[0].trim());
					int indexB = proteinIndexMap.get(lineData[1].trim());
					networkData[indexA][indexB] = Double.parseDouble(lineData[2].trim());
					networkData[indexB][indexA] = Double.parseDouble(lineData[2].trim());
					MyLink edge = new MyLink(Double.parseDouble(lineData[2].trim()), j);
					MyNode n1 = new MyNode(indexA, lineData[0].trim());
					MyNode n2 = new MyNode(indexB, lineData[1].trim());
					g.addEdge(edge,n1,n2);
					int check = Double.compare(Double.parseDouble(lineData[2].trim()),0);
					if(check > 0)
					{
						a[indexA][indexB] = 1;
						a[indexB][indexA] = 1;
					}else if(check == 0)
					{
						a[indexA][indexB] = 0;
						a[indexB][indexA] = 0;
					}else
					{
						a[indexA][indexB] = -1;
						a[indexB][indexA] = -1;
					}
				}else
				{
					
				}
			}
		}
	}
	
	public static void networkCreationGeneTerrain(Map<Integer, List<Integer>> PPIMatrix,Map<Integer, String> geneMap, double networkData[][],Map<String,Integer> proteinIndexMap,double a[][], Double compressionFactor,Graph<MyNode,MyLink> g)
	{
		int count = 0;
		for(Entry<Integer, List<Integer>> entry : PPIMatrix.entrySet())
		{
			for(Integer i : entry.getValue())
			{
				int indexA = proteinIndexMap.get(geneMap.get(entry.getKey()));
				int indexB = proteinIndexMap.get(geneMap.get(i));
				MyLink edge = new MyLink(Double.parseDouble("1")*compressionFactor, count);
				MyNode n1 = new MyNode(indexA, geneMap.get(entry.getKey()));
				MyNode n2 = new MyNode(indexB, geneMap.get(i));
				g.addEdge(edge,n1,n2);
				networkData[indexA][indexB] = 1d*compressionFactor;
				networkData[indexB][indexA] = 1d*compressionFactor;
				int check = Double.compare(1d*compressionFactor,0);
				if(check > 0)
				{
					a[indexA][indexB] = 1;
					a[indexB][indexA] = 1;
				}else if(check == 0)
				{
					a[indexA][indexB] = 0;
					a[indexB][indexA] = 0;
				}else
				{
					a[indexA][indexB] = -1;
					a[indexB][indexA] = -1;
				}
				count++;
			}
		}
	}
	
	public static void outdegreeCalculation(int N,double a[][],double outDegree[])
	{
		double degreeCount;
		for(int i=0;i<N;i++)
		{
			degreeCount = 0;
			for(int j=0;j<N;j++)
			{
				/*degreeCount+=networkData[i][j];*/
				
				if(a[i][j]>0)
				{
					degreeCount = degreeCount + 1;
				}
			}
			outDegree[i] = degreeCount;
		}
	}

}
