package com.jaytronix.reaverend;

import java.io.IOException;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;

/*
 * searching places
 * 	@param lat -> latitude of the place
 * 	@param lng -> longitude of the place
 * 	@param radius -> radius of searchable area
 * 	@param types -> type of places to search
 * 	rt list f places
 * 
 * searching single place 4 all details
 * 	@param reference -> referenceID of the place
 * 		....[available in search api request]
 */


public class GooglePlaces {

	//httptransport
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	//GPlaces API key
	private static final String API_KEY = "AIzaSyBwgdxF1gNuhNxBTFKQ6N2x7K37tZcPPg4";
	
	//Gplaces search urls
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String  PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	
	private double _lat;
	private double _lng;
	private double _radius;
	
	//log tags
	private  String APP_LOG_TAG = "GooglePlaces";
	
	public PlacesList search(double lat, double lng, double radius, String types) throws Exception{
		this._lat = lat;
		this._lng = lng;
		this._radius = radius;
		
		try{
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
			
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("location", _lat+","+_lng);
			request.getUrl().put("radius", _radius);
			request.getUrl().put("sensor", "false");
			
			if(types != null)
				request.getUrl().put("types", types);
			
			PlacesList list = request.execute().parseAs(PlacesList.class);
			Log.i(APP_LOG_TAG,"Places status: "+list.status);
			
			return list;
		}catch(HttpResponseException e){
			Log.e(APP_LOG_TAG,e.getMessage());
			return null;
		}
	}
	
	
	public PlaceDetails getPlaceDetails(String reference) throws Exception {
		try{
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
			
			request.getUrl().put("key",API_KEY);
			request.getUrl().put("reference", reference);
			request.getUrl().put("sensor", "false");
			
			PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
			
			return place;
			
		}catch(HttpResponseException e){
			Log.e(APP_LOG_TAG, "Place Details:: "+e.getMessage());
			throw e;
		}
	}
	
	
	//create httprequestfactory
	public static HttpRequestFactory createRequestFactory(final HttpTransport transport){
		return transport.createRequestFactory(new HttpRequestInitializer() {
			
			@Override
			public void initialize(HttpRequest request) throws IOException {
				// TODO Auto-generated method stub
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Reaverend");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
			}
		});
	}
	
	
}
