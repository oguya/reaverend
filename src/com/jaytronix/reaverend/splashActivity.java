package com.jaytronix.reaverend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class splashActivity extends Activity{

	private long ms=0;
	private long splashTime = 2000;
	private boolean splashActive = true;
	private boolean paused = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		
		//remove window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash_layout);
		

	
		//create thread to show splash 4 a few sec.
		Thread newthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					while(splashActive && ms < splashTime){
						if(!paused)
							ms += 100;
						Thread.sleep(100);
					}
				}catch(Exception e){e.printStackTrace();}
				
				finally{
					startActivity(new Intent(splashActivity.this,ReaverendActivity.class));
				}
			}
		});
		newthread.start();
	}
	
}
