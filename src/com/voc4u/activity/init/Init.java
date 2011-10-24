package com.voc4u.activity.init;

import com.voc4u.activity.dashboard.Dashboard;
import com.voc4u.activity.setting.WordSetting;
import com.voc4u.activity.speaker.Speaker;
import com.voc4u.activity.train.Train;
import com.voc4u.controller.WordController;
import com.voc4u.controller.updateLisener;
import com.voc4u.core.InitData;
import com.voc4u.core.LangSetting;
import com.voc4u.czen1.R;
import com.voc4u.lang.DataCS;
import com.voc4u.lang.DataDE;
import com.voc4u.lang.DataEN;
import com.voc4u.lang.DataES;
import com.voc4u.lang.DataFR;
import com.voc4u.lang.DataPL;
import com.voc4u.lang.DataPT;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangType;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Init extends Activity implements OnItemSelectedListener, updateLisener
{
	private int mIndex;
	protected int mLern;
	private LangType[] lernLangType;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		CommonSetting.restore(this);

		fillNative();
		//fillLern();
	}

	private void fillNative()
	{
		Spinner nat = (Spinner) findViewById(R.id.spnNative);
		nat.setOnItemSelectedListener(this);
		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_dropdown_item_1line,
				LangSetting.getLangTextArray(this));
		nat.setAdapter(spinnerArrayAdapter);
		nat.setSelection(0, false);
	}

	public void onStart(View view)
	{
		CommonSetting.store(this);
		CommonSetting.restore(this);

//		ProgressDialog dialog = ProgressDialog.show(this, "",
//				"Loading. Please wait...", true);
//		dialog.show();
//		WordController.getInstance(this).initLesson(this);
		//new DownloadFilesTask().execute("");
		finish();
	}

	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int index,
			long arg3)
	{
		CommonSetting.nativeCode = LangSetting.getLangArray()[index];
		fillLern();
	}

	private void fillLern()
	{
		Spinner nat = (Spinner) findViewById(R.id.spnLern);
		nat.setOnItemSelectedListener(this);

		
		
		lernLangType = createArrayWithoutSelectedInNative();

		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_dropdown_item_1line, lernLangType);
		nat.setAdapter(spinnerArrayAdapter);
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
		final LangType[] all = LangSetting.getLangTextArray(this);
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

}
