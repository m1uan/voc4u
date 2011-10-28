package com.voc4u.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class BaseActivity extends Activity implements OnMenuItemClickListener
{

	public static final int	DIALOG_ADD_WORD							= 101;
	public static final int	DIALOG_CONFIRM_CONTINUE_SAVE_SETTING	= 103;
	public static final int	DIALOG_MUST_CHECK_AT_LEAST_ONE			= 102;
	public static final int	DIALOG_PROGRESS							= 104;

	private MenuItem		mMenuDictionary;
	private MenuItem		mSpeachSetting;
	private MenuItem	mAddWord;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		mMenuDictionary = menu.add(R.string.menuDictionary).setOnMenuItemClickListener(this);
		mSpeachSetting = menu.add(R.string.menuSettingSpeech).setOnMenuItemClickListener(this);
		
		if(CommonSetting.DEBUG)
			mAddWord = menu.add(R.string.menuAddWord).setOnMenuItemClickListener(this);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		if (item == mMenuDictionary)
			WordController.getInstance(this).showWordsMenu();
		else if (item == mSpeachSetting)
			onShowSpeechMenu();
		else if(item == mAddWord)
			showDialog(DIALOG_ADD_WORD);
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

	@Override
	protected Dialog onCreateDialog(int id)
	{
		if (id == DIALOG_ADD_WORD)
		{
			// Context mContext = getApplicationContext();
			final Dialog dialog = new Dialog(this);

			// dialog.
			dialog.setContentView(R.layout.add_word_dialog);
			dialog.setTitle("Custom Dialog");

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.FILL_PARENT;
			// lp.height = WindowManager.LayoutParams.FILL_PARENT;
			// dialog.show();
			dialog.getWindow().setAttributes(lp);

			final EditText edtNative = (EditText) dialog.findViewById(R.id.edtNative);
			final EditText edtLern = (EditText) dialog.findViewById(R.id.edtLern);
			Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					dialog.cancel();
					
					String nat = edtNative.getText().toString() + "~";
					String lern = edtLern.getText().toString() + "~";
					
					WordController.getInstance(BaseActivity.this).addWordEx(WordController.CUSTOM_WORD_LESSON, nat,lern, 1, 1);
					
					Word word = new Word(WordController.CUSTOM_WORD_LESSON,nat, lern,1,1);
					onAddCustomWord(word);
					
				}
			});
			return dialog;
		}
		else
			return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		if (id == DIALOG_ADD_WORD)
		{
			final EditText edtNative = (EditText) dialog.findViewById(R.id.edtNative);
			final EditText edtLern = (EditText) dialog.findViewById(R.id.edtLern);
			edtLern.setText("");
			edtNative.setText("");
		}
		super.onPrepareDialog(id, dialog);
	}
	
	protected void onAddCustomWord(Word word)
	{
		String tst = getResources().getString(R.string.toas_word_is_add, word.getWord(), word.getWord2());	
		Toast.makeText(BaseActivity.this, tst, Toast.LENGTH_SHORT).show();
	}

}
