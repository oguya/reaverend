package com.jaytronix.reaverend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	//dl json file from server
	public String getServerContent(String url){
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
	public String getClubs(String JSONString){
		String[] clubTags = {"clubID","latitude","longitude","name","isSupported"};
		
		//ret. string contains:=> lat,lng|name|issupported#
		String clubList=null;
		try{
			Log.i(APP_LOG_TAG,JSONString);
			
			//obtain json objects('{') since theaz no jsonarrays('[') & store in hashmap
			JSONObject jobj = new JSONObject(JSONString);
			for(String tag : clubTags){
				clubList = jobj.getString(tag)+"#";
				Log.i(APP_LOG_TAG,tag+ ":"+jobj.getString(tag));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return clubList;
	}
	
	
	//split jsonstrings based on '#'
	public String[] splitJSONStrings(String jsonString){
		return jsonString.split("#");
	}
}
