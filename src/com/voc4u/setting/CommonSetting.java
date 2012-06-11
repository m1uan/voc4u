package com.voc4u.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MPMetrics;

public class CommonSetting {
	private static final String LANG_NATIVE_NUM = "langNativeNum";

	// public static LangType langType = LangType.ENG_2_CZECH;
	public static int langNativeNum = 0;
	public static LangSetting langSetting;
	public static LangType nativeCode = null;
	public static LangType lernCode = null;
	public static boolean NSMInit = false;
	public static boolean NSMDictionary = false;
	public static boolean NSMTrain = false;
	public static boolean NSMDashboard = false;

	public static boolean[] lessonsEnambled = new boolean[Consts.NATIVE_LESSON_NUM];

	public static long nextKnowWordsLevel;

	private static final String PREFS_FILE = "preferences";
	private static final String LANG_NATIVE_CODE = "native_code";
	private static final String LANG_LERN_CODE = "lern_code";
	private static final String LESSON_ENABLED = "lessons_enabled";
	private static final String NEXT_KNOW_WORDS_LEVEL = "next_know_words_level";
	private static final String NSM_INIT = "nsm_init";
	private static final String NSM_DICTIONARY = "nsm_dictionary";
	private static final String NSM_TRAIN = "nsm_train";
	private static final String NSM_DASBOARD = "nsm_dashboard";

	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(PREFS_FILE, Activity.MODE_PRIVATE);
	}

	private static void putBoolean(Context context, String key, boolean value) {
		getPrefs(context).edit().putBoolean(key, value).commit();
	}

	private static boolean getBoolean(Context context, String key,
			boolean defValue) {
		return getPrefs(context).getBoolean(key, defValue);
	}

	private static void putBooleanArray(Context context, String key,
			boolean[] value) {
		String svalues = "";
		for (boolean v : value) {
			svalues += String.valueOf(v) + ";";
		}

		getPrefs(context).edit().putString(key, svalues).commit();
	}

	private static void getBooleanArray(Context context, String key,
			boolean[] retValues, boolean defValue) {
		String svalues = getPrefs(context).getString(key, "");

		String[] avalues = null;
		if (svalues != null && svalues.length() > 0) {
			avalues = svalues.split(";");
		}

		for (int i = 0; i != retValues.length; i++) {
			if (avalues != null && i < avalues.length) {
				retValues[i] = Boolean.parseBoolean(avalues[i]);
			} else {
				retValues[i] = defValue;
			}
		}
	}

	private static void putInt(Context context, String key, int value) {
		getPrefs(context).edit().putInt(key, value).commit();
	}

	private static int getInt(Context context, String key, int defValue) {
		return getPrefs(context).getInt(key, defValue);
	}

	private static void putLong(Context context, String key, long value) {
		getPrefs(context).edit().putLong(key, value).commit();
	}

	private static long getLong(Context context, String key, int defValue) {
		return getPrefs(context).getLong(key, defValue);
	}

	private static void putString(Context context, String key, String value) {
		getPrefs(context).edit().putString(key, value).commit();
	}

	private static String getString(Context context, String key, String defValue) {
		return getPrefs(context).getString(key, defValue);
	}

	public static void store(Context context) {
		// putInt(context, LANG_TYPE, langType == LangType.CZECH_2_ENG ? 0 : 1);
		putInt(context, LANG_NATIVE_NUM, langNativeNum);
		putString(context, LANG_NATIVE_CODE,
				nativeCode != null ? nativeCode.code : "INIT");
		putString(context, LANG_LERN_CODE, lernCode != null ? lernCode.code
				: "INIT");
		putBoolean(context, NSM_INIT, NSMInit);
		putBoolean(context, NSM_DICTIONARY, NSMDictionary);
		putBoolean(context, NSM_TRAIN, NSMTrain);
		putBoolean(context, NSM_DASBOARD, NSMDashboard);

		putBooleanArray(context, LESSON_ENABLED, lessonsEnambled);
		putLong(context, NEXT_KNOW_WORDS_LEVEL, nextKnowWordsLevel);
	}

	public static void restore(Context context) {
		// langType = getInt(context, LANG_TYPE, 0) == 0 ? LangType.CZECH_2_ENG
		// : LangType.ENG_2_CZECH;
		langNativeNum = getInt(context, LANG_NATIVE_NUM,
				Consts.MAX_WORD_NATIVE_LEARN / 2);

		final String nativeCd = getString(context, LANG_NATIVE_CODE, "INIT");
		final String lernCd = getString(context, LANG_LERN_CODE, "INIT");

		nativeCode = LangSetting.getLangTypeFromCode(nativeCd);
		lernCode = LangSetting.getLangTypeFromCode(lernCd);

		NSMInit = getBoolean(context, NSM_INIT, false);
		NSMDictionary = getBoolean(context, NSM_DICTIONARY, false);
		NSMTrain = getBoolean(context, NSM_TRAIN, false);
		NSMDashboard = getBoolean(context, NSM_DASBOARD, false);

		nextKnowWordsLevel = getLong(context, NEXT_KNOW_WORDS_LEVEL,
				Consts.FIRST_KNOWN_LEVEL);
		getBooleanArray(context, LESSON_ENABLED, lessonsEnambled, false);
	}

	public static MPMetrics initMetrics(Context ctx) {
		MPMetrics mMPMetrics = MPMetrics
				.getInstance(ctx, Consts.MPMETRICS_CODE);
		Log.i("voc4u", "mMPMetrics enabled");

		JSONObject properties = new JSONObject();
		try {
			if (CommonSetting.lernCode != null) {
				properties.put("learn", CommonSetting.lernCode.code);
			}
			if (CommonSetting.nativeCode != null) {
				properties.put("native", CommonSetting.nativeCode.code);
			}
			properties.put("version", Consts.VERSION);

			long i = 0;

			for (int pos = CommonSetting.lessonsEnambled.length - 1; pos > -1; pos--) {
				long e = CommonSetting.lessonsEnambled[pos] ? 1 : 0;
				i = (i << 1) | e;
			}
			properties.put("lesson_enabled", i);
			mMPMetrics.registerSuperProperties(properties);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mMPMetrics;
	}

}
