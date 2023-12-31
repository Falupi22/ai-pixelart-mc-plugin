package org.frum.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    public static void saveAsPng(byte[] imageData, String outputPath) {
        try {
        	// Get the current working directory
            String currentDir = System.getProperty("user.dir");

            // Create an absolute path by combining the current directory and the relative path
            String absolutePath = Paths.get(currentDir, outputPath).toString();
            
        	Files.write(Paths.get(absolutePath), imageData);
        } catch (IOException e) {
        	System.err.println("Error: " + e.getStackTrace());
        	e.printStackTrace();
        }
    }
    
    public static void createFolder() {
    	String folderName = "frum"; // Replace with your desired folder name

        Path folder = Paths.get(folderName);

        if (!Files.exists(folder)) {
            try {
                Files.createDirectory(folder);
                System.out.println("Folder created successfully.");
            } catch (IOException e) {
                System.err.println("Failed to create folder: " + e.getMessage());
            }
        } else {
            System.out.println("Folder already exists.");
        }
    }
}
