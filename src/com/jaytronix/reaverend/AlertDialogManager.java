package com.jaytronix.reaverend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/*
 * PURPOSE: reusable alert class
 * 
 * @param context -> app. context
 * @param title -> alert dialog title
 * @param message -> alert msg
 * @status -> success/failure(set icon) | null 4 no ic
 */

public class AlertDialogManager {
	
	public String APP_LOG_TAG="AlertDialogManager";
	
	public void showAlertDialog(Context context, String title, String message, Boolean status){
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		
		//set title & msg
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		//set title		
		if(status != null)
			alertDialog.setIcon( (status) ? R.drawable.success : R.drawable.fail );
		
		//set OK btn
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.i(APP_LOG_TAG,"user clicked OK");
			}
		});
		
		//show alert dialog
		alertDialog.show();
		
	}

}
