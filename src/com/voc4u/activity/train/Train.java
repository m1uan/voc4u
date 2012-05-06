package com.voc4u.activity.train;

import junit.framework.Assert;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
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

	// if true set word as known
	private final boolean mKnowetIt = true;
	private String TAG;
	private int mAppWidgetId;
	private ListView lvLastItems;
	private View mKnowButton;
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
		Assert.assertTrue(mWCtrl != null);
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
			TextView et = (TextView) findViewById(R.id.wordTextView);
			et.setText(mPublicWord.getTestString());

			
			mWord2TextView.setText(mPublicWord.getBaseWord().getWeight() + "/"
					+ mPublicWord.getBaseWord().getWeight2());
			mWord2TextView.setVisibility(View.VISIBLE);
			mWord2TextView.setVisibility(Consts.DEBUG ? View.VISIBLE
					: View.GONE);
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

	private void updateWord(boolean know)
	{
		mWCtrl.updatePublicWord(know);
		
		//mListAdapter = new LastListAdapter(this);
		
		//lvLastItems.setAdapter(mListAdapter);
		lvLastItems.invalidateViews();
		setupFirstWord(true);
		
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
