package com.voc4u.activity.setting;

import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ItemView extends LinearLayout implements OnCheckedChangeListener
{

	private WordController mWordCtrl;
	private String mTitle;
	private int mLesson;

	public ItemView(Context context, WordController wordCtrl)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.word_setting_item, this);

		mWordCtrl = wordCtrl;
	}

	public void setup(int lesson)
	{
		mLesson = lesson;
		mTitle = getContext().getString(R.string.settingItem, lesson);

		final TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(mTitle);

		final boolean enable = mWordCtrl.isEnableLesson(mLesson);
		final CheckBox chkbox = (CheckBox) findViewById(R.id.checkbox);
		chkbox.setChecked(enable);
		chkbox.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
			boolean isChecked)
	{
		if (!isChecked)
		{
			if (mWordCtrl.isEnableLesson(mLesson))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setMessage("Are you sure you want to disable?")
						.setCancelable(false).setTitle(mTitle)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int id)
									{
										setVocabulary(false);
									}
								}).setNegativeButton("No",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int id)
									{
										dialog.cancel();
										buttonView.setChecked(true);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		} 
		else if(!mWordCtrl.isEnableLesson(mLesson))
			setVocabulary(true);
	}

	protected void setVocabulary(boolean enable)
	{
		mWordCtrl.enableLesson(mLesson, enable, null);
	}

}
