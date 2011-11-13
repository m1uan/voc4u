package com.voc4u.activity.init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.dictionary.Dictionary;
import com.voc4u.controller.updateLisener;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangSetting;
import com.voc4u.setting.LangType;

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

		Intent intent = new Intent(this, Dictionary.class);
		intent.putExtra(BaseActivity.FROM_INIT, BaseActivity.FROM_INIT);
		startActivity(intent);
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

}
