package com.voc4u.activity.init;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mixpanel.android.mpmetrics.MPMetrics;
import com.voc4u.R;
import com.voc4u.activity.BaseActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.activity.dashboard.Dashboard;
import com.voc4u.controller.WordController;
import com.voc4u.controller.updateLisener;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.Consts;
import com.voc4u.setting.LangSetting;
import com.voc4u.setting.LangType;
import com.voc4u.widget.TrainWidget;

public class Init extends Activity implements OnItemSelectedListener,
		updateLisener, OnInitListener {
	private static final String TAG = "VOC4UInit";
	private int mIndex;
	protected int mLern;
	private LangType[] lernLangType;
	private TextToSpeech mTts = null;
	private LangType ltNative;
	private LangType ltLearn;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private View mSpinner1;
	private View mSpinner2;
	private View mText1;
	private View mText2;
	private View mButton;
	private View mLogo;
	private ProgressDialog mProgresDialog = null;
	private MPMetrics mMPMetrics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!BaseActivity.isDebuggable(this)) {
			mMPMetrics = MPMetrics.getInstance(this, Consts.MPMETRICS_CODE);
			mMPMetrics.track("Init activity", null);
		}
		setContentView(R.layout.init);
		CommonSetting.restore(this);

		fillNative();
		// fillLern();

		setResult(RESULT_CANCELED);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (!DialogInfo.GetChecked(DialogInfo.TYPE_INIT)) {
			showDialog(BaseActivity.DIALOG_SHOW_INFO);
		}

		mSpinner1 = findViewById(R.id.spnLern);
		mSpinner2 = findViewById(R.id.spnNative);
		mText1 = findViewById(R.id.text1);
		mText2 = findViewById(R.id.text2);
		mButton = findViewById(R.id.btnStart);
		mLogo = findViewById(R.id.logo);

	}

	@Override
	protected void onDestroy() {
		TtsShutdown();

		if (mMPMetrics != null) {
			mMPMetrics.flush();
		}

		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	private void fillNative() {
		Spinner nat = (Spinner) findViewById(R.id.spnNative);
		nat.setOnItemSelectedListener(this);
		// ArrayAdapter spinnerArrayAdapter = new Country(this,
		// android.R.layout.simple_dropdown_item_1line,
		// LangSetting.getLangArray());
		// nat.setAdapter(spinnerArrayAdapter);

		LangType[] langs = LangSetting.getLangArray();

		CountryAdapter ca = new CountryAdapter(langs);
		nat.setAdapter(ca);

		Locale def = Locale.getDefault();
		String sdef = def.getLanguage().toLowerCase();

		int select = 0;
		boolean selected = false;
		for (LangType lt : langs) {
			String sl = lt.code.toLowerCase();
			if (sl.contains(sdef)) {
				selected = true;
				break;
			}
			select++;
		}

		nat.setSelection(selected ? select : 0, false);
	}

	class CountryAdapter extends BaseAdapter {
		final LangType[] mArray;

		public CountryAdapter(LangType[] array) {
			mArray = array;
		}

		@Override
		public int getCount() {
			return mArray.length;
		}

		@Override
		public LangType getItem(int position) {
			return mArray[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return new CountryItem(Init.this.getApplicationContext(),
					getItem(position));
		}

	}

	public void onStart(View view) {
		CommonSetting.lernCode = ltLearn;
		CommonSetting.nativeCode = ltNative;
		CommonSetting.store(this);
		CommonSetting.restore(this);
		WordController.getInstance(this).enableLessonAsync(1, true, null);

		mLogo.clearAnimation();
		Animation anim1 = new RotateAnimation(0, 4320.f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		anim1.setDuration(4320);
		anim1.setRepeatCount(Animation.INFINITE);
		mLogo.startAnimation(anim1);

		// showDialog(BaseActivity.DIALOG_PROGRESS);
		mButton.setEnabled(false);
		mSpinner1.setEnabled(false);
		mSpinner2.setEnabled(false);
		// without sleep is the word setting returned back
		// because isn't load any word between
		// finish() and resume() new activity
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mMPMetrics != null) {
			try {
				JSONObject properties = new JSONObject();
				properties.put("learn", CommonSetting.lernCode.code);
				properties.put("native", CommonSetting.nativeCode.code);
				mMPMetrics.track("onStart", properties);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		mTts = new TextToSpeech(this, this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int index,
			long arg3) {
		ltNative = LangSetting.getLangArray()[index];
		fillLern();
	}

	@SuppressWarnings("unchecked")
	private void fillLern() {
		Spinner nat = (Spinner) findViewById(R.id.spnLern);
		nat.setOnItemSelectedListener(this);

		lernLangType = createArrayWithoutSelectedInNative();

		// ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
		// android.R.layout.simple_dropdown_item_1line, lernLangType);
		// nat.setAdapter(spinnerArrayAdapter);
		CountryAdapter ca = new CountryAdapter(
				createArrayWithoutSelectedInNative());
		nat.setAdapter(ca);
		nat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ltLearn = LangSetting
						.getLangTypeFromCode(lernLangType[arg2].code);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private final LangType[] createArrayWithoutSelectedInNative() {
		int m = 0;
		final LangType[] all = LangSetting.getLangArray();
		LangType[] lLT = new LangType[all.length - 1];
		for (int i = 0; i != all.length; i++) {
			LangType source = all[i];

			if (source.id == ltNative.id)
				continue;

			lLT[m++] = source;
		}

		return lLT;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateDone() {
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == BaseActivity.DIALOG_SHOW_INFO) {
			return DialogInfo.create(this);
		} else if (id == BaseActivity.DIALOG_TTS_DATA_MISSING) {
			return ShowDialogForTtsSetting(R.string.msg_tts_data_missing);
		} else if (id == BaseActivity.DIALOG_PROGRESS) {
			mProgresDialog = ProgressDialog.show(this, "",
					getString(R.string.preparing), false, true);

			mProgresDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					TtsShutdown();
				}
			});

			return mProgresDialog;
		} else
			return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (id == BaseActivity.DIALOG_SHOW_INFO)
			DialogInfo.setup(this, DialogInfo.TYPE_INIT, dialog);
		else
			super.onPrepareDialog(id, dialog);
	}

	public Dialog ShowDialogForTtsSetting(int message) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.form_dashboard);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(
				this.getResources().getString(android.R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						showTtsSetting();
					}
				}).setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						showDashboard();
					}
				});
		dialog = builder.create();
		return dialog;
	}

	private void showTtsSetting() {
		Intent intent = new Intent();
		intent.setAction("com.android.settings.TTS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		// onShowSpeechMenu();
	}

	@Override
	protected void onResume() {
		// if goes from tts setting
		if (mTts != null) {
			showDashboard();
		} else if (CommonSetting.lernCode != null
				&& CommonSetting.nativeCode != null) {
			// sometime when is application start and select you languages
			// 1. after you take start
			// 2. push back button and you are in phone screen
			// 3. hold home button and try start voc4u -> you will appear again
			// in init setting
			// but you should be in dashboard
			showDashboard();
		} else {
			animate(true);

		}

		super.onResume();
	}

	private void animate(boolean ingoing) {

		mLogo.clearAnimation();

		float m1 = ingoing ? 3.0f : 0f;
		float m1r = -1.0f * m1;

		float m2 = ingoing ? 0f : 3.f;
		float m2r = -1.0f * m2;

		Animation anim1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				m1, Animation.RELATIVE_TO_SELF, m2, Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		anim1.setDuration(300);
		anim1.setStartOffset(100);

		Animation anim12 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				m1, Animation.RELATIVE_TO_SELF, m2, Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		anim12.setDuration(300);
		anim12.setStartOffset(400);
		Animation anim2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				m1r, Animation.RELATIVE_TO_SELF, m2r, Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		anim2.setDuration(300);
		anim2.setStartOffset(100);
		Animation anim22 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				m1r, Animation.RELATIVE_TO_SELF, m2r, Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		anim22.setDuration(300);
		anim22.setStartOffset(400);
		// anim.setStartOffset(500);

		// anim2.setStartOffset(500);
		Animation anim3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, m1, Animation.RELATIVE_TO_SELF, m2);
		anim3.setDuration(300);
		anim3.setStartOffset(600);

		Animation anim4 = new AlphaAnimation(0.0f, 1.0f);
		anim4.setDuration(5500);
		anim4.setStartOffset(600);

		if (!ingoing) {
			anim1.setAnimationListener(new HideAfterFinish(mText2, false));
			anim2.setAnimationListener(new HideAfterFinish(mSpinner2, false));
			anim12.setAnimationListener(new HideAfterFinish(mText1, false));
			anim22.setAnimationListener(new HideAfterFinish(mSpinner1, false));
			anim3.setAnimationListener(new HideAfterFinish(mButton, false));
		}
		// mText1.setAnimation(anim);
		mText2.startAnimation(anim1);
		mSpinner2.startAnimation(anim2);
		mText1.startAnimation(anim12);
		mSpinner1.startAnimation(anim22);
		mButton.startAnimation(anim3);

		if (ingoing) {
			mLogo.startAnimation(anim4);
		} else {
			Animation anim5 = AnimationUtils.loadAnimation(this,
					R.anim.logo_disapear);
			mLogo.startAnimation(anim5);
			anim5.setAnimationListener(new HideAfterFinish(mLogo, true));
		}
	}

	class HideAfterFinish implements AnimationListener {

		private final View mView;
		private final boolean mLast;

		public HideAfterFinish(View v, boolean last) {
			mView = v;
			mLast = last;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mView.setVisibility(View.INVISIBLE);
			if (mLast) {
				showDashboard();
			}
		}
	}

	@Override
	public void onInit(int status) {

		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			Locale loc = CommonSetting.lernCode.toLocale();
			int result = mTts.setLanguage(loc);

			if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e(TAG,
						"Language is not available. code: " + loc.getLanguage());
				showDialog(BaseActivity.DIALOG_TTS_DATA_MISSING);
				// result = mTts.setLanguage(Locale.ENGLISH);

			}

			else if (result == TextToSpeech.LANG_MISSING_DATA) {
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
				showDialog(BaseActivity.DIALOG_TTS_DATA_MISSING);
			} else {
				// Check the documentation for other possible result codes.
				// For example, the language may be available for the locale,
				// but not for the specified country and variant.

				// The TTS engine has been successfully initialized.
				// Allow the user to press the button for the app to speak
				// again.
				// mAgainButton.setEnabled(true);
				// Greet the user.
				// sayHello();
				animate(false);
			}
		} else {
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}

	}

	private void showDashboard() {

		Intent intent = new Intent(this, Dashboard.class);
		intent.putExtra(BaseActivity.FROM_INIT, BaseActivity.FROM_INIT);

		if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
			// Push widget update to surface with newly set prefix
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);
			TrainWidget.appWasInitialized(this, appWidgetManager, mAppWidgetId);

			// Make sure we pass back the original appWidgetId
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);
		}

		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fadeout, R.anim.fadein);
	}

	private void TtsShutdown() {
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
			mTts = null;
		}
	}

}
