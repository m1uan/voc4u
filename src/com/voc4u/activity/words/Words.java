package com.voc4u.activity.words;

import java.util.ArrayList;

import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.train.LastItem;
import com.voc4u.controller.Word;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Words extends BaseWordActivity {
	
	public static final String WORDS_LESSON_FIELD = "Words_lesson_field";
	public static final String WORDS_LEARN_CODE_FIELD = "Words_learn_code_field";
	public static final String WORDS_NATIVE_CODE_FIELD = "Words_native_code_field";
	
	private String mLesson;
	private String mLearn;
	private String mNative;
	
	
	protected ListView mList;
	protected Adapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//setContentView();
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey(WORDS_LESSON_FIELD))
		{
			mLesson = extras.getString(WORDS_LESSON_FIELD);
		}
		
		if(extras != null && extras.containsKey(WORDS_LEARN_CODE_FIELD))
		{
			mLearn = extras.getString(WORDS_LEARN_CODE_FIELD);
		}
		
		if(extras != null && extras.containsKey(WORDS_NATIVE_CODE_FIELD))
		{
			mNative = extras.getString(WORDS_NATIVE_CODE_FIELD);
		}
		
		
		mAdapter = new Adapter();
			
		mList = (ListView)findViewById(R.id.list);
		mList.setAdapter(mAdapter);
		
		registerForContextMenu(mList);
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		
		((WordsItem)((AdapterContextMenuInfo)menuInfo).targetView).createMenu(menu);
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		((WordsItem)((AdapterContextMenuInfo)item.getMenuInfo()).targetView).onContextItemSelected(item);
		return super.onContextItemSelected(item);
	}
	
	public class Adapter extends BaseAdapter
	{
		ArrayList<Word> mWords;
		
		public Adapter()
		{
			int lesson = 0;// Integer.parseInt(mLesson);
			mWords = mWCtrl.getWordsInLesson(lesson);
		}
		
		
		@Override
		public int getCount() {
			if(mWords == null){
				return 0;
			}
			else{
				return mWords.size();
			}
		}

		@Override
		public Word getItem(int index) {
			return mWords.get(index);
		}

		@Override
		public long getItemId(int index) {
			
			return 0;
		}

		@Override
		public View getView(int index, View arg1, ViewGroup arg2) {
			
			if(arg1 == null)
			{
				
				arg1 = new WordsItem(Words.this);
			}
			
			Word w = getItem(index);
			
			
			((WordsItem)arg1).setup(w);
			return arg1;
		}
		
	}


	@Override
	protected int getContentView() {
		return R.layout.dictionary;
	}
}
