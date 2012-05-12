package com.voc4u.widget;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
	public static String ACTION_WIDGET_INIT = "ActionReceiverINIT";
	public static String ACTION_WIDGET_KNOW = "ActionReceiverNEXT";
	public static String ACTION_WIDGET_DONT = "ActionReceiverWORD";
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
		
		CommonSetting.restore(context);
		if (CommonSetting.lernCode == null || CommonSetting.nativeCode == null) 
		{
			onInitClick(context);
		}
		
	}

	public static void appWasInitialized(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId );
        
        // Construct the RemoteViews object.  It takes the package name (in our case, it's our
        // package, but it needs this because on the other side it's the widget host inflating
        // the layout from our package).
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setViewVisibility(R.id.info, View.GONE);
		remoteViews.setViewVisibility(R.id.word_1, View.VISIBLE);
		remoteViews.setViewVisibility(R.id.word_2, View.VISIBLE);
		remoteViews.setViewVisibility(R.id.word_3, View.VISIBLE);
		
		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_PLAY);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
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
		
		
        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
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
				R.layout.widget);

		if (CommonSetting.lernCode == null || CommonSetting.nativeCode == null)
		{
			CommonSetting.restore(context);
			if (CommonSetting.lernCode == null || CommonSetting.nativeCode == null) 
			{
				setInactiveMode(context, remoteViews);
			}
			else
			{
				setupActualWord(remoteViews,WordController.getInstance(context).getActualPublicWord());
				setupButtons(context, remoteViews);
			}
		}
		else
		{
		
			setupActualWord(remoteViews,WordController.getInstance(context).getActualPublicWord());
			setupButtons(context, remoteViews);
		}
		//remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	private void setInactiveMode(Context context, RemoteViews remoteViews) {
		String s = context.getResources().getString(R.string.form_init);
		remoteViews.setViewVisibility(R.id.info, View.VISIBLE);
		remoteViews.setTextViewText(R.id.wordTextView, s);
		remoteViews.setViewVisibility(R.id.word_1, View.GONE);
		remoteViews.setViewVisibility(R.id.word_2, View.GONE);
		remoteViews.setViewVisibility(R.id.word_3, View.GONE);
		
		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_INIT);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_layout,
				actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.nextButton,
				actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.dontKnowButton,
				actionPendingIntent);
	}

	private void setupButtons(Context context, RemoteViews remoteViews)
	{
		Intent active = new Intent(context, TrainWidget.class);
		active.setAction(ACTION_WIDGET_PLAY);
		active.putExtra(WORD_INDENT_ID, "ahoj");
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		
//		remoteViews.setOnClickPendingIntent(R.id.playButton,
//				actionPendingIntent);

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
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(ACTION_WIDGET_INIT))
		{
			Log.i("onReceive", ACTION_WIDGET_INIT);
			onInitClick(context);
			//onPlayClick(context, intent);
		} else if (intent.getAction().equals(ACTION_WIDGET_KNOW))
		{
			Log.i("onReceive", ACTION_WIDGET_KNOW);
			onNextClick(context);
		} else if (intent.getAction().equals(ACTION_WIDGET_DONT))
		{
			onWordClick(context);
		} else
		{
			
		}
		super.onReceive(context, intent);
	}

	private void onInitClick(Context context)
	{
		Log.i(TAG, "onPlayClick");
		final Intent intent = new Intent(context, Init.class);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), TrainWidget.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		
		Log.d(TAG, String.format("putExtra:%d", appWidgetIds[0]));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(context, "test2", Toast.LENGTH_LONG).show();
		context.startActivity(intent);
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
		Log.i("onReceive", ACTION_WIDGET_DONT);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		if(remoteViews != null)
		{
			Log.i("onWordClick", ACTION_WIDGET_DONT);
			PublicWord pw = WordController.getInstance(context).getFirstPublicWord();
			setupActualWord(remoteViews, pw);
			
			// v testing flag je commionsetting ktere neni po restartu zarizeni initializovane!!
			remoteViews.setImageViewResource(R.id.flag, pw.getTestingFlag(context));
			
			
			
			int [] id_text1 = new int [] {R.id.text1_1, R.id.text1_2, R.id.text1_3 };
			int [] id_text2 = new int [] {R.id.text2_1, R.id.text2_2, R.id.text2_3 };
			
			List<PublicWord> ll = WordController.getInstance(context).getLastList();
			int llmax = ll.size() > 3 ? 3 : ll.size();
			for(int i = 0; i != llmax; i++)
			{
				int size = ll.size();
				int step = (i + 1);
				int pos = size - step;
				PublicWord pwl = ll.get(pos);
			
				remoteViews.setTextViewText(id_text1[i], pwl.getLern());
				remoteViews.setTextViewText(id_text2[i], pwl.getNative());
			}
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
		
		//remoteViews.setViewVisibility(R.id.word2TextView, View.INVISIBLE);
		
		Log.d(TAG, String.format("setupActualWord:%s", pw.getLern()));
		
		remoteViews.setTextViewText(R.id.wordTextView, pw.getTestString());
		
		//remoteViews.setTextViewText(R.id.word2TextView, pw.getNative());
	}

	private void onWordClick(Context context)
	{
		Log.i("onReceive", ACTION_WIDGET_DONT);
		mRemoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		if(mRemoteViews != null)
		{
			Log.i("onWordClick", ACTION_WIDGET_DONT);
			//mRemoteViews.setViewVisibility(R.id.word2TextView, View.VISIBLE);
			
			
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
				R.layout.widget);
		//rv.setViewVisibility(R.id.word2TextView, View.VISIBLE);
	}


}
