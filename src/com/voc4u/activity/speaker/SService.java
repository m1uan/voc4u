package com.voc4u.activity.speaker;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SService extends Service {
	private static final int PERIOD=10000;
	private static Speaker mSpeaker;
	static long i=0;

	private final Timer timer = new Timer();

	@Override
	public void onCreate() 
	{
		super.onCreate();
		start();
	}

	public static void setMainActivity(Speaker activity) {
		mSpeaker = activity;
		//YOS_CALL.t.setText("started!!");
		//_startService();

		//SystemClock.sleep(PERIOD);
	}

	public void start()
	{
		i = 0;
		_startService();
	}
	
	public void stop()
	{
		timer.cancel();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		i = 0;
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return(null);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		stop();
	}

	
	private void _startService() 
	{
		i = 0;
		  timer.scheduleAtFixedRate(
		      new TimerTask() {
		        public void run() {
		        	mSpeaker.onNextInUiThread(i > 0);
		        	
		        	if(i++ > 100)
		        	{
		        		//mSpeaker.onNextButtonUI();
		        		//SService.this.stopSelf();
		        	}
		        }
		      },0, PERIOD);
		}


}
