package kr.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class LocationUtil {

	private static final String KAKAO_API_KEY = "afb5bbd7fe1d0af65406d14c27af30ac";

	public static String getRegionName(double x, double y) {
		if (x == 0.0 && y == 0.0) return "좌표 미설정";

		String apiUrl = String.format("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%f&y=%f", y, x);

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_API_KEY);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			in.close();

			// JSON 파싱
			JSONObject json = new JSONObject(response.toString());
			if (json.has("documents")) {
				JSONObject region = json.getJSONArray("documents").getJSONObject(0);
				String regionName = region.getString("region_1depth_name") + " " + region.getString("region_2depth_name") + " " + region.getString("region_3depth_name");
//				String regionName = region.getString("region_3depth_name");
				
				return regionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "지역 미설정";
	}
	
}
