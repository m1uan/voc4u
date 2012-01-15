package com.voc4u.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.voc4u.activity.dictionary.Dictionary;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.Consts;
import com.voc4u.setting.LangSetting;

public class WordController {
	private DictionaryOpenHelper mDictionary = null;
	private final ArrayAdapter<String> mAdapter;
	private final Random mRandomGenerator;
	private PublicWord mPublicWord;
	private boolean mSwitchPoliticy;
	private int mWeight;
	private final ArrayList<PublicWord> mLastList;
	private LessonWorker mAsyncUpdate;
	private final Context mContext;
	private static int mNativeWordNum = 0;
	private static int mWordNum = 0;

	public static final int CUSTOM_WORD_LESSON = 0;

	static WordController mInstance = null;

	public static WordController getInstance(Context act) {
		if (mInstance == null)
			mInstance = new WordController(act);

		return mInstance;
	}

	protected WordController(Context activity) {
		mDictionary = new DictionaryOpenHelper(activity);
		mAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1);
		mRandomGenerator = new Random();
		mLastList = new ArrayList<PublicWord>();
		mNativeWordNum = 0;
		mContext = activity;

		mAddList = new ArrayList<Integer>();
		mRemoveList = new ArrayList<Integer>();
	}

	public void addWord(String text, String text2) {
		if (mDictionary != null)
			mDictionary.addWord(DictionaryOpenHelper.CZEN_GROUP, text, text2);

	}

	public void addWordEx(final int lesson, String text, String text2,
			int weight1, int weight2) {
		if (mDictionary != null)
			mDictionary.addWordEx(lesson, text, text2, weight1, weight2);

	}

	public ArrayList<Word> getWordsInLesson(int lesson) {
		return mDictionary.getWordsInLesson(lesson);
	}

	static ArrayList<Word> mFistWordList = null;

	/**
	 * get the first word which is usable for learning
	 * 
	 * @return
	 */
	public PublicWord getFirstPublicWord() 
	{
		if (mDictionary == null)
			return null;

		boolean addNewWord = false;
		
		if (mFistWordList == null || mFistWordList.size() < 1) 
		{
			// try get words
			mFistWordList = mDictionary.getPublicWords(getLastListIds());

			// first trial isn't work and last list still empty
			// try add new words
			if(mFistWordList == null || mFistWordList.size() < Consts.MAX_LAST_LIST)
			{
				mDictionary.setupFirstWordWeight(Consts.MAX_LAST_LIST);
				addNewWord = true;
			}
			
			// first trial isn't work and last list still empty
			// try again, is expecting the setupFirstWordWeight was add some word
			while (mFistWordList == null || mFistWordList.size() < 1)
			{
				// the last list isn't empty
				// make his less about one
				if(mLastList != null && mLastList.size() > 1)
				{	
					mLastList.remove(0);
				}
				else
				{
					// it's seems the mLastList is empty
					// and the mFirstWordList still empty
					showWordsMenu();
					return null;
				}
				
				// try again
				mFistWordList = mDictionary.getPublicWords(getLastListIds());
			}

			
			// add the word normaly way
			if (!addNewWord && mDictionary.isAnyUnknownWord()) 
			{
				mDictionary.setupFirstWordWeight(5);
			}
			
		}

		if (mFistWordList != null && mFistWordList.size() > 0) 
		{
			Random rand = new Random();
			int index = rand.nextInt(mFistWordList.size());

			Word w1 = mFistWordList.get(index);
			mFistWordList.remove(index);

			mSwitchPoliticy = w1.getWeight() > w1.getWeight2();

			int pos = 0;// Math.abs(mRandomGenerator.nextInt() % list.size());
			mPublicWord = new PublicWord(w1,
					!mSwitchPoliticy ? EPoliticy.PRIMAR : EPoliticy.SECUNDAR);
			addLastList(mPublicWord);

			return mPublicWord;
		}
		return null;
	}

	private void addLastList(PublicWord publicWord) {
		mLastList.add(publicWord);
		if (mLastList.size() > Consts.MAX_LAST_LIST)
			mLastList.remove(0);
	}

	public List<PublicWord> getLastList() {
		return mLastList;
	}

	public int[] getLastListIds() 
	{
		return mDictionary.getLastListIds(mLastList) ;
	}

	public void updatePublicWord(boolean remember) 
	{
		Assert.assertNotNull(mDictionary);
		if (mDictionary != null) 
		{
			mPublicWord.setSuccess(remember);
			mPublicWord.setRemember(remember);
			//mDictionary.updateWordWeights(mPublicWord.getBaseWord());
			new UpdatePublicWordWorker().execute(mPublicWord);
		}
	}

	private class UpdatePublicWordWorker extends AsyncTask<PublicWord, Integer, Long> 
	{

		@Override
		protected Long doInBackground(PublicWord... arg0) 
		{
			mDictionary.updateWordWeights(arg0[0].getBaseWord());
			return null;
		}
		
	}
	
	
	public static int calcWeight(int weight, boolean remember) {
		weight = weight > 0 ? weight : 1;

		if (remember) {
			return weight < 10000 ? weight * 3 : weight + 30000;
		} else {
			weight = weight / 2;
			return weight < 1 ? 1 : weight;
		}
	}

	public boolean isChangedPoliticy() {
		return mSwitchPoliticy;
	}

	public PublicWord getActualPublicWord() {
		return mPublicWord != null ? mPublicWord : getFirstPublicWord();
	}

	public long count() {
		return mDictionary.getCount();
	}

	/**
	 * testing the lessonworker is in task else only DB check
	 * 
	 * @param lesson
	 * @return
	 */
	public boolean isEnableLesson(int lesson) {
		if (isAsyncRunning()) {
			synchronized (mAddList) {
				if (mCurrentLesson == lesson)
					return true;

				for (int l : mAddList)
					if (l == lesson)
						return true;

				for (int l : mRemoveList)
					if (l == lesson)
						return false;
			}
		}

		if (mDictionary != null)
			return mDictionary.isLessonLoaded(lesson);

		return false;
	}

	public void initLesson(updateLisener ul) {
		new LessonWorker(ul).execute("");
	}

	public Word getPublicWordById(int id) {
		return mDictionary.getPublicWordById(id);
	}

	public void unloadAllLesson() {
		mDictionary.unloadLesson(-1);
	}

	private final ArrayList<Integer> mAddList;
	private final ArrayList<Integer> mRemoveList;
	private int mCurrentLesson;

	public void add(int lesson2) {
		synchronized (mAddList) {
			if (isInList(mAddList, lesson2))
				return;

			mAddList.add(lesson2);
		}
	}

	public void remove(int lesson2) {
		boolean negate;
		synchronized (mAddList) {
			negate = negateInList(mAddList, lesson2);
			if (isInList(mRemoveList, lesson2))
				return;
			else if (!negate)
				mRemoveList.add(lesson2);
		}
	}

	private boolean negateInList(final ArrayList<Integer> list, int lesson2) {
		if (isInList(list, lesson2)) {
			for (int i = 0; i != list.size(); i++) {
				int les = list.get(i);
				if (les == lesson2) {
					list.remove(i);
					return true;
				}
			}
		}

		return false;
	}

	public boolean isPrepairing(int lesson) {
		if (isAsyncRunning()) {
			synchronized (mAddList) {
				if (mCurrentLesson == lesson)
					return true;

				for (int l : mAddList)
					if (l == lesson)
						return true;

				for (int l : mRemoveList)
					if (l == lesson)
						return true;
			}
		}

		return false;
	}

	/**
	 * is contained item in list
	 * 
	 * @param list
	 *            - list with content
	 * @param lesson2
	 *            - item
	 * @return true if already in list
	 */
	private boolean isInList(final ArrayList<Integer> list, int lesson2) {

		if (list != null)
			for (int i = 0; i != list.size(); i++)
				if (list.get(i) == lesson2)
					return true;

		return false;
	}

	public void setUpdateListener(updateLisener ul) {
		if (isAsyncRunning())
			mAsyncUpdate.setUpdateListener(ul);
	}

	private class LessonWorker extends AsyncTask<String, Integer, Long> {

		updateLisener mUpdateListener;

		private Boolean mDone;

		public LessonWorker(updateLisener ul) {
			mDone = false;
			mUpdateListener = ul;
		}

		private int getActualProvidedLesson() {
			int lesson = -1;
			synchronized (mAddList) {
				if (mAddList != null && mAddList.size() > 0) {
					lesson = mAddList.get(0);
					mAddList.remove(0);
					mCurrentLesson = lesson;
				} else {
					mCurrentLesson = -1;
					synchronized (mDone) {
						mDone = false;
					}
				}
			}

			return lesson;
		}

		@Override
		protected Long doInBackground(String... urls) {
			while (true) {
				// get first task for adding
				int lesson = getActualProvidedLesson();
				if (lesson < 0)
					break;

				addLessonFromList(lesson, 0);
			}

			// remove all for in remove list
			removeLessonFromList(0);

			return 10L;
		}

		/**
		 * add lesson
		 * 
		 * @param lesson
		 *            - number of lesson
		 * @param weights
		 *            - normaly 0, but in initial is set to 1
		 */
		private void addLessonFromList(int lesson, int weights) {
			String[] nt = LangSetting.getInitDataFromLT(
					CommonSetting.nativeCode, lesson);
			String[] lr = LangSetting.getInitDataFromLT(CommonSetting.lernCode,
					lesson);

			int start = 0;
			int end = nt.length < lr.length ? nt.length : lr.length;

			// if it is first lesson which add
			// to DB set first words as weight1,weight2 to 1,1
			// because else isn't work getFirstWords
			// the last list is bigger as used words
			boolean initialize = weights == 0
					&& mDictionary.getCount() < Consts.MAX_LAST_LIST;

			boolean anyRemove = false;

			if (initialize)
				weights = 1;

			int num = 0;
			if (start != -1 && end != -1 && start < nt.length) {
				if (end >= nt.length)
					end = nt.length - 1;

				for (int i = start; i != end; i++) 
				{
					final String slr = lr[i];
					final String snt = nt[i];

					// is task for remove all lesson in list
					synchronized (mAddList) 
					{
						if (mRemoveList != null && mRemoveList.size() > 0)
							anyRemove = true;
					}

					// removeLessonFromList probably
					// had task for remove actualy adding lesson
					// and this lesson was removed -> stop addings
					if (anyRemove && removeLessonFromList(lesson))
						return;

					if (snt == null || snt.length() < 1 || slr == null
							|| slr.length() < 1)
						continue;

					addWordEx(lesson, slr, snt, weights, weights);

					// stop initialize first words to used value
					if (initialize && num++ > Consts.MAX_LAST_LIST) 
					{
						initialize = false;
						weights = 0;
					}
					
					try
					{
						Thread.sleep(20);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}

				callUpdateListener();
			}
		}

		private boolean removeLessonFromList(int lesson) {
			boolean removethis = false;
			boolean anyRemove = false;

			synchronized (mAddList) {
				for (int r = 0; r != mRemoveList.size(); r++) {
					int remove = mRemoveList.get(r);
					mDictionary.unloadLesson(remove);
					if (remove == lesson)
						removethis = true;

					anyRemove = true;
				}
				mRemoveList.clear();
			}

			if (anyRemove && mUpdateListener != null)
				mUpdateListener.onUpdateDone();

			return removethis;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Long result) {
			mDone = true;
			callUpdateListener();
			setUpdateListener(null);
		}

		private void callUpdateListener() {
			synchronized (mAddList) {
				if (mUpdateListener != null)
					mUpdateListener.onUpdateDone();
			}

		}

		public void setUpdateListener(updateLisener ul) {
			synchronized (mAddList) {
				mUpdateListener = ul;
			}
		}

		public boolean isDone() {
			synchronized (mDone) {
				return mDone;
			}
		}

	}

	public void enableLessonAsync(int lesson, boolean enable, updateLisener ul) {
		if (enable)
			add(lesson);
		else
			remove(lesson);

		if (!isAsyncRunning()) {
			mAsyncUpdate = new LessonWorker(ul);
			mAsyncUpdate.execute("");
		}

	}

	public boolean isAsyncRunning() {
		return mAsyncUpdate != null && !mAsyncUpdate.isDone();
	}

	public void showWordsMenu() {
		Intent intent = new Intent(mContext, Dictionary.class);
		mContext.startActivity(intent);
	}
}
