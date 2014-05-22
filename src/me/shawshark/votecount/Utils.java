package me.shawshark.votecount;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Utils {

	@SuppressWarnings("resource")
	public static String getUUID(String player) {
        String uuid = null;
        try {
            // Get the UUID from SwordPVP
            URL url = new URL("http://uuid.craftshark.net/uuid/" + player);
            URLConnection uc = url.openConnection();
            uc.setUseCaches(false);
            uc.setDefaultUseCaches(false);
            uc.addRequestProperty("User-Agent", "Mozilla/5.0");
            uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            uc.addRequestProperty("Pragma", "no-cache");

            // Parse it
            String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            uuid = (String) ((JSONObject) ((JSONArray) ((JSONObject) obj).get("profiles")).get(0)).get("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uuid;
    }
}
