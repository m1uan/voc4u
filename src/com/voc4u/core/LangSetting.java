package com.voc4u.core;

import com.voc4u.lang.DataCS;
import com.voc4u.lang.DataDE;
import com.voc4u.lang.DataEN;
import com.voc4u.lang.DataES;
import com.voc4u.lang.DataFR;
import com.voc4u.lang.DataPL;
import com.voc4u.lang.DataPT;
import com.voc4u.setting.LangType;
import com.voc4u.setting.LangTypeText;

import android.R;
import android.content.Context;
import android.content.res.Resources;

public class LangSetting
{
	public static final String LANG_DE = "DE";
	public static final String LANG_CZ = "CZ";
	public static final String LANG_EN = "EN";
	public static final String LANG_ES = "ES";
	public static final String LANG_FR = "FR";
	public static final String LANG_PT = "PT";
	public static final String LANG_PL = "PL";
	
	public static final LangType CZ = new LangType(1, LANG_CZ);
	public static final LangType DE = new LangType(2, LANG_DE);
	public static final LangType EN = new LangType(3, LANG_EN);
	public static final LangType ES = new LangType(4, LANG_ES);
	public static final LangType FR = new LangType(5, LANG_FR);
	public static final LangType PL = new LangType(6, LANG_PL);
	public static final LangType PT = new LangType(7, LANG_PT);
	
	/**
	 * num words in lesson
	 * size of array is define num of lessons
	 */
	public static final int[] LESSON_SIZES = new int[]{ 50, 500, 1000, 500};
	
	
	/**
	 * calc position on getLangTextArray for lesson
	 * see to LESSON_SIZES
	 * @param lesson
	 * @return
	 */
	public static int getLessonStart(int lesson) 
	{
		int result = -1;
		if(lesson <= LangSetting.LESSON_SIZES.length)
		{
			result = 0;
			for(int i = 0; i < lesson;i++)
			{
				result += LangSetting.LESSON_SIZES[i];
			}
		}
		return result;
	}
	
	public final static LangTypeText[] getLangTextArray(Context ctx)
	{
		LangType[] alt = getLangArray();
		LangTypeText[] altt = new LangTypeText[alt.length];
		
		for(int i = 0; i != altt.length; i++)
		{
			altt[i] = new LangTypeText(alt[i], ctx);
		}
		
		return altt;
	}
	
	
	public final static LangType[] getLangArray()
	{
		return new LangType[]
		              {
						CZ,
						(DE),
						(EN),
						(ES),
						(FR),
						(PL),
						(PT),
		              };	
	}
	
	
	
	public static LangType getLangTypeFromCode(final String code)
	{	
		LangType[] lang = getLangArray();
		
		for(int i = 0; i != lang.length; i++)
		{
			if(lang[i].code.contentEquals(code))
			{
				return lang[i];
			}
		}
		
//		if(code.contentEquals(LangSetting.LANG_CZ))
//			return CZ;
//		else if(code.contentEquals(LangSetting.LANG_EN))
//			return EN;
//		else if(code.contentEquals(LangSetting.LANG_DE))
//			return DE;
//		else if(code.contentEquals(LangSetting.LANG_FR))
//			return FR;
//		else if(code.contentEquals(LangSetting.LANG_ES))
//			return ES;
//		else if(code.contentEquals(LangSetting.LANG_PL))
//			return PL;
//		else if(code.contentEquals(LangSetting.LANG_PT))
//			return PT;
		
		return null;
	}
	
	public static String[] getInitDataFromLT(final LangType lt)
	{
		final String code = lt.code;
		
		if(code.contentEquals(LangSetting.LANG_CZ))
			return DataCS.text;
		else if(code.contentEquals(LangSetting.LANG_EN))
			return DataEN.text;
		else if(code.contentEquals(LangSetting.LANG_DE))
			return DataDE.text;
		else if(code.contentEquals(LangSetting.LANG_FR))
			return DataFR.text;
		else if(code.contentEquals(LangSetting.LANG_ES))
			return DataES.text;
		else if(code.contentEquals(LangSetting.LANG_PL))
			return DataPL.text;
		else if(code.contentEquals(LangSetting.LANG_PT))
			return DataPT.text;
		
		return null;
	}
}
