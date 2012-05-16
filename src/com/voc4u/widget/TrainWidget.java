package com.voc4u.widget;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.voc4u.R;
import com.voc4u.activity.init.Init;
import com.voc4u.activity.train.Train;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;

public class TrainWidget extends AppWidgetProvider

{
	private static final String WORD_INDENT_ID = "word";
	public static final String ACTION_WIDGET_RECEIVER = "23432432";
	public static String ACTION_WIDGET_PLAY = "ActionReceiverPLAY";
	public static String ACTION_WIDGET_TRAIN = "ActionReceiverTRAIN";
	public static String ACTION_WIDGET_INIT = "ActionReceiverINIT";
	public static String ACTION_WIDGET_KNOW = "ActionReceiverNEXT";
	public static String ACTION_WIDGET_DONT = "ActionReceiverWORD";
	private Context mContext;
	private static String TAG = "TrainWidget";
	private RemoteViews mRemoteViews;
	private int mAppWidgetId;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// called when widgets are deleted
		// see that you get an array of widgetIds which are deleted
		// so handle the delete of multiple widgets in an iteration
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		// runs when all of the instances of the widget are deleted from
		// the home screen
		// here you can do some setup
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		mContext = context;

		CommonSetting.restore(context);
		if (cannotContinue()) {
			onInitClick(context);
		}

	}

	public static void appWasInitialized(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId);

		// Construct the RemoteViews object. It takes the package name (in our
		// case, it's our
		// package, but it needs this because on the other side it's the widget
		// host inflating
		// the layout from our package).
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		remoteViews.setViewVisibility(R.id.info, View.GONE);
		remoteViews.setViewVisibility(R.id.flag, View.VISIBLE);
		setListVisibility(remoteViews, View.VISIBLE);

		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_PLAY);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_KNOW);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.nextButton,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_DONT);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.wordTextView,
				actionPendingIntent);

		setupActualWord(context, remoteViews);
		// Tell the widget manager
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

	private static void setListVisibility(RemoteViews remoteViews, int visible) {
		int[] id_text1 = new int[] { R.id.word_1, R.id.word_2, R.id.word_3, R.id.word_4, R.id.word_5 };
		for (int id : id_text1) {
			remoteViews.setViewVisibility(id, visible);
		}
		remoteViews.setViewVisibility(R.id.info, View.GONE);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(TAG, "onUpdate");

		for (int id : appWidgetIds) {

		}

		mAppWidgetId = appWidgetIds[0];

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		if (cannotContinue()) {
			CommonSetting.restore(context);
			if (cannotContinue()) {
				setInactiveMode(context, remoteViews);
				appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
				return;
			}
		}

			setupActualWord(context, remoteViews);
			setupButtons(context, remoteViews);
		
		// remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	private void setInactiveMode(Context context, RemoteViews remoteViews) {
		Log.i(TAG, "setInactiveMode");
		String s = context.getResources().getString(R.string.form_init);
		remoteViews.setViewVisibility(R.id.info, View.VISIBLE);
		remoteViews.setTextViewText(R.id.wordTextView, s);
		remoteViews.setViewVisibility(R.id.flag, View.GONE);
		setListVisibility(remoteViews, View.GONE);

		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_INIT);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout,
				actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.nextButton,
				actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.dontKnowButton,
				actionPendingIntent);
	}

	private static void setupButtons(Context context, RemoteViews remoteViews) {
		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_PLAY);
		active.putExtra(WORD_INDENT_ID, "ahoj");
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);

		// remoteViews.setOnClickPendingIntent(R.id.playButton,
		// actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_TRAIN);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_KNOW);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.nextButton,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_DONT);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.dontKnowButton,
				actionPendingIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_WIDGET_INIT)) {
			Log.i("onReceive", ACTION_WIDGET_INIT);
			if (cannotContinueWithRestore(context)) {
				onInitClick(context);
				return;
			}
			else {
				RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
						R.layout.widget);
				updateWidget(context, remoteViews);
			}
			// onPlayClick(context, intent);
		} else if (intent.getAction().equals(ACTION_WIDGET_KNOW)) {
			Log.i("onReceive", ACTION_WIDGET_KNOW);
			knowOrDont(context, true);
		} else if (intent.getAction().equals(ACTION_WIDGET_DONT)) {
			knowOrDont(context, false);
		} else if (intent.getAction().equals(ACTION_WIDGET_TRAIN)) {
			onTrainClick(context);
		}
		super.onReceive(context, intent);
	}

	private void onInitClick(Context context) {
		Log.i(TAG, "onPlayClick");
		final Intent intent = new Intent(context, Init.class);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		Log.d(TAG, String.format("putExtra:%d", appWidgetIds[0]));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void onTrainClick(Context context) {
		Log.i(TAG, "onPlayClick");
		final Intent intent = new Intent(context, Train.class);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		Log.d(TAG, String.format("putExtra:%d", appWidgetIds[0]));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void onPlayClick(Context context, Intent intent2) {
		Log.i(TAG, "onPlayClick");
		final Intent intent = new Intent(context, Train.class);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		Log.d(TAG, String.format("putExtra:%d", appWidgetIds[0]));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// final ComponentName cn = new ComponentName("com.voc4u.czen",
		// "com.voc4u.activity.Train");

		// intent.setComponent(cn);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(context, "test2", Toast.LENGTH_LONG).show();
		context.startActivity(intent);

		// TextToSpeechController ttsc =
		// TextToSpeechController.getInstance(context);
		// ttsc.speak(text);
	}

	private void knowOrDont(Context context, boolean know) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		if (remoteViews != null) {
			if (cannotContinueWithRestore(context)) {
				updateWidget(context, remoteViews);
				return;
			}

			Log.i("onWordClick", ACTION_WIDGET_DONT);

			WordController wc = WordController.getInstance(context);

			PublicWord pw = wc.getActualPublicWord();
			wc.updatePublicWord(know);

			pw = wc.getFirstPublicWord();
			// setupActualWord(remoteViews, pw);

			setupActualWord(context, remoteViews);
			updateLastList(context, remoteViews);

			updateWidget(context, remoteViews);
		}
	}

	private static boolean cannotContinueWithRestore(Context context) {
		if (cannotContinue()) {
			CommonSetting.restore(context);
			return cannotContinue();
		}
		return false;
	}

	private static boolean cannotContinue() {
		return CommonSetting.lernCode == null
				|| CommonSetting.nativeCode == null;
	}

	private void updateWidget(Context context, RemoteViews remoteViews) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		onUpdate(context, appWidgetManager, appWidgetIds);
		appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);
	}

	private static void updateLastList(Context context, RemoteViews remoteViews) {
		int[] id_text1 = new int[] { R.id.text1_1, R.id.text1_2, R.id.text1_3,
				R.id.text1_4, R.id.text1_5 };
		int[] id_text2 = new int[] { R.id.text2_1, R.id.text2_2, R.id.text2_3,
				R.id.text2_4, R.id.text2_5 };

		List<PublicWord> ll = WordController.getInstance(context).getLastList();
		int llmax = ll.size() > 6 ? 6 : ll.size();
		for (int i = 1; i != llmax; i++) {
			int size = ll.size();
			int step = (i + 1);
			int pos = size - step;
			PublicWord pwl = ll.get(pos);

			int listpos = i - 1;

			remoteViews.setTextViewText(id_text1[listpos], pwl.getLern());
			remoteViews.setTextViewText(id_text2[listpos], pwl.getNative());
			remoteViews.setTextColor(id_text1[listpos],
					pwl.getSuccess() ? Color.BLACK : Color.RED);
			remoteViews.setTextColor(id_text2[listpos],
					pwl.getSuccess() ? Color.BLACK : Color.RED);
		}
	}

	public static void setupActualWord(Context context, RemoteViews remoteViews) {
		Log.i(TAG, "setupActualWord");
		// remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);
		if (!cannotContinueWithRestore(context)) {
			PublicWord pw = WordController.getInstance(context)
					.getActualPublicWord();

			Log.d(TAG, String.format("setupActualWord:%s", pw.getLern()));

			remoteViews.setTextViewText(R.id.wordTextView, pw.getTestString());
			if (CommonSetting.lernCode != null && CommonSetting.nativeCode != null) {
				remoteViews.setImageViewResource(R.id.flag,
						pw.getTestingFlag(context));
			}
		}

		
		// remoteViews.setTextViewText(R.id.word2TextView, pw.getNative());
	}

	public void onWordTextViewClick(View v) {
		Log.d(TAG, "onWordTextViewClick()");

		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.widget);
		// rv.setViewVisibility(R.id.word2TextView, View.VISIBLE);
	}

	public static void appAfterTrain(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		setupActualWord(context, remoteViews);
		setupButtons(context, remoteViews);
		updateLastList(context, remoteViews);

		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

}
