package com.spring.common.spinner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkGenerationBIOGRID {

	private final static String INTERMIDIATE_FILE_PATH = "Node_Interactions.txt";
	private final static String OUTPUT_FILE_PATH = "Final_Node_Interactions.txt";

	public static void main(String[] args) {
		List<String> geneList = new ArrayList<String>();
		geneList.add("TP53");
		getPPIDataFromBIOGRID(geneList, 0);
	}
	public static String getPPIDataFromBIOGRID(List<String> genesList,int proteinSeed)
	{

		BufferedReader br;
		List<String> stringResult;
		try {
			/*br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			//Skipping the first header line
			String line = br.readLine();
			genesList = new ArrayList<String>();
			while (line != null) {
				String lineData[] = line.split("\t");
				genesList.add(lineData[1]);
				line = br.readLine();
			}
			br.close();*/
			//System.out.println(genesList.size());
			//Reading from String Database
			OutputStream out;
			PrintWriter writer = new PrintWriter(OUTPUT_FILE_PATH, "UTF-8");
			for(int i=0;i<genesList.size();i++)
			{
				if(i == 0)
				{
					out = new FileOutputStream(INTERMIDIATE_FILE_PATH);
				}else
				{
					out = new FileOutputStream(INTERMIDIATE_FILE_PATH,true);
				}
				URL url = new URL("http://webservice.thebiogrid.org/interactions?searchNames=true&geneList="+genesList.get(i)+"&includeInteractors=true&includeInteractorInteractions=false&taxId=9606&includeHeader=true&format=tab1&accesskey=bb6b99ed172eb8b61a9c51c0a0cea302&throughputTag=low");
				if((HttpURLConnection) url.openConnection() != null)
				{
					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					//Check for not getting result
					if(httpCon.getErrorStream() == null && httpCon.getResponseCode() != 500)
					{
						//Making an Intermediate file as the data form input data is not correct format.
						java.io.InputStream in = httpCon.getInputStream();
						out = new BufferedOutputStream(out);
						byte[] buf = new byte[8192];
						int len = 0;
						while ((len = in.read(buf)) != -1) {
							out.write(buf, 0, len);
						}
						String s = new String(buf);
						out.close();
					}else
					{
						/*The data for the current gene node is not found in String DB
						java.io.InputStream in = httpCon.getErrorStream();
						out = new BufferedOutputStream(out);
						byte[] buf = new byte[8192];
						String s;
						int len = 0;
						while ((len = in.read(buf)) != -1) {
							out.write(buf, 0, len);
							s = new String(buf);
							System.out.println(s);
						}
						out.close();*/
						System.out.println("Error");
					}
				}
			}
			
			br = new BufferedReader(new FileReader(INTERMIDIATE_FILE_PATH));
			stringResult = new ArrayList<String>();
			String line = br.readLine();
			while (line != null) {
				stringResult.add(line);
				//System.out.println(line);
				line = br.readLine();
			}
			br.close();
			//System.out.println("Node 1"+"\t"+"Node 2"+"\t"+"Combined Score(>0.9)");
			writer.println("Node 1"+"\t"+"Node 2"+"\t"+"Score");
			//Pre processing the data
			for(int j=0;j<stringResult.size();j++) {
				String lineData[] = stringResult.get(j).split("\t");
				String s;
				if(proteinSeed == 0)
				{
					if(genesList.contains(lineData[2].trim()) && genesList.contains(lineData[3].trim()))
					{
						writer.println(lineData[2].trim()+"\t"+lineData[3].trim()+"\t"+1);
					}
				}else if(proteinSeed == 1){
					if(genesList.contains(lineData[2].trim()) || genesList.contains(lineData[3].trim()))
					{
						writer.println(lineData[2].trim()+"\t"+lineData[3].trim()+"\t"+1);
					}
				}else
				{
					//System.out.println(stringResult.get(j));
				}
			}
			writer.close();

		} 
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getStackTrace());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getStackTrace());
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
			System.out.println("Not able to reterive data from STRING Database");
			System.exit(0);
		}
		
		//File file = new File(INTERMIDIATE_FILE_PATH);
		//System.out.println(file.delete());
		return OUTPUT_FILE_PATH;
	}
}
