package com.jaytronix.reaverend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;


public class JSONParser{
	
	//app tag for log
	private String APP_LOG_TAG = "JSONParser";
	
	//getclubs URL
	private String GET_CLUBS_URL = "http://bitcypher.co.ke/raeverend/getClubs.php";
	private String GET_CLUB_BEER_URL = "http://bitcypher.co.ke/raeverend/getBeers.php";
	private String GET_CLUB_TOTS_URL = "http://bitcypher.co.ke/raeverend/getTots.php";
	private String GET_CLUB_SPIRITS_URL = "http://bitcypher.co.ke/raeverend/getSpirits.php";
	private String GET_CLUB_OFFERS_URL = "http://bitcypher.co.ke/raeverend/getOffers.php";
	private String GET_CLUB_EVENTS_URL = "http://bitcypher.co.ke/raeverend/getEvents.php";
	private String GET_CLUB_COCKTAILS_URL = "http://bitcypher.co.ke/raeverend/getCocktails.php";
	
	//hashmap
	public ArrayList<JSONObject> jsonClubList ;

	public JSONParser(){
		jsonClubList = new ArrayList<JSONObject>();
	}
	
	//dl json file from server
	public String getServerContent(String type, double lat, double lng){
		
		//round up coords
		if(lat != 0.00 && lng != 0.00){
			lat = new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			lng = new BigDecimal(lng).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		String url="";
		
		if(type.equalsIgnoreCase("CLUBS_LIST"))
			url = this.GET_CLUBS_URL;
		else if( (type.equalsIgnoreCase("CLUB_BEER_LIST")) ){
			url = this.GET_CLUB_BEER_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);
			
		}else if(type.equalsIgnoreCase("CLUB_TOTS_LIST") ){
			url = this.GET_CLUB_TOTS_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);

		}else if(type.equalsIgnoreCase("CLUB_SPIRITS_LIST") ){
			url = this.GET_CLUB_SPIRITS_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);

		}else if(type.equalsIgnoreCase("CLUB_OFFERS_LIST") ){
			url = this.GET_CLUB_OFFERS_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);

		}else if(type.equalsIgnoreCase("CLUB_EVENTS_LIST") ){
			url = this.GET_CLUB_EVENTS_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);

		}else if(type.equalsIgnoreCase("CLUB_COCKTAILS_LIST") ){
			url = this.GET_CLUB_COCKTAILS_URL;
			url += "?lat="+lat+"&long="+lng;
			
			Log.i(APP_LOG_TAG,"Requesting list URL: "+url);

		}
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		try{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine= response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if(statusCode == 200){
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while( (line = reader.readLine())!= null ){
					builder.append(line);
				}
			}else{
				Log.i(APP_LOG_TAG,"Failed to dl json file! Server Response: "+statusCode);
			}
			
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	//parse the jsonstring
	public ArrayList<JSONObject> parseToJSON(String JSONString){
		
		//input string :=> {"clubID":"6","latitude":"-1.268254","longitude":"36.811464","name":"K1","isSupported":"1"}#{"clubID":"7","latitude":"-1.265435","longitude":"36.804798","name":"Skylux","isSupported":"1"}
		//item input string => {"beerName":"Tusker","price":"200"}#{"beerName":"Pilsner","price":"180"}#{"beerName":"Guinness","price":"220"}
		
		String[] temp = JSONString.split("#");
		for(String jsonSingleLine : temp ){
			try{
				Log.i(APP_LOG_TAG,jsonSingleLine);
				
				//obtain json objects('{') since theaz no jsonarrays('[') & store in arraylist
				jsonClubList.add(new JSONObject(jsonSingleLine));
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return jsonClubList;
	}
}
