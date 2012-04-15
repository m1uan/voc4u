package com.voc4u.activity.words;

import yuku.iconcontextmenu.IconContextMenu;
import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.activity.BaseWordActivity;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.Word;
import com.voc4u.setting.CommonSetting;

public class WordsItem extends LinearLayout
{
	public static final int DELETE = 3;
	public static final int EDIT = 2;
	public static final int PLAY = 1;
	Word mWord;
	
	protected final TextView tv1;
	protected final TextView tv2;
	protected final TextView tv3;
	public WordsItem(Context context)
	{
		super(context);
		
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.train_last_item, this);
		
		
		tv1 = (TextView)findViewById(R.id.text1);
		tv2 = (TextView)findViewById(R.id.text2);
		tv3 = (TextView)findViewById(R.id.text3);
	}

	public void setup(Word w)
	{
		mWord = w;
		
		
		
		if(tv1 != null)
			tv1.setText(mWord.getLern());
		
		if(tv2 != null)
			tv2.setText(mWord.getNative());
		
//		if(tv3 != null)
//			setupText3(tv3, pw);
		
		ImageView flag1 = (ImageView)findViewById(R.id.flag1);
		if(flag1 != null)
			flag1.setImageResource(CommonSetting.lernCode.getDrawableID(getContext()));
	
		ImageView flag2 = (ImageView)findViewById(R.id.flag2);
		if(flag2 != null)
			flag2.setImageResource(CommonSetting.nativeCode.getDrawableID(getContext()));
	}


	
	
	public Word getWord()
	{
		return mWord;
	}


}
