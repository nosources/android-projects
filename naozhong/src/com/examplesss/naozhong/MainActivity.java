package com.examplesss.naozhong;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
//	private MyService.MyBinder myBinder;
	private MyAIDLService myAIDLService;
	private ServiceConnection connection = new ServiceConnection(){
		@Override
		public void onServiceDisconnected(ComponentName name){
			
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service){
			//this is for local service.
//			myBinder = (MyService.MyBinder)service;
//			myBinder.startDownLoad();
			//this is for remote service.
			myAIDLService = MyAIDLService.Stub.asInterface(service);
			try{
				int result = myAIDLService.plus(3, 3);
				String upperStr = myAIDLService.toUpperCase("hello world");
				Log.d("plus", "result is :"+result);
				Log.d("upper case", "upper case is " + upperStr);
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(new AlarmRestoreOnBoot(), new IntentFilter(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE));
		Parse.initialize(this, "1x5V4YPYrMvoaCpLsIeB2R0I18PBAfCl55G8ypnm", "QsWoxqlbepJmwZPXAVWg0JmuZPoELmE6rSM56eaY");
		ParseAnalytics.trackAppOpened(getIntent());
		
		ParseInstallation.getCurrentInstallation().saveInBackground();
		PushService.subscribe(this, "aadsfadsf", MainActivity.class);
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo2", "bar2");
		testObject.saveInBackground();
		
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		String data = (String)(ParseInstallation.getCurrentInstallation().get("installationId"));
		PushService.subscribe(this, "a2222222222222222333333322222--11", MainActivity.class);
		setContentView(R.layout.activity_main);
		Log.d("main activity", "process id is "+Process.myPid());
		Intent startIntent = new Intent(this,MyService.class);
		startService(startIntent);
		Intent bindIntent = new Intent(this, MyService.class);
		this.bindService(bindIntent, connection, BIND_AUTO_CREATE);
		
		Button relaunch = (Button)findViewById(R.id.relanuch);
		relaunch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					myAIDLService.relaunch();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Button set = (Button)findViewById(R.id.setinterval);
		set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText edit = (EditText)findViewById(R.id.editText2);
				if (!edit.getText().toString().matches("[0-9]+")) {
					Toast.makeText(getApplicationContext(), "😜输入数字",
						     Toast.LENGTH_SHORT).show();
					return;
				}
				DbHelper dbhelper = new DbHelper(getApplicationContext());
				SQLiteDatabase db = dbhelper.getReadableDatabase();
				ContentValues cv = new ContentValues();
				cv.put(Constant.VALUE, edit.getText().toString());
				String whereClause = "key = ?";
				String[] whereArgs = {Constant.INTERVAL};
				db.update("config", cv, whereClause, whereArgs);
				db.close();
			}
		});
		
		Button setvibrate = (Button)findViewById(R.id.setvibrate);
		setvibrate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText edit = (EditText)findViewById(R.id.editText1);
				if (!edit.getText().toString().matches("[0-9]+")) {
					Toast.makeText(getApplicationContext(), "输入数字",
						     Toast.LENGTH_SHORT).show();
					return;
				}
				DbHelper dbhelper = new DbHelper(getApplicationContext());
				SQLiteDatabase db = dbhelper.getReadableDatabase();
				ContentValues cv = new ContentValues();
				cv.put(Constant.VALUE, edit.getText().toString());
				String whereClause = "key = ?";
				String[] whereArgs = {Constant.LAST};
				db.update("config", cv, whereClause, whereArgs);
				db.close();
			}
		});
		
		
		DbHelper dbhelper = new DbHelper(getApplicationContext());
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from config where key = ?", new String[]{Constant.INTERVAL});
		if (!c.moveToFirst()) {
			ContentValues cv = new ContentValues();
			cv.put(Constant.KEY, Constant.INTERVAL);
			cv.put(Constant.VALUE, "90");
			db.insert("config", null, cv);
			ContentValues cv2 = new ContentValues();
			cv2.put(Constant.KEY, Constant.LAST);
			cv2.put(Constant.VALUE, "4000");
			db.insert("config", null, cv2);
			db.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onDestroy(){
		unbindService(connection);
		super.onDestroy();
	}
	

}
