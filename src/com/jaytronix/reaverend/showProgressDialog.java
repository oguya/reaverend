package com.jaytronix.reaverend;

import android.app.ProgressDialog;
import android.content.Context;

public class showProgressDialog {
	public ProgressDialog progDiag;
	public Context context;
	public String title , message;
	
	public showProgressDialog(Context context, String title, String message){
		this.context = context;
		this.title = title;
		this.message = message;
	}
	
	public void showDialog(){
		progDiag = ProgressDialog.show(context, this.title, this.message, true, false);
	}
	
	public void dismiss(){
		progDiag.dismiss();
	}
}
