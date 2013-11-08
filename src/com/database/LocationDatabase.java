package com.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDatabase extends SQLiteOpenHelper
{

	private static String DB_NAME= "locationDb";
	private static int DB_VERSION= 1;
	private SQLiteDatabase locationDb;
	private String Key="mykey";	

	
	String note[] = null;
	String lat[] = null;
	String log[] = null;
	

	/************** Encryption Key ******************/
	public LocationDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	public LocationDatabase(Context context) { 
		super(context, DB_NAME, null, DB_VERSION);
		locationDb=this.getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {		
      
		db.execSQL("CREATE TABLE IF NOT EXISTS location(lat text,log text,note text)");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LocationDatabase.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS location");
		
		onCreate(db);
		
	}
//............................Video Tables.......................

public void insertInToLocationTable(String lat,String log,String note){
	
	
    String query="insert into location(lat,log,note) values ('"+lat+"','"+log+"','"+note+"')";			
    locationDb.execSQL(query);
	 System.out.println("inserted locationTable");
}

public void DeleteFromLocation(String lat,String log){
	
    locationDb.execSQL("delete from location where lat= '"+lat+"' and log= '"+log+"'" );
	 System.out.println("deleted location table");
}

public void getMainLocation()
{
Cursor cursor;
int noOfItems=0;

cursor = locationDb.rawQuery("select  lat,log,note from location ",null);

cursor.moveToFirst();
noOfItems=cursor.getCount();
System.out.println("nmbr of items = "+cursor.getCount());

if(cursor.getCount()>0)
{
	lat=new String[noOfItems];
	log=new String[noOfItems];
	note=new String[noOfItems];
}
for(int i=0;i<noOfItems;i++)
{
	lat[i]=cursor.getString(0);
	log[i]=cursor.getString(1);
	note[i]=cursor.getString(2);
	cursor.moveToNext();
}
for(int i=0;i<noOfItems;i++)
{
System.out.println("000lat===="+lat[i]);
System.out.println("000log===="+log[i]);
System.out.println("000note===="+note[i]);

}

cursor.close();

}
public String[] getlat()
{

return lat;
}

public String[] getlog()
{
return log;
}

public String[] getnote()
{
return note;
}



}