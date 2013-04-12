package com.jaytronix.reaverend;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomPriceListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] beerList, beerPrices;
	private String type;
	
	static class ViewHolder{
		public TextView itemName;
		public TextView price;
	}
	
	//def const.
	public CustomPriceListAdapter(Activity context, String[] beerList, String[] beerPrices) {
		super(context, R.layout.itemdetails_rowlayout,beerList);
		this.context = context;
		this.beerPrices = beerPrices;
		this.beerList = beerList;
		this.type = "drinks";
	}

	//default constructor..eg offers
	public CustomPriceListAdapter(Activity context, String[] beerList, String[] beerPrices,String type) {
		super(context, R.layout.offers_row,beerList);
		this.context = context;
		this.beerPrices = beerPrices;
		this.beerList = beerList;
		this.type = "Offers";
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView;
		
		if(rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = (type.equals("drinks")) ? inflater.inflate(R.layout.itemdetails_rowlayout, null) : inflater.inflate(R.layout.offers_row, null) ;
			
			//--create a view holder
			ViewHolder viewHolder = new ViewHolder();
			
			//---get the references to all the views in the row---
			if(type.equals("drinks")){
				viewHolder.itemName = (TextView)rowView.findViewById(R.id.clubItemName);
				viewHolder.price = (TextView)rowView.findViewById(R.id.clubItemPrice);
				
			}else if(type.equals("Offers")){
				viewHolder.itemName = (TextView)rowView.findViewById(R.id.offerName);
				viewHolder.price = (TextView)rowView.findViewById(R.id.offerDesc);
				
			}
			
			//---assign the view holder to the rowView---
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder)rowView.getTag();
		String currentItem = beerList[position];
		holder.itemName.setText(currentItem);
		String currentPrice = beerPrices[position];
		holder.price.setText(currentPrice);
		
		//log
		Log.i(this.toString(),"List Details: ["+currentItem+": "+currentPrice+"]");
		
		return rowView;
	}	

	

}
