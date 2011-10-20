package com.voc4u.activity.dashboard;


import com.voc4u.activity.init.Init;
import com.voc4u.activity.setting.WordSetting;
import com.voc4u.activity.speaker.Speaker;
import com.voc4u.activity.speech.Speech;
import com.voc4u.activity.train.Train;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		CommonSetting.restore(this);
		
		Intent train = new Intent(this, Train.class);
		startActivity(train);
	}
	
	public void onTrainButton(View view)
	{
		Intent it = new Intent(this, Train.class);
		startActivity(it);
	}
	
	public void onSpeakerButton(View view)
	{
		Intent it = new Intent(this, Speaker.class);
		startActivity(it);
	}
	
	public void onSpeechButton(View view)
	{
		Intent it = new Intent(this, Speech.class);
		startActivity(it);
	}
	
	public void onSettingButton(View view)
	{
		Intent it = new Intent(this, WordSetting.class);
		startActivity(it);
	}
}
