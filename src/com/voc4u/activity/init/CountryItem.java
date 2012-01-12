package com.voc4u.activity.init;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.setting.LangType;

public class CountryItem extends LinearLayout
{

	public CountryItem(Context context, LangType lt)
	{
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.init_spinner_row, this);

		
		TextView tv = (TextView)findViewById(R.id.text);
		tv.setText(lt.toString());
		int resid = lt.getDrawableID(context);
		if(resid > 0)
		{
			ImageView iv = (ImageView)findViewById(R.id.image);
			iv.setImageResource(resid);
		}
	}

}
