/* Created by sanu.
 */
package com.example.locationreminder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
public class Splash extends Activity {
	private static final int SPLASH_DISPLAY_TIME =2000;  /* 2 seconds */       
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);  
		new Handler().postDelayed(new Runnable() {
			@SuppressLint("NewApi")
			public void run() {          		

				Intent mainIntent = new Intent(Splash.this, AndroidAlarmService.class);
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();        	
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); 

			}
		}, SPLASH_DISPLAY_TIME);
	}    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



}