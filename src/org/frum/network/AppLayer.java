package org.frum.network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import org.frum.files.FileHelper;
import io.github.cdimascio.dotenv.Dotenv;

public class AppLayer {
    public boolean getImagesByPrompt(String prompt) {
    	System.out.println("API key:" + getApiKey());
        String apiKey = "Bearer " + getApiKey(); // Replace with your API Key

        boolean isSucceeded = false;
        
        try {
            // Create the URL for the Bing Image Search API
            String apiUrl = "https://api.openai.com/v1/images/generations";
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            // Create a JSON request body
            String requestBody = String.format("{\r\n"
            		+ "    \"prompt\": \"%s\",\r\n"
            		+ "    \"n\": 1,\r\n"
            		+ "    \"size\": \"1024x1024\",\r\n"
            		+ "    \"response_format\": \"b64_json\"\r\n"
            		+ "  }", prompt);

            // Get the output stream to write the request body
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(requestBody);
            osw.flush();
            osw.close();
            
            System.out.println(connection.toString());
            
            // Get the response code
            int responseCode = connection.getResponseCode();

            System.out.println("Response code: " + responseCode);
            
            if (responseCode == 200) {
                // Read the response data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("Response content: " + response.toString());
                
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray images = jsonResponse.getJSONArray("data");

                System.out.println("Images array length: " + images.length());
                if (images.length() > 0) {
                	for (int index = 0; index < images.length(); index++) {
                		JSONObject firstImage = images.getJSONObject(index);
                		String imageUrl = firstImage.getString("b64_json");
                		
                		System.out.println("URL string: " + imageUrl);
                		// Decode the Base64 string to obtain the binary image data
                        byte[] imageData = Base64.getDecoder().decode(imageUrl);
                        
                        System.out.println("ImageData decoded length: " + imageData.length);
                        String path = "plugins/PixelPainter/images/response" + index + ".png";
                        
                        FileHelper.saveAsPng(imageData, path);
                        
                        isSucceeded = true;
                	}
                } else {
                    System.out.println("No images found.");
                }
            } else {
                System.out.println("HTTP Request failed with response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return isSucceeded;
    }
    private String getApiKey() { 
    	String result = "";
    	FileInputStream in = null;
		try {
			String currentDir = System.getProperty("user.dir");
	        System.out.println("Current Directory: " + currentDir);
		    // Initially empty.
		    Properties properties = new Properties();

		    // You can read files using FileInputStream or FileReader.
		    in = new FileInputStream("plugins/ai-pixelart/config.yml");

		    // This line reads the properties file.
		    properties.load(in);
		    
		    result = properties.getProperty("api_key");
		    
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
		
		return result;
    }
    
}
