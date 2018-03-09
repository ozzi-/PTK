package helper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Network {
	public static String doGetAsString(String getURL) {
		StringBuilder result = new StringBuilder();
		URL url;
		try {
			url = new URL(getURL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd;
			String line;
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("Cannot get "+getURL+"  - Aborting");
			e.printStackTrace();
			System.exit(-1);
		}
		return result.toString();
	}
	
	public static boolean download(String url, String fileName) throws Exception {
	    try (InputStream in = URI.create(url).toURL().openStream()) {
	    	Path path = Paths.get(fileName);
	        Files.copy(in,path);
	        return true;
	    }catch (FileNotFoundException e) {
			System.out.println("404 when trying to download file "+url);
		}
	    return false;
	}
	
	public static String downloadAndReturnString(String url, String fileName) throws Exception {
		boolean res = download(url, fileName);
		if(res) {
			  byte[] encoded = Files.readAllBytes(Paths.get(fileName));
			  return new String(encoded);
		}
		return null;
	}


	
	
//	public static String downloadAndReturnString(String url, String fileName) throws Exception {
//	    try (InputStream in = URI.create(url).toURL().openStream()) {
//	    	String res = inputStreamToString(in);
//	    	BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
//			out.write(res);
//			out.close();
//	        return res;
//	    }catch (FileNotFoundException e) {
//			System.out.println("404 when trying to download file "+url);
//		}
//	    return null;
//	}
	
	public static String inputStreamToString(InputStream inputStream) throws IOException {
	    try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inputStream.read(buffer)) != -1) {
	            result.write(buffer, 0, length);
	        }

	        return result.toString();
	    }
	}

}
