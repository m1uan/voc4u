package com.voc4u.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.voc4u.activity.init.Init;
import com.voc4u.activity.setting.WordSetting;
import com.voc4u.activity.train.Train;
import com.voc4u.core.InitData;
import com.voc4u.core.LangSetting;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.Consts;
import com.voc4u.setting.LangType;

import junit.framework.Assert;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.widget.ArrayAdapter;

public class WordController
{
	private DictionaryOpenHelper mDictionary = null;
	private ArrayAdapter<String> mAdapter;
	private Random mRandomGenerator;
	private PublicWord mPublicWord;
	private boolean mSwitchPoliticy;
	private int mWeight;
	private final ArrayList<PublicWord> mLastList;
	private UpdateTask mAsyncUpdate;
	private final Context mContext;
	private static int mNativeWordNum = 0;
	private static int mWordNum = 0;
	

	static WordController mInstance = null;

	public static WordController getInstance(Context act)
	{
		if (mInstance == null)
			mInstance = new WordController(act);

		return mInstance;
	}

	protected WordController(Context activity)
	{
		mDictionary = new DictionaryOpenHelper(activity);
		mAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1);
		mRandomGenerator = new Random();
		mLastList = new ArrayList<PublicWord>();
		mNativeWordNum = 0;
		mContext = activity;
	}

	public void addWord(String text, String text2)
	{
		if (mDictionary != null)
			mDictionary.addWord(DictionaryOpenHelper.CZEN_GROUP, text, text2);

	}

	public void addWordEx(final int lesson, String text, String text2,
			int weight1, int weight2)
	{
		if (mDictionary != null)
			mDictionary.addWordEx(lesson, text, text2, weight1, weight2);

	}

	public PublicWord getFirstPublicWord()
	{
		Assert.assertNotNull(mDictionary);
		if (mDictionary != null)
		{
			if(!mDictionary.isAnyUnknownWord())
				mDictionary.setupFirstWordWeight();
			
			mSwitchPoliticy = getSwitchPoliticyOrNot();

			// get normaly all words
			ArrayList<Word> list = mDictionary.getPublicWords(!mSwitchPoliticy, getLastListIds());

			if(list == null)
			{
				// its posible in the db is less words as is size of last list
				// get the word without lastlist
				list = mDictionary.getPublicWords(!mSwitchPoliticy, null);
				
				// db is null -> open the setting with words
				if(list == null)
				{
					showWordsMenu();
					return null;
				}
			}
			int pos = 0;//Math.abs(mRandomGenerator.nextInt() % list.size());
			mPublicWord = new PublicWord(list.get(pos),
					!mSwitchPoliticy ? EPoliticy.PRIMAR : EPoliticy.SECUNDAR);
			addLastList(mPublicWord);

			return mPublicWord;
		}
		return null;
	}

	private boolean getSwitchPoliticyOrNot()
	{
		boolean politicy;
		final int max = Consts.MAX_WORD_NATIVE_LEARN;
		int czvsen = Math.abs(mRandomGenerator.nextInt() % max);

		// if can switch
		if ((max - mWordNum) > CommonSetting.langNativeNum)
			politicy = czvsen < CommonSetting.langNativeNum;
		else if (mNativeWordNum < CommonSetting.langNativeNum)
			politicy = true;
		else
			politicy = false;

		if (mWordNum >= max - 1)
		{
			mNativeWordNum = 0;
			mWordNum = 0;
		} else
		{
			mWordNum++;

			if (politicy)
				mNativeWordNum++;
		}

		return politicy;
		// (politicy && !CommonSetting.langType.getCrossBase())
		// || (!politicy && CommonSetting.langType.getCrossBase());
	}

	private void addLastList(PublicWord publicWord)
	{
		mLastList.add(publicWord);
		if (mLastList.size() > CommonSetting.MAX_LAST_LIST)
			mLastList.remove(0);
	}

	public List<PublicWord> getLastList()
	{
		return mLastList;
	}

	public int[] getLastListIds()
	{
		if (mLastList == null || mLastList.size() < 1)
			return null;

		int[] result = new int[mLastList.size()];

		for (int i = 0; i != mLastList.size(); i++)
		{
			result[i] = mLastList.get(i).getId();
		}

		return result;

	}

	public void updatePublicWord(boolean remember)
	{
		Assert.assertNotNull(mDictionary);
		if (mDictionary != null)
		{
			mPublicWord.setSuccess(remember);

			mPublicWord.setRemember(remember);

			mDictionary.updateWordWeights(mPublicWord.getBaseWord());

		}
	}

	public static int calcWeight(int weight, boolean remember)
	{
		weight = weight > 0 ? weight : 1;

		if (remember)
		{
			return weight < 10000 ? weight * 3 : weight + 30000;
		} else
		{
			weight = weight / 2;
			return weight < 1 ? 1 : weight;
		}
	}

	public boolean isChangedPoliticy()
	{
		return mSwitchPoliticy;
	}

	public PublicWord getActualPublicWord()
	{
		return mPublicWord != null ? mPublicWord : getFirstPublicWord();
	}

	public long count()
	{
		return mDictionary.getCount();
	}

	public boolean getEnable(int idFrom, int idTo)
	{
		return mDictionary.getEnabled(idFrom, idTo);
	}

	public void setEnable(int idFrom, int idTo, boolean enable)
	{
		new UpdateTask(idFrom, enable, null).execute("");
		// mDictionary.setEnabled(idFrom, idTo, enable);
	}

	public boolean isEnableLesson(int lesson)
	{
		if(mDictionary != null)
			return mDictionary.isLessonLoaded(lesson);
		
		return false;
	}
	
	public void enableLesson(int lesson, boolean enable, updateLisener ul)
	{
		new UpdateTask(lesson, enable, ul).execute("");
	}
	
	public void initLesson(updateLisener ul)
	{
		new UpdateTask(0, ul).execute("");
	}

	public Word getPublicWordById(int id)
	{
		return mDictionary.getPublicWordById(id);
	}
	
	
	/**
	 * add lesson
	 * @param lesson - number of lesson
	 * @param weights - normaly 0, but in  initial is set to 1
	 */
	private void addLesson(int lesson, int weights) 
	{
		String[] nt = LangSetting.getInitDataFromLT(CommonSetting.nativeCode);
		String[] lr = LangSetting.getInitDataFromLT(CommonSetting.lernCode);

		int start = LangSetting.getLessonStart(lesson);
		int end = LangSetting.getLessonStart(lesson + 1);

		// if it is first lesson which add
		// to DB set first words as weight1,weight2 to 1,1
		// because else isn't work getFirstWords
		// the last list is bigger as used words
		boolean initialize = weights == 0 && mDictionary.getCount() < CommonSetting.MAX_LAST_LIST;
		
		if(initialize)
			weights = 1;
			
		
		int num = 0;
		if (start != -1 && end != -1 && start < nt.length)
		{
			if(end >= nt.length)
				end = nt.length -1;
			
			for (int i = start; i != end; i++)
			{
				final String slr = lr[i];
				final String snt = nt[i];
				if (snt == null || snt.length() < 1 || slr == null
						|| slr.length() < 1)
					continue;

				addWordEx(lesson, slr, snt, weights, weights);
				
				// stop initialize first words to used value
				if(initialize && num++ > CommonSetting.MAX_LAST_LIST)
				{
					initialize = false;
					weights = 0;
				}
			}
		}
	}

	

	private class UpdateTask extends AsyncTask<String, Integer, Long>
	{
		final int mLesson;
		final boolean mEnable;
		updateLisener mUpdateListener;
		final int mWeights;
		private boolean mDone;
		private ArrayList<Integer> mAddList;
		private ArrayList<Integer> mRemoveList;
		
		
		
		
		public UpdateTask(int less, boolean remove, updateLisener ul)
		{
			mLesson = less;
			mEnable = remove;
			mUpdateListener = ul;
			mWeights = 0;
		}
		
		public UpdateTask(int less, updateLisener ul)
		{
			mLesson = less;
			mEnable = true;
			mUpdateListener = ul;
			mWeights = 1;
		}

		protected Long doInBackground(String... urls)
		{
			mDone = false;

			if(mAddList != null && mAddList.size() > 0)
				for(int i = 0; i != mAddList.size(); i++)
					addLesson(mAddList.get(i), 0);
			
			if(mRemoveList != null && mRemoveList.size() > 0)
				for(int i = 0; i != mRemoveList.size(); i++)
					mDictionary.unloadLesson(mRemoveList.get(i));
			
			return 10L;
		}

		

		protected void onProgressUpdate(Integer... progress)
		{
		}

		protected void onPostExecute(Long result)
		{
			
			mAddList = null;
			mDone = true;
			if (mUpdateListener != null)
				mUpdateListener.onUpdateDone();
		}

		public boolean isDone() {
			return mDone;
		}

		public void add(int lesson2) 
		{
			if(mAddList == null)
				mAddList = new ArrayList<Integer>();
			else if(isInList(mAddList, lesson2))
				return;
			
			mAddList.add(lesson2);
			
		}

		/**
		 * is contained item in list
		 * @param list - list with content
		 * @param lesson2 - item
		 * @return true if already in list
		 */
		private boolean isInList(final ArrayList<Integer> list, int lesson2) {
			
			if(list != null)
				for(int i = 0; i != list.size(); i++)
					if(list.get(i) == lesson2)
						return true;
			
			return false;
		}

		public void remove(int lesson2) 
		{
			if(mRemoveList == null)
				mRemoveList = new ArrayList<Integer>();
			else if(isInList(mRemoveList, lesson2))
				return;
			mRemoveList.add(lesson2);
		}
	}



	public void runAsyncTask() 
	{
		Assert.assertTrue(mAsyncUpdate != null);
		if(mAsyncUpdate != null && !mAsyncUpdate.isDone())
			mAsyncUpdate.execute("");
	}

	public void enableLessonAsync(int lesson, boolean enable) 
	{
		if(mAsyncUpdate == null || mAsyncUpdate.isDone())
			mAsyncUpdate = new UpdateTask(0, null);
	
		if(enable)
			mAsyncUpdate.add(lesson);
		else
			mAsyncUpdate.remove(lesson);
	}
	
	public boolean isAsyncRunning()
	{
		return mAsyncUpdate != null && !mAsyncUpdate.isDone();
	}
	
	public void showWordsMenu()
	{
		Intent intent = new Intent(mContext, WordSetting.class);
		mContext.startActivity(intent);
	}
}
