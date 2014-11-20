package com.examplesss.naozhong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "data.db";
	
	public DbHelper(Context context){
		super(context, DB_NAME, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		String sql = "create table config(key varchar(50) not null, value varchar(50) not null);";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
	
}
