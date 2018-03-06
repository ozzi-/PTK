package helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class File {
	public static String readFileAsString(String path) {
		String res="";
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			res = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Could not open file "+path+" - "+e.getMessage()+" - Aborting");
			System.exit(-1);
		}
		return res;
	}

	public static List<String> readFileAsArray(String path) {
		ArrayList<String> toList = new ArrayList<String>();
		BufferedReader in;
		String str;
		try {
			in = new BufferedReader(new FileReader(path));
			while ((str = in.readLine()) != null) {
				toList.add(str);
			}
		} catch (Exception e) {
			System.out.println("Could not open file "+path+" - "+e.getMessage()+" - Aborting");
			System.exit(-1);
		}
		return toList;
	}
	
	public static Properties loadProperties() {
		Properties configProperties = new Properties();
		FileInputStream file;
		String path = "./config.properties";
		try {
			file = new FileInputStream(path);
			configProperties.load(file);
			file.close();
		} catch (Exception e) {
			System.out.println("Could not open properties file - "+e.getMessage()+" - Aborting");
			System.exit(-1);
		}
		return configProperties;
	}
}
