package com.voc4u.activity.lessons;

import com.voc4u.R;
import com.voc4u.activity.dictionary.Dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Lessons extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lessons);
		
		
		findViewById(R.id.btnAddLesson).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Lessons.this, Dictionary.class);
				startActivity(intent);
			}
		});
	}
}
