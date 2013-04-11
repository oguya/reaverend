package com.jaytronix.reaverend;

import java.io.Serializable;

import com.google.api.client.util.Key;

/*
 * Implement as Serializable so that we can pass this object to 
 * another class using intent...else u cant pass 2 another activity
 * 
 */

public class PlaceDetails implements Serializable{

	@Key
	public String status;
	
	@Key
	public String result;
	
	@Override
	public String toString(){
		if(result != null){
			return result.toString();
		}
		return super.toString();
	}
	
}
