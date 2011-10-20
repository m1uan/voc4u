package com.voc4u.controller;

import com.voc4u.setting.CommonSetting;

public class PublicWord
{
	private final Word mWord;
	private final EPoliticy mPoliticy;
	
	private boolean success = true;
	
	
	public PublicWord(Word word, EPoliticy politicy)
	{
		mWord = word;
		mPoliticy = politicy;
	}
	
	
	public String getTestString()
	{
		if(isBasePrimary())
			return mWord.getWord();
		else
			return mWord.getWord2();
	}


	private boolean isBasePrimary()
	{
		return mPoliticy == EPoliticy.PRIMAR;
		//(CommonSetting.langType.getCrossBase() && mPoliticy == EPoliticy.PRIMAR) ||
		//		(!CommonSetting.langType.getCrossBase() && mPoliticy == EPoliticy.SECUNDAR);
	}
	
	public void setRemember(boolean remember)
	{
		int weight = getTestWeight();
		
		weight = WordController.calcWeight(weight, remember);
		
		setTestWeight(weight);
	}
	
	public void setSuccess(boolean suc)
	{
		success = suc;
	}
	
	public final boolean getSuccess()
	{
		return success;
	}
	
	public String toString()
	{
		return String.format("%s - %s (%d/%d)", getPrimary(), getSecondary(),  mWord.getWeight(), mWord.getWeight2());
	}
	
	private int getTestWeight()
	{
		if(isBasePrimary())
			return mWord.getWeight();
		else
			return mWord.getWeight2();
	}
	
	
	private void setTestWeight(int weight)
	{
		if(isBasePrimary())
			mWord.setWeight(weight);
		else
			mWord.setWeight2(weight);
	}

	
	public String getPrimary()
	{
		//return CommonSetting.langType.getCrossBase() ? mWord.getWord() : mWord.getWord2();
		return mWord.getWord();
	}
	
	public String getSecondary()
	{
		//return CommonSetting.langType.getCrossBase() ? mWord.getWord2() : mWord.getWord();
		return mWord.getWord2();
	}


	public int getId()
	{
		return mWord.getId();
	}


	public Word getBaseWord()
	{
		return mWord;
	}
	
}
