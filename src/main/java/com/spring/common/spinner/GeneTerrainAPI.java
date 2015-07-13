package com.spring.common.spinner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.spring.common.spinner.pojo.GeneTerrainData;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
/**
 * 
 * @author Ashish Jain
 * This class is used to calculate and store the Initial and 
 * Iterative rank score of the the interaction of the protein 
 * node in a protein network by reading the protein-protein 
 * interaction data.
 *
 */
public class GeneTerrainAPI {

	public final static double SIGMA = 0.85;
	public final static int ALPHA = 2;
	InputStream fileURL = this.getClass().getResourceAsStream("/com/spring/common/spinner/STRING_Proteins_RankingScore.txt"); 
	
	public Map<String,Map<Integer, String>> getSpinnerResults(GeneTerrainData geneTerrainData)
	{
		Map<String,Map<Integer, String>> spinnerResultMap = new HashMap<String, Map<Integer,String>>();
		final String INITIAL_SCORE= "INITIAL_SCORE";
		final String ITERATIVE_SCORE= "ITERATIVE_SCORE";
		final String DEGREE= "DEGREE";
		final String BETWEENESS= "BETWEENESS";
		final String CLUSTERINGCOEFFICIENT= "CLUSTERINGCOEFFICIENT";
		
		BufferedReader br;
		List<String> genesList = new ArrayList<String>();
		String FILE_PATH = "";
		String diseaseName = "Breast Cancer";//
		Double compressionFactor = 1d;
		boolean globalAdjustment = false;//
		int sFactorBase = 10;
		int ITERATION_NUMBER = 40;
		//boolean iterationFlag = false;
		int proteinSeed = 1;//
		boolean isFileFound = false;
		Graph<MyNode, MyLink> PPINetworkGraph = new SparseMultigraph<MyNode, MyLink>();
		String proteinSource = "STRING";//
		Map<Integer, String> geneMap = geneTerrainData.getGeneMap();
		Map<String, Integer> inverseGeneMap = new HashMap<String, Integer>();
		for(Entry<Integer, String> entry:geneMap.entrySet())
		{
			genesList.add(entry.getValue());
			inverseGeneMap.put(entry.getValue(), entry.getKey());
		}
		
		GeneTerrainAPI geneTerrainAPI = new GeneTerrainAPI();
		Map<String,Integer> proteinIndexMap = new HashMap<String, Integer>();
		Map<Integer,String> indexProteinMap = new HashMap<Integer,String>();
		Map<Integer,Double> initialRankingMap = new HashMap<Integer,Double>();
		Map<Integer,Double> iterativeRankingMap = new HashMap<Integer,Double>();
		double [] outDegree;
		List<String> fileData = new LinkedList<String>();
		double networkData[][];
		double r[][];
		double a[][];
		int N = 0;
		Map<Integer, Integer> networkDivisionMap = new HashMap<Integer, Integer>();//Node No and Network Id
		Map<Integer, Integer> networkDivisionFinal = new HashMap<Integer, Integer>();
		Map<Integer, Integer> networkSize = new HashMap<Integer, Integer>();
		//Map<String, String> geneProteinMapping = new HashMap<String, String>();
		Map<Integer, Integer> GlobalProteinInitialRank = new HashMap<Integer, Integer>();
		Map<Integer, Integer> GlobalProteinIterativeRank = new HashMap<Integer, Integer>();
		Map<Integer, Float> GlobalProteinDegree = new HashMap<Integer, Float>();
		Map<String, Double> PPIWeights = new HashMap<String, Double>();
		try {
			
			GetUniqueNodesFromPPIData.getUniqueNodesFromTerrainData(geneMap, proteinIndexMap, indexProteinMap, fileData, networkSize, networkDivisionMap,PPINetworkGraph);
			if(globalAdjustment)
			{
				//Global String Database File Upload
				br = new BufferedReader(new InputStreamReader(geneTerrainAPI.fileURL));
				String line = br.readLine();
				while (line != null) {
					String lineData[] = line.split("\t");
					if(proteinIndexMap.get(lineData[1]) != null)
					{
						GlobalProteinInitialRank.put(Integer.parseInt(lineData[4]),proteinIndexMap.get(lineData[1]));
						GlobalProteinIterativeRank.put(Integer.parseInt(lineData[ITERATION_NUMBER+3+((ITERATION_NUMBER-1))]), proteinIndexMap.get(lineData[1]));
						GlobalProteinDegree.put(proteinIndexMap.get(lineData[1]), Float.parseFloat(lineData[2]));
					}
					line = br.readLine();
				}
				br.close();
			}
			
			//Network Creation
			N = indexProteinMap.size();
			networkData = new double[N][N];
			a = new double[N][N];
			r = new double [N][ITERATION_NUMBER];
			NetworkCreation.networkCreationGeneTerrain(geneTerrainData.getPPIMatrix(),geneMap, networkData, proteinIndexMap, a,compressionFactor,PPINetworkGraph);
			//System.out.println("After Network Creation");
			//Calculating OutDegree
			outDegree = new double [N];
			NetworkCreation.outdegreeCalculation(N, a, outDegree);
			// Calculating Initial Rp Score
			ScoreCalculation.calculateInitialRScore(N, networkData, a, initialRankingMap, r);
			//System.out.println("After Score Calculation");
			//*************Network division finding Start*****************//
			SubNetworkIdentification.subNetworkIdentification(N, a, networkDivisionMap, networkDivisionFinal, networkSize, indexProteinMap);
			//*************Network division finding End*****************//
			Map<Integer, Integer> networkCount = new HashMap<Integer, Integer>();
			for(Entry<Integer, Integer> entry: networkDivisionFinal.entrySet())
			{
				if(networkCount.get(entry.getValue()) != null)
				{
					int count = networkCount.get(entry.getValue());
					networkCount.put(entry.getValue(),count+1);
				}else
				{
					networkCount.put(entry.getValue(),1);
				}
			}
			//Calculating Iterative Confidence
			ScoreCalculation.calculateIterativeRScore(N, ITERATION_NUMBER, outDegree, networkData, iterativeRankingMap, r);
			//System.out.println("After Iterative Score Calculation");
			initialRankingMap = SpinnerModular.sortByComparator(initialRankingMap);
			int intialRanking[] = SpinnerModular.createRankingArray(initialRankingMap, N);
			int iterativeRank [];
			iterativeRankingMap = SpinnerModular.sortByComparator(iterativeRankingMap);
			iterativeRank = SpinnerModular.createRankingArray(iterativeRankingMap, N);
			Map<Integer, Map<Integer, Integer>> networkBasedIntitalRanks = CreateNetworkBasedRank.createNetworkBasedRankMap(networkDivisionFinal, initialRankingMap);
			Map<Integer, Map<Integer, Integer>> networkBasedIterativeRanks;
			networkBasedIterativeRanks = CreateNetworkBasedRank.createNetworkBasedRankMap(networkDivisionFinal, iterativeRankingMap);
			//System.out.println("Entering delta calculation");
			//Creating Delta From R[0] and R[No of iterations]
			Map<Integer,Double> outDegreeMap = new HashMap<Integer,Double>();
			for(int i=0;i<outDegree.length;i++)
			{
				outDegreeMap.put(i, outDegree[i]);
			}
			outDegreeMap = SpinnerModular.sortByComparator(outDegreeMap);
			int outDegreeRanking[] = SpinnerModular.createRankingArray(outDegreeMap, N);
			
			Map<Integer, Double> deltaValueMap = new HashMap<Integer, Double>();// Contains Node Index as key and Delta as Map
			Map<Integer, Double> deltaRankMap = new HashMap<Integer, Double>();
			Double delta = 0.0;
			Double Rank = 0.0;
			double R0 = 0;
			double RN = 0;
			for(int i=0;i<N;i++)
			{
				R0 = intialRanking[i];
				RN = iterativeRank[i];
				Rank = (R0 - RN);
				delta = Rank /R0;
				deltaRankMap.put(i,Rank);
				deltaValueMap.put(i,delta);
			}

			//Global Adjustment Starts
			Map<Integer, Integer> GlobalProteinInitialRankSorted = new HashMap<Integer, Integer>();
			Map<Integer, Integer> GlobalProteinIterativeRankSorted = new HashMap<Integer, Integer>();
			Double GInitialDeltaRank [] = new Double[N];
			Double GIterativeDeltaRank []= new Double[N];
			double SFactorInitial [] = new double[N];
			double SFactorIterative []= new double[N];
			double AdjustedRpScoreInitial [] = new double[N];
			double AdjustedRpScoreIterative []= new double[N];
			if(globalAdjustment)
			{

				int count = 1;
				List<Integer> list = new ArrayList<Integer>(GlobalProteinInitialRank.keySet());
				Collections.sort(list);
				for(Integer i : list)
				{
					GlobalProteinInitialRankSorted.put(GlobalProteinInitialRank.get(i), count);
					count++;
				}
				count = 1;
				list = new ArrayList<Integer>(GlobalProteinIterativeRank.keySet());
				Collections.sort(list);
				for(Integer i : list)
				{
					GlobalProteinIterativeRankSorted.put(GlobalProteinIterativeRank.get(i), count);
					count++;
				}


				for(int i=0;i<N;i++)
				{
					if(GlobalProteinInitialRankSorted.get(i) != null)
					{
						GInitialDeltaRank[i] = (double)(GlobalProteinInitialRankSorted.get(i) - intialRanking[i])/*/(double)GlobalProteinIterativeRankSorted.get(i)*/;
					}else
					{
						GInitialDeltaRank[i]= 0d;
					}
					double sFactorBaseCalculated = (networkCount.get(networkDivisionFinal.get(i))*sFactorBase)/100d;
					SFactorInitial[i] = Math.pow(sFactorBaseCalculated,(GInitialDeltaRank[i]/N));
					AdjustedRpScoreInitial [i] = SFactorInitial[i] * r[i][0];
					if(GlobalProteinIterativeRankSorted.get(i) != null)
					{
						GIterativeDeltaRank[i] = (double)(GlobalProteinIterativeRankSorted.get(i) - iterativeRank[i])/*/(double)GlobalProteinIterativeRankSorted.get(i)*/;
					}else
					{
						GIterativeDeltaRank[i] = 0d;
					}
					SFactorIterative[i] = Math.pow(Double.valueOf(sFactorBase),(GIterativeDeltaRank[i]/N));
					AdjustedRpScoreIterative[i] = SFactorIterative[i] * r[i][ITERATION_NUMBER - 1];
				}
			}
			Map<Integer,Double> AdjustedIterativeRankingMap = new HashMap<Integer,Double>();
			Map<Integer,Double> AdjustedInitialRankingMap = new HashMap<Integer,Double>();
			for(int i=0;i<AdjustedRpScoreInitial.length;i++)
			{
				AdjustedInitialRankingMap.put(i, AdjustedRpScoreInitial[i]);
				AdjustedIterativeRankingMap.put(i, AdjustedRpScoreIterative[i]);
			}
			AdjustedInitialRankingMap = SpinnerModular.sortByComparator(AdjustedInitialRankingMap);
			int AdjustedintialRanking[] = SpinnerModular.createRankingArray(AdjustedInitialRankingMap, N);
			int AdjustedIterativeRank [];
			AdjustedIterativeRankingMap = SpinnerModular.sortByComparator(AdjustedIterativeRankingMap);
			AdjustedIterativeRank = SpinnerModular.createRankingArray(AdjustedIterativeRankingMap, N);
			//System.out.println("Before Priting file Result");
			
			//Betweeness Centrality
			BetweennessCentrality<MyNode, MyLink> PPINodesBetweenness = new BetweennessCentrality<MyNode, MyLink>(PPINetworkGraph);
			Map<MyNode, Double> betweenessMap = new HashMap<MyNode, Double>();
			for(int i=0;i<N;i++)
			{
				betweenessMap.put(new MyNode(i, indexProteinMap.get(i)), PPINodesBetweenness.getVertexScore(new MyNode(i, indexProteinMap.get(i))));
			}
			betweenessMap = SpinnerModular.sortByComparatorCC(betweenessMap);
			int ccRank = 1;
			Map<MyNode, Integer> betweenessRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : betweenessMap.entrySet())
			{
				betweenessRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}
			
			//Clustering Coefficient
			Map<MyNode, Double> clusteringCoefficients = Metrics.clusteringCoefficients(PPINetworkGraph);
			clusteringCoefficients = SpinnerModular.sortByComparatorCC(clusteringCoefficients);
			ccRank = 1;
			Map<MyNode, Integer> ccRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : clusteringCoefficients.entrySet())
			{
				ccRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}
			
