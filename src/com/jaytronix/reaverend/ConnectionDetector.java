package com.jaytronix.reaverend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * PURPOSE: detects whether there is a working Internet connection or not
 */

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}
	
	//check 4 all possible internet providers
	public boolean isConnectionEnabled(){
		ConnectivityManager net =  (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(net != null){
			NetworkInfo[] info = net.getAllNetworkInfo();
			if(info != null){
				for(int i=0; i<info.length; i++){
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
						return true; 
				}
			}
		}
		return false;
	}

}
