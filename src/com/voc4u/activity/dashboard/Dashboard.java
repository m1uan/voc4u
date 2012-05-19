package com.voc4u.activity.dashboard;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.activity.dictionary.Dictionary;
import com.voc4u.activity.lessons.Lessons;
import com.voc4u.activity.listener.Listener;
import com.voc4u.activity.speaker.Speaker;
import com.voc4u.activity.train.Train;
import com.voc4u.activity.words.Words;
import com.voc4u.controller.DictionaryOpenHelper.NUM_WORDS_TYPE;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;


public class Dashboard extends BaseActivity
{
	Button btnTrain;
	Button btnListen;
	Button btnSpeech;
	View vLogo;
	View vInfoPanel;
	private long numKnow;
	private long numTotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		CommonSetting.restore(this);
		
//		Intent train = new Intent(this, Train.class);
//		startActivity(train);
		
		btnTrain = (Button)findViewById(R.id.trainButton);
		btnListen = (Button)findViewById(R.id.speakerButton);
		btnSpeech = (Button)findViewById(R.id.speechButton);
		vLogo = findViewById(R.id.logo);
		vInfoPanel = findViewById(R.id.infoPanel);
		
		
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
		numKnow = wc.getNumWordsInDB(NUM_WORDS_TYPE.KNOWS);
		numTotal = wc.count();
		
		numow.setText(String.valueOf(numKnow) + " / " + String.valueOf(numTotal));
		numow.setVisibility(View.VISIBLE);
		
		
		btnTrain.setVisibility(View.VISIBLE);
		btnListen.setVisibility(View.VISIBLE);
		btnSpeech.setVisibility(View.VISIBLE);
		vLogo.setVisibility(View.VISIBLE);
		vInfoPanel.setVisibility(View.VISIBLE);
		
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain);
		btnTrain.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboard_listen);
		btnListen.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech);
		btnSpeech.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.train_list);
		vLogo.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain);
		vInfoPanel.startAnimation(animation);
		//btnTrain.setVisibility(View.VISIBLE);
		
		super.onResumeSuccess();
	}
	
	public void onTrainButton(View view)
	{
		final Intent it = new Intent(this, Train.class);
		outgoingAnimation(it);
	}

	private void outgoingAnimation(final Intent it) {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain_r);
		btnTrain.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboard_listen_r);
		btnListen.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech_r);
		btnSpeech.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.train_list_r);
		vLogo.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain_r);
		vInfoPanel.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				btnTrain.setVisibility(View.INVISIBLE);
				btnListen.setVisibility(View.INVISIBLE);
				btnSpeech.setVisibility(View.INVISIBLE);
				vLogo.setVisibility(View.INVISIBLE);
				vInfoPanel.setVisibility(View.INVISIBLE);
				// TODO Auto-generated method stub
				startActivity(it);
				overridePendingTransition(R.anim.fadeout, R.anim.fadein);
			}
		});
	}
	
	public void onSpeakerButton(View view)
	{
		//Intent it = new Intent(this, Words.class);
		Intent it = new Intent(this, Speaker.class);
		outgoingAnimation(it);
	}
	
	public void onSpeechButton(View view)
	{
		Intent it = new Intent(this, Listener.class);
		outgoingAnimation(it);
	}
	
	public void onSettingButton(View view)
	{
		Intent it = new Intent(this, Lessons.class);
		outgoingAnimation(it);
	}
	
	@Override
	protected String GetShowInfoType()
	{
		return DialogInfo.TYPE_DASHBOARD;
	}
	
	
	@Override
	protected void onDestroy() {
		try {
			JSONObject properties = new JSONObject();
			properties.put("know", numKnow);
			properties.put("total", numTotal);
			mMPMetrics.track("Dashboard_leaving", properties);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
