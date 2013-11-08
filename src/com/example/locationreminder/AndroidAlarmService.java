package com.example.locationreminder;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.database.LocationDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.netconnect.NetworkInformation;



public class AndroidAlarmService extends Activity {
	private PendingIntent pendingIntent;
	String lat;
	String longi;
	LatLng origin;
	GPSTracker gps_o;	
	double latitude;
	double longitude;
	
	String [] db_lat;
	String [] db_log;
	String [] db_note;

	
	float[] new_lat;
	float[] new_log;
	
	StringBuilder strReturnedAddress;

	/** Called when the activity is first created. */

	@Override

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm);

		Button buttonStart = (Button)findViewById(R.id.startalarm);

		Button buttonCancel = (Button)findViewById(R.id.cancelalarm);
		gps_o = new GPSTracker(AndroidAlarmService.this);
		CurrentLocationThread cthread=new CurrentLocationThread();
		cthread.start();
		buttonStart.setOnClickListener(new Button.OnClickListener(){

			

			@Override

			public void onClick(View arg0) {

				// TODO Auto-generated method stub





				Toast.makeText(AndroidAlarmService.this, "Start Alarm", Toast.LENGTH_LONG).show();

			}});



		buttonCancel.setOnClickListener(new Button.OnClickListener(){



			@Override

			public void onClick(View arg0) {

				// TODO Auto-generated method stub

				AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

				alarmManager.cancel(pendingIntent);



				// Tell the user about what we did.

				Toast.makeText(AndroidAlarmService.this, "Cancel!", Toast.LENGTH_LONG).show();


			}});


	}
	public void add(View v)
	{
		Intent intent = new Intent(this,Home.class);
		startActivity(intent);
	}
	
	//..........................get current location..............................
			public class CurrentLocationThread extends Thread{
				public static final int SUCCESS 				= 0;
				public static final int FAILURE 				= 1;
				public static final int NO_DATA 				= 2;
				public static final int NO_NETWORK_CONNECTION 	= 3;
				public CurrentLocationThread() {
				}
				@Override
				public void run() {
					try {
						if(NetworkInformation.isNetworkAvailable(AndroidAlarmService.this)){
							finalSubHandler.sendEmptyMessage(SUCCESS);
						}
						else{
							finalSubHandler.sendEmptyMessage(NO_NETWORK_CONNECTION);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}		}
			}	@SuppressLint("HandlerLeak")
			private Handler finalSubHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case CurrentLocationThread.SUCCESS:
						// check if GPS enabled	
						Toast.makeText(AndroidAlarmService.this, "hgere", Toast.LENGTH_LONG).show();	
						try
						{
							
						if(gps_o.canGetLocation()){
							Toast.makeText(AndroidAlarmService.this, "hgereddcxfb", Toast.LENGTH_LONG).show();
							latitude = gps_o.getLatitude();
							longitude = gps_o.getLongitude();	        	
							Geocoder geocoder = new Geocoder(AndroidAlarmService.this, Locale.ENGLISH);
							lat = String.valueOf(latitude);
							longi = String.valueOf(longitude);
							
							
							Toast.makeText(AndroidAlarmService.this, lat, Toast.LENGTH_LONG).show();
							//Saving current lattitude and Long											
							try {
							List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
							if(addresses != null) {
							Address returnedAddress = addresses.get(0);
							 strReturnedAddress = new StringBuilder("");
							for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
							strReturnedAddress.append(returnedAddress.getAddressLine(i));
							}							
							}
							else{
								
							}
							} catch (IOException e) {
							// TODO Auto-generated catch block
								
							e.printStackTrace();
							
							}
						
							// \n is for new line
							//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
						}else{
							// can't get location
							// GPS or Network is not enabled
							// Ask user to enable GPS/network in settings
							gps_o.showSettingsAlert();
						
						}
						
						try
						{	
							
							Toast.makeText(AndroidAlarmService.this, "hgere1", Toast.LENGTH_LONG).show();	
							LocationDatabase dbObj=new LocationDatabase(getApplicationContext());
							dbObj.getMainLocation();
//							db_lat = new String[20];
//							db_log = new String[20];
//							db_note = new String[20];
//							
							new_lat = new float[20];
							new_log = new float[20];
							
							db_lat=dbObj.getlat();
							db_log=dbObj.getlog();
							db_note=dbObj.getnote();
							
							Toast.makeText(AndroidAlarmService.this, "cccccccc1", Toast.LENGTH_LONG).show();	
							for(int i=0;i<10;i++)
							{
								
								new_lat[i]=(float) (latitude+i*.0000001);
								new_log[i]=(float)(longitude+i*.0000001);
								
								new_lat[10+i]=(float) (latitude-i*.0000001);							
								new_log[10+i]=(float)(longitude-i*.0000001);
							}
							Toast.makeText(AndroidAlarmService.this, "cccccccc", Toast.LENGTH_LONG).show();	
						
							for(int x=0;x<new_lat.length;x++)
						{
							for(int y=0;y<db_lat.length;y++)
							{
								System.out.println("new_lat[x]===="+new_lat[x]);
								System.out.println("new_ladb]===="+db_lat[y]);
								if(new_lat[x]-Float.parseFloat((db_lat[y]))>=.000000001)
										{
									Toast.makeText(AndroidAlarmService.this, "cccccccc3", Toast.LENGTH_LONG).show();
									if(new_log[x]-Float.parseFloat((db_log[y]))>=.00000001)
									{

										Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);

										pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);



										AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



										Calendar calendar = Calendar.getInstance();	
										

										calendar.setTimeInMillis(System.currentTimeMillis());

										calendar.add(Calendar.SECOND, 3);

										alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);



										Toast.makeText(AndroidAlarmService.this, "Start Alarm", Toast.LENGTH_LONG).show();	
										x=100; y=100;
									}
										}
							}
						}
						}
						catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
						catch (Exception e) {
							// TODO: handle exception
						}
						break;			
					case CurrentLocationThread.NO_DATA:	
						
						Toast.makeText(AndroidAlarmService.this, "Sorry! Network Error",Toast.LENGTH_LONG).show();
						break;
					case CurrentLocationThread.NO_NETWORK_CONNECTION:		
						Toast.makeText(AndroidAlarmService.this, "Sorry! Network Error",Toast.LENGTH_LONG).show();	
						
						break;
					default:
						break;
					}
					super.handleMessage(msg);
				}
			};
			
			
			public void list(View v)
			{
				Intent i = new Intent (this,Data.class);
				startActivity(i);
			}
			
			
}
