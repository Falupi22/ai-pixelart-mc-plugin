package org.frum.plugins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.frum.files.FileHelper;
import org.frum.network.AppLayer;

public class Main {

	public static void main(String[] args) {
		FileInputStream in = null;
		try {
			String currentDir = System.getProperty("user.dir");
	        System.out.println("Current Directory: " + currentDir);
		    // Initially empty.
		    Properties properties = new Properties();

		    // You can read files using FileInputStream or FileReader.
		    in = new FileInputStream("ai-pixelart/config.yml");

		    // This line reads the properties file.
		    properties.load(in);
		    System.out.println(properties.toString());
		    for (Entry<Object, Object> prop: properties.entrySet())
		    {
		    	System.out.println(prop.getValue());
		    }
		    
		    // Your code goes here.
		} catch (FileNotFoundException ex) {
		    // Handle missing properties file errors.
			System.err.println(ex);
		} catch (IOException ex) {
		    // Handle IO errors.
			System.out.println(ex);
		} finally {
		    // Need to do some work to close the stream.
		    try {
		        if (in != null) in.close();
		    } catch (IOException ex) {
		        // Handle IO errors or log a warning.
		    }
		}
		// TODO Auto-generated method stub
		AppLayer layer = new AppLayer();
		
		 // Create a JSON request body
        String requestBody = String.format("{\r\n"
        		+ "    \"prompt\": \"%s\",\r\n"
        		+ "    \"n\": 1,\r\n"
        		+ "    \"size\": \"1024x1024\",\r\n"
        		+ "    \"response_format\": \"b64_json\"\r\n"
        		+ "  }", "ap ap");
		
        System.out.println(requestBody);
        
        String[] argss = new String[6];
        argss[0] = "aipa";
        argss[1] = "\"" + "hello";
        argss[2] = "my";
        argss[3] = "world" + "\"";
        argss[4] = "e";
        argss[5] = "100";
        
        String[] argsss = parse(argss);
        for (String ar : argsss) {
        	System.out.print(ar + " ");
        }
	}
	
	public static String[] parse(String[] args) {
		List<String> allParameters = new ArrayList<String>();
		String temp = "";
		
		boolean isInside = false;
		for (String arg : args) {
		  if (arg.startsWith("\"")) { 
		     isInside = true;
		     temp += arg.substring(1, arg.length()) + " "; //we don't want the quotation mark
		  }
		  else if (arg.endsWith("\"")) {
		     isInside = false;
		     temp += arg.substring(0, arg.length()-1) + " ";
			 allParameters.add(temp);
			 temp = ""; 
		  }
		  else if (isInside) {
			    temp += arg + " ";
		  }
		  else { 
			 allParameters.add(arg);
		  }
		}

		 return allParameters.toArray(new String[allParameters.size()]);
	}

}
