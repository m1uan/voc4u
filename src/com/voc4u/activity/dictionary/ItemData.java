package com.voc4u.activity.dictionary;

import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.LangSetting;

public class ItemData 
{
	public static final int	MAX_EXAMPLES_IN_VIEW	= 50;
	
	WordController mWCtrl;
	int mLesson;
	String mWords = null;
	boolean enabled;
	boolean filled = false;
	
	public ItemData(WordController wc, int lesson) {
		mWCtrl = wc;
		mLesson = lesson;
	}
	
	
	public boolean getEnabledLesson() {
		
		if(!filled) {
			enabled = mWCtrl.isEnableLesson(mLesson);
			filled = true;
		}
		
		return enabled;
	}
	
	public void setEnabled(boolean e){
		enabled = e;
	}
	
	public String getWords() {
		
		if(mWords != null) {
			return mWords;
		}
		
		String[] aex = LangSetting.getInitDataFromLT(CommonSetting.lernCode, mLesson);
		String mWords = "";
		
		if(aex != null && aex.length > 0)
		{
			int lex = aex.length > MAX_EXAMPLES_IN_VIEW? MAX_EXAMPLES_IN_VIEW : aex.length;

			mWords = getFirstWord(aex[0]);
			
			for(int i = 1; i < lex; i++)
				mWords += ", " + getFirstWord(aex[i]);
		}
		
		return mWords;
	}
	
	private String getFirstWord(final String string)
	{
		int inx = string.indexOf("|");
		if(inx > -1)
			return string.substring(0, inx);
		else 
			return string;
	}


	public int getLesson() {
		return mLesson;
	}
	
}
