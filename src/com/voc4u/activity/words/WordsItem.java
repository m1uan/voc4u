package com.voc4u.activity.words;

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
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.Word;
import com.voc4u.setting.CommonSetting;

public class WordsItem extends LinearLayout
{
	Word mWord;
	
	public WordsItem(Context context)
	{
		super(context);
		
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.train_last_item, this);
		
	}

	public void setup(Word w)
	{
		mWord = w;
		
		TextView tv1 = (TextView)findViewById(R.id.text1);
		TextView tv2 = (TextView)findViewById(R.id.text2);
		TextView tv3 = (TextView)findViewById(R.id.text3);
		
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

	public void createMenu(ContextMenu menu) {
		menu.setHeaderTitle(mWord.getLern());
		menu.add(0,1,0,"play");
		menu.add(0,2,0,"edit");
		menu.add(0,3,0,"delete");
	}

	public void onContextItemSelected(MenuItem item) {
		int i = item.getItemId();
	}
	
}
