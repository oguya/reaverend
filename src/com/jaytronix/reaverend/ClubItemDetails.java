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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClubItemDetails extends ListActivity{

	String ITEM_NAME;
	String[] ItemList;
	String[] ItemPrice;
	String[] eventName;
	String[] eventDate;
	String[] eventDesc;
	String[] offerList;
	String[] offerDesc;
	boolean NULL_JSON=false;
	
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
		ArrayList<JSONObject> jsonItems = new ArrayList<JSONObject>();
		

		
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
			loadItemDetails(ITEM_NAME);
			return null;
		}
		
		//proc n load item details from srv
		public void loadItemDetails(String itemName){
			
			String itemType=""; 
			
			if(itemName.equalsIgnoreCase("Beer") || itemName.equalsIgnoreCase("Tots")  || itemName.equalsIgnoreCase("Spirits")
					|| itemName.equalsIgnoreCase("Cocktails") ){
				
				if( itemName.equalsIgnoreCase("Beer") ){
					itemType="CLUB_BEER_LIST";
				}else if( itemName.equalsIgnoreCase("Tots") ){
					itemType="CLUB_TOTS_LIST";
				}else if( itemName.equalsIgnoreCase("Spirits") ){
					itemType="CLUB_SPIRITS_LIST";
				}else if( itemName.equalsIgnoreCase("Cocktails") ){
					itemType="CLUB_COCKTAILS_LIST";
				}
				
				String jsonString = jsonParser.getServerContent(itemType, CLUB_LAT, CLUB_LONG);
				NULL_JSON = (jsonString.equals("")) ? true : false;
				jsonItems = jsonParser.parseToJSON(jsonString);
				
				StringBuilder tempName = new StringBuilder();
				StringBuilder tempPrice = new StringBuilder();
				
				try{
				
					for(JSONObject jobj : jsonItems ){
						
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

				
			}else if(itemName.equalsIgnoreCase("Offers")){
				
				
				String jsonString = jsonParser.getServerContent("CLUB_OFFERS_LIST", CLUB_LAT, CLUB_LONG);
				NULL_JSON = (jsonString.equals("")) ? true : false;
				
				jsonItems = jsonParser.parseToJSON(jsonString);
				
				StringBuilder tempName = new StringBuilder();
				StringBuilder tempDesc = new StringBuilder();
				
				try{
				
					for(JSONObject jobj : jsonItems ){
						
						tempName.append(jobj.getString("name"));
						tempName.append("#");
						
						tempDesc.append(jobj.getString("description"));
						tempDesc.append("#");
						
					}
					
					//split
					offerList = tempName.toString().split("#");
					offerDesc = tempDesc.toString().split("#");
				
				}catch(JSONException e){
					e.printStackTrace();
				}

				
				
			}else if(itemName.equalsIgnoreCase("Events")){
				String jsonString = jsonParser.getServerContent("CLUB_EVENTS_LIST", CLUB_LAT, CLUB_LONG);
				NULL_JSON = (jsonString.equals("")) ? true : false;
				jsonItems = jsonParser.parseToJSON(jsonString);
				
				StringBuilder tempName = new StringBuilder();
				StringBuilder tempDate = new StringBuilder();
				StringBuilder tempDesc = new StringBuilder();
				
				try{
				
					for(JSONObject jobj : jsonItems ){
						
						tempName.append(jobj.getString("name"));
						tempName.append("#");
						
						tempDate.append(jobj.getString("date"));
						tempDate.append("#");
						
						tempDesc.append(jobj.getString("description"));
						tempDesc.append("#");
						
					}
					
					//split
					eventName = tempName.toString().split("#");
					eventDate = tempDate.toString().split("#");
					eventDesc = tempDesc.toString().split("#");
				
				}catch(JSONException e){
					e.printStackTrace();
				}

			}
			
		}
		
		//remove dialog
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			
			if(ITEM_NAME.equalsIgnoreCase("Beer") || ITEM_NAME.equalsIgnoreCase("Tots")  || ITEM_NAME.equalsIgnoreCase("Spirits")
					|| ITEM_NAME.equalsIgnoreCase("Cocktails") ){
				
				if(NULL_JSON){
					TextView noInfotxt= (TextView)findViewById(R.id.noInfo);
					noInfotxt.setVisibility(View.VISIBLE);
				}else{
					ListView lv = getListView();
					lv.setAdapter(new CustomPriceListAdapter(ClubItemDetails.this, ItemList, ItemPrice));
				}
				
			}else if(ITEM_NAME.equalsIgnoreCase("Events")){
				
				if(NULL_JSON){
					TextView noInfotxt= (TextView)findViewById(R.id.noInfo);
					noInfotxt.setVisibility(View.VISIBLE);
				}else{
					ListView lv = getListView();
					lv.setAdapter(new CustomEventListAdapter(ClubItemDetails.this, eventName, eventDate, eventDesc));
				}
				
			}else if(ITEM_NAME.equalsIgnoreCase("Offers")){
				
				if(NULL_JSON){
					TextView noInfotxt= (TextView)findViewById(R.id.noInfo);
					noInfotxt.setVisibility(View.VISIBLE);
				}else{
					ListView lv = getListView();
					lv.setAdapter(new CustomPriceListAdapter(ClubItemDetails.this, offerList, offerDesc, ""));
				}
			}
			
			pDlg.dismiss();

		}
	}
}
