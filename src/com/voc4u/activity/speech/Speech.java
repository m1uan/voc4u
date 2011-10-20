package com.voc4u.activity.speech;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.train.LastListAdapter;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class Speech extends BaseWordActivity implements OnClickListener
{

	private static final int MAX_REPEAT = 2;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 10;
	private ListView lvLastItems;
	private PublicWord mPublicWord;
	private int mRepeat;
	private static LastListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		lvLastItems = (ListView) findViewById(R.id.lastList);

		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		

		
		mPublicWord = null;
	}

	@Override
	protected int getContentView()
	{
		return R.layout.speech;
	}


	@Override
	public void onResumeSuccess()
	{
		super.onResumeSuccess();
	
		if(mPublicWord == null)
		{
			mRepeat = 0;
			mPublicWord = mWCtrl.getFirstPublicWord();
			startVoiceRecognitionActivity();
		}
		
		mListAdapter = new LastListAdapter(this);
		lvLastItems.setAdapter(mListAdapter);
	}
	
	@Override
	public void onClick(View v)
	{
		startVoiceRecognitionActivity();
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				mPublicWord.getSecondary());
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				// Fill the list view with the strings the recognizer thought it
				// could have heard
				ArrayList<String> matches = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				final String word = mPublicWord.getPrimary();
				
				if(isRecognised(matches) || mRepeat++ == MAX_REPEAT)
				{
					
					mWCtrl.updatePublicWord(isRecognised(matches));
					mRepeat = 0;
					mPublicWord = mWCtrl.getFirstPublicWord();
					mListAdapter = new LastListAdapter(this);
					lvLastItems.setAdapter(mListAdapter);
				}
				
				playCurrentWord(word);
				
				
				startVoiceRecognitionActivity();
			}
			else if(resultCode == RESULT_CANCELED)
				finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void playCurrentWord(String word)
	{
		onPlay(word);
		try
		{
			Thread.sleep(1000L);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isRecognised(ArrayList<String> matches)
	{
		return (matches.size() > 0 && matches.get(0).contentEquals(mPublicWord.getPrimary()));
	}

}