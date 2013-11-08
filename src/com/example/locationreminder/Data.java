package com.example.locationreminder;



import java.util.List;
import java.util.Locale;

import com.database.LocationDatabase;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Data extends Activity {
TextView data;

String [] db_lat;
String [] db_log;
String [] db_note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listlocation);
		data= (TextView)findViewById(R.id.data);
		LocationDatabase dbObj=new LocationDatabase(getApplicationContext());
		dbObj.getMainLocation();
//		db_lat = new String[20];
//		db_log = new String[20];
//		db_note = new String[20];//		
		
		db_lat=dbObj.getlat();
		db_log=dbObj.getlog();
		db_note=dbObj.getnote();
		
		data .setText("Location"+"       "+"Note"+"\n");
		Geocoder gcd = new Geocoder(Data.this, Locale.getDefault());
		List<Address> addresses;
		try {
			for(int i=0;i<db_lat.length;i++)
			{
			addresses = gcd.getFromLocation(Double.parseDouble(db_lat[i]), Double.parseDouble(db_log[i]), 1);
			
			if (addresses.size() > 0) 
			{
				data .append(addresses.get(0).getLocality()+ "        "+db_note[i]+"\n");	
			}
			}	
			
				
		
		
		}
		catch(Exception e)
		{
			
		}
		
		
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
