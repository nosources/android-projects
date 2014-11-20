package com.examplesss.naozhong;


import java.util.Map;
import java.util.Set;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmRestoreOnBoot extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) 
	{
		final String pluginName = "JK_ANE_LocalNotification";
		Log.d("AlarmRestoreOnBoot:", "Class name:");
		Toast.makeText(context, "heheheheheh", 20000).show();
		Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{100,4000,3000,400}, -1);;
	}
}
