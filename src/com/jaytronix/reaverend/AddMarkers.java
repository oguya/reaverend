package com.jaytronix.reaverend;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMarkers {
	
	//gmap object
	GoogleMap gMap;
	
	public AddMarkers(GoogleMap gMap){
		this.gMap = gMap;
	}

	//app tag for log
	private String APP_LOG_TAG = "JSONParser";
	
	//json parser object
	JSONParser jsonParser = new JSONParser();
	
	//creates a marker on the map
	public void createMarker(GoogleMap gMap, double lat, double lng, String name, boolean isSupported){
		
		//create a marker based on issupported bool.
		MarkerOptions options = (isSupported) ? new MarkerOptions().position(new LatLng(lat,lng)).title(name).snippet("extra desc here").icon(BitmapDescriptorFactory.fromResource(R.drawable.nippon_shaft)) :
			new MarkerOptions().position(new LatLng(lat,lng)).title(name).snippet("extra desc here").icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue));
		
		//add marker to map
		Marker clubMarker = gMap.addMarker(options);
		
	}
	
}
