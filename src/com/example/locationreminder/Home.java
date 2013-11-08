package com.example.locationreminder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.database.LocationDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.netconnect.NetworkInformation;


public class Home extends FragmentActivity implements OnMapClickListener,OnClickListener{
	private SeekBar SeekBar1 = null;
	Integer p1=0;	
	String p="1";
	TextView within;	
	GoogleMap map;
	RelativeLayout locationLayout;
	EditText heading;
	String x="1";	
	String lat;
	String longi;
	LatLng origin;
	GPSTracker gps;	
	double latitude;
	double longitude;
	StringBuilder strReturnedAddress;
	EditText alarmnote;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		SeekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		within 		= (TextView)findViewById(R.id.radius);
		locationLayout= (RelativeLayout)findViewById(R.id.locationlayout);
		heading= (EditText)findViewById(R.id.heading);
		alarmnote =  (EditText)findViewById(R.id.alarmnote);
		gps = new GPSTracker(Home.this);
		SeekBar1.setThumb(getResources().getDrawable(R.drawable.seek_thumb));		
		SeekBar1.setMax(99);
		CurrentLocationThread cthread=new CurrentLocationThread();
		cthread.start();
		try {
			SeekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() { 
				@Override 
				public void onStopTrackingTouch(SeekBar analyseSeekBar) {
				// TODO Auto-generated method stub 
				p1 = analyseSeekBar.getProgress();		
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
				// TODO Auto-generated method stub
				p1=progress;
				p=p1.toString();
				x=p1.toString();
				within.setText( x+=" Kms");				
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub					
				}
			});		
		} 
		catch (OutOfMemoryError e) 
		{

		}
		catch (Exception e) {
		e.printStackTrace();
		}
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);		
		map = fm.getMap();	
		map.setOnMapClickListener(this) ;
		
		
		
						        	String location=heading.getText().toString().trim();
						       
	}

	//...search location in map
		public void searchPlace(String value)
		{  
		      Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());    
		        try {
		            List<Address> addresses = geoCoder.getFromLocationName(
		                value, 5);		          
		            if (addresses.size() > 0) {
		            	
		            	double latitude= 0.0, longtitude= 0.0;
		            	GeoPoint     p = new GeoPoint(
		                        (int) (addresses.get(0).getLatitude() * 1E6), 
		                        (int) (addresses.get(0).getLongitude() * 1E6));
		            	latitude=p.getLatitudeE6()/1E6;
						longtitude=p.getLongitudeE6()/1E6;	             
						lat = String.valueOf(latitude);
						longi = String.valueOf(longtitude);
						origin = new LatLng(latitude,longtitude);
		            	map.moveCamera( CameraUpdateFactory.newLatLngZoom(origin, (float) 14.0) ); 
		            }    
		        } catch (IOException e) {
		            e.printStackTrace();
		        }



		   

		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/*Map click Action for selecting Location */
	public void onMapClick (LatLng point) {
		map.clear();
		double lat1 = point.latitude;
		double lng1 = point.longitude;
		lat=String.valueOf(lat1);
		longi=String.valueOf(lng1);
		Geocoder gcd = new Geocoder(Home.this, Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(lat1, lng1, 1);
			if (addresses.size() > 0) 
				
				heading.setText(addresses.get(0).getSubAdminArea());
			Toast.makeText(Home.this, addresses.get(0).getLocality(),Toast.LENGTH_SHORT).show();			
			MarkerOptions options = new MarkerOptions();
			options.position(point);
			Bitmap icon = BitmapFactory.decodeResource(Home.this.getResources(),
					R.drawable.pin);
			Bitmap bhalfsize=Bitmap.createScaledBitmap(icon, icon.getWidth()/5,icon.getHeight()/5, false);
			options.icon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
			options.title(addresses.get(0).getLocality());		
			map.addMarker(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void getFromLocation(String address)
	{
		double latitude= 0.0, longtitude= 0.0;

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
		try 
		{
			List<Address> addresses = geoCoder.getFromLocationName(address , 1);
			if (addresses.size() > 0) 
			{            
				GeoPoint p = new GeoPoint(
						(int) (addresses.get(0).getLatitude() * 1E6), 
						(int) (addresses.get(0).getLongitude() * 1E6));

				latitude=p.getLatitudeE6()/1E6;
				longtitude=p.getLongitudeE6()/1E6;	             
				lat = String.valueOf(latitude);
				longi = String.valueOf(longtitude);

			}
		}
		catch(Exception ee)
		{

		}
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
					if(NetworkInformation.isNetworkAvailable(Home.this)){
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
					
					try
					{
					if(gps.canGetLocation()){
						latitude = gps.getLatitude();
						longitude = gps.getLongitude();	        	
						Geocoder geocoder = new Geocoder(Home.this, Locale.ENGLISH);
						lat = String.valueOf(latitude);
						longi = String.valueOf(longitude);
						//Saving current lattitude and Long											
						try {
						List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
						if(addresses != null) {
						Address returnedAddress = addresses.get(0);
						 strReturnedAddress = new StringBuilder("");
						for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
						strReturnedAddress.append(returnedAddress.getAddressLine(i));
						}
						heading.setText(strReturnedAddress.toString());
						}
						else{
							heading.setText("Not getting location!!!Please try Again");
						}
						} catch (IOException e) {
						// TODO Auto-generated catch block
							heading.setText("Not getting location!!!Please try Again");
						e.printStackTrace();
						
						}
					
						// \n is for new line
						//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
					}else{
						// can't get location
						// GPS or Network is not enabled
						// Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					
					}
					
					try
					{					
					double ulat = Double.parseDouble(lat);
					double ulon = Double.parseDouble(longi);
					LatLng	origin = new LatLng(ulat,ulon);
					MarkerOptions    options = new MarkerOptions();
					options.position(origin);					
					map.moveCamera( CameraUpdateFactory.newLatLngZoom(origin, (float) 16.0) ); 
					Bitmap icon = BitmapFactory.decodeResource(Home.this.getResources(),
			                R.drawable.pin);
					Bitmap bhalfsize=Bitmap.createScaledBitmap(icon, icon.getWidth()/6,icon.getHeight()/6, false);
					options.icon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
					options.title(strReturnedAddress.toString());					
					map.addMarker(options);
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
					
					Toast.makeText(Home.this, "Sorry! Network Error",Toast.LENGTH_LONG).show();
					break;
				case CurrentLocationThread.NO_NETWORK_CONNECTION:		
					Toast.makeText(Home.this, "Sorry! Network Error",Toast.LENGTH_LONG).show();	
					
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		public void save(View v)
		{
			
			String note = alarmnote.getText().toString().trim();	
			Toast.makeText(Home.this, lat+"   "+longi+"  "+note,Toast.LENGTH_LONG).show();	
			LocationDatabase dbObj=new LocationDatabase(getApplicationContext()); 			
			dbObj.insertInToLocationTable(lat,longi ,note);
			dbObj.close();
			Toast.makeText(Home.this, "Values are saved",Toast.LENGTH_LONG).show();	
		}
}
