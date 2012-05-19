package com.voc4u.activity.speaker;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.train.LastListAdapter;
import com.voc4u.controller.PublicWord;

public class Speaker extends BaseWordActivity implements OnTouchListener
{
	private static final int REPEAT_TIMEMILIS = 7000;
	private Button mDontKnowButton;
	private TextView txtActual;
	private ListView lvLastItems;
	
	private int mNum;
	private Button mKnowButton;
	
	SService mService = null;
	//private Button btnShowHelp;
	private PublicWord mActualPW;
	private Intent mIService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDontKnowButton = (Button) findViewById(R.id.dontKnowButton);
		
		
		mKnowButton = (Button) findViewById(R.id.nextButton);
		mKnowButton.setEnabled(false);
		mKnowButton.setVisibility(View.GONE);
		
		txtActual = (TextView) findViewById(R.id.wordTextView);
		txtActual.setOnTouchListener(this);
		visibleSpokenWord(false);
		
		lvLastItems = (ListView) findViewById(R.id.lastList);
		lvLastItems.setAdapter(new LastListAdapter(this));

		View nextBtnLayer = findViewById(R.id.nextButtonLayout);
		if(nextBtnLayer != null)
			nextBtnLayer.setVisibility(View.GONE);
		//btnShowHelp = (Button) findViewById(R.id.btnHelp);
		
		
		mNum = 0;
		
		SService.setMainActivity(this);
	}

	
	@Override
	protected int getContentView()
	{
		return R.layout.train;
	}

	public void onBtnHelp(View v)
	{
		visibleSpokenWord(true);
	}
	
	@Override
	public void onResumeSuccess() 
	{
		super.onResumeSuccess();
		
		onDontKnowButton(null);
		
		mIService = new Intent(this, SService.class);
		startService(mIService);
		mDontKnowButton.setText(R.string.btn_speaker_repeat);
	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
		onNextButton(null);
	}
	
	public void onDontKnowButton(View v)
	{
		mActualPW = mWCtrl.getActualPublicWord();
		if(mActualPW == null)
			mActualPW = mWCtrl.getFirstPublicWord();
		
		if(mActualPW != null)
			onPlay(mActualPW.getLern());
	}

	public void onNextInUiThread(boolean getFirst)
	{
		if(getFirst)
			mActualPW = mWCtrl.getFirstPublicWord();
		else
			mActualPW = mWCtrl.getActualPublicWord();
		
		onPlay(mActualPW.getLern());
		
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				setupUI(false);
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
			onPlay(mActualPW.getLern());
		setupUI(true);
		
		
		// lvLastItems.setAdapter(new LastListAdapter(this));
		
		
	}

	private void setupUI(boolean visibleShowWord)
	{
		//txtActual.setText(mActualPW.getLern());
		visibleSpokenWord(visibleShowWord);
		lvLastItems.invalidateViews();
	}

	private void visibleSpokenWord(boolean visible)
	{	
		String str = visible ? 
				mActualPW.getLern()
				: getResources().getString(R.string.tap_for_display);
		txtActual.setText(str);
		txtActual.setClickable(!visible);
	}

	public void onNextButton(View v)
	{
		//if (mHandler != null)
		//	mHandler.removeCallbacks(mUpdateTimeTask);
		//mService.stop();
		//mDontKnowButton.setText(R.string.btnStart);
		//mDontKnowButton.setEnabled(true);
		mKnowButton.setEnabled(false);
		
		if(mIService != null)
			stopService(mIService);
		//if(mService != null)
		//	mService.stop();
	}
	
	@Override
	public void onDestroy()
	{
		if (mMPMetrics != null) {
			try {
				JSONObject properties = new JSONObject();
				properties.put("time_in_speaker", getTotalTime());
				mMPMetrics.track("Speaker", null);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mIService != null)
			stopService(mIService);
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		if(v == txtActual)
			visibleSpokenWord(true);
		return false;
	}
	
	@Override
	public void doRedrawList()
	{
		lvLastItems.invalidateViews();
	}
}
