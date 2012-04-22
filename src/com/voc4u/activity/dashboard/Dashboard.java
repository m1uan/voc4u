package com.voc4u.activity.dashboard;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.activity.dictionary.Dictionary;
import com.voc4u.activity.listener.Listener;
import com.voc4u.activity.speaker.Speaker;
import com.voc4u.activity.train.Train;
import com.voc4u.activity.words.Words;
import com.voc4u.controller.DictionaryOpenHelper.NUM_WORDS_TYPE;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;

public class Dashboard extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		CommonSetting.restore(this);
		
//		Intent train = new Intent(this, Train.class);
//		startActivity(train);
	}
	
	@Override
	public void onResumeSuccess() 
	{
		TextView desc = (TextView)findViewById(R.id.numOfWordsDesc);
		TextView numow = (TextView)findViewById(R.id.numOfWords);
		
		Resources res = getResources();
		String know = res.getString(R.string.btn_know);
		String dontknow = res.getString(R.string.btn_dontknow);
		know = know + " / " + dontknow;
		
		desc.setText(know);
		desc.setVisibility(View.VISIBLE);
		
		WordController wc = WordController.getInstance(this);
		long numKnow = wc.getNumWordsInDB(NUM_WORDS_TYPE.KNOWS);
		long numAll = wc.count();
		
		numow.setText(String.valueOf(numKnow) + " / " + String.valueOf(numAll));
		numow.setVisibility(View.VISIBLE);
		
		super.onResumeSuccess();
	}
	
	public void onTrainButton(View view)
	{
		Intent it = new Intent(this, Train.class);
		startActivity(it);
	}
	
	public void onSpeakerButton(View view)
	{
		//Intent it = new Intent(this, Words.class);
		Intent it = new Intent(this, Speaker.class);
		startActivity(it);
	}
	
	public void onSpeechButton(View view)
	{
		Intent it = new Intent(this, Listener.class);
		startActivity(it);
	}
	
	public void onSettingButton(View view)
	{
		Intent it = new Intent(this, Dictionary.class);
		startActivity(it);
	}
	
	@Override
	protected String GetShowInfoType()
	{
		return DialogInfo.TYPE_DASHBOARD;
	}
}
