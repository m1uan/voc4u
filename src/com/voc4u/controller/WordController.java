package com.voc4u.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.voc4u.activity.init.Init;
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
	private static int mNativeWordNum = 0;
	private static int mWordNum = 0;
	public static final int MAX_LAST_LIST = 30;

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
			mSwitchPoliticy = getSwitchPoliticyOrNot();

			ArrayList<Word> list = !mSwitchPoliticy ? mDictionary
					.getPublicWords(DictionaryOpenHelper.CZEN_GROUP, true,
							getLastListIds()) : mDictionary.getPublicWords2(
					DictionaryOpenHelper.CZEN_GROUP, true, getLastListIds());

			int pos = Math.abs(mRandomGenerator.nextInt() % list.size());
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
		if (mLastList.size() > MAX_LAST_LIST)
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
			return mDictionary.isLessonEnabled(lesson);
		
		return false;
	}
	
	public void enableLesson(int lesson, boolean enable, updateLisener ul)
	{
		new UpdateTask(lesson, enable, ul).execute("");
		// mDictionary.setEnabled(idFrom, idTo, enable);
	}

	public Word getPublicWordById(int id)
	{
		return mDictionary.getPublicWordById(id);
	}

	private void addLesson(int lesson)
	{
		String[] nt = LangSetting.getInitDataFromLT(CommonSetting.nativeCode);
		String[] lr = LangSetting.getInitDataFromLT(CommonSetting.lernCode);

		int start = lesson * CommonSetting.LESSON_NUM;
		int end = start + CommonSetting.LESSON_NUM;

		if (start < nt.length)
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

				addWordEx(lesson, slr, snt, 1, 1);
			}
		}
	}

	private class UpdateTask extends AsyncTask<String, Integer, Long>
	{
		final int lesson;
		final boolean mEnable;
		updateLisener mUpdateListener;

		public UpdateTask(int less, boolean remove, updateLisener ul)
		{
			lesson = less;
			mEnable = remove;
			mUpdateListener = ul;
		}

		protected Long doInBackground(String... urls)
		{

			if (mEnable)
				addLesson(lesson);
			else
			{
				mDictionary.removeLesson(lesson);
			}

			return 10L;
		}

		protected void onProgressUpdate(Integer... progress)
		{
		}

		protected void onPostExecute(Long result)
		{
			if (mUpdateListener != null)
				mUpdateListener.onUpdateDone();
		}
	}
}
