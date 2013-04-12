package com.jaytronix.reaverend;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReaverendActivity extends FragmentActivity {
	
	//appname...for debugging & log
	public String APP_LOG_TAG = "ReaverendActivity";
	
	//net connection flag
	public boolean isNetAvailable = false;
	
	//net connection detection obj.
	ConnectionDetector checkNet;
	
	//googleMap handle
	private GoogleMap gMap;
	
	//nbi latlng
	private LatLng NBICOORDS = new LatLng(-1.28333, 36.81667);
	
	//alert dialog mgr
	AlertDialogManager alertDM = new AlertDialogManager();
	
	
	//progress dlg
	ProgressDialog pDlg;
	
	//next activity intent
	Intent nextActivity;
	
	//splash vars.
	private static final int MAX_SPLASH_SECONDS = 5;
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
			
			//transitions
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			
			initializeUI();
			
		}else{
			showSplashScreen();
			
			setContentView(R.layout.activity_reaverend);
			
			//transitions
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			
			initializeUI();
		}
		
	}
	
	//initialize ui stuff
	public void initializeUI(){
		//transitions
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
		checkNet = new ConnectionDetector(getApplicationContext());
		
		//check if net is available
		isNetAvailable = checkNet.isConnectionEnabled();
		if(!isNetAvailable){
			//no net! exit
			alertDM.showAlertDialog(ReaverendActivity.this, "Internet Connection", "Please connect to working Internet connection", false);
			return;
		}
		
		//check for play services availability
		isGooglePlayStoreAvailable();
		
		//get map handle
		gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.Gmap)).getMap();
		
		//load markers
		new LoadMarkers().execute();
		
		//position the camera & zoom levels
		CameraPosition NBICameraPos = new CameraPosition.Builder().target(NBICOORDS).zoom(12).build();
		gMap.animateCamera(CameraUpdateFactory.newCameraPosition(NBICameraPos));
		
		//intent for nextactivity
		nextActivity = new Intent(this, ClubItemsPopup.class);

		//set info window click events
		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				double clickedMarkerLat = marker.getPosition().latitude;
				double clickedMarkerLng = marker.getPosition().longitude;
				String clickedClubname = marker.getTitle(); 
				
				Log.i(APP_LOG_TAG,"Marker Clicked: "+marker.getTitle()+" Coords: "+clickedMarkerLat+","+clickedMarkerLng);
				
				//pass data to another intent
				
				nextActivity.putExtra("CLUB_LATITUDE", clickedMarkerLat);
				nextActivity.putExtra("CLUB_LONGITUDE", clickedMarkerLng);
				nextActivity.putExtra("CLUB_NAME", clickedClubname);
				
				startActivity(nextActivity);
				
			}
		});
	}
	
	
	//check google play store availability
	public void isGooglePlayStoreAvailable(){
		int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if( (resCode ==  ConnectionResult.SERVICE_MISSING) || (resCode == ConnectionResult.SERVICE_DISABLED) || (resCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) ){
			GooglePlayServicesUtil.getErrorDialog(resCode, this, 0, new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					Log.i(APP_LOG_TAG,"Google Play services not available! Quiting");
					finish();
				}
			});
		}else{
			Log.i(APP_LOG_TAG,"Google Play services available! ");
		}
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance(){
		StateSaver data = new StateSaver();
		//save important data into this object
		
		if(splashDialog != null){
			data.showSplashScreen = false;
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
		
		//check for play services availability
		isGooglePlayStoreAvailable();
		
		if(!createMapIfNull()){
			//failed to load map...do something
			
		}
	}
	
	
	
	class LoadMarkers extends AsyncTask<String, String, String>{
		
		//json vars
		String jsonString="";
		String parsedJSON="";
		String sampleClub="";
		
		String logging = ""; 
		
		boolean marker = true;
		
		private JSONParser jsonParser = new JSONParser();
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDlg = new ProgressDialog(ReaverendActivity.this);
			pDlg.setMessage(Html.fromHtml("<b>Raeverend</b><br/><i>Loading Clubs...</i>"));
			pDlg.setIndeterminate(true);
			pDlg.setCancelable(true);
			pDlg.show();
			
		}

		//dl club coords in bg thread
		@Override
		protected String doInBackground(String... params) {
			jsonString = jsonParser.getServerContent("CLUBS_LIST",0.00,0.00);
			
			return null;
		}
		
		//creates a marker on the map
		public void createMarker(GoogleMap gMap, double lat, double lng, String name, boolean isSupported){
			
			//create a marker based on issupported bool.
			MarkerOptions options = (isSupported) ? new MarkerOptions().position(new LatLng(lat,lng)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.bar_coktail)) :
				new MarkerOptions().position(new LatLng(lat,lng)).title(name);
			
			//add marker to map
			gMap.addMarker(options);
			
		}
		
		//remove pdiag
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			
			ArrayList<JSONObject> parsedJSON = jsonParser.parseToJSON(jsonString);
			

			try{
				//create n add markers
				for(JSONObject jobj : parsedJSON ){
					String name = jobj.getString("name");
					double lat = jobj.getDouble("latitude");
					double lng = jobj.getDouble("longitude");
					boolean isSupported = (jobj.getString("isSupported").equalsIgnoreCase("1") ) ? true : false;
					
					//log
					Log.i(APP_LOG_TAG,"Club:"+name+" => "+lat+","+lng+" "+isSupported);
					
					//add marker
					createMarker(gMap, lat, lng, name, isSupported);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}

			pDlg.dismiss();
			
		}

	}
}
