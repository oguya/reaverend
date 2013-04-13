package com.jaytronix.reaverend;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class ClubItemsPopup extends ListActivity implements OnItemClickListener{
	
	String[] listItems = {"Beer","Tots","Spirits","Offers","Events","Cocktails"};
	private String APP_LOG_TAG = "ClubItemsPopup";
	
	//intent...receive data from previous activity
	Intent previousActivity;
	
	//received data from previous intent
	public String CLUB_NAME = "";
	public double CLUB_LAT = 0.00;
	public double CLUB_LONG = 0.00;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//transitions
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
		setContentView(R.layout.activity_clubitemspopup);
		
		//get previous intent
		previousActivity = getIntent();
		
		//data from previous intents
		CLUB_NAME = previousActivity.getStringExtra("CLUB_NAME");
		CLUB_LAT  = previousActivity.getDoubleExtra("CLUB_LATITUDE", 0.00);
		CLUB_LONG = previousActivity.getDoubleExtra("CLUB_LONGITUDE", 0.00);
		
		//LOG...
		Log.i(APP_LOG_TAG,"Received data from previous Intent : "+CLUB_NAME+" COORDS: "+CLUB_LAT+","+CLUB_LONG);
		
		ListView lv = getListView();

		//setting header
//		LayoutInflater inflater = getLayoutInflater();
//		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listheader, lv, false);
//		lv.addHeaderView(header, null, false);		
		setListAdapter(new CustomListAdapter(this,listItems ));
		lv.setOnItemClickListener(this);
		
		//setting activity title...eg Betty's
		Log.i(APP_LOG_TAG,"Changing activity title ");
		setTitle(CLUB_NAME);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.popuptitlebar);
			
	}
	
	//handle list item clicks....send data via intent to next activity
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Intent nextActivity = new Intent(this, ClubItemDetails.class);
		if(listItems[position].equalsIgnoreCase("Beer")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
				
		}else if(listItems[position].equalsIgnoreCase("Wines")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
				
		}else if(listItems[position].equalsIgnoreCase("Spirits")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
				
		}else if(listItems[position].equalsIgnoreCase("Tots")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
				
		}else if(listItems[position].equalsIgnoreCase("Offers")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
				
		}else if(listItems[position].equalsIgnoreCase("Events")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
			
		}else if(listItems[position].equalsIgnoreCase("Cocktails")){
			//--add payload to the intent...
			nextActivity.putExtra("CLUB_NAME", CLUB_NAME);
			nextActivity.putExtra("ITEM_NAME", listItems[position]);
			nextActivity.putExtra("CLUB_LAT", CLUB_LAT);
			nextActivity.putExtra("CLUB_LONG", CLUB_LONG);
			startActivity(nextActivity);
			
			Log.i(APP_LOG_TAG,"sending data["+listItems[position]+"] via intent to new activity");
			
		}
	}	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_reaverend, menu);
//		return true;
//		
//	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item){
//		return super.onOptionsItemSelected(item);
//	}
	
}
