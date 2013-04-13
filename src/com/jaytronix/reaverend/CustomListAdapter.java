package com.jaytronix.reaverend;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] listItems;
	
	static class ViewHolder{
		public TextView text;
		public ImageView image;
	}
	
	public CustomListAdapter(Activity context, String[] listItems){
		super(context, R.layout.rowlayout, listItems);
		this.context = context;
		this.listItems = listItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView;
		
		if(rowView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.rowlayout, null);
			
			//--create a view holder
			ViewHolder viewHolder = new ViewHolder();
			
			//---get the references to all the views in the row---
			viewHolder.text = (TextView)rowView.findViewById(R.id.listItemName);
			viewHolder.image = (ImageView)rowView.findViewById(R.id.listIcon);
			
			//---assign the view holder to the rowView---
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder)rowView.getTag();
		String currentItem = listItems[position];
		holder.text.setText(currentItem);
		
		//set images for each item
		if(currentItem.equalsIgnoreCase("Beer")){
			holder.image.setImageResource(R.drawable.beer_mug);
		}else if(currentItem.equalsIgnoreCase("Tots")){
			holder.image.setImageResource(R.drawable.tot);
		}else if(currentItem.equalsIgnoreCase("Spirits")){
			holder.image.setImageResource(R.drawable.spirits);
		}else if(currentItem.equalsIgnoreCase("Offers")){
			holder.image.setImageResource(R.drawable.offers);
		}else if(currentItem.equalsIgnoreCase("Events")){
			holder.image.setImageResource(R.drawable.events);
		}else if(currentItem.equalsIgnoreCase("Cocktails")){
			holder.image.setImageResource(R.drawable.cocktail);
		}
		
		return rowView;
	}
	

}
