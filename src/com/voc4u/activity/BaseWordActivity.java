package com.voc4u.activity;

import java.util.Locale;

import junit.framework.Assert;

import com.voc4u.activity.init.Init;
import com.voc4u.activity.setting.WordSetting;
import com.voc4u.activity.train.Train;
import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public abstract class BaseWordActivity extends Activity implements OnInitListener, OnMenuItemClickListener
{
	private static final String TAG = "VOC4UBaseWordActivity";
	protected WordController mWCtrl;
	protected TextToSpeech mTts;
	private MenuItem mMenuHomeId;
	private MenuItem mMenuDictionary;
	private MenuItem mSpeachSetting;

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		
		CommonSetting.restore(this);
		
		mWCtrl = WordController.getInstance(this);
		mTts = new TextToSpeech(this, this );
	}
	
	
	protected abstract int getContentView();

	
	public void onResumeSuccess()
	{
		
	}

	@Override
	protected void onResume()
	{
		if(mWCtrl.count() < 1)
		{
			Intent init = new Intent(this, Init.class);
			startActivity(init);
		}
		else if(mWCtrl.getFirstPublicWord() == null)
		{
			onShowMenu();
		}
		else
			onResumeSuccess();
		
		super.onResume();
	}
	
	
	@Override
	public void onInit(int status)
	{
		 // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
                //mAgainButton.setEnabled(true);
                // Greet the user.
                //sayHello();
            }
        } else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
		
	} 
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
		mMenuHomeId = menu.add(R.string.menuHome).setOnMenuItemClickListener(this);
    	mMenuDictionary = menu.add(R.string.menuDictionary).setOnMenuItemClickListener(this);
    	mSpeachSetting = menu.add(R.string.menuSettingSpeech).setOnMenuItemClickListener(this);
    	
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    private void onShowSpeechMenu()
	{
    	ComponentName componentToLaunch = new ComponentName(
    	        "com.android.settings",
    	        "com.android.settings.TextToSpeechSettings");
    	Intent intent = new Intent();
    	intent.addCategory(Intent.CATEGORY_LAUNCHER);
    	intent.setComponent(componentToLaunch);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
	}
    
    
    public void onShowMenu()
	{
		Intent intent = new Intent(this, WordSetting.class);
		startActivity(intent);
	}
    
	@Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }

        super.onDestroy();
    }

	
	protected void onPlay(String text)
	{
		Assert.assertTrue(mTts != null && text != null && text.length() > 0);
		if (mTts != null && text != null && text.length() > 0)
		{
			mTts.speak(text,
		            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		            null);
		}
	}
	
	
	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		if(item == mMenuDictionary)
			onShowMenu();
		else if(item == mMenuHomeId)
			finish();
		else if(item == mSpeachSetting)
			onShowSpeechMenu();
		else
			return false;
		
		return true;
	}
}
