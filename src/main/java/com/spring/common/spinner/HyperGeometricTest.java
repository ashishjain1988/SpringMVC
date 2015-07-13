package com.spring.common.spinner;

import java.util.ArrayList;
import java.util.List;

public class HyperGeometricTest {


	//private static String sign = "";
	public static void main(String[] args) {
		int N, K, n, k;
		N = 1000;//Total Protein In Network
		K = 100;// Seeds in all the Network
		n = 95;//Total Protein in ALS Sub Network
		k = 51;//Seed in Subnetwork
		HyperGeometricTest obj = new HyperGeometricTest();
		List<Object> obj1 = obj.hyperGeometricTest(N, K, n, k);
		System.out.println("P(N="+N+",K="+K+",n="+n+",k="+k+"): "+obj1.get(0)+" "+obj1.get(1));
	}

	/*private float chooseComb(double n,double k)
	{
		float s = 1;
		if (k > n/2)
		{
			k = n-k;
		}
		for (int i=0;i<k;i++)i in range(0, k){
			s = s * (n-i) / (k-i);
		}
		return s;

	}
*/
	public static double LogChoosecomb(double n,double k)
	{
		double s = 1;
		if (k > n/2)
		{
			k = n-k;
		}
		for (int i=0;i<k;i++)/*i in range(0, k)*/{
			s = s + Math.log10(n-i) - Math.log10(k-i);
		}
		return s;

	}

	public static List<Object> hyperGeometricTest(int N,int K,int n,int k)
	{
		List<Object> obj = new ArrayList<Object>();
		double p = LogChoosecomb (K,k) + LogChoosecomb (N-K, n-k) - LogChoosecomb (N, n);
		String sign="";
		//Float used for calculating  the over/under representation
		float fN,fK,fn,fk;
		fN = N;
		fk = k;
		fK = K;
		fn = n;
		if(fK/fN < fk/fn)
			sign = "Over-representation";
		else
			sign = "Under-representation";

		try {
			p = Math.pow(10, p-1);
		} catch (Exception e) {
			System.out.println("Error: Arithmetic Error!\n");
			System.exit(0);
		}
		obj.add(p);
		obj.add(sign);
		return obj;/*, sign*/

	}

}
