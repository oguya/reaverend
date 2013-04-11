package com.jaytronix.reaverend;

import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ClubItemDetails extends ListActivity{

	String[] beerList={"Tusker","another beer","another beer","another beer"};
	String[] beerPrices={"150","200","150","194"};
	String ITEM_NAME;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clubitemsdetails);
		
		//transitions
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
		//get send payload
		getPayload();
		
		//set title
		setTitle(ITEM_NAME);
		
		ListView lv = getListView();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, beerList);
		lv.setAdapter(new CustomPriceListAdapter(this, beerList, beerPrices));
	}
	
	public void getPayload(){
		Intent lastActivity = getIntent();
		this.ITEM_NAME = lastActivity.getStringExtra("ITEM_NAME");
//		this.beerList = lastActivity.getStringArrayExtra("");
		
	}
	
	
}
