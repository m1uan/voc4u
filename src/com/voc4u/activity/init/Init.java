package com.voc4u.activity.init;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.activity.dashboard.Dashboard;
import com.voc4u.controller.WordController;
import com.voc4u.controller.updateLisener;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangSetting;
import com.voc4u.setting.LangType;

public class Init extends Activity implements OnItemSelectedListener, updateLisener, OnInitListener
{
	private static final String TAG = "VOC4UInit";
	private int mIndex;
	protected int mLern;
	private LangType[] lernLangType;
	private TextToSpeech mTts = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		CommonSetting.restore(this);

		fillNative();
		//fillLern();
		
		if(!DialogInfo.GetChecked(DialogInfo.TYPE_INIT))
			showDialog(BaseActivity.DIALOG_SHOW_INFO);
	}

	@SuppressWarnings("unchecked")
	private void fillNative()
	{
		Spinner nat = (Spinner) findViewById(R.id.spnNative);
		nat.setOnItemSelectedListener(this);
//		ArrayAdapter spinnerArrayAdapter = new Country(this,
//				android.R.layout.simple_dropdown_item_1line,
//				LangSetting.getLangArray());
		//nat.setAdapter(spinnerArrayAdapter);
		
		CountryAdapter ca = new CountryAdapter(LangSetting.getLangArray());
		nat.setAdapter(ca);
		nat.setSelection(0, false);
	}

	
	class CountryAdapter extends BaseAdapter
	{
		final LangType[] mArray;
		public CountryAdapter(LangType[] array)
		{
			mArray = array;
		}

		@Override
		public int getCount()
		{
			return mArray.length;
		}

		@Override
		public LangType getItem(int position)
		{
			return mArray[position];
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return new CountryItem(Init.this.getApplicationContext(), getItem(position));
		}
		
	}
	
	public void onStart(View view)
	{
		CommonSetting.store(this);
		CommonSetting.restore(this);
		WordController.getInstance(this).enableLessonAsync(1, true, null);
		
		showDialog(BaseActivity.DIALOG_PROGRESS);
		
		
	
		// without sleep is the word setting returned back
		// because isn't load any word between 
		// finish() and resume() new activity
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTts = new TextToSpeech(this, this);
	}

	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int index,
			long arg3)
	{
		CommonSetting.nativeCode = LangSetting.getLangArray()[index];
		fillLern();
	}

	@SuppressWarnings("unchecked")
	private void fillLern()
	{
		Spinner nat = (Spinner) findViewById(R.id.spnLern);
		nat.setOnItemSelectedListener(this);

		
		
		lernLangType = createArrayWithoutSelectedInNative();

//		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
//				android.R.layout.simple_dropdown_item_1line, lernLangType);
//		nat.setAdapter(spinnerArrayAdapter);
		CountryAdapter ca = new CountryAdapter(createArrayWithoutSelectedInNative());
		nat.setAdapter(ca);
		nat.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				CommonSetting.lernCode = LangSetting.getLangTypeFromCode(lernLangType[arg2].code);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	private final LangType[] createArrayWithoutSelectedInNative()
	{
		int m = 0;
		final LangType[] all = LangSetting.getLangArray();
		LangType[] lLT = new LangType[all.length - 1];
		for (int i = 0; i != all.length; i++)
		{
			LangType source = all[i];

			if (source.id == CommonSetting.nativeCode.id)
				continue;

			lLT[m++] = source;
		}
		
		return lLT;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateDone()
	{
		finish();
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		if(id == BaseActivity.DIALOG_SHOW_INFO)
		{
			return DialogInfo.create(this);
		}
		else if(id == BaseActivity.DIALOG_TTS_DATA_MISSING)
		{
			return ShowDialogForTtsSetting(R.string.msg_tts_data_missing);
		}
		else if(id == BaseActivity.DIALOG_PROGRESS)
		{
			return ProgressDialog.show(this, "", getString(R.string.preparing), false, false);
		}
		else
			return super.onCreateDialog(id);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		if(id == BaseActivity.DIALOG_SHOW_INFO)
			DialogInfo.setup(this, DialogInfo.TYPE_INIT, dialog);
		else
			super.onPrepareDialog(id, dialog);
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
				showDashboard();
			}
		});
		dialog = builder.create();
		return dialog;
	}
	
	private void showTtsSetting() 
	{
		ComponentName componentToLaunch = new ComponentName(
				"com.android.settings",
				"com.android.settings.TextToSpeechSettings");
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(componentToLaunch);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		//onShowSpeechMenu();
	}

	@Override
	protected void onResume() 
	{
		// if goes from tts setting
		if(mTts != null)
			showDashboard();
		super.onResume();
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
				showDialog(BaseActivity.DIALOG_TTS_DATA_MISSING);
				//result = mTts.setLanguage(Locale.ENGLISH);
				
			}
			
			if (result == TextToSpeech.LANG_MISSING_DATA)
			{
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
				showDialog(BaseActivity.DIALOG_TTS_DATA_MISSING);
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
				showDashboard();
			}
		}
		else
		{
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
		
		
	}

	private void showDashboard() 
	{
		Intent intent = new Intent(this, Dashboard.class);
		intent.putExtra(BaseActivity.FROM_INIT, BaseActivity.FROM_INIT);
		startActivity(intent);
		finish();
	}

}
