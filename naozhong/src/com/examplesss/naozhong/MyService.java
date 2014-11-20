package com.examplesss.naozhong;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;

import com.examplesss.naozhong.MyAIDLService.Stub;


@SuppressLint("NewApi")
public class MyService extends Service {
	public String tag = "MyService";
	private Timer timer = new Timer();
	private int interval = 60;//it mul 60.
	private int endTime = 0;
//	private MyBinder mBinder = new MyBinder();//this a binder for local service  stub is for remote service
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(tag, "process id is "+Process.myPid());
		Log.d(tag, " on create");
		DbHelper dbhelper = new DbHelper(getApplicationContext());
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		
		Cursor c = db.rawQuery("select * from config where key = ?", new String[]{Constant.INTERVAL});
		if (c.moveToFirst()) {
			interval = c.getInt(c.getColumnIndex(Constant.VALUE));
		}
		endTime = (int)((System.currentTimeMillis() / 1000) + interval * 60);
		timer.schedule(task,1000,1000);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(tag, "on start command");
		Log.d(tag, "endtime is "+endTime);
		return super.onStartCommand(intent, flags, startId);
		
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(tag, "on destroy");
	}
	
	@Override
	public IBinder onBind(Intent intent){
		return mBinder;
	}
	class MyBinder extends Binder{
		public void startDownLoad(){
			Log.d(tag, "start download");
			
		}
	}
	public void showNotification(){
		Notification notification = new Notification(R.drawable.ic_launcher,  
                "NaoZhong", System.currentTimeMillis());  
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,  
                notificationIntent, 0);
        int timeleft = endTime - (int)(System.currentTimeMillis() / 1000);
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("00");
        int h = timeleft/3600;
        int m = (timeleft%3600)/60;
        int s = (timeleft%3600)%60;
        notification.setLatestEventInfo(this, "time left", df.format(h)+":"+df.format(m)+":"+df.format(s),
                pendingIntent);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
	}
	TimerTask task = new TimerTask(){
		@Override
		public void run(){
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
//					Log.d(tag, "run timer task" + System.currentTimeMillis()/1000);
					showNotification();
					if((int)(System.currentTimeMillis()/1000) > (endTime)){
						DbHelper dbhelper = new DbHelper(getApplicationContext());
						SQLiteDatabase db = dbhelper.getReadableDatabase();
						
						Cursor c = db.rawQuery("select * from config where key = ?", new String[]{Constant.LAST});
						long time = 4000;
						if (c.moveToFirst()) {
							time = c.getInt(c.getColumnIndex(Constant.VALUE));
						}
						Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
						vibrator.vibrate(new long[]{100,time,3000,time}, -1);
						endTime = (int)((System.currentTimeMillis() / 1000) + interval * 60);
					}
				}
			});
		}

		private void runOnUiThread(Runnable runnable) {
			// TODO Auto-generated method stub
			runnable.run();
		}
	};
	
	MyAIDLService.Stub mBinder = new Stub(){
		@Override
		public String toUpperCase(String str) throws RemoteException{
			if (str != null) {
				return str.toUpperCase(Locale.getDefault());
			}
			return null;
		}
		
		@Override
		public int plus(int a, int b) throws RemoteException{
			return a + b;
		}
		
		@Override
		public void relaunch() throws RemoteException{
			DbHelper dbhelper = new DbHelper(getApplicationContext());
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			
			Cursor c = db.rawQuery("select * from config where key = ?", new String[]{Constant.INTERVAL});
			if (c.moveToFirst()) {
				interval = c.getInt(c.getColumnIndex(Constant.VALUE));
			}
			endTime = (int)((System.currentTimeMillis() / 1000) + interval * 60);
		}
	};
}
