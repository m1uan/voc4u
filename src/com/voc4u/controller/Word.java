package com.voc4u.controller;

public class Word
{
	private final int mId;
	private final String mWord;
	private final String mWord2;
	private int mWeight;
	private int mWeight2;
	
	
	
	public Word(int id, String word, String word2, int weight, int weight2)
	{
		this.mId = id;
		this.mWord = word;
		this.mWord2 = word2;
		mWeight = weight;
		mWeight2 = weight2;
	}
	
	public final int getId()
	{
		return mId;
	}

	public final String getWord()
	{
		return mWord;
	}

	public final String getWord2()
	{
		return mWord2;
	}
	
	public final int getWeight()
	{
		return mWeight;
	}

	public final int getWeight2()
	{
		return mWeight2;
	}
	
	public final void setWeight(int weight)
	{
		mWeight = weight;
	}

	public final void setWeight2(int weight)
	{
		mWeight2 = weight;
	}
	

}
