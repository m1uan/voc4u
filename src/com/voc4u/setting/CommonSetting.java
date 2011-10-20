package com.voc4u.setting;

import com.voc4u.core.LangSetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CommonSetting
{
	private static final String LANG_NATIVE_NUM = "langNativeNum";
	private static final String LANG_TYPE = "langType";
	public static final int INITIAL_LESSON = 40;
	public static final int LESSON_NUM = 500;
	public static final int MAX_LAST_LIST = 15;
	
	//public static LangType langType = LangType.ENG_2_CZECH;
	public static int langNativeNum = 0;
	public static boolean DEBUG = true;
	public static LangSetting langSetting;
	public static LangType nativeCode = null;
	public static LangType lernCode = null;
	
	private static final String	PREFS_FILE	= "preferences";
	private static final String LANG_NATIVE_CODE = "native_code";
	private static final String LANG_LERN_CODE = "lern_code";
	
	private static SharedPreferences getPrefs(Context context)
	{
		return context.getSharedPreferences(PREFS_FILE, Activity.MODE_PRIVATE);
	}
	
	private static void putInt(Context context, String key, int value)
	{
		getPrefs(context).edit().putInt(key, value).commit();
	}
	
	private static int getInt(Context context, String key, int defValue)
	{
		return getPrefs(context).getInt(key, defValue);
	}
	
	private static void putString(Context context, String key, String value)
	{
		getPrefs(context).edit().putString(key, value).commit();
	}
	
	private static String getString(Context context, String key, String defValue)
	{
		return getPrefs(context).getString(key, defValue);
	}
	
	public static void store(Context context)
	{
		//putInt(context, LANG_TYPE, langType == LangType.CZECH_2_ENG ? 0 : 1);
		putInt(context, LANG_NATIVE_NUM, langNativeNum);
		putString(context, LANG_NATIVE_CODE, nativeCode.code);
		putString(context, LANG_LERN_CODE, lernCode.code);
	}
	
	public static void restore(Context context)
	{
		//langType = getInt(context, LANG_TYPE, 0) == 0 ? LangType.CZECH_2_ENG : LangType.ENG_2_CZECH;
		langNativeNum = getInt(context, LANG_NATIVE_NUM, Consts.MAX_WORD_NATIVE_LEARN / 2);
		
		final String nativeCd = getString(context, LANG_NATIVE_CODE, "PL");
		final String lernCd = getString(context, LANG_LERN_CODE, "PT");
		
		nativeCode = LangSetting.getLangTypeFromCode(nativeCd);
		lernCode = LangSetting.getLangTypeFromCode(lernCd);
		//langSetting = new LangSetting(context);
	}
	
	
}
