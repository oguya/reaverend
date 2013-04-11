package com.jaytronix.reaverend;

import android.util.Log;
import android.webkit.WebIconDatabase.IconListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMarkers {

	//app tag for log
	private String APP_LOG_TAG = "JSONParser";
	
	//json parser object
	JSONParser jsonParser = new JSONParser();
	
	public AddMarkers(double lat, double lng){
	}
	
	
	//ret. string contains:=> lat,lng|name|issupported#
	public void processClubList(String JSONString) throws ArrayIndexOutOfBoundsException{
		String clubList = jsonParser.getClubs(JSONString);
		
		//do a null check...quit for empty string[cud lead to errors]
		if(clubList == null){
			Log.i(APP_LOG_TAG,"Fatal Error! empty clubList received from JSONParser object!");
			return;
		}
		
		String[] singleclubCoords = clubList.split("#");
		
		for(String clubCoords : singleclubCoords){
			String[] otherDetails = clubCoords.split("|");
			String coords = otherDetails[0];
			String name = otherDetails[1];
			boolean isSupported = (otherDetails[2].equalsIgnoreCase("1")) ? true : false;
			
			double lat = Double.valueOf(coords.split(",")[0]);
			double lng = Double.valueOf(coords.split(",")[1]);

			//log the details..4 debugging
			Log.i(APP_LOG_TAG,"Name: "+name+" Coords: "+lat+","+lng+" issupported: "+isSupported);
			
			//create a marker
			
		}
		
	}
	
	//creates a marker on the map
	public void createMarker(GoogleMap gMap, double lat, double lng, String name, boolean isSupported){
		
		//create a marker based on issupported bool.
		MarkerOptions options = (isSupported) ? new MarkerOptions().position(new LatLng(lat,lng)).title(name).snippet("extra desc here").icon(BitmapDescriptorFactory.fromResource(R.drawable.nippon_shaft)) :
			new MarkerOptions().position(new LatLng(lat,lng)).title(name).snippet("extra desc here").icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue));
		
		//add marker to map
		Marker clubMarker = gMap.addMarker(options);
		
	}
	
}
