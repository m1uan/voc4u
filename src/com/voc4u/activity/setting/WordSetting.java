package com.voc4u.activity.setting;

import junit.framework.Assert;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.voc4u.controller.WordController;
import com.voc4u.core.LangSetting;
import com.voc4u.czen1.R;
import com.voc4u.setting.CommonSetting;

public class WordSetting extends Activity implements OnClickListener
{
	private static final int	DIALOG_ADD_WORD							= 101;
	private static final int	DIALOG_CONFIRM_CONTINUE_SAVE_SETTING	= 103;
	private static final int	DIALOG_MUST_CHECK_AT_LEAST_ONE			= 102;
	private static final int	DIALOG_PROGRES							= 104;
	private WordController		mWordCtrl;
	private ListView			mList;
	private View				btnAddWord;
	private Adapter				mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_setting);

		mWordCtrl = WordController.getInstance(this);

		mAdapter = new Adapter();

		mList = (ListView) findViewById(R.id.list);
		mList.setAdapter(mAdapter);

		if (CommonSetting.DEBUG)
		{
			findViewById(R.id.addword).setVisibility(View.VISIBLE);
			btnAddWord = findViewById(R.id.btnAddWord);
			btnAddWord.setOnClickListener(this);
		}
	}

	@Override
	protected void onResume()
	{
		// FIXME: close dialog when mWordCtrl is finish
		// if still runing the async task
		// isn't posible changing anything
		if (mWordCtrl.isAsyncRunning())
		{
			showDialog(DIALOG_PROGRES);
		}

		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		// TODO: move this to special button for save
		// TODO: show information about changes and leaving without this changes

		// super is called in store() -> showDialogAboutDurationOfOperation() ->
		// "YES"
		super.onBackPressed();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	private void store()
	{
		boolean anyChanges = false;
		boolean anyChecked = false;

		for (int i = 0; i != mAdapter.getLessonCount(); i++)
		{
			ItemView item = mAdapter.getLessonItem(i);

			Assert.assertNotNull(item);
			if (item != null)
			{
				ItemStatus is = item.getStatus();
				if (is != ItemStatus.NONE)
				{
					mWordCtrl.enableLessonAsync(i, is == ItemStatus.ADD);
					anyChanges = true;
				}

				if (item.isChecked())
					anyChecked = true;
			}
		}

		if (!anyChecked)
		{
			showDialog(DIALOG_MUST_CHECK_AT_LEAST_ONE);
			// showDialogAboutMustCheckAtleasOneItem();
		}
		else if (anyChanges)
		{
			showDialog(DIALOG_CONFIRM_CONTINUE_SAVE_SETTING);
			// showDialogAboutDurationOfOperation();
		}
		else
			finish();
	}

	


	protected void superOnBackPresed()
	{
		mWordCtrl.runAsyncTask();
		finish();
	}

	public class Adapter implements ListAdapter
	{

		private static final int	NUM_ADAPTING	= 50;
		private static final int	VOCABULARY_TYPE	= 0;
		private static final int	SETTING_TYPE	= 1;
		final private int			mLessonNum;
		final ItemView[]			mLessons;
		private int					mLastItem		= 0;

		public Adapter()
		{
			mLessonNum = LangSetting.LESSON_SIZES.length;
			mLessons = new ItemView[mLessonNum];
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount()
		{
			// first is setting
			return mLessonNum + 1;
		}

		public int getLessonCount()
		{
			return mLessonNum;
		}

		ItemView getLessonItem(int position)
		{
			if (mLessonNum > position)
				return mLessons[position];

			return null;
		}

		@Override
		public ItemView getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position)
		{
			if (position == 0)
				return SETTING_TYPE;
			else
				return VOCABULARY_TYPE;
		}

		@Override
		public View getView(int position, View convertView, final ViewGroup parent)
		{
			if (getItemViewType(position) == VOCABULARY_TYPE)
				return createWordView(position, convertView);
			else if (getItemViewType(position) == SETTING_TYPE)
				return createSettingView(convertView);

			return convertView;
		}

		private View createSettingView(View convertView)
		{
			// SettingItemView setting;

			if (convertView == null)
			{
				convertView = new SettingItemView(WordSetting.this);
				((SettingItemView) convertView).setup();
			}

			return convertView;
		}

		public View createWordView(int position, View convertView)
		{
			ItemView item;

			final int lesson = (position - 1);

			if (convertView == null)
				item = new ItemView(WordSetting.this, mWordCtrl);
			else
				item = (ItemView) convertView;

			item.setup(lesson);
			mLessons[mLastItem++] = item;
			return item;
		}

		@Override
		public int getViewTypeCount()
		{
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public boolean hasStableIds()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnAddWord)
		{
			// showDialog(101);
			CommonSetting.store(this);
			store();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		final Dialog dialog;
		switch (id)
		{
			case DIALOG_ADD_WORD:
			{
				// Context mContext = getApplicationContext();
				dialog = new Dialog(this);

				dialog.setContentView(R.layout.add_word_dialog);
				dialog.setTitle("Custom Dialog");

				final EditText edtNative = (EditText) dialog.findViewById(R.id.edtNative);
				final EditText edtLern = (EditText) dialog.findViewById(R.id.edtLern);
				Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
				btnAdd.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						WordController.getInstance(WordSetting.this).addWordEx(4, edtNative.getText().toString() + "~",
						edtLern.getText().toString() + "~", 1, 1);
						dialog.cancel();
					}
				});
				break;
			}
			case DIALOG_CONFIRM_CONTINUE_SAVE_SETTING:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.vocabulary_you_are_make_some_changes).setCancelable(false).setPositiveButton(
				this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						superOnBackPresed();
					}
				}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				dialog = builder.create();
				break;
			}
			case DIALOG_MUST_CHECK_AT_LEAST_ONE:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("setting");
				builder.setMessage(R.string.vocabulary_you_must_enable_at_least_one_lesson_);
				builder.setCancelable(true);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				//builder.set
				dialog = builder.create();
				
				break;
			}
			case DIALOG_PROGRES:
			{
				dialog = ProgressDialog.show(this, "", getString(R.string.database_still_initializing_please_wait), false, true);
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
				{
					@Override
					public void onCancel(DialogInterface dialog)
					{
						finish();
					}
				});
				break;
			}
			default:
			{
				return null;
			}
		}

		return dialog;
	}

}
