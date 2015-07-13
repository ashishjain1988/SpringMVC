package com.spring.common.spinner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
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
public class SpinnerModular {

	public final static double SIGMA = 0.85;
	public final static int ALPHA = 2;
	//private final static String INPUT_GLOBAL_PPI_FILE = "STRING_Proteins_RankingScore.txt";
	InputStream fileURL = this.getClass().getResourceAsStream("/com/spring/common/spinner/STRING_Proteins_RankingScore.txt");  
	
	public static void main(String[] args) {
		BufferedReader br;
		List<String> genesList = new ArrayList<String>();
		String FILE_PATH = "";
		String INPUT_FILE_PATH = "D:\\Lab_Projects\\SPINNER_Project\\MCBIOS_Paper\\breastCancerGenelistNew.txt" ;
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
		try {
			br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			//Skipping the first header line
			String line = br.readLine();
			String lineData[] = line.split(",");
			for(int j=0;j<lineData.length;j++)
			{
				genesList.add(lineData[j].trim());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();;
			System.out.println("Input Protein List File path is not correct "+INPUT_FILE_PATH );
			System.exit(0);
		}
		SpinnerModular spinnerModular = new SpinnerModular();
		/*for(int i=0;i<args.length;i++)
		{
			if(args[i].startsWith("-F"))
			{
				isFileFound = true;
				try {
					INPUT_FILE_PATH = args[i].split("=")[1];
					br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
					//Skipping the first header line
					String line = br.readLine();
					String lineData[] = line.split(",");
					for(int j=0;j<lineData.length;j++)
					{
						genesList.add(lineData[j].trim());
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();;
					System.out.println("Input Protein List File path is not correct "+INPUT_FILE_PATH );
					System.exit(0);
				}
				break;
			}
		}
		if(!isFileFound)
		{
			System.out.println("Protein List path is not given in arguments");
			System.exit(0);
		}
		for(int i=0;i<args.length;i++)
		{
			if(args[i].startsWith("-D"))
			{
				diseaseName = args[i].split("=")[1];
				System.out.println(diseaseName);
			}
			if(args[i].startsWith("-I"))
			{
				try {
					compressionFactor = Double.parseDouble(args[i].split("=")[1]);
					if(!(compressionFactor <= 1 && compressionFactor > 0))
					{
						System.out.println("Compression factor should be greater than 0 or equal to 1");
						System.exit(0);
					}
				} catch (NumberFormatException e) {
					System.out.println("Compression factor is not in correct format");
					System.exit(0);
				}catch (Exception e) {
					System.out.println("Compression factor is not in correct range");
					System.exit(0);
				}				
			}
			
			if(args[i].startsWith("-N"))
			{
				try {
					sFactorBase = Integer.parseInt(args[i].split("=")[1]);
					if(sFactorBase > 0)
					{
						globalAdjustment = true;
					}else if(sFactorBase < 0)
					{
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					System.out.println("S-factor base is not in correct format");
					System.exit(0);
				}				
			}
			
			if(args[i].startsWith("-R"))
			{
				try {
					ITERATION_NUMBER = Integer.parseInt(args[i].split("=")[1]);
					if(ITERATION_NUMBER < 2 && ITERATION_NUMBER > 200)
					{
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					System.out.println("Iteration Number is not in correct format");
					System.exit(0);
				}				
			}
			
			if(args[i].startsWith("-P"))
			{
				try {
					proteinSeed = Integer.parseInt(args[i].split("=")[1]);
					if(proteinSeed > 1 || proteinSeed < 0)
					{
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					System.out.println("Protein Seed option is not correct");
					System.exit(0);
				}				
			}
			if(args[i].startsWith("-S"))
			{
				try {
					proteinSource = args[i].split("=")[1];
					if(!(proteinSource.equals("STRING") || proteinSource.equals("0")))
					{
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					System.out.println("PPI Source option is not correct");
					System.exit(0);
				}				
			}
			
		}*/
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
			//Arguments Validations
			if(proteinSource.equals("STRING"))
			{
				FILE_PATH = NetworkGenerationMain.getPPIDataFromSTRINGDB(genesList,proteinSeed);
				GetUniqueNodesFromPPIData.getUniqueNodesFromPPIData(FILE_PATH, proteinIndexMap, indexProteinMap, fileData, networkSize, networkDivisionMap,PPINetworkGraph);
			}else if(proteinSource.equals("BIOGRID"))
			{
				FILE_PATH = NetworkGenerationBIOGRID.getPPIDataFromBIOGRID(genesList, proteinSeed);
				//System.out.println("After Getting PPI");
				GetUniqueNodesFromPPIData.getUniqueNodesFromPPIData(FILE_PATH, proteinIndexMap, indexProteinMap, fileData, networkSize, networkDivisionMap,PPINetworkGraph);
			}else
			{
				if(proteinSeed != 0)
				{
					System.out.println("PPI Source option is not correct for your input Protein Expansion option");
					System.exit(0);
				}
				//Generate Random Network
				//System.out.println("Going in Random "+genesList.size());
				PPIWeights = CreateRandomNetwork.createRandomNetworkWithBias(genesList,compressionFactor);
				GetUniqueNodesFromPPIData.getUniqueNodesFromRandomData(genesList, PPIWeights,proteinIndexMap, indexProteinMap, fileData, networkSize, networkDivisionMap,PPINetworkGraph);
			}
			
			//System.out.println("Nodes in Network "+proteinIndexMap.size());
			/*if(compressionFactor < Math.sqrt((double)(1/(double)proteinIndexMap.size())))
			{
				System.out.println("Compression factor should be greater than "+Math.sqrt((double)(1/(double)proteinIndexMap.size())));
				System.exit(0);	
			}*/
			if(globalAdjustment)
			{
				//Global String Database File Upload
				br = new BufferedReader(new InputStreamReader(spinnerModular.fileURL));
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
			NetworkCreation.networkCreation(fileData, networkData, proteinIndexMap, a,compressionFactor,PPINetworkGraph);
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
			initialRankingMap = sortByComparator(initialRankingMap);
			int intialRanking[] = createRankingArray(initialRankingMap, N);
			int iterativeRank [];
			iterativeRankingMap = sortByComparator(iterativeRankingMap);
			iterativeRank = createRankingArray(iterativeRankingMap, N);
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
			outDegreeMap = sortByComparator(outDegreeMap);
			int outDegreeRanking[] = createRankingArray(outDegreeMap, N);
			
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
			AdjustedInitialRankingMap = sortByComparator(AdjustedInitialRankingMap);
			int AdjustedintialRanking[] = createRankingArray(AdjustedInitialRankingMap, N);
			int AdjustedIterativeRank [];
			AdjustedIterativeRankingMap = sortByComparator(AdjustedIterativeRankingMap);
			AdjustedIterativeRank = createRankingArray(AdjustedIterativeRankingMap, N);
			System.out.println("Before Priting file Result");
			//Betweeness Centrality
			BetweennessCentrality<MyNode, MyLink> PPINodesBetweenness = new BetweennessCentrality<MyNode, MyLink>(PPINetworkGraph);
			Map<MyNode, Double> betweenessMap = new HashMap<MyNode, Double>();
			for(int i=0;i<N;i++)
			{
				betweenessMap.put(new MyNode(i, indexProteinMap.get(i)), PPINodesBetweenness.getVertexScore(new MyNode(i, indexProteinMap.get(i))));
			}
			betweenessMap = sortByComparatorCC(betweenessMap);
			int ccRank = 1;
			Map<MyNode, Integer> betweenessRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : betweenessMap.entrySet())
			{
				betweenessRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}
			//Clustering Coefficient
			Map<MyNode, Double> clusteringCoefficients = Metrics.clusteringCoefficients(PPINetworkGraph);
			clusteringCoefficients = sortByComparatorCC(clusteringCoefficients);
			ccRank = 1;
			Map<MyNode, Integer> ccRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : clusteringCoefficients.entrySet())
			{
				ccRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}
			//Jung Page Rank Score
			Transformer<MyLink, Double> wtTransformer = new Transformer<MyLink, Double>() {
				
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
			PageRankMap = sortByComparatorCC(PageRankMap);
			ccRank = 1;
			Map<MyNode, Integer> PageRankRankMap = new HashMap<MyNode, Integer>();
			for(Entry<MyNode, Double> entry : PageRankMap.entrySet())
			{
				PageRankRankMap.put(entry.getKey(), ccRank);
				ccRank++;
			}
			
			//PubMed Count
			Map<Integer, Integer> networkSeedMap = new HashMap<Integer, Integer>();
			int totalSeed = 0;
			String pubMed[] = diseaseName.split(" ");
			String pubMedString = "";
			for(int j=0;j<pubMed.length;j++)
			{
				pubMedString = pubMedString+pubMed[j]+"+";
			}
			//int diseasePubMedCount = Integer.parseInt(PubMedAPITest.run(pubMedString));
			//Printing the result into result.txt file
			System.out.println("Priting file Result");
			PrintWriter writer = new PrintWriter(diseaseName+"STRING-0.7_40_iterations.spr", "UTF-8");
			writer.print("GENE NAME\tLOCAL DEGREE\tDEGREE RANK\tINTITAL SCORE\tINITIAL RANK\tINITIAL RANK IN SUB-NETWORK\tITERATIVE SCORE["+ITERATION_NUMBER+"]\tITERATIVE RANK\tITERATIVE RANK["+ITERATION_NUMBER+"] IN SUB-NETWORK\tSEED FLAG\tSUB-NETWORK ID\tDelta Rank((R["+ITERATION_NUMBER+"]-R0))\tDelta((R["+ITERATION_NUMBER+"]-R0)/R0)");
			if(globalAdjustment)
			{
				writer.print("\tGLOBAL DEGREE\tGINITIAL RANK\tGIINITIAL RANK DELTA\tGINITIAL S-FACTOR\tADJUSTED Rp INITIAL RANK\tADJUSTED Rp INITIAL SCORE\tGITERATIVE RANK\tGITERATIVE RANK DELTA\tGITERATIVE S-FACTOR\tADJUSTED Rp ITERATIVE RANK\tADJUSTED Rp ITERATIVE SCORE");
			}
			writer.print("\tPubMed CITATION COUNT\tBETWEENESS RANK\tBETWEENESS\tCC RANK\tCLUSTERING COEFFICIENT\tPAGERANK RANK\tPAGERANK SCORE");
			writer.println();
			for(int i=0;i<N;i++)
			{
				writer.print(indexProteinMap.get(i));
				writer.print("\t");
				writer.printf("%.2f",outDegree[i]);
				writer.print("\t");
				writer.print(outDegreeRanking[i]);
				writer.print("\t");
				writer.printf("%.2f",r[i][0]);
				writer.print("\t");
				writer.print(intialRanking[i]);
				writer.print("\t");
				writer.print(networkBasedIntitalRanks.get(networkDivisionFinal.get(i)).get(i));
				writer.print("\t");
				writer.printf("%.5f",r[i][ITERATION_NUMBER - 1]);
				writer.print("\t");
				writer.print(iterativeRank[i]);
				writer.print("\t");
				writer.print(networkBasedIterativeRanks.get(networkDivisionFinal.get(i)).get(i));
				writer.print("\t");
				writer.print(genesList.contains(indexProteinMap.get(i))?"Y":"N");
				if(genesList.contains(indexProteinMap.get(i)))
				{
					if(proteinSeed == 1)
					{
						if(networkSeedMap.get(networkDivisionFinal.get(i)) != null)
						{
							int count = networkSeedMap.get(networkDivisionFinal.get(i));
							networkSeedMap.put(networkDivisionFinal.get(i),count+1);
						}else
						{
							networkSeedMap.put(networkDivisionFinal.get(i),1);
						}
					}else if(proteinSeed == 0)
					{
						if(networkSeedMap.get(networkDivisionFinal.get(i)) != null)
						{
							int count = networkSeedMap.get(networkDivisionFinal.get(i));
							networkSeedMap.put(networkDivisionFinal.get(i),count+(int)outDegree[i]);
						}else
						{
							networkSeedMap.put(networkDivisionFinal.get(i),(int)outDegree[i]);
						}
					} 
					totalSeed++;
				}
				writer.print("\t");
				writer.print(networkDivisionFinal.get(i));
				writer.print("\t");
				writer.print(deltaRankMap.get(i));
				writer.print("\t");
				writer.print(deltaValueMap.get(i));
				writer.print("\t");
				if(globalAdjustment)
				{
					if(GlobalProteinDegree.get(i)== null)
					{
						System.out.println(indexProteinMap.get(i));
					}
					writer.print(GlobalProteinDegree.get(i));
					writer.print("\t");
					writer.print(GlobalProteinInitialRankSorted.get(i)== null?intialRanking[i]:GlobalProteinInitialRankSorted.get(i));
					writer.print("\t");
					writer.print(GInitialDeltaRank[i]);
					writer.print("\t");
					writer.print(SFactorInitial[i]);
					writer.print("\t");
					writer.print(AdjustedintialRanking[i]);
					writer.print("\t");
					writer.print(AdjustedRpScoreInitial[i]);
					writer.print("\t");
					writer.print(GlobalProteinIterativeRankSorted.get(i)== null?iterativeRank[i]:GlobalProteinIterativeRankSorted.get(i));
					writer.print("\t");
					writer.print(GIterativeDeltaRank[i]);
					writer.print("\t");
					writer.print(SFactorIterative[i]);
					writer.print("\t");
					writer.print(AdjustedIterativeRank[i]);
					writer.print("\t");
					writer.print(AdjustedRpScoreIterative[i]);
					writer.print("\t");
				}
				/*String pubMedCount = PubMedAPITest.run(pubMedString+"[Title/Abstract]+AND+"+indexProteinMap.get(i)+"[Title/Abstract]");
				System.out.println(pubMedCount);
				if(pubMedCount.equals("NA"))
				{
					writer.print("NA");
				}else
				{
					//long PubMedCount = Long.parseLong(pubMedCount);
					//writer.print(PubMedCount == diseasePubMedCount?0:PubMedCount);
					writer.print(pubMedCount);
				}*/
				writer.print("\t");
				writer.print(betweenessRankMap.get(new MyNode(i, indexProteinMap.get(i))));
				writer.print("\t");
				writer.print(PPINodesBetweenness.getVertexScore(new MyNode(i, indexProteinMap.get(i))));
				writer.print("\t");
				writer.print(ccRankMap.get(new MyNode(i, indexProteinMap.get(i))));
				writer.print("\t");
				writer.print(clusteringCoefficients.get(new MyNode(i, indexProteinMap.get(i))));
				writer.print("\t");
				writer.print(PageRankRankMap.get(new MyNode(i, indexProteinMap.get(i))));
				writer.print("\t");
				writer.print(PageRankMap.get(new MyNode(i, indexProteinMap.get(i))));
				writer.println();
			}
			writer.close();
			//System.out.println("Priting Network Analysis Result");
			/*PrintWriter writer = new PrintWriter(diseaseName+"-Iterative-Scores.spr", "UTF-8");
			writer.print("GENE\t");
			for(int i=0;i<ITERATION_NUMBER;i++)
			{
				writer.print("RpScore["+i+"]\t");
			}
			writer.println();
			for(int i=0;i<N;i++)
			{
				writer.print(indexProteinMap.get(i));
				writer.print("\t");
				for(int j=0;j<ITERATION_NUMBER;j++)
				{
					writer.print(r[i][j]);
					writer.print("\t");
				}
				writer.println();
			}
			writer.close();*/
			
			writer = new PrintWriter(diseaseName+"-Sub-Network-Analysis.spr", "UTF-8");
			writer.println("SUB-NETWORK ID\tSIZE\tINDEX OF AGGREGATION\tP-VALUE");
			for(Entry<Integer, Integer> entry: networkCount.entrySet())
			{
				double indexOfAggregation = (entry.getValue()*100d)/N;
				List<Object> list = new ArrayList<Object>();
				if(proteinSource.equals("STRING"))
				{
					list = HyperGeometricTest.hyperGeometricTest(N,totalSeed, entry.getValue(),networkSeedMap.get(entry.getKey()));
				}/*else
				{
					list = HyperGeometricTest.hyperGeometricTest((N*(N-1))/2,totalSeedSum of Degree, (entry.getValue()*(entry.getValue()-1))/2,networkSeedMap.get(entry.getKey())Sum of degree in network);
				}*/
				
				writer.println(entry.getKey()+"\t"+entry.getValue()+"\t"+indexOfAggregation+"\t"+list.get(0));
			}
			writer.close();
			System.out.println("You can see your result in the "+diseaseName+".slr file in your current folder.");
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
		
	}
	public static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Integer, Double>> list = 
			new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
                                           Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static Map<Integer, Integer> sortByComparatorIntegerAsc(Map<Integer, Integer> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Integer, Integer>> list = 
			new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1,
                                           Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Iterator<Map.Entry<Integer, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	public static Map<MyNode, Double> sortByComparatorCC(Map<MyNode, Double> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<MyNode, Double>> list = 
			new LinkedList<Map.Entry<MyNode, Double>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<MyNode, Double>>() {
			public int compare(Map.Entry<MyNode, Double> o1,
                                           Map.Entry<MyNode, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<MyNode, Double> sortedMap = new LinkedHashMap<MyNode, Double>();
		for (Iterator<Map.Entry<MyNode, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<MyNode, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static List<Map.Entry<Integer, Integer>> ListIntegerAsc(Map<Integer, Integer> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Integer, Integer>> list = 
			new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1,
                                           Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		return list;
		// Convert sorted map back to a Map
	}
	public static int[] createRankingArray(Map<Integer, Double> sortMap, int size)
	{
		int[] a = new int[size];
		int count = 1;
		double previous=0;
		for (Map.Entry<Integer, Double> entry : sortMap.entrySet()) {
			a[entry.getKey()] = count;
			if(entry.getValue() != previous)
			{
				count++;
			}
		}
		return a;
	}
}
