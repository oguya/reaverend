package com.jaytronix.reaverend;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
	
	private final Context _context;
	
	//gps status
	boolean isGPSEnabled = false;
	boolean canGetLocation = false;
	
	//net status
	boolean isNetworkEnabled = false;
	
	Location location = null;
	double lat, lng; //lat lng
	
	//min. distance to query 4 loc. updates [meters]...10meters
	private static final long MIN_DISTANCE_QUERY_UPDATES = 10;
	
	//min time btwn loc. updates....millisec..5mike
	private static final long MIN_TIME_BTWN_UPDATES = 1000 * 60 * 5;
	
	//loc mgr
	protected LocationManager locationManager;
	
	//log tags
	private  String APP_LOG_TAG = "GPSTracker";
	
	//def. const
	public GPSTracker(Context context){
		this._context = context;
		getLocation();
	}
	
	public Location getLocation(){
		try{
			
			locationManager = (LocationManager)_context.getSystemService(LOCATION_SERVICE);
			
			//get gps status & net status[net provider]
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPSEnabled && !isNetworkEnabled){
				//no loc. provider enabled!!
			}else{
				this.canGetLocation = true;
				if(isNetworkEnabled){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BTWN_UPDATES, MIN_DISTANCE_QUERY_UPDATES, this);
					Log.i(APP_LOG_TAG,"Network Loc. Provider Enabled");
					
					if(locationManager != null){
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location != null){
							lat = location.getLatitude();
							lng = location.getLongitude();
							Log.i(APP_LOG_TAG,"LKL: "+lat+","+lng);
						}
					}
				}
				
				//if gps is enabled...find lat+lng..using GPS
				if(isGPSEnabled){
					if(location == null){
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTWN_UPDATES, MIN_DISTANCE_QUERY_UPDATES, this);
						Log.i(APP_LOG_TAG,"Network Loc. Provider Enabled");
						
						if(locationManager != null){
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(location != null){
								lat = location.getLatitude();
								lng = location.getLongitude();
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return location;	
	}
	
	//stop using gps
	public void stopUsingGPS(){
		if(locationManager != null)
			locationManager.removeUpdates(GPSTracker.this);
	}

	//get lat.
	public double getLatitude(){
		if(location != null){
			lat = location.getLatitude();
		}
		return lat;
	}
	
	//get lng
	public double getLongitude(){
		if(location != null){
			lng = location.getLongitude();
		}
		return lng;
	}
	
	//check whether gps/wifi enabled
	public boolean areProvidersAlive(){
		return this.canGetLocation;
	}
	
	//open settings intent...to enable providers
	public void showSettings(){
		AlertDialog.Builder openSettings = new AlertDialog.Builder(_context);

		//set title & message
		openSettings.setTitle("GPS settings");
		openSettings.setMessage("Please switch on your GPS");
		
		//add btns
		openSettings.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		
		openSettings.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		openSettings.show();
	}
	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
