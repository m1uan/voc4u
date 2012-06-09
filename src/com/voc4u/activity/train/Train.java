package com.voc4u.activity.train;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.voc4u.R;
import com.voc4u.activity.BaseWordActivity;
import com.voc4u.activity.DialogInfo;
import com.voc4u.activity.dashboard.Dashboard;
import com.voc4u.controller.DictionaryOpenHelper.NUM_WORDS_TYPE;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.Word;
import com.voc4u.setting.CommonSetting;
import com.voc4u.setting.Consts;
import com.voc4u.widget.TrainWidget;

public class Train extends BaseWordActivity implements OnItemClickListener {
	public PublicWord mPublicWord;
	private TextView mWord2TextView;
	private Button mDontKnowButton;
	private View vWord;
	private View vLogo;
	private ImageView ivFlag;

	// if true set word as known
	private final boolean mKnowetIt = true;
	private String TAG;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private ListView lvLastItems;
	private View mKnowButtonLayout;
	private Button mKnowButton;
	private TextView tvTestWord;
	private static LastListAdapter mListAdapter;

	public static String NUM_KNOW_PARAM = "NUM_KNOW_PARAM";

	// know words which have higher learn as 0 and higher native as 0
	// for facebook and user info progress
	long mNumRealKnow = -1;
	// only counting how many user push button know for MixPanel
	int numKnow = 0;
	// only counting how many user push button dontknow for MixPanel
	int numDontKnow = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mWord2TextView = (TextView) findViewById(R.id.word2TextView);
		mDontKnowButton = (Button) findViewById(R.id.dontKnowButton);
		mKnowButtonLayout = findViewById(R.id.nextButtonLayout);
		mKnowButton = (Button) findViewById(R.id.nextButton);
		// if(mWCtrl != null && mWCtrl.count() > 0)
		// setupFirstWord();

		mPublicWord = null;
		lvLastItems = (ListView) findViewById(R.id.lastList);
		lvLastItems.setOnItemClickListener(this);

		vWord = (View) findViewById(R.id.word);
		vLogo = (View) findViewById(R.id.logo);

		ivFlag = (ImageView) findViewById(R.id.flag);
		tvTestWord = (TextView) findViewById(R.id.wordTextView);
		registerForContextMenu(lvLastItems);

		if (mMPMetrics != null) {
			mMPMetrics.track("Train", null);
		}

