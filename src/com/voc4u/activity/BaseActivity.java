package com.voc4u.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;

public class BaseActivity extends Activity implements OnMenuItemClickListener
{
	private MenuItem mMenuDictionary;
	private MenuItem mSpeachSetting;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		mMenuDictionary = menu.add(R.string.menuDictionary).setOnMenuItemClickListener(this);
    	mSpeachSetting = menu.add(R.string.menuSettingSpeech).setOnMenuItemClickListener(this);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		if(item == mMenuDictionary)
			WordController.getInstance(this).showWordsMenu();
		else if(item == mSpeachSetting)
			onShowSpeechMenu();
		return true;
	}

	
	private void onShowSpeechMenu()
	{
		ComponentName componentToLaunch = new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings");
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(componentToLaunch);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
