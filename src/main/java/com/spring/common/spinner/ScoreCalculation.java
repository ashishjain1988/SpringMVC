package com.spring.common.spinner;

import java.util.Map;
/**
 * 
 * @author Ashish Jain
 *
 */
public class ScoreCalculation {

	public static void calculateInitialRScore(int N, double networkData[][],double a[][],Map<Integer,Double> initialRankingMap,double r[][])
	{
		double intialRSum = 0;
		double intialASum = 0;
		for(int i=0;i<N;i++)
		{
			intialRSum = 0;
			intialASum = 0;
			for(int j=0;j<N;j++)
			{
				intialRSum += networkData[i][j];
				intialASum +=a[i][j];
			}
			intialRSum = Math.pow(intialRSum, SpinnerModular.ALPHA);
			if(intialASum != 0)
			{
				r[i][0] = intialRSum/intialASum;
				initialRankingMap.put(i, r[i][0]);
			}
		}
	}
	
	public static void calculateIterativeRScore(int N,int ITERATION_NUMBER,double outDegree[],double networkData[][],Map<Integer,Double> iterativeRankingMap,double r[][])
	{
		for(int k=1;k<ITERATION_NUMBER;k++)
		{
			for(int i=0;i<N;i++)
			{
				double sumR = 0;
				for(int j=0;j<N;j++)
				{
					if(outDegree[j] != 0)
					{
						sumR += ((networkData[j][i]*r[j][k-1]/outDegree[j]));
					}else
					{
						sumR+=0;
					}
				}
				r[i][k] = (1-SpinnerModular.SIGMA)*r[i][0] + SpinnerModular.SIGMA * sumR;
				if(k == ITERATION_NUMBER - 1)
				{
					iterativeRankingMap.put(i, r[i][k]);
				}
			}
		}
	}
}