		// for case when is orientation changed
		if (mNumRealKnow == -1) {
			mNumRealKnow = getIntent().getLongExtra(NUM_KNOW_PARAM, -1);
		}
	}

	@Override
	public void onResumeSuccess() {
		mListAdapter = new LastListAdapter(this);
		lvLastItems.setAdapter(mListAdapter);

		if (mPublicWord == null)
			setupFirstWord(false);

		super.onResumeSuccess();

		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.dashboard_listen);
		mDontKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech);
		mKnowButtonLayout.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain);
		vWord.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				ivFlag.setVisibility(View.INVISIBLE);
				tvTestWord.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animateTestWord();
			}
		});
		anim = AnimationUtils.loadAnimation(this, R.anim.train_list);
		vLogo.startAnimation(anim);
		lvLastItems.startAnimation(anim);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (mNumRealKnow == -1) {
			mNumRealKnow = mWCtrl.getNumWordsInDB(NUM_WORDS_TYPE.KNOWS);
		}
	}

	private void setupFirstWord(boolean loadNew) {

		// load new public word
		if (mWCtrl != null) {
			if (loadNew)
				mPublicWord = mWCtrl.getFirstPublicWord();
			else
				mPublicWord = mWCtrl.getActualPublicWord();
		}

		// Assert.assertTrue(mPublicWord != null);
		if (mPublicWord != null) {

			tvTestWord.setText(mPublicWord.getTestString());

			mWord2TextView.setText(mPublicWord.getBaseWord().getWeight() + "/"
					+ mPublicWord.getBaseWord().getWeight2());
			// mWord2TextView.setVisibility(View.VISIBLE);
			mWord2TextView.setVisibility(Consts.DEBUG ? View.VISIBLE
					: View.GONE);

			ivFlag.setImageResource(mPublicWord.getTestingFlag(this));
		}
	}

	@Override
	protected void onResume() {

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.dashboard_listen_r);
		mDontKnowButton.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboard_speech_r);
		mKnowButtonLayout.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.dashboardtrain_r);
		anim = AnimationUtils.loadAnimation(this, R.anim.train_list_r);
		vLogo.startAnimation(anim);
		lvLastItems.startAnimation(anim);
		vWord.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDontKnowButton.setVisibility(View.INVISIBLE);
				mKnowButtonLayout.setVisibility(View.INVISIBLE);
				vWord.setVisibility(View.INVISIBLE);
				vLogo.setVisibility(View.INVISIBLE);
				lvLastItems.setVisibility(View.INVISIBLE);
				finish();
				overridePendingTransition(R.anim.fadeout, R.anim.fadein);
			}
		});
	}

	@Override
	protected void onPause() {

		/*
		 * AppWidgetManager appWidgetManager =
		 * AppWidgetManager.getInstance(this);
		 * 
		 * RemoteViews views = new RemoteViews(this.getPackageName(),
		 * R.layout.main);
		 * 
		 * TrainWidget.setupActualWord(views, WordController.getInstance(this)
		 * .getActualPublicWord());
		 * appWidgetManager.updateAppWidget(mAppWidgetId, views);
		 * 
		 * Intent resultValue = new Intent();
		 * resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
		 * mAppWidgetId); setResult(RESULT_OK, resultValue);
		 */
		super.onPause();
	}

	public void onPlayButton(View v) {
		onPlay(mPublicWord.getLern());
	}

	public void onDontKnowButton(View v) {
		updateWord(false);
		numDontKnow++;
	}

	public void onNextButton(View v) {
		// get actual word because after update will be else
		Word w = mPublicWord.getBaseWord();
		PublicWord pw = mPublicWord;

		// update before show user result
		// because when sommeting happens and this word isn't finish saved
		// user could see information about improve his skill many times
		updateWord(true);

		// get values for debug
		int weight1 = w.getWeight();
		int weight2 = w.getWeight2();

		// test if this word is new know word
		if ((pw.isBasePrimary() && weight1 == 1 && weight2 > 1)
				|| (!pw.isBasePrimary() && weight1 > 1 && weight2 == 1)) {
			mNumRealKnow++;

			if (mNumRealKnow > CommonSetting.nextKnowWordsLevel) {
				showDialog(DIALOG_PROGRESS_FACEBOOK);
				CommonSetting.nextKnowWordsLevel = CommonSetting.nextKnowWordsLevel
						+ CommonSetting.nextKnowWordsLevel * 2;
				CommonSetting.store(this);

			}
		}
		// showDialog(DIALOG_PROGRESS_FACEBOOK);
		numKnow++;

	}

	private void updateWord(final boolean know) {
		enableActionButton(false);
		Animation anim = new TranslateAnimation(0, 0, 0, 200);
		anim.setDuration(300);
		Animation anim3 = new TranslateAnimation(0, 0, 0, 150);
		anim3.setDuration(200);

		ivFlag.startAnimation(anim3);
		anim3.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivFlag.setVisibility(View.INVISIBLE);
			}
		});

		// tvTestWord.setTextColor(Color.BLACK);
		tvTestWord.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				lvLastItems.invalidateViews();

				tvTestWord.setVisibility(View.INVISIBLE);
				mWCtrl.updatePublicWord(know);
				setupFirstWord(true);
				animateTestWord();
			}
		});

		// mListAdapter = new LastListAdapter(this);

		// lvLastItems.setAdapter(mListAdapter);

		// Animation anim = AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in);
		// anim.setDuration(500);
		// LastItem v = (LastItem)mListAdapter.getLastView();
		// if(v != null) {
		// v.startAnimation(anim );
		//
		// new Handler().postDelayed(new Runnable() {
		//
		// public void run() {
		//
		// }
		//
		// }, anim.getDuration());
		// }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		PublicWord pw = (PublicWord) lvLastItems.getItemAtPosition(position);
		onPlay(pw.getLern());
	}

	@Override
	protected int getContentView() {
		return R.layout.train;
	}

	@Override
	protected String GetShowInfoType() {
		return DialogInfo.TYPE_TRAIN;
	}

	@Override
	public void doRedrawList() {
		lvLastItems.invalidateViews();
	}

	private void animateTestWord() {
		Animation anim2 = new TranslateAnimation(300, 0, 0, 0);
		anim2.setDuration(300);
		anim2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				enableActionButton(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				enableActionButton(true);
				// Animation anim = new ScaleAnimation(1, 0.8f, 1, 0.8f);
				// anim.setRepeatCount(-1);
				// anim.setRepeatMode(Animation.REVERSE);
				// anim.setDuration(500);
				// tvTestWord.startAnimation(anim);
			}

		});
		tvTestWord.setAnimation(anim2);
		tvTestWord.setVisibility(View.VISIBLE);

		Animation anim4 = new TranslateAnimation(-300, 0, 0, 0);
		anim4.setDuration(300);
		ivFlag.startAnimation(anim4);
		ivFlag.setVisibility(View.VISIBLE);
	}

	private void enableActionButton(boolean enabled) {
		mDontKnowButton.setClickable(enabled);
		mKnowButton.setClickable(enabled);
		mDontKnowButton.setEnabled(enabled);
		mKnowButton.setEnabled(enabled);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onHome();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean onHome() {
		if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
			Intent intent = new Intent(this, Dashboard.class);

			// Push widget update to surface with newly set prefix
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);
			TrainWidget.appAfterTrain(this, appWidgetManager, mAppWidgetId);

			// Make sure we pass back the original appWidgetId
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);

			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onHome();
		}
	}

	@Override
	public void onDestroy() {
		if (mMPMetrics != null) {
			try {
				JSONObject properties = new JSONObject();
				properties.put("know_count", numKnow);
				properties.put("dontknow_count", numDontKnow);
				properties.put("total_count", numDontKnow + numKnow);
				properties.put("time_in_train", getTotalTime());
				mMPMetrics.track("Train_leave", properties);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_PROGRESS_FACEBOOK) {
			return prepareDialogAboutUserProgress();
		} else {
			return super.onCreateDialog(id);
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (id == DIALOG_PROGRESS_FACEBOOK) {
			String progresInfo = getResources().getString(
					R.string.msg_congratulation, mNumRealKnow);
			TextView tv = (TextView) dialog
					.findViewById(R.id.tvMsgCongratulation);
			tv.setText(progresInfo);
		} else {
			super.onPrepareDialog(id, dialog);
		}

	}

	private Dialog prepareDialogAboutUserProgress() {
		final Dialog dialog = new Dialog(this);

		// dialog.
		dialog.setContentView(R.layout.dialog_words_known_info);
		dialog.setTitle(R.string.tilte_congratulation);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		// lp.height = WindowManager.LayoutParams.FILL_PARENT;
		// dialog.show();
		dialog.getWindow().setAttributes(lp);
		dialog.show();

		dialog.findViewById(R.id.btnOk).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.findViewById(R.id.btnFacebook).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showFacebookDialog(dialog);
					}

				});

		return dialog;
	}

	private void showFacebookDialog(final Dialog dialog) {
		final Facebook mFacebook = new Facebook("184157091711392");
		// post on user's wall.
		Bundle parameters = new Bundle();
		parameters.putString("link",
				"https://play.google.com/store/apps/details?id=com.voc4u");
		parameters.putString("picture",
				"http://voc4u9.appspot.com/styles/icon_hg.png");

		Resources res = getResources();

		parameters.putString("name",
				res.getString(R.string.msg_facebook_msg, mNumRealKnow));
		parameters.putString("caption",
				res.getString(R.string.msg_facebook_caption));

		parameters.putString("description",
				res.getString(R.string.msg_facebook_desc));
		mFacebook.dialog(Train.this, "feed", parameters, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				dialog.dismiss();
			}

			@Override
			public void onFacebookError(FacebookError error) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onCancel() {
			}
		});
	}
}
