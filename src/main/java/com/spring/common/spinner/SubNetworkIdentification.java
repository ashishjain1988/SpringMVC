package com.spring.common.spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 
 * @author Ashish Jain
 *
 */
public class SubNetworkIdentification {

	
	public static int subNetworkIdentification(int N,double a[][],Map<Integer, Integer> networkDivisionMap,Map<Integer, Integer> networkDivisionFinal,Map<Integer, Integer> networkSize,Map<Integer,String> indexProteinMap)
	{
		int temp;
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(a[i][j] != 0)
				{
					if(networkDivisionMap.get(i) == networkDivisionMap.get(j))
					{
						if(networkSize.get(networkDivisionMap.get(i)) != networkSize.get(networkDivisionMap.get(j)))
						{
							System.err.println("Something Wrong for "+indexProteinMap.get(i)+" "+indexProteinMap.get(j));
						}
					}
					if(i!=j && Integer.compare(networkDivisionMap.get(i), networkDivisionMap.get(j)) != 0)
					{
						int largerNetworkNode = 0;
						int smallerNetworkNode = 0;
						if(Integer.compare(networkSize.get(networkDivisionMap.get(i)),networkSize.get(networkDivisionMap.get(j))) < 0)
						{
							largerNetworkNode = j;
							smallerNetworkNode = i;
						}else if(Integer.compare(networkSize.get(networkDivisionMap.get(i)),networkSize.get(networkDivisionMap.get(j))) > 0)
						{
							largerNetworkNode = i;
							smallerNetworkNode = j;
						}else
						{
							largerNetworkNode = i;
							smallerNetworkNode = j;
						}
						
						temp = networkSize.get(networkDivisionMap.get(largerNetworkNode)) + networkSize.get(networkDivisionMap.get(smallerNetworkNode));
						networkSize.put(networkDivisionMap.get(largerNetworkNode), (temp));
						networkSize.put(networkDivisionMap.get(smallerNetworkNode), 0);
						for(int k=0;k<N;k++)
						{
							if(Integer.compare(networkDivisionMap.get(k),networkDivisionMap.get(smallerNetworkNode)) == 0)
							{
								networkDivisionMap.put(k, networkDivisionMap.get(largerNetworkNode));
							}
						}
					}
				}
			}
		}
		temp = 0;
		for(Entry<Integer, Integer> entry : networkSize.entrySet())
		{
			if(entry.getValue() > 0)
			{
				temp = temp + entry.getValue();
			}
		}
		Map<Integer, Integer> networkSizeMap = new HashMap<Integer, Integer>();
		//Networks Nodes Counting
		for(int i=0;i<N;i++)
		{
			if(networkSizeMap.containsKey(networkDivisionMap.get(i)))
			{
				int count1 = networkSizeMap.get(networkDivisionMap.get(i)) + 1;
				networkSizeMap.put(networkDivisionMap.get(i),(count1));
			}else
			{
				networkSizeMap.put(networkDivisionMap.get(i),1);
			}
		}
		

		//System.out.println("Size of the network data is : "+networkSizeMap.size());
		
		//Network Division Sorting
		List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(networkSizeMap.entrySet());
		Collections.sort(list, new ValueThanKeyComparator<Integer, Integer>());
		   
		   //Network Division Normalization on the basis of size of tree
		   int count1 = 1;
		   for(Map.Entry<Integer, Integer> key : list)
		   {
			  
			   for(int i=0;i<N;i++)
			   {
				   if(Integer.compare(networkDivisionMap.get(i), key.getKey()) == 0)
				   {
					   networkDivisionFinal.put(i, count1);
				   }
			   }
			   count1++;
		   }
		   
		   return networkSizeMap.size();
	}
}
