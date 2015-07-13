package com.spring.common.spinner;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;
/**
 * 
 * @author Ashish Jain
 *
 */
public class ConvertUniprotIdToGeneId {

	  private static final String UNIPROT_SERVER = "http://www.uniprot.org/";
	  private static final Logger LOG = Logger.getAnonymousLogger();

	  public static String run(String tool, ParameterNameValue[] params)
	    throws Exception
	  {
	    StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool + "/?");
	    for (int i = 0; i < params.length; i++)
	    {
	      if (i > 0)
	        locationBuilder.append('&');
	      locationBuilder.append(params[i].name).append('=').append(params[i].value);
	    }
	    String location = locationBuilder.toString();
	    URL url = new URL(location);
	    //LOG.info("Submitting...");
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
	      //LOG.info("Waiting (" + wait + ")...");
	      conn.disconnect();
	      Thread.sleep(wait * 1000);
	      conn = (HttpURLConnection) new URL(location).openConnection();
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
	      //System.out.print(builder.toString());
	      //return builder.toString();
	    }
	    else
	      LOG.severe("Failed, got " + conn.getResponseMessage() + " for "
	        + location);
	    conn.disconnect();
	    return builder.toString();
	    
	  }

	  public static void main(String[] args)
	    throws Exception
	  {
	    run("mapping", new ParameterNameValue[] {
	      new ParameterNameValue("from", "ACC+ID"),
	      new ParameterNameValue("to", "GENEWIKI_ID"),
	      new ParameterNameValue("format", "tab"),
	      new ParameterNameValue("query", "Q96PA0_HUMAN BCL2_HUMAN C9JHD5_HUMAN Q53TV2_HUMAN Q53TT1_HUMAN A8K4R4_HUMAN"),
	    });
	  }

	  static class ParameterNameValue
	  {
	    private final String name;
	    private final String value;

	    public ParameterNameValue(String name, String value)
	      throws UnsupportedEncodingException
	    {
	      this.name = URLEncoder.encode(name, "UTF-8");
	      this.value = URLEncoder.encode(value, "UTF-8");
	    }
	  }
}
