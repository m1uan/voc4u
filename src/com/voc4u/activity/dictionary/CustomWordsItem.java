package com.voc4u.activity.dictionary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.voc4u.R;
import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;

public class CustomWordsItem extends LinearLayout implements
		OnItemSelectedListener, OnSeekBarChangeListener {

	private WordController mWordCtrl;
	private String mTitle;
	private int mIdFrom;
	private int mIdTo;

	// private LangSpinnerItem[] langSpinnerItemArray = new LangSpinnerItem[] {
	// new LangSpinnerItem( LangType.CZECH_2_ENG, this.getContext() ),
	// new LangSpinnerItem( LangType.ENG_2_CZECH, this.getContext() )
	// };
	// private final SeekBar mSeeekBar;

	public CustomWordsItem(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dictionary_item, this);

		// setBackgroundResource(R.color.cbg_lesson_1);

		findViewById(R.id.checkbox).setVisibility(View.INVISIBLE);

		String title = getContext().getString(R.string.custom_word_item);
		final TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(title);

		final Button btnAdd = (Button) findViewById(R.id.btnAddWord);
		btnAdd.setVisibility(View.VISIBLE);
		// ArrayAdapter spinnerArrayAdapter = new
		// ArrayAdapter(this.getContext(),
		// android.R.layout.simple_dropdown_item_1line, langSpinnerItemArray);

		// mSpinner = new Spinner(this, MODE_DROPDOWN);

		// mSeeekBar = (SeekBar)findViewById(R.id.seekbar);
		// mSeeekBar.setMax(Consts.MAX_WORD_NATIVE_LEARN);
		// mSeeekBar.setOnSeekBarChangeListener(this);

		// btnAddWord = (Button)findViewById(R.id.btnAddWord);
		// btnAddWord.setVisibility(View.VISIBLE);

	}

	public void setup(final WordController wc) {

		final TextView ex = (TextView) findViewById(R.id.examples);
		String sex;
		ArrayList<Word> al = wc
				.getWordsInLesson(WordController.CUSTOM_WORD_LESSON);
		if (al != null && al.size() > 0) {
			sex = "";

			int first = al.size() - ItemView.MAX_EXAMPLES_IN_VIEW;
			if (first < 0)
				first = 0;

			for (int i = first; i != al.size(); i++) {
				sex += ", " + al.get(i).getLern();
			}

			sex = sex.substring(2, sex.length());
		} else {
			sex = getContext().getString(R.string.empty_custom_list);
		}

		ex.setText(sex);

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// CommonSetting.langType = ((LangSpinnerItem)o).getLangType();
		// CommonSetting.langType = langSpinnerItemArray[i].getLangType();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// CommonSetting.langNativeNum = mSeeekBar.getProgress();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
