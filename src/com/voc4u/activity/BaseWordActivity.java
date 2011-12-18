package com.voc4u.activity;

import java.util.Locale;

import junit.framework.Assert;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.voc4u.R;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangSetting;

public abstract class BaseWordActivity extends BaseActivity implements OnInitListener
{
	private static final String	TAG	= "VOC4UBaseWordActivity";
	protected WordController	mWCtrl;
	protected TextToSpeech mTts = null;
	private MenuItem			mMenuHomeId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getContentView());

		mWCtrl = WordController.getInstance(this);
		
	}

	protected abstract int getContentView();


	@Override
	public void onResumeSuccess() 
	{
		super.onResumeSuccess();
		if(mTts == null)
			mTts = new TextToSpeech(this, this);
	}


	@Override
	public void onInit(int status)
	{
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS)
		{
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			Locale loc = CommonSetting.lernCode.toLocale();
			int result = mTts.setLanguage(loc);
			
			if(result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Log.e(TAG, "Language is not available. code: " + loc.getLanguage());
				//showDialog(BaseActivity.DIALOG_TTS_LANGUAGE_MISSING);
				//result = mTts.setLanguage(Locale.ENGLISH);
				
			}
			
			if (result == TextToSpeech.LANG_MISSING_DATA)
			{
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
				//showDialog(BaseActivity.DIALOG_TTS_DATA_MISSING);
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
		mMenuHomeId = menu.add(R.string.btn_menu_home).setOnMenuItemClickListener(this);
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
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		Dialog dialog = null;
		switch(id)
		{
			case BaseActivity.DIALOG_TTS_DATA_MISSING:
			{
				dialog = ShowDialogForTtsSetting(R.string.msg_tts_data_missing);
				break;
			}
			case BaseActivity.DIALOG_TTS_LANGUAGE_MISSING:
				dialog = ShowDialogForTtsSetting(R.string.msg_tts_language_missing);
				break;
			default:
				return super.onCreateDialog(id);
		}
		
		return dialog;
	}

	public Dialog ShowDialogForTtsSetting(int message) 
	{
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.form_dashboard);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(
		this.getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				showTtsSetting();
			}
		}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		dialog = builder.create();
		return dialog;
	}
	
	private void showTtsSetting() 
	{
		getIntent().putExtra("onShowSpeechMenu", true);
		if(mTts != null)
		{
			mTts.shutdown();
			mTts = null;
		}
		onShowSpeechMenu();
	}
}