			//Jung Page Rank Score
			/*Transformer<MyLink, Double> wtTransformer = new Transformer<MyLink, Double>() {
				
				public Double transform(MyLink input) {
					return input.getWeight();
				}
			};
			PageRank<MyNode, MyLink> pageRank = new PageRank<MyNode, MyLink>(PPINetworkGraph, wtTransformer, 0.85d);
			pageRank.initialize();
			pageRank.setMaxIterations(ITERATION_NUMBER);
			pageRank.evaluate();
			Map<MyNode, Double> PageRankMap = new HashMap<MyNode, Double>();
			for(int i=0;i<N;i++)
			{
				PageRankMap.put(new MyNode(i, indexProteinMap.get(i)), pageRank.getVertexScore(new MyNode(i, indexProteinMap.get(i))));
			}
			PageRankMap = SpinnerModular.sortByComparatorCC(PageRankMap);
			ccRank = 1;
			Map<MyNode, Integer> PageRankRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : PageRankMap.entrySet())
			{
				PageRankRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}*/
			
			//PubMed Count
			String pubMed[] = diseaseName.split(" ");
			String pubMedString = "";
			for(int j=0;j<pubMed.length;j++)
			{
				pubMedString = pubMedString+pubMed[j]+"+";
			}
			//int diseasePubMedCount = Integer.parseInt(PubMedAPITest.run(pubMedString));
			//Printing the result into result.txt file
			Map<Integer, String> intitalScore = new HashMap<Integer, String>();
			Map<Integer, String> iterativeScore = new HashMap<Integer, String>();
			Map<Integer, String> degree = new HashMap<Integer, String>();
			Map<Integer, String> betweeness = new HashMap<Integer, String>();
			Map<Integer, String> CC = new HashMap<Integer, String>();
			//Map<Integer, String> PubMedCitation = new HashMap<Integer, String>();
			for(int i=0;i<N;i++)
			{
				intitalScore.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(r[i][0]));
				iterativeScore.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(r[i][ITERATION_NUMBER-1]));
				degree.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(outDegree[i]));
				betweeness.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(betweenessRankMap.get(new MyNode(i, indexProteinMap.get(i)))));
				CC.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(ccRankMap.get(new MyNode(i, indexProteinMap.get(i)))));
				//PubMedCitation.put(inverseGeneMap.get(indexProteinMap.get(i)),String.valueOf(r[i][ITERATION_NUMBER-1]));
			}
			spinnerResultMap.put(INITIAL_SCORE, intitalScore);
			spinnerResultMap.put(ITERATIVE_SCORE, iterativeScore);
			spinnerResultMap.put(DEGREE, degree);
			spinnerResultMap.put(BETWEENESS, betweeness);
			spinnerResultMap.put(CLUSTERINGCOEFFICIENT, CC);
		} 
		catch (FileNotFoundException e) {
			System.out.println("The input file location is not correct or Access is restricted");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("There is some problem in reading/writing input/output file");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("Data is not in correct format in input file");
			e.printStackTrace();
		}
		
		return spinnerResultMap;
	}
}
