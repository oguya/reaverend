package com.jaytronix.reaverend;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/*
 * Implement as Serializable so that we can pass this object to 
 * another class using intent...else u cant pass 2 another activity
 * 
 */

public class PlacesList implements Serializable {

	@Key
	public String status;
	
	@Key
	public List<Place> results;
	
}
