package com.spring.common.spinner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


public class GetClinVarMutationCount {/*

	  public String run(String geneName)
	  {
		  try {
			  String PUBMED_SERVER = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=clinvar&term="+geneName+"[gene]+AND+single_gene[prop]&retmax=10000";
			  URL url = new URL(PUBMED_SERVER);
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			  HttpURLConnection.setFollowRedirects(true);
			  conn.setDoInput(true);
			  conn.connect();

			  int status = conn.getResponseCode();
			  while (true)
			  {
				  int wait = 0;
				  String header = conn.getHeaderField("Retry-After");
				  if (header != null)
					  wait = Integer.valueOf(header);
				  if (wait == 0)
					  break;
				  conn.disconnect();
				  Thread.sleep(wait * 1000);
				  conn = (HttpURLConnection) new URL(PUBMED_SERVER).openConnection();
				  conn.setDoInput(true);
				  conn.connect();
				  status = conn.getResponseCode();
			  }
			  StringBuilder builder = new StringBuilder();
			  if (status == HttpURLConnection.HTTP_OK)
			  {
				  //LOG.info("Got a OK reply");
				  InputStream reader = conn.getInputStream();
				  URLConnection.guessContentTypeFromStream(reader);
				  int a = 0;
				  while ((a = reader.read()) != -1)
				  {
					  builder.append((char) a);
				  }
			  }
			  InputStream stream = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
			  JAXBContext jaxbContext = JAXBContext.newInstance(ClinVarSearchResult.class);
			  Unmarshaller jaxbUnmarsheller = jaxbContext.createUnmarshaller();
			  ClinVarSearchResult result = (ClinVarSearchResult) jaxbUnmarsheller.unmarshal(stream);
			  System.out.println(result.getCount());
			  //System.out.println(result.getIdList().size());
			  System.out.println("After");
			  if(builder.toString().contains("<ErrorList>"))
			  {
				  return "0";
			  }
			  conn.disconnect();
			  return "";
		  } catch (Exception e) {
			  e.printStackTrace();
			  return "NA";
		  }

	  }
	  public static void main(String[] args) {
		 GetClinVarMutationCount obj = new GetClinVarMutationCount();
		 obj.run("BRCA1");
	}

*/}
