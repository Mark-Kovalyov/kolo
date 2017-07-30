package mayton.kolo.fb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FBGraph {

	private String accessToken;

	static Logger logger = LoggerFactory.getLogger(FBConnection.class);

	public FBGraph(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFBGraph() {
		String graph = null;
		try {
			String g = "https://graph.facebook.com/me?" + accessToken;
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuilder b = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				b.append(inputLine);
				b.append("\n");
			}
			in.close();
			graph = b.toString();
			logger.info(graph);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		return graph;
	}

	public Map<String, String> getGraphData(String fbGraph) {
		Map<String, String> fbProfile = new HashMap<>();
		try {
			JSONObject json = new JSONObject(fbGraph);
			fbProfile.put("id", json.getString("id"));
			fbProfile.put("first_name", json.getString("first_name"));
			if (json.has("email")) {
				fbProfile.put("email", json.getString("email"));
			}
			if (json.has("gender")) {
				fbProfile.put("gender", json.getString("gender"));
			}
		} catch (JSONException e) {
			logger.error(e.toString());
			throw new RuntimeException("ERROR in parsing FB graph data. " + e);
		}
		return fbProfile;
	}
}
