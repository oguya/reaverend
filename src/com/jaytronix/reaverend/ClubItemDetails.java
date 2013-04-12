package com.jaytronix.reaverend;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;
import android.widget.Toast;

public class ClubItemDetails extends ListActivity{

	String ITEM_NAME;
	String[] ItemList;
	String[] ItemPrice;
	
	//received data from previous intent
	public double CLUB_LAT = 0.00;
	public double CLUB_LONG = 0.00;
	public String CLUB_NAME = "";
	
	//loading dialog
	ProgressDialog pDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clubitemsdetails);
		
		//transitions
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
		//get send payload
		Intent lastActivity = getIntent();
		CLUB_LAT = lastActivity.getDoubleExtra("CLUB_LAT", 0.00);
		CLUB_LONG = lastActivity.getDoubleExtra("CLUB_LONG", 0.00);
		ITEM_NAME = lastActivity.getStringExtra("ITEM_NAME");
		CLUB_NAME = lastActivity.getStringExtra("CLUB_NAME");
		
		//set title
		setTitle(ITEM_NAME);
		
		//load prices
		new LoadItems().execute();
		
	}
	
	
	
	class LoadItems extends AsyncTask<String, String, String>{

		JSONParser jsonParser = new JSONParser();
		ArrayList<JSONObject> itemPrices = new ArrayList<JSONObject>();
		

		
		//add a loading dialog
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			pDlg = new ProgressDialog(ClubItemDetails.this);
			pDlg.setMessage(Html.fromHtml("<b>"+CLUB_NAME+"</b><br/><i>The Reaverend is processing your order..."));
			pDlg.setCancelable(true);
			pDlg.setIndeterminate(true);
			pDlg.show();
			
		}
		
		
		@Override
		protected String doInBackground(String... params) {
			String jsonString = jsonParser.getServerContent("CLUB_BEER_LIST", CLUB_LAT, CLUB_LONG);
			itemPrices = jsonParser.parseToJSON(jsonString);
			
			StringBuilder tempName = new StringBuilder();
			StringBuilder tempPrice = new StringBuilder();
			
			try{
			
				for(JSONObject jobj : itemPrices ){
					
					tempName.append(jobj.getString("name"));
					tempName.append("#");
					
					tempPrice.append(jobj.getString("price"));
					tempPrice.append("#");
					
				}
				
				//split
				ItemList = tempName.toString().split("#");
				ItemPrice = tempPrice.toString().split("#");
			
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		//proc n load item details from srv
		public void loadItemDetails(String itemName){
			
			
		}
		
		//remove dialog
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			pDlg.dismiss();
			
			ListView lv = getListView();
			if(ItemList!=null || ItemPrice != null){
				lv.setAdapter(new CustomPriceListAdapter(ClubItemDetails.this, ItemList, ItemPrice));
			}
			else
				Toast.makeText(ClubItemDetails.this, "empty list content", Toast.LENGTH_LONG).show();

		}
	}
}
