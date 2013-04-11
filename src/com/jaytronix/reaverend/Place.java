package com.jaytronix.reaverend;

import java.io.Serializable;

import android.location.Location;

import com.google.api.client.util.Key;

/*
 * when parsing google places api response,  
 * make every place as an object to make it reusable component.
 * 
 * Implement as Serializable so that we can pass this object to 
 * another class using intent...else u cant pass 2 another activity
 */

public class Place implements Serializable {

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String reference;
	
	@Key
	public String icon;
	
	@Key
	public String vicinity;
	
	@Key
	public Geometry geometry;
	
	@Key
	public String formatted_address;
	
	@Key
	public String formatted_phone_number;
	
	@Override
	public String toString(){
		return name+" - "+id+" - "+reference;
	}
	
	public static class Geometry implements Serializable{
		@Key
		public Location location;
	}
	
	public static class Location implements Serializable{
		@Key
		public double lat;
		
		@Key
		public double lng;
	}
	
}
