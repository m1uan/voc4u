package com.voc4u.activity.setting;


import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.core.LangSetting;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_setting);
		
		mWordCtrl = WordController.getInstance(this);
		
		//mAdapter = new 
		
		mList = (ListView)findViewById(R.id.list);
		mList.setAdapter(new Adapter());
		
		
		if(CommonSetting.DEBUG)
		{
			findViewById(R.id.addword).setVisibility(View.VISIBLE);
			btnAddWord = findViewById(R.id.btnAddWord);
			btnAddWord.setOnClickListener(this);
		}
	}
	
	@Override
	protected void onPause()
	{
		CommonSetting.store(this);
		super.onPause();
	}
	
	public class Adapter implements ListAdapter
	{

		private static final int  NUM_ADAPTING = 50;
		private static final int VOCABULARY_TYPE = 0;
		private static final int SETTING_TYPE = 1;
		private int mNum;

		public Adapter()
		{
			mNum = LangSetting.LESSON_SIZES.length;
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
			return mNum + 1;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
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
