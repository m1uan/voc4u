package com.voc4u.activity.train;

import junit.framework.Assert;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.voc4u.R;
import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.Word;
import com.voc4u.setting.Consts;

public class Train extends BaseWordActivity implements OnItemClickListener
{
	public PublicWord mPublicWord;
	private TextView mWord2TextView;
	private Button mDontKnowButton;
	private View vWord;
	private View vLogo;
	private ImageView ivFlag;

	// if true set word as known
	private final boolean mKnowetIt = true;
	private String TAG;
	private int mAppWidgetId;
	private ListView lvLastItems;
	private View mKnowButton;
	private TextView tvTestWord;
	private static LastListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mWord2TextView = (TextView) findViewById(R.id.word2TextView);
		mDontKnowButton = (Button) findViewById(R.id.dontKnowButton);
		mKnowButton = findViewById(R.id.nextButtonLayout);
		//if(mWCtrl != null && mWCtrl.count() > 0)
		//	setupFirstWord();
		
		mPublicWord = null;
		lvLastItems = (ListView) findViewById(R.id.lastList);
		lvLastItems.setOnItemClickListener(this);
		
		vWord = (View)findViewById(R.id.word);
		vLogo = (View)findViewById(R.id.logo);
		
		ivFlag = (ImageView)findViewById(R.id.flag);
		tvTestWord = (TextView) findViewById(R.id.wordTextView);
		registerForContextMenu(lvLastItems);
	}
	
	@Override
	public void onResumeSuccess()
	{
		mListAdapter = new LastListAdapter(this);
		lvLastItems.setAdapter(mListAdapter);
		
		if(mPublicWord == null)
			setupFirstWord(false);
			
		
		super.onResumeSuccess();
		
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_listen);
		mDontKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech);
		mKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain);
		vWord.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.train_list);
		vLogo.startAnimation(anim);
		lvLastItems.startAnimation(anim);
	}

	private void setupFirstWord(boolean loadNew)
	{
		
		// load new public word
		if (mWCtrl != null)
		{
			if(loadNew)
				mPublicWord = mWCtrl.getFirstPublicWord();
			else
				mPublicWord = mWCtrl.getActualPublicWord();
		}
		
		//Assert.assertTrue(mPublicWord != null);
		if (mPublicWord != null)
		{
			
			tvTestWord.setText(mPublicWord.getTestString());

			
			mWord2TextView.setText(mPublicWord.getBaseWord().getWeight() + "/"
					+ mPublicWord.getBaseWord().getWeight2());
			//mWord2TextView.setVisibility(View.VISIBLE);
			mWord2TextView.setVisibility(Consts.DEBUG ? View.VISIBLE
					: View.GONE);
			
			ivFlag.setImageResource(mPublicWord.getTestingFlag(this));
		}
	}

	@Override
	protected void onResume()
	{

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_listen_r);
		mDontKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech_r);
		mKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain_r);
		anim = AnimationUtils.loadAnimation(this, R.anim.train_list_r);
		vLogo.startAnimation(anim);
		lvLastItems.startAnimation(anim);
		vWord.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mDontKnowButton.setVisibility(View.INVISIBLE);
				mKnowButton.setVisibility(View.INVISIBLE);
				vWord.setVisibility(View.INVISIBLE);
				vLogo.setVisibility(View.INVISIBLE);
				lvLastItems.setVisibility(View.INVISIBLE);
				finish();
				overridePendingTransition(R.anim.fadeout, R.anim.fadein);
			}
		});
	}
	
	@Override
	protected void onPause()
	{
		
		/*AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

		RemoteViews views = new RemoteViews(this.getPackageName(),
				R.layout.main);

		TrainWidget.setupActualWord(views, WordController.getInstance(this)
				.getActualPublicWord());
		appWidgetManager.updateAppWidget(mAppWidgetId, views);

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);*/
		super.onPause();
	}

	public void onPlayButton(View v)
	{
		onPlay(mPublicWord.getLern());
	}

	public void onDontKnowButton(View v)
	{
		updateWord(false);
	}

	public void onNextButton(View v)
	{

		updateWord(true);

	}

	private void updateWord(final boolean know)
	{
		Animation anim = new TranslateAnimation(0,0,0,200);
		anim.setDuration(300);
		Animation anim3 = new TranslateAnimation(0, 0,0,150);
		anim3.setDuration(200);
		
		ivFlag.startAnimation(anim3);
		anim3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				ivFlag.setVisibility(View.INVISIBLE);
			}
		});
		
		//tvTestWord.setTextColor(Color.BLACK);
		tvTestWord.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				lvLastItems.invalidateViews();
				
				tvTestWord.setVisibility(View.INVISIBLE);
				mWCtrl.updatePublicWord(know);
				setupFirstWord(true);
				Animation anim2 = new TranslateAnimation(300,0,0,0);
				anim2.setDuration(300);
				
				tvTestWord.setAnimation(anim2);
				tvTestWord.setVisibility(View.VISIBLE);
				
				
				
				Animation anim4 = new TranslateAnimation(-300,0,0,0);
				anim4.setDuration(300);
				ivFlag.startAnimation(anim4);
				ivFlag.setVisibility(View.VISIBLE);
				
			}
		});
		
		
		
		
		//mListAdapter = new LastListAdapter(this);
		
		//lvLastItems.setAdapter(mListAdapter);

		
//		Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
//		anim.setDuration(500);
//		LastItem v = (LastItem)mListAdapter.getLastView();
//		if(v != null) {
//		v.startAnimation(anim );
//
//			new Handler().postDelayed(new Runnable() {
//
//					public void run() {
//
//					}
//
//				}, anim.getDuration());
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3)
	{
		PublicWord pw = (PublicWord) lvLastItems.getItemAtPosition(position);
		onPlay(pw.getLern());
	}

	@Override
	protected int getContentView()
	{
		return R.layout.train;
	}
	
	@Override
	protected String GetShowInfoType()
	{
		return DialogInfo.TYPE_TRAIN;
	}
	
	@Override
	public void doRedrawList()
	{
		lvLastItems.invalidateViews();
	}
	

}
