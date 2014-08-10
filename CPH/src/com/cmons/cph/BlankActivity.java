package com.cmons.cph;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class BlankActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView textView = new TextView(this);
		textView.setBackgroundColor(Color.WHITE);
		textView.setTextColor(Color.GRAY);
		textView.setGravity(Gravity.CENTER);
		textView.setText("메인페이지 UI 작업 진행.");
		
		setContentView(textView);
	}
}
