package com.spring.common.spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author Ashish Jain
 *
 */
public class CreateRandomNetwork {
	
	public static void main(String[] args) throws FileNotFoundException {
		List<String> geneList = new ArrayList<String>();
		geneList.add("P1");
		geneList.add("P2");
		geneList.add("P3");
		geneList.add("P4");
		/*for(int i=0;i<10;i++)
		{
			geneList.add("Protein_"+(i+1));
		}*/
		Map<String, Double> PPIList = createRandomNetworkWithBias(geneList, 0.9d);
		PrintWriter pw = new PrintWriter(new File("RandomNetworkWithBias.txt"));
		pw.println("Protein_1" + "\t" + "Protein_2" + "\t" + "Interaction_Score");
		for(String s:PPIList.keySet())
		{
			String PPI[] = s.split(",");
			pw.println(geneList.get(Integer.parseInt(PPI[0])) + "\t" + geneList.get(Integer.parseInt(PPI[1])) + "\t" + PPIList.get(s));
		}
		pw.close();
		/*PPIList = createRandomNetworkWithoutBias(geneList, 0.9d);
		pw = new PrintWriter(new File("RandomNetworkWithoutBias.txt"));
		pw.println("Protein_1" + "\t" + "Protein_2" + "\t" + "Interaction_Score");
		for(String s:PPIList.keySet())
		{
			String PPI[] = s.split(",");
			pw.println(geneList.get(Integer.parseInt(PPI[0])) + "\t" + geneList.get(Integer.parseInt(PPI[1])) + "\t" + PPIList.get(s));
		}
		pw.close();*/
	}
	public static Map<String, Double> createRandomNetworkWithBias(List<String> genesList,Double Cfactor)
	{
		Map<Integer,Double> proteinWeights = new HashMap<Integer, Double>();
		Map<Integer,Double> proteinWeightScale = new HashMap<Integer, Double>();
		Map<String, Double> PPIWeights = new HashMap<String, Double>();
		for(int i=0;i<genesList.size();i++)
		{
			proteinWeights.put(i, 1d);
			proteinWeightScale.put(i,(double)(i+1));
		}
		int numberOfProteins = genesList.size();
		int iterationLength = (genesList.size()*(genesList.size()-1))/2;
		double sumOfProteinWeighs = 0;
		int protein1 = 0,protein2 = 0;
		double random;
		for(int i=0;i<iterationLength;i++)
		{
			protein1 = protein2 = -1;
			sumOfProteinWeighs = 0;
			for(int j=0;j<proteinWeights.size();j++)
			{
				sumOfProteinWeighs +=proteinWeights.get(j);
			}
			while(protein1 == -1)
			{
				random = Math.random()*sumOfProteinWeighs;
				for(int k=0;k<proteinWeightScale.size();k++)
				{
					if(proteinWeightScale.get(k)>=random)
					{
						//System.out.println("First Random Protein " +k);
						protein1 = k;
						double weigh = proteinWeights.get(k);
						proteinWeights.put(k, weigh+1);
						break;
					}
				}
			}
			//System.out.println("Protein Weights "+proteinWeightScale);
			while(true)
			{
				boolean flag = false;
				random = Math.random()*sumOfProteinWeighs;
				for(int k=0;k<proteinWeightScale.size();k++)
				{
					if(proteinWeightScale.get(k)>=random)
					{
						if(protein1!=k)
						{
							//System.out.println("Second Random Protein " +k/*+ " and protein 1 is "+protein1*/);
							flag=true;
							protein2 = k;
							double weigh = proteinWeights.get(k);
							proteinWeights.put(k, weigh+1);
						}
						break;
					}
				}
				if(flag)
				{
					break;
				}
			}
			String proteinPairString = protein1+","+protein2;
			String proteinPairString1 = protein2+","+protein1;
			Double PPIweight = 0d;
			if(PPIWeights.containsKey(proteinPairString))
			{
				PPIweight = PPIWeights.get(proteinPairString);
				PPIweight = 1-((1-PPIweight)*(1-Cfactor));
				PPIWeights.put(proteinPairString, PPIweight);
			}else if(PPIWeights.containsKey(proteinPairString1))
			{
				PPIweight = PPIWeights.get(proteinPairString1);
				PPIweight = 1-((1-PPIweight)*(1-Cfactor));
				PPIWeights.put(proteinPairString1, PPIweight);
			}else
			{
				PPIweight = 1-((1-PPIweight)*(1-Cfactor));
				PPIWeights.put(proteinPairString, PPIweight);
			}
			
			sumOfProteinWeighs +=2;
			for(int j=0;j<proteinWeights.size();j++)
			{
				if(j==0)
				proteinWeightScale.put(j,((numberOfProteins*proteinWeights.get(j))/sumOfProteinWeighs));
				else
				{
					proteinWeightScale.put(j,proteinWeightScale.get(j-1)+((numberOfProteins*proteinWeights.get(j))/sumOfProteinWeighs));
				}
			}
		}
		
		//return PPIWeights;
		/*for(String s:PPIWeights.keySet())
		{
			System.out.println(s + " "+ PPIWeights.get(s));
		}*/
		return PPIWeights;
	}
	public static Map<String, Double> createRandomNetworkWithoutBias(List<String> genesList,Double Cfactor)
	{
		Map<String, Double> PPIWeights = new HashMap<String, Double>();
		int numberOfProteins = genesList.size();
		int iterationLength = (numberOfProteins*(numberOfProteins-1))/2;
		int protein1 = 0,protein2 = 0;
		for(int i=0;i<iterationLength;i++)
		{
			protein1 = protein2 = -1;
			while(protein1 == -1)
			{
				protein1 = (int)Math.floor(Math.random()*numberOfProteins);
			}
			while(true)
			{
				boolean flag = false;
				protein2 = (int)Math.floor(Math.random()*numberOfProteins);
				if(protein1 != protein2)
				{
					flag=true;
				}
				if(flag)
				{
					break;
				}
			}
			String proteinPairString = protein1+","+protein2;
			String proteinPairString1 = protein2+","+protein1;
			Double PPIweight = 0d;
			if(PPIWeights.containsKey(proteinPairString))
			{
				PPIweight = PPIWeights.get(proteinPairString);
				PPIweight = 1-((1-PPIweight)*Cfactor);
				PPIWeights.put(proteinPairString, PPIweight);
			}else if(PPIWeights.containsKey(proteinPairString1))
			{
				PPIweight = PPIWeights.get(proteinPairString1);
				PPIweight = 1-((1-PPIweight)*Cfactor);
				PPIWeights.put(proteinPairString1, PPIweight);
			}else
			{
				PPIweight = 1-((1-PPIweight)*Cfactor);
				PPIWeights.put(proteinPairString, PPIweight);
			}
		}
		
		/*for(String s:PPIWeights.keySet())
		{
			System.out.println(s + " "+ PPIWeights.get(s));
		}*/
		return PPIWeights;
	}
}
