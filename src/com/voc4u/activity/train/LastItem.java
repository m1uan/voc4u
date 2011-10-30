package com.voc4u.activity.train;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voc4u.controller.PublicWord;
import com.voc4u.czen1.R;
import com.voc4u.setting.Consts;

public class LastItem extends LinearLayout
{

	private final PublicWord mPW;

	public LastItem(Context context, PublicWord pw)
	{
		super(context);
		mPW = pw;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.train_last_item, this);
		
		setup(pw);
	}

	private void setup(PublicWord pw)
	{
		Assert.assertTrue(pw != null);
		if(pw == null)
			return;
		
		TextView tv1 = (TextView)findViewById(R.id.text1);
		TextView tv2 = (TextView)findViewById(R.id.text2);
		TextView tv3 = (TextView)findViewById(R.id.text3);
		
		Assert.assertTrue(tv1 != null);
		if(tv1 != null)
			setupText1(tv1, pw);
		
		Assert.assertTrue(tv2 != null);
		if(tv2 != null)
			setupText2(tv2, pw);
		
		Assert.assertTrue(tv3 != null);
		if(tv3 != null)
			setupText3(tv3, pw);
	}

	private void setupText3(TextView tv3, PublicWord pw)
	{
		tv3.setVisibility(Consts.DEBUG ? View.VISIBLE : View.GONE);
		tv3.setText("(" + 
				String.valueOf(pw.getBaseWord().getWeight() 
						+ "/"
						+ String.valueOf(pw.getBaseWord().getWeight2())
						+ ")"));
	}

	private void setupText2(TextView tv2, PublicWord pw)
	{
		tv2.setText(pw.getNative());
		tv2.setTextColor(getColor(pw));
	}

	private int getColor(PublicWord pw)
	{
		return pw.getSuccess() ? Color.LTGRAY : Color.RED;
	}

	private void setupText1(TextView tv1, PublicWord pw)
	{
		tv1.setText(pw.getLern());
		tv1.setTextColor(getColor(pw));
	}

}
