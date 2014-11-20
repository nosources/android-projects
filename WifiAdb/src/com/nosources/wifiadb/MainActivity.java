package com.nosources.wifiadb;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	private String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(p.getOutputStream());
			dis = new DataInputStream(p.getInputStream());
			dos.writeBytes("setprop service.adb.tcp.port 5555\n");
			dos.flush();
			dos.writeBytes("stop adbd\n");
			dos.flush();
			dos.writeBytes("start adbd\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while((line = dis.readLine()) != null){
				result += line + "\n";
			}
			p.waitFor();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (dos != null) {  
                try {  
                    dos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (dis != null) {  
                try {  
                    dis.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            } 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
