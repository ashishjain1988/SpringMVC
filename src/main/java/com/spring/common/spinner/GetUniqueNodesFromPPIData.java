package com.spring.common.spinner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;
/**
 * 
 * @author Ashish Jain
 *
 *This class is being used to get the unique number of 
 *protein nodes from the given Protein-Protein Interaction
 *data input by the user.
 */
public class GetUniqueNodesFromPPIData {

	static BufferedReader br;
	//This method is used for getting the initial list of unique proteins from the given file
	public static void getUniqueNodesFromPPIData(String FILE_PATH,Map<String,Integer> proteinIndexMap,Map<Integer,String> indexProteinMap,List<String> fileData,Map<Integer, Integer> networkSize, Map<Integer, Integer> networkDivisionMap,Graph<MyNode, MyLink> g) throws IOException
	{
		br = new BufferedReader(new FileReader(FILE_PATH));
		//Skipping the first header line
		String line = br.readLine();
		line = br.readLine();
		int count = 0;

		//Index Creation
		while (line != null) {
			fileData.add(line);
			String lineData[] = line.split("\t");
			if(proteinIndexMap.get(lineData[0].trim()) == null)
			{
				proteinIndexMap.put(lineData[0].trim(),count);
				MyNode node = new MyNode(count, lineData[0].trim());
				g.addVertex(node);
				networkDivisionMap.put(count,count);
				networkSize.put(count, 1);
				indexProteinMap.put(count, lineData[0].trim());
				count++;
			}
			if(proteinIndexMap.get(lineData[1].trim()) == null)
			{
				proteinIndexMap.put(lineData[1].trim(),count);
				MyNode node = new MyNode(count, lineData[1].trim());
				g.addVertex(node);
				networkDivisionMap.put(count,count);
				networkSize.put(count, 1);
				indexProteinMap.put(count, lineData[1].trim());
				count++;
			}
			line = br.readLine();
		}
		br.close();
	}
	
	public static void getUniqueNodesFromRandomData(List<String> geneList,Map<String,Double> RandomPPIData,Map<String,Integer> proteinIndexMap,Map<Integer,String> indexProteinMap,List<String> fileData,Map<Integer, Integer> networkSize, Map<Integer, Integer> networkDivisionMap,Graph<MyNode, MyLink> g) throws IOException
	{
		/*for(int i=0;i<geneList.size();i++) {
			
				proteinIndexMap.put(geneList.get(i),i);
				networkDivisionMap.put(i,i);
				networkSize.put(i, 1);
				indexProteinMap.put(i, geneList.get(i));
		}*/
		int count = 0;
		//System.out.println(s);
		for(String s:RandomPPIData.keySet())
		{
			String PPI[] = s.split(",");
			if(proteinIndexMap.get(geneList.get(Integer.parseInt(PPI[0]))) == null)
			{
				proteinIndexMap.put(geneList.get(Integer.parseInt(PPI[0])),count);
				MyNode node = new MyNode(count, geneList.get(Integer.parseInt(PPI[0])));
				g.addVertex(node);
				networkDivisionMap.put(count,count);
				networkSize.put(count, 1);
				indexProteinMap.put(count, geneList.get(Integer.parseInt(PPI[0])));
				count++;
			}
			if(proteinIndexMap.get(geneList.get(Integer.parseInt(PPI[1]))) == null)
			{
				proteinIndexMap.put(geneList.get(Integer.parseInt(PPI[1])),count);
				MyNode node = new MyNode(count, geneList.get(Integer.parseInt(PPI[1])));
				g.addVertex(node);
				networkDivisionMap.put(count,count);
				networkSize.put(count, 1);
				indexProteinMap.put(count, geneList.get(Integer.parseInt(PPI[1])));
				count++;
			}
		}
		for(String s:RandomPPIData.keySet())
		{
			String PPI[] = s.split(",");
			fileData.add(geneList.get(Integer.parseInt(PPI[0])) + "\t" + geneList.get(Integer.parseInt(PPI[1])) + "\t" + RandomPPIData.get(s));
		}
	}
	
	// This method has been used to get the unique nodes from the new network data
	public static void getUniqueNodesFromPPIData(List<String> fileData,String nodeName,List<String> nodesList,Map<String,Integer> proteinIndexMap,Map<Integer,String> indexProteinMap,Map<Integer, Integer> networkSize, Map<Integer, Integer> networkDivisionMap) throws IOException
	{
		//Skipping the first header line
		//System.out.println("Entering unique nodes");
		int count = 0;
		for(int j=0;j<fileData.size();j++)
		{
			String line = fileData.get(j);
			String lineData[] = line.split("\t");
			/*if(!(lineData[1].trim().equals(nodeName) || lineData[0].trim().equals(nodeName)))
			{*/
			if(nodesList.contains(lineData[1].trim()) || nodesList.contains(lineData[0].trim()))
			{
				if(proteinIndexMap.get(lineData[0].trim()) == null)
				{
					if(!(lineData[0].trim().equals(nodeName)))
					{
						proteinIndexMap.put(lineData[0].trim(),count);
						networkDivisionMap.put(count,count);
						networkSize.put(count, 1);
						indexProteinMap.put(count, lineData[0].trim());
						count++;
					}
				}
				if(proteinIndexMap.get(lineData[1].trim()) == null)
				{
					if(!(lineData[1].trim().equals(nodeName)))
					{
						proteinIndexMap.put(lineData[1].trim(),count);
						networkDivisionMap.put(count,count);
						networkSize.put(count, 1);
						indexProteinMap.put(count, lineData[1].trim());
						count++;
					}
				}
			}
			/*}*/
		}
		
		//System.out.println("EXITING unique nodes");
	}
	
		public static void getUniqueNodesFromTerrainData(Map<Integer, String> geneMap,Map<String,Integer> proteinIndexMap,Map<Integer,String> indexProteinMap,List<String> fileData,Map<Integer, Integer> networkSize, Map<Integer, Integer> networkDivisionMap,Graph<MyNode, MyLink> g) throws IOException
		{
			int count = 0;
			for(Entry<Integer, String> entry : geneMap.entrySet())
			{
				String geneName = entry.getValue();
				if(proteinIndexMap.get(geneName) == null)
				{
					proteinIndexMap.put(geneName,count);
					MyNode node = new MyNode(count, geneName);
					g.addVertex(node);
					networkDivisionMap.put(count,count);
					networkSize.put(count, 1);
					indexProteinMap.put(count, geneName);
					count++;
				}
			}
		}
}
