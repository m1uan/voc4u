package com.voc4u.activity.setting;


import junit.framework.Assert;

import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.core.LangSetting;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.DataSetObserver;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class WordSetting extends Activity implements OnClickListener
{
	private WordController mWordCtrl;
	private ListView mList;
	private View btnAddWord;
	private Adapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_setting);
		
		mWordCtrl = WordController.getInstance(this);
		
		mAdapter = new Adapter();
		
		mList = (ListView)findViewById(R.id.list);
		mList.setAdapter(mAdapter);
		
		
		if(CommonSetting.DEBUG)
		{
			findViewById(R.id.addword).setVisibility(View.VISIBLE);
			btnAddWord = findViewById(R.id.btnAddWord);
			btnAddWord.setOnClickListener(this);
		}
	}
	
	@Override
	protected void onResume() 	
	{
		// if still runing the async task
		// isn't posible changing anything
		if(mWordCtrl.isAsyncRunning())
		{
			ProgressDialog dialog = ProgressDialog.show(this, "", 
                    "Words still initializing, Please wait...", true);
			dialog.setOnCancelListener(new OnCancelListener() 
			{	
				@Override
				public void onCancel(DialogInterface dialog)
				{
					finish();
				}
			});
		}
		
		super.onResume();
	}
	
	@Override
	public void onBackPressed() 
	{
		CommonSetting.store(this);
		store();
		
		// super is called in store() -> showDialogAboutDurationOfOperation() -> "YES"
		//super.onBackPressed();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	private void store() 
	{
		boolean anyChanges = false;
		boolean anyChecked = false;
		
		for(int i = 0; i != mAdapter.getLessonCount(); i++)
		{
			ItemView item = mAdapter.getLessonItem(i);
			
			Assert.assertNotNull(item);
			if(item != null)
			{
				ItemStatus is = item.getStatus();
				if( is != ItemStatus.NONE)
				{
					mWordCtrl.enableLessonAsync(i, is == ItemStatus.ADD);
					anyChanges = true;
				}
				else if(item.isChecked())
					anyChecked = true;
			}
		}
		
		if(!anyChecked)
		{
			showDialogAboutMustCheckAtleasOneItem();
		}
		else if(anyChanges)
		{
			showDialogAboutDurationOfOperation();
		}
		
		
	}

	private void showDialogAboutMustCheckAtleasOneItem() 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.vocabulary_you_must_enable_at_least_one_lesson_)
		       .setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showDialogAboutDurationOfOperation() 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.vocabulary_you_are_make_some_changes)
		       .setCancelable(false)
		       .setPositiveButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                mWordCtrl.runAsyncTask();
		                superOnBackPresed();
		           }
		       })
		       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void superOnBackPresed() 
	{
		super.onBackPressed();
	}

	public class Adapter implements ListAdapter
	{

		private static final int  NUM_ADAPTING = 50;
		private static final int VOCABULARY_TYPE = 0;
		private static final int SETTING_TYPE = 1;
		final private int mLessonNum;
		final ItemView[] mLessons;
		private int mLastItem = 0;
		
		
		public Adapter()
		{
			mLessonNum = LangSetting.LESSON_SIZES.length;
			mLessons = new ItemView[mLessonNum];
		}
		
		@Override
		public boolean areAllItemsEnabled()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount()
		{
			// first is setting
			return mLessonNum + 1;
		}
		
		public int getLessonCount()
		{
			return mLessonNum;
		}

		ItemView getLessonItem(int position)
		{
			if(mLessonNum > position)
				return mLessons[position];
			
			return null;
		}
		
		@Override
		public ItemView getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position)
		{
			if(position == 0)
				return SETTING_TYPE;
			else
				return VOCABULARY_TYPE;
		}

		@Override
		public View getView(int position, View convertView, final ViewGroup parent)
		{
			if(getItemViewType(position) == VOCABULARY_TYPE)
				return createWordView(position, convertView);
			else if(getItemViewType(position) == SETTING_TYPE)
				return createSettingView(convertView);

			return convertView;
		}

		private View createSettingView(View convertView)
		{
			//SettingItemView setting;
			
			if(convertView == null)
			{
				convertView = new SettingItemView(WordSetting.this);
				((SettingItemView)convertView).setup();
			}
			
			return convertView;
		}

		public View createWordView(int position, View convertView)
		{
			ItemView item;
			
			final int lesson = (position-1);
			
			if(convertView == null)
				item = new ItemView(WordSetting.this, mWordCtrl);
			else
				item =(ItemView)convertView;
			
			item.setup(lesson);
			mLessons[mLastItem++] = item;
			return item;
		}
		
		@Override
		public int getViewTypeCount()
		{
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public boolean hasStableIds()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub
			
		}
		}

	@Override
	public void onClick(View v)
	{
		if(v == btnAddWord)
		{
			showDialog(101);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		if(id == 101)
		{
			
			//Context mContext = getApplicationContext();
			final Dialog dialog = new Dialog(this);

			dialog.setContentView(R.layout.add_word_dialog);
			dialog.setTitle("Custom Dialog");
			
			final EditText edtNative = (EditText)dialog.findViewById(R.id.edtNative);
			final EditText edtLern = (EditText)dialog.findViewById(R.id.edtLern);
			Button btnAdd = (Button)dialog.findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					WordController.getInstance(WordSetting.this)
						.addWordEx(4, edtNative.getText().toString() + "~", edtLern.getText().toString() + "~", 1, 1);
					dialog.cancel();
				}
			});
			return dialog;
		}
		
		return super.onCreateDialog(id);
	}

	
}
