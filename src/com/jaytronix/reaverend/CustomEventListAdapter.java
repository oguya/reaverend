package com.jaytronix.reaverend;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomEventListAdapter extends ArrayAdapter<String>{

	private final Activity context;
	private final String[] eventName, eventDate, eventDesc;
	
	static class ViewHolder{
		public TextView eventNameTxt, eventDateTxt, eventDescTxt;
	}
	
	//default constructor..eg events
	public CustomEventListAdapter(Activity context, String[] eventName, String[] eventDate, String[] eventDesc) {
		super(context, R.layout.events_row,eventName);
		
		this.context = context;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventDesc = eventDesc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView;
		
		if(rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.events_row, null);
			
			//--create a view holder
			ViewHolder viewHolder = new ViewHolder();
			
			//---get the references to all the views in the row---
			viewHolder.eventNameTxt = (TextView)rowView.findViewById(R.id.eventName);
			viewHolder.eventDateTxt = (TextView)rowView.findViewById(R.id.eventDate);
			viewHolder.eventDescTxt = (TextView)rowView.findViewById(R.id.eventDesc);
			
			//---assign the view holder to the rowView---
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder)rowView.getTag();
		String currentEventName = eventName[position];
		holder.eventNameTxt.setText(currentEventName);
		String currentEventDate = eventDate[position];
		holder.eventDateTxt.setText(currentEventDate);
		String currentEventDesc = eventDesc[position];
		holder.eventDescTxt.setText(currentEventDesc);
		
		//log
		Log.i(this.toString(),"List Details: ["+currentEventName+": "+currentEventDate+": "+currentEventDesc+ "]");
		
		return rowView;
	}	
}
