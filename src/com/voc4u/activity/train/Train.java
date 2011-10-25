package com.voc4u.activity.train;

import junit.framework.Assert;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.voc4u.activity.BaseWordActivity;
import com.voc4u.controller.PublicWord;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class Train extends BaseWordActivity implements OnItemClickListener
{
	public PublicWord mPublicWord;
	private TextView mWord2TextView;
	private Button mDontKnowButton;

	// if true set word as known
	private final boolean mKnowetIt = true;
	private String TAG;
	private int mAppWidgetId;
	private ListView lvLastItems;
	private static LastListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mWord2TextView = (TextView) findViewById(R.id.word2TextView);
		mDontKnowButton = (Button) findViewById(R.id.dontKnowButton);

		//if(mWCtrl != null && mWCtrl.count() > 0)
		//	setupFirstWord();
		
		mPublicWord = null;
		lvLastItems = (ListView) findViewById(R.id.lastList);
		lvLastItems.setOnItemClickListener(this);
		
		
		
	}
	
	@Override
	public void onResumeSuccess()
	{
		if(mPublicWord == null)
			setupFirstWord();
		
		mListAdapter = new LastListAdapter(this);
		lvLastItems.setAdapter(mListAdapter);
		
		super.onResumeSuccess();
	}

	private void setupFirstWord()
	{
		Assert.assertTrue(mWCtrl != null);
		// load new public word
		if (mWCtrl != null)
			mPublicWord = mWCtrl.getFirstPublicWord();
		
		//Assert.assertTrue(mPublicWord != null);
		if (mPublicWord != null)
		{
			TextView et = (TextView) findViewById(R.id.wordTextView);
			et.setText(mPublicWord.getTestString());

			
			mWord2TextView.setText(mPublicWord.getBaseWord().getWeight() + "/"
					+ mPublicWord.getBaseWord().getWeight2());
			mWord2TextView.setVisibility(View.VISIBLE);
			mWord2TextView.setVisibility(CommonSetting.DEBUG ? View.VISIBLE
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
		onPlay(mPublicWord.getPrimary());
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
		// TODO: no finish() this activity - use the same for show new word
		mWCtrl.getActualPublicWord();
		Intent intent = new Intent(this, Train.class);
		startActivity(intent);
		finish();

		mWCtrl.updatePublicWord(know);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3)
	{
		PublicWord pw = (PublicWord) lvLastItems.getItemAtPosition(position);
		onPlay(pw.getPrimary());
	}

	@Override
	protected int getContentView()
	{
		return R.layout.train;
	}
}
