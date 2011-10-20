package com.voc4u.widget;

import com.voc4u.activity.train.Train;
import com.voc4u.controller.PublicWord;
import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;
import com.voc4u.czen1.R.id;
import com.voc4u.czen1.R.layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TrainWidget extends AppWidgetProvider
	
{
	private static final String WORD_INDENT_ID = "word";
	public static final String ACTION_WIDGET_RECEIVER = "23432432";
	public static String ACTION_WIDGET_PLAY = "ActionReceiverPLAY";
	public static String ACTION_WIDGET_NEXT = "ActionReceiverNEXT";
	public static String ACTION_WIDGET_WORD = "ActionReceiverWORD";
	private Context mContext;
	private static String TAG = "TrainWidget";
	private RemoteViews mRemoteViews;
	private int mAppWidgetId;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		// called when widgets are deleted
		// see that you get an array of widgetIds which are deleted
		// so handle the delete of multiple widgets in an iteration
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context)
	{
		super.onDisabled(context);
		// runs when all of the instances of the widget are deleted from
		// the home screen
		// here you can do some setup
	}

	@Override
	public void onEnabled(Context context)
	{
		super.onEnabled(context);
		
		mContext = context;
		// runs when all of the first instance of the widget are placed
		// on the home screen
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		Log.i(TAG, "onUpdate");
		
		for(int id : appWidgetIds)
		{
		
		}
		
		mAppWidgetId = appWidgetIds[0];
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);

		setupActualWord(remoteViews,WordController.getInstance(context).getActualPublicWord());
		setupButtons(context, remoteViews);

		//remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	private void setupButtons(Context context, RemoteViews remoteViews)
	{
		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_PLAY);
		active.putExtra(WORD_INDENT_ID, "ahoj");
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		
		remoteViews.setOnClickPendingIntent(R.id.playButton,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_NEXT);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.nextButton,
				actionPendingIntent);

		active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_WORD);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.wordTextView,
				actionPendingIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(ACTION_WIDGET_PLAY))
		{
			Log.i("onReceive", ACTION_WIDGET_PLAY);
			onPlayClick(context, intent);
		} else if (intent.getAction().equals(ACTION_WIDGET_NEXT))
		{
			Log.i("onReceive", ACTION_WIDGET_NEXT);
			onNextClick(context);
		} else if (intent.getAction().equals(ACTION_WIDGET_WORD))
		{
			onWordClick(context);
		} else
		{
			
		}
		super.onReceive(context, intent);
	}

	private void onPlayClick(Context context, Intent intent2)
	{
		Log.i(TAG, "onPlayClick");
		final Intent intent = new Intent(context, Train.class);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		
		Log.d(TAG, String.format("putExtra:%d", appWidgetIds[0]));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		
		//final ComponentName cn = new ComponentName("com.voc4u.czen", "com.voc4u.activity.Train");

		//intent.setComponent(cn);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(context, "test2", Toast.LENGTH_LONG).show();
		context.startActivity(intent);

		//TextToSpeechController ttsc = TextToSpeechController.getInstance(context);
		//ttsc.speak(text);
	}

	private void onNextClick(Context context)
	{
		Log.i("onReceive", ACTION_WIDGET_WORD);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);
		if(remoteViews != null)
		{
			Log.i("onWordClick", ACTION_WIDGET_WORD);
			PublicWord pw = WordController.getInstance(context).getFirstPublicWord();
			setupActualWord(remoteViews, pw);
			//setupButtons(context, remoteViews);
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), TrainWidget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

			//onUpdate(context, appWidgetManager, appWidgetIds);
			appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);
		}
	}

	public static void setupActualWord(RemoteViews remoteViews, PublicWord pw)
	{

		remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);
		
		Log.d(TAG, String.format("setupActualWord:%s", pw.getPrimary()));
		
		remoteViews.setTextViewText(R.id.wordTextView, pw.getPrimary());
		remoteViews.setTextViewText(R.id.word2TextView, pw.getSecondary());
	}

	private void onWordClick(Context context)
	{
		Log.i("onReceive", ACTION_WIDGET_WORD);
		mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);
		if(mRemoteViews != null)
		{
			Log.i("onWordClick", ACTION_WIDGET_WORD);
			mRemoteViews.setViewVisibility(R.id.word2TextView, View.VISIBLE);
			
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), TrainWidget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

			//onUpdate(context, appWidgetManager, appWidgetIds);
			appWidgetManager.updateAppWidget(thisAppWidget, mRemoteViews);
			
//			AppWidgetManager awm = AppWidgetManager.getInstance(context);
//			awm.updateAppWidget(appWidgetId, views)
		}	
	}

	public void onWordTextViewClick(View v)
	{
		Log.d(TAG, "onWordTextViewClick()");

		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.main);
		rv.setViewVisibility(R.id.word2TextView, View.VISIBLE);
	}


}
