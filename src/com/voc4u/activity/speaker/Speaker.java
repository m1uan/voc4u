package com.voc4u.activity.speaker;

import java.util.Timer;
import java.util.TimerTask;



import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.train.LastListAdapter;
import com.voc4u.controller.PublicWord;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class Speaker extends BaseWordActivity
{
	private static final int REPEAT_TIMEMILIS = 7000;
	private Button mDontKnowButton;
	private TextView txtActual;
	private ListView lvLastItems;
	
	private int mNum;
	private Button mKnowButton;
	
	SService mService = null;
	private Button btnShowHelp;
	private PublicWord mActualPW;
	private Intent mIService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDontKnowButton = (Button) findViewById(R.id.dontKnowButton);
		
		
		mKnowButton = (Button) findViewById(R.id.nextButton);
		mKnowButton.setEnabled(false);
		
		txtActual = (TextView) findViewById(R.id.wordTextView);
		txtActual.setText("");

		lvLastItems = (ListView) findViewById(R.id.lastList);
		lvLastItems.setAdapter(new LastListAdapter(this));

		btnShowHelp = (Button) findViewById(R.id.btnHelp);
		
		
		mNum = 0;
		
		SService.setMainActivity(this);
		//mActualPW = mWCtrl.getFirstPublicWord();
	}

	@Override
	protected int getContentView()
	{
		return R.layout.speaker;
	}

	public void onBtnHelp(View v)
	{
		visibleShowWord(false);
	}
	
	
	public void onDontKnowButton(View v)
	{
		if(mKnowButton.isEnabled())
		{
			onPlay(mActualPW.getPrimary());
			return;
		}
		
		//onNext(true);
		mIService = new Intent(this, SService.class);
		startService(mIService);
		//mService = new SService();
		//mService.start();
		//mHandler.removeCallbacks(mUpdateTimeTask);
		//mHandler.postDelayed(mUpdateTimeTask, REPEAT_TIMEMILIS);
		btnShowHelp.setEnabled(true);
		mDontKnowButton.setText(R.string.btnRepeat);
		mKnowButton.setEnabled(true);
	}

	public void onNextInUiThread()
	{
		mActualPW = mWCtrl.getFirstPublicWord();
		onPlay(mActualPW.getPrimary());
		
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				setupUI(true);
			}
		});
		
	}
	
	public void onNextButtonUI()
	{
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				onNextButton(null);
			}
		});
	}
	
	
	public void onNext(boolean play)
	{
		mActualPW = mWCtrl.getFirstPublicWord();
		if(play)
			onPlay(mActualPW.getPrimary());
		setupUI(true);
		
		
		// lvLastItems.setAdapter(new LastListAdapter(this));
		
		
	}

	private void setupUI(boolean visibleShowWord)
	{
		txtActual.setText(mActualPW.getPrimary());
		visibleShowWord(visibleShowWord);
		lvLastItems.invalidateViews();
	}

	private void visibleShowWord(boolean visible)
	{	
		if(!visible)
			txtActual.setText(mActualPW.getPrimary());
		txtActual.setVisibility(visible ? View.GONE : View.VISIBLE);
		btnShowHelp.setVisibility(visible ? View.VISIBLE : View.GONE );
	}

	public void onNextButton(View v)
	{
		//if (mHandler != null)
		//	mHandler.removeCallbacks(mUpdateTimeTask);
		//mService.stop();
		mDontKnowButton.setText(R.string.btnStart);
		//mDontKnowButton.setEnabled(true);
		mKnowButton.setEnabled(false);
		
		if(mIService != null)
			stopService(mIService);
		//if(mService != null)
		//	mService.stop();
	}
}
