package com.voc4u.activity;

import java.util.Locale;

import junit.framework.Assert;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.voc4u.R;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;

public abstract class BaseWordActivity extends BaseActivity implements OnInitListener
{
	private static final String	TAG	= "VOC4UBaseWordActivity";
	protected WordController	mWCtrl;
	protected TextToSpeech		mTts;
	private MenuItem			mMenuHomeId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getContentView());

		CommonSetting.restore(this);

		mWCtrl = WordController.getInstance(this);
		mTts = new TextToSpeech(this, this);
	}

	protected abstract int getContentView();





	@Override
	public void onInit(int status)
	{
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS)
		{
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = mTts.setLanguage(Locale.US);
			// Try this someday for some interesting results.
			// int result mTts.setLanguage(Locale.FRANCE);
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
			}
			else
			{
				// Check the documentation for other possible result codes.
				// For example, the language may be available for the locale,
				// but not for the specified country and variant.

				// The TTS engine has been successfully initialized.
				// Allow the user to press the button for the app to speak
				// again.
				// mAgainButton.setEnabled(true);
				// Greet the user.
				// sayHello();
			}
		}
		else
		{
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		mMenuHomeId = menu.add(R.string.menuHome).setOnMenuItemClickListener(this);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onDestroy()
	{
		// Don't forget to shutdown!
		if (mTts != null)
		{
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
			mTts.speak(text, TextToSpeech.QUEUE_FLUSH, // Drop all pending
														// entries in the
														// playback queue.
			null);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		if (item == mMenuHomeId)
		{
			finish();
			return true;
		}
		else
			return super.onMenuItemClick(item);
	}
}
