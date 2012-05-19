package com.voc4u.controller;

import android.content.Context;

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
	
	
	public int getTestingFlag(Context ctx){
		if(isBasePrimary()) {
			return CommonSetting.lernCode.getDrawableID(ctx);
		}
		else {
			return CommonSetting.nativeCode.getDrawableID(ctx);
		}
	}
	
	public String getTestString()
	{
		if(isBasePrimary())
			return getLern();
		else
			return getNative();
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
	
	@Override
	public String toString()
	{
		return String.format("%s - %s (%d/%d)", getLern(), getNative(),  mWord.getWeight(), mWord.getWeight2());
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

	
	public String getLern()
	{
		//return CommonSetting.langType.getCrossBase() ? mWord.getWord() : mWord.getWord2();
		return mWord.getLern().replace("|", ",");
	}
	
	public String getNative()
	{
		//return CommonSetting.langType.getCrossBase() ? mWord.getWord2() : mWord.getWord();
		return mWord.getNative().replace("|", ",");
	}


	public long getId()
	{
		return mWord.getId();
	}


	public Word getBaseWord()
	{
		return mWord;
	}
	
}
