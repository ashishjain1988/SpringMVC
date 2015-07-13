package com.spring.common.spinner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
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

/**
 * 
 * @author Ashish Jain
 * 
 * This class basically Data Mine the String Database and finds the
 * proteins that are interacting with the input genes and give output 
 * the two interacting nodes with their combined score greater than
 * 0.9.
 *
 */
public class NetworkGenerationMain {

	private final static String INTERMIDIATE_FILE_PATH = "Node_Interactions.txt";
	private final static String OUTPUT_FILE_PATH = "Final_Node_Interactions.txt";

	public static String getPPIDataFromSTRINGDB(List<String> genesList,int proteinSeed)
	{

		BufferedReader br;
		//List<String> genesList;
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
				URL url = new URL("http://string-db.org/api/psi-mi-tab/interactionsList?identifiers="+genesList.get(i)+"&limit=1&species=9606&required_score=700");
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
						//String s = new String(buf);
						//System.out.println(s);
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
			writer.println("Node 1"+"\t"+"Node 2"+"\t"+"Combined Score(>0.9)");
			//Pre processing the data
			for(int j=0;j<stringResult.size();j++) {
				String lineData[] = stringResult.get(j).split("\t");
				String s;
				Float f;
				if(proteinSeed == 0)
				{
					if(genesList.contains(lineData[2].trim()) && genesList.contains(lineData[3].trim()))
					{
						s = lineData[14].split(":")[1];
						//Split for | is not working
						try {
							f = Float.valueOf(s.substring(0, 5));
						} catch (NumberFormatException e) {
							try {
								f = Float.valueOf(s.substring(0, 4));
							} catch (NumberFormatException e2) {
								try {
									f = Float.valueOf(s.substring(0, 3));
								} catch (NumberFormatException e3) {
									f=0.0f;
								}
							}
						}
						writer.println(lineData[2].trim()+"\t"+lineData[3].trim()+"\t"+f);
					}
				}else if(proteinSeed == 1){
					if(genesList.contains(lineData[2].trim()) || genesList.contains(lineData[3].trim()))
					{
						s = lineData[14].split(":")[1];
						//Split for | is not working
						try {
							f = Float.valueOf(s.substring(0, 5));
						} catch (NumberFormatException e) {
							try {
								f = Float.valueOf(s.substring(0, 4));
							} catch (NumberFormatException e2) {
								try {
									f = Float.valueOf(s.substring(0, 3));
								} catch (NumberFormatException e3) {
									f=0.0f;
								}
							}
						}
						//System.out.println(lineData[2]+"\t"+lineData[3]+"\t"+f);
						writer.println(lineData[2].trim()+"\t"+lineData[3].trim()+"\t"+f);
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
