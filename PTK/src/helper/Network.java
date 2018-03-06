package helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {
	public static String doGet(String getURL) {
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
			System.out.println("Cannot get log.json  - Aborting");
			e.printStackTrace();
			System.exit(-1);
		}
		return result.toString();
	}
}
