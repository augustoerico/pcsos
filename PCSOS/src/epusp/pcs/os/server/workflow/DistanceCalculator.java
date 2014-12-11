package epusp.pcs.os.server.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epusp.pcs.os.shared.model.oncall.Position;

public class DistanceCalculator {

	private static final String key = "AIzaSyDNVvFn0tU2jIjWsOj4oiGaWACX6BgF7Eo";

	public DistanceCalculator(){
	}

	public int minTimeDistance(Position destination, List<Position> origins){
		Integer min = -1;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

		if(!origins.isEmpty() && (destination != null && !destination.isEmpty())){

			if(origins.size() == 1){
				return 0;
			}
			
			if(origins.size() > 5){
				origins = filter(map, origins);
			}

			LinkedList<Integer> totalTravelTime = new LinkedList<Integer>();

			try {
				String sUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";

				String sOrigins = "";
				Iterator<Position> positionIterator = origins.iterator();
				if(positionIterator.hasNext()){
					Position origin = positionIterator.next();
					String latitude = URLEncoder.encode(String.valueOf(origin.getLatitude()), "UTF-8");
					String longitude = URLEncoder.encode(String.valueOf(origin.getLongitude()), "UTF-8");
					sOrigins = sOrigins.concat(latitude).concat(",").concat(longitude);
					while(positionIterator.hasNext()){
						origin = positionIterator.next();
						latitude = URLEncoder.encode(String.valueOf(origin.getLatitude()), "UTF-8");
						longitude = URLEncoder.encode(String.valueOf(origin.getLongitude()), "UTF-8");
						sOrigins = sOrigins.concat(URLEncoder.encode("|", "UTF-8")).concat(latitude).concat(",").concat(longitude);
					}
				}

				System.out.println(sOrigins);

				String sdestinations = "";
				String latitude = URLEncoder.encode(String.valueOf(destination.getLatitude()), "UTF-8");
				String longitude = URLEncoder.encode(String.valueOf(destination.getLongitude()), "UTF-8");
				sdestinations = sdestinations.concat(latitude).concat(",").concat(longitude);

				System.out.println(sdestinations);

				URL url = new URL(sUrl.concat("origins=").concat(sOrigins).concat("&destinations=")
						.concat(sdestinations).concat("&key=").concat(key));

				System.out.println(sUrl.concat("origins=").concat(sOrigins).concat("&destinations=")
						.concat(sdestinations).concat("&key=").concat(key));

				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

				String line, answer = "";
				while ((line = reader.readLine()) != null) {
					answer = answer.concat(line);
					System.out.println(line);
				}

				JSONObject jAnswer = null;
				try {
					jAnswer = new JSONObject(answer);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				JSONArray rows = jAnswer.getJSONArray("rows");
				for(int i = 0; i < rows.length(); i++){
					JSONArray element = rows.getJSONObject(i).getJSONArray("elements");
					for(int j=0; j < element.length(); j++){
						JSONObject object = element.getJSONObject(j);
						JSONObject duration = object.getJSONObject("duration");
						totalTravelTime.addFirst(duration.getInt("value"));
					}
				}

				System.out.println(totalTravelTime.toString());

				min = totalTravelTime.size() - 1 - totalTravelTime.indexOf(Collections.min(totalTravelTime));

				reader.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(min > 0 && origins.size() > 5){
				min = map.get(min);
			}
		}
		
		System.out.println("Mininum time distance index: " + min);
		
		return min;
	}
	
	private List<Position> filter(HashMap<Integer, Integer> map, List<Position> positions){
		List<IndexPositionMapper> positionMap = new ArrayList<IndexPositionMapper>();
		List<Position> filteredPositions = new ArrayList<Position>();
		
		for(int i = 0; i < positions.size(); i++){
			positionMap.add(new IndexPositionMapper(positions.get(i), i));
		}
		
		Collections.sort(positionMap);
		
		for(int i = 0; i < 5; i++){
			filteredPositions.add(positionMap.get(i).getPosition());
			map.put(i, positionMap.get(i).getIndex());
		}
		
		return filteredPositions;
	}
}
