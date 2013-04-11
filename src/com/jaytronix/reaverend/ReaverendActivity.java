package com.jaytronix.reaverend;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

public class ReaverendActivity extends FragmentActivity {
	
	//appname...for debugging & log
	public String APP_LOG_TAG = "ReaverendActivity";
	
	//net connection flag
	public boolean isNetAvailable = false;
	
	//net connection detection obj.
	ConnectionDetector checkNet;
	
	//googleMap handle
	private GoogleMap gMap;
	
	//alert dialog mgr
	AlertDialogManager alertDM = new AlertDialogManager();
	
	//google place
	GooglePlaces googlePlaces;
	
	//places list
	PlacesList nearPlaces;
	
	//GPS location
	GPSTracker gps;
	
	//progress dlg
	ProgressDialog pDlg;
	
	//ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	//refID of the place..name & area name
	public String KEY_REFERENCE = "reference";
	public String KEY_NAME = "name";
	public String KEY_VICINITY = "vicinity";
	
	//next activity intent
	Intent nextActivity = new Intent(this, ClubItemsPopup.class);
	
	//splash vars.
	private static final int MAX_SPLASH_SECONDS = 2;
	private Dialog splashDialog;
	
	//state saver for splash
	private class StateSaver {
		private boolean showSplashScreen = true;
		// Other save state info here...
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StateSaver data = (StateSaver)getLastCustomNonConfigurationInstance();
		
