package com.voc4u.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class SettingItemView extends LinearLayout implements OnItemSelectedListener, OnSeekBarChangeListener
{

	private WordController mWordCtrl;
	private String mTitle;
	private int mIdFrom;
	private int mIdTo;
	
//	private LangSpinnerItem[] langSpinnerItemArray = new LangSpinnerItem[] {   
//            new LangSpinnerItem( LangType.CZECH_2_ENG, this.getContext() ), 
//            new LangSpinnerItem( LangType.ENG_2_CZECH, this.getContext() )
//            };
	//private final  SeekBar mSeeekBar;
private final  Button	btnAddWord;
	
	
	public SettingItemView(Context context)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.word_setting_setting_item, this);

		
		//ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this.getContext(),
		//          android.R.layout.simple_dropdown_item_1line, langSpinnerItemArray);
		
		//mSpinner = new Spinner(this, MODE_DROPDOWN);
		
//		mSeeekBar = (SeekBar)findViewById(R.id.seekbar);
//		mSeeekBar.setMax(Consts.MAX_WORD_NATIVE_LEARN);
//		mSeeekBar.setOnSeekBarChangeListener(this);
		
		btnAddWord = (Button)findViewById(R.id.btnAddWord);
		btnAddWord.setVisibility(CommonSetting.DEBUG ? View.VISIBLE : View.GONE);
		
		setup();
	}

	public void setup()
	{
		int n = 0;
//		for(LangSpinnerItem lsi : langSpinnerItemArray)
//		{
//			
//			if(lsi.getLangType() == CommonSetting.langType)
//			{
//				mSpinner.setSelection(n);
//				break;
//			}
//			n++;
//		}
//		
		//mSeeekBar.setProgress(CommonSetting.langNativeNum);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
		//CommonSetting.langType = ((LangSpinnerItem)o).getLangType();
		//CommonSetting.langType = langSpinnerItemArray[i].getLangType();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		//CommonSetting.langNativeNum = mSeeekBar.getProgress();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
	
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		
	}
}
