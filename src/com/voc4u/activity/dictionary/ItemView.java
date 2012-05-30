package com.voc4u.activity.dictionary;

import junit.framework.Assert;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.voc4u.R;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangSetting;


public class ItemView extends LinearLayout implements OnCheckedChangeListener
{
	public static final int	MAX_EXAMPLES_IN_VIEW	= 50;
	private final WordController mWordCtrl;
	private String mTitle;
	private int mLesson;
	private final CheckBox chkBox;
	private final ItemStatus mStatus;
	private final Dictionary	mDictionary;
	private ItemData[] mItems;
	private ItemData mCurrentItem;
	
	
	public ItemView(Dictionary context, WordController wordCtrl)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dictionary_item, this);

		mWordCtrl = wordCtrl;
		
		chkBox = (CheckBox)findViewById(R.id.checkbox);
	
		mStatus = ItemStatus.NONE;
		
		mDictionary = context;
	}

	public void setup(int position, ItemData[] items)
	{
		mItems = items;
		mCurrentItem = items[position];
		mLesson = mCurrentItem.getLesson();
		

		
		//if(position < lessons.length)
			mTitle = "lesson " + mLesson;
//		else
//			mTitle = getContext().getString(R.string.dictionaryItem, mLesson);
		
		findViewById(R.id.preparing).setVisibility(mWordCtrl.isPrepairing(mLesson) ? View.VISIBLE : View.GONE);
		
		
		final TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(mTitle);

		final boolean enable = mCurrentItem.getEnabledLesson();
		chkBox.setChecked(enable);
		chkBox.setOnCheckedChangeListener(this);

		
		final TextView ex = (TextView)findViewById(R.id.examples);
		ex.setText(mCurrentItem.getWords());
	}

	public int getLesson()
	{
		return mLesson;
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
			boolean isChecked)
	{
		if (!isChecked)
		{
			// test set mTestAnyChecked was set
			// and test any checked return true -> some items still checked (this is not calced)
			// and test enable in db when in db take to question to user
			if (mWordCtrl.isEnableLesson(mLesson) && (mItems == null || testAnyChecked()))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setMessage(R.string.msg_your_are_sure_disable_lesson)
						.setCancelable(false).setTitle(mTitle)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int id)
									{
										setVocabulary(false);
									}
								}).setNegativeButton(android.R.string.no,
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

	private boolean testAnyChecked() {
		for(int i = 0; i != mItems.length; i++) {
			if(mItems[i].getEnabledLesson()) {
				return true;
			}
		}
		return false;
	}

	protected void setVocabulary(boolean enable)
	{
//		if(mStatus == ItemStatus.NONE)
//			mStatus = enable ? ItemStatus.ADD : ItemStatus.REMOVE;
//		else if(mStatus == ItemStatus.ADD)
//			mStatus = !enable ? ItemStatus.NONE : ItemStatus.REMOVE;
//		else if(mStatus == ItemStatus.REMOVE)
//			mStatus = enable ? ItemStatus.NONE : ItemStatus.REMOVE;
		mCurrentItem.setEnabled(enable);
		mWordCtrl.enableLessonAsync(mLesson, enable, mDictionary);
		mDictionary.onUpdateDone();
	}
	
	

	public boolean isChecked() 
	{
		return chkBox.isChecked();
	}
	
	public void setChecked(boolean checked)
	{
		chkBox.setChecked(checked);
	}

}