		if(data != null){
			if(data.showSplashScreen){
				showSplashScreen();
			}
			setContentView(R.layout.activity_reaverend);
		}else{
			showSplashScreen();
			
			setContentView(R.layout.activity_reaverend);
			
			checkNet = new ConnectionDetector(getApplicationContext());
			
			//check if net is available
			isNetAvailable = checkNet.isConnectionEnabled();
			if(!isNetAvailable){
				//no net! exit
				alertDM.showAlertDialog(ReaverendActivity.this, "Internet Connection", "Please connect to working Internet connection", false);
				return;
			}
			
			//gps obj
			gps = new GPSTracker(this);
			
			//query loc. from gps
			if(gps.areProvidersAlive()){
				Log.i(APP_LOG_TAG,"Current Loc: "+gps.getLatitude()+","+gps.getLongitude());
			}else{
				//cant get current loc
				alertDM.showAlertDialog(ReaverendActivity.this, "GPS Information", "Couldn't obtain your location. Please enable GPS", false);
				return;
			}
			
			//bg asynctask to load gp
//			new LoadPlaces().execute();
			
			//get map handle
//			gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.Gmap)).getMap();

			//set marker click events
			gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker marker) {
					
					double clickedMarkerLat = marker.getPosition().latitude;
					double clickedMarkerLng = marker.getPosition().longitude;
					String clickedClubname = marker.getTitle(); 
					
					Log.i(APP_LOG_TAG,"Marker Clicked: "+marker.getTitle()+" Coords: "+clickedMarkerLat+","+clickedMarkerLng);
					
					//pass data to another intent
					
					nextActivity.putExtra("CLUB_LATITUDE", String.valueOf(clickedMarkerLat));
					nextActivity.putExtra("CLUB_LONGITUDE", String.valueOf(clickedMarkerLng));
					nextActivity.putExtra("CLUB_NAME", clickedClubname);
					
					startActivity(nextActivity);
					return true;
				}
			});
			
			//transitions
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

		}
		
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance(){
		StateSaver data = new StateSaver();
		//save important data into this object
		
		if(splashDialog != null){
			data.showSplashScreen = true;
			removeSplashScreen();
		}
		return data;
	}
	
	private void removeSplashScreen(){
		if(splashDialog != null){
			splashDialog.dismiss();
			splashDialog = null;
		}
	}
	
	private void showSplashScreen(){
		splashDialog = new Dialog(this);
		splashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		splashDialog.setContentView(R.layout.splash_layout);
		splashDialog.setCancelable(false);
		splashDialog.show();
		
		//bg handler to cancel splash dlg
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				removeSplashScreen();
			}
		}, MAX_SPLASH_SECONDS * 1000);
	}
	
	//--initiate menu.xml
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	
	//event handling for each menu item selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.ic_nearby: 	//--show clubs nearby
			Toast.makeText(getApplicationContext(), "showing clubs nearby...standby", Toast.LENGTH_LONG).show();
			
			startActivity(new Intent(this, ClubItemsPopup.class));
			
			return true;
		case R.id.ic_district: 	//--widening view..nairobi area
			Toast.makeText(getApplicationContext(), "showing clubs [wide area]...standby", Toast.LENGTH_LONG).show();
			return true;
		case R.id.ic_settings:
			Toast.makeText(getApplicationContext(), "showing settings...standby", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//verify map availability...calld onResume() & onStart()
	public boolean createMapIfNull(){
		if(gMap != null){
//			gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.Gmap)).getMap();
			
			if(gMap != null){
				Log.i(APP_LOG_TAG,"Successfully obtained map!");
				return true;
			}else{
				Log.i(APP_LOG_TAG,"failed to obtain map!");
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(!createMapIfNull()){
			//failed to load map...do something
			
		}
	}
	
	
	
	//background asynctask to load gplaces
	class LoadPlaces extends AsyncTask<String, String, String>{
		
		//show progress dialog
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDlg = new ProgressDialog(ReaverendActivity.this);
			pDlg.setMessage(Html.fromHtml("<b>Club Search</b><br/><i>Loading Clubs...</i>"));
			pDlg.setIndeterminate(true);
			pDlg.setCancelable(true);
			pDlg.show();
			
		}

		//get clubs...parse json
		@Override
		protected String doInBackground(String... args) {
			googlePlaces = new GooglePlaces();
			
			try{
				//separate place types with '|'
				String types = "cafe|restaurant";
				
				//radius..100m
				double radius = 100;
				
				//get nearest clubs
				nearPlaces = googlePlaces.search(gps.getLatitude(), gps.getLongitude(), radius, types);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		/*
		 * dismiss pDlg
		 * show data on ui using runOnUIThread(new Runnable()) to update ui from bg thread
		 * 
		 */
		
		@Override
		protected void onPostExecute(String file_url){
			//dismiss progdialog
			pDlg.dismiss();
			
			//update ui from bg thread
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					//json response status
					String status = nearPlaces.status;
					
					//check 4 all status
					if(status.equalsIgnoreCase("OK")){
						//success: found places & details
						if(nearPlaces.results != null){
							//loop thru each place n add 2 hashmap
							for(Place p : nearPlaces.results){
								HashMap<String, String> map = new HashMap<String, String>();
								
								//place ref. is used to place full details
								map.put(KEY_REFERENCE, p.reference);
								
								//place name
								map.put(KEY_NAME, p.name);
								
								//add hashmap to arraylist
								placesListItems.add(map);
							}
						}
					}else if(status.equalsIgnoreCase("ZERO_RESULTS")){
						//no results found
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", "Sorry no clubs found near you", false);
					}else if(status.equalsIgnoreCase("UNKOWN_ERROR")){
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", "Sorry Unknown error occured!", false);
					}else if(status.equalsIgnoreCase("OVER_QUERY_LIMIT")){
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", "Sorry Query limit to Google Places API exceed!", false);
					}else if(status.equalsIgnoreCase("REQUEST_DENIED")){
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", "Sorry Error occured! Request Denied", false);
					}else if(status.equalsIgnoreCase("INVALID_REQUEST")){
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", " Sorry Error occured! Invalid Request!", false);
					}else{
						alertDM.showAlertDialog(ReaverendActivity.this, "Near clubs", "Sorry an error occured!", false);
					}
					
				}
			});
		}
		
	}

}
