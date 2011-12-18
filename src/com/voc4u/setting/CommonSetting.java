package com.voc4u.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class CommonSetting
{
	private static final String LANG_NATIVE_NUM = "langNativeNum";
	
	//public static LangType langType = LangType.ENG_2_CZECH;
	public static int langNativeNum = 0;
	public static LangSetting langSetting;
	public static LangType nativeCode = null;
	public static LangType lernCode = null;
	public static boolean NSMInit = false;
	public static boolean NSMDictionary = false;
	public static boolean NSMTrain = false;
	public static boolean NSMDashboard = false;
	
	private static final String	PREFS_FILE	= "preferences";
	private static final String LANG_NATIVE_CODE = "native_code";
	private static final String LANG_LERN_CODE = "lern_code";
	private static final String NSM_INIT = "nsm_init";
	private static final String NSM_DICTIONARY = "nsm_dictionary";
	private static final String NSM_TRAIN = "nsm_train";
	private static final String NSM_DASBOARD = "nsm_dashboard";
	
	private static SharedPreferences getPrefs(Context context)
	{
		return context.getSharedPreferences(PREFS_FILE, Activity.MODE_PRIVATE);
	}
	
	private static void putBoolean(Context context, String key, boolean value)
	{
		getPrefs(context).edit().putBoolean(key, value).commit();
	}
	
	private static boolean getBoolean(Context context, String key, boolean defValue)
	{
		return getPrefs(context).getBoolean(key, defValue);
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
		putString(context, LANG_NATIVE_CODE, nativeCode != null ? nativeCode.code : "INIT");
		putString(context, LANG_LERN_CODE, lernCode != null ? lernCode.code : "INIT");
		putBoolean(context, NSM_INIT, NSMInit);
		putBoolean(context, NSM_DICTIONARY, NSMDictionary);
		putBoolean(context, NSM_TRAIN, NSMTrain);
		putBoolean(context, NSM_DASBOARD, NSMDashboard);
	}
	
	public static void restore(Context context)
	{
		//langType = getInt(context, LANG_TYPE, 0) == 0 ? LangType.CZECH_2_ENG : LangType.ENG_2_CZECH;
		langNativeNum = getInt(context, LANG_NATIVE_NUM, Consts.MAX_WORD_NATIVE_LEARN / 2);
		
		final String nativeCd = getString(context, LANG_NATIVE_CODE, "INIT");
		final String lernCd = getString(context, LANG_LERN_CODE, "INIT");
		
		nativeCode = LangSetting.getLangTypeFromCode(nativeCd);
		lernCode = LangSetting.getLangTypeFromCode(lernCd);
		
		NSMInit = getBoolean(context, NSM_INIT, false);
		NSMDictionary = getBoolean(context, NSM_DICTIONARY, false);
		NSMTrain = getBoolean(context, NSM_TRAIN, false);
		NSMDashboard = getBoolean(context, NSM_DASBOARD, false);
		//langSetting = new LangSetting(context);
	}
	
	
}
