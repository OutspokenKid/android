package com.byecar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.AppInfoUtils;

public class FocusHandlingEditText extends EditText {

	public FocusHandlingEditText(Context context) {
		this(context, null, 0);
	}
	
	public FocusHandlingEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public FocusHandlingEditText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void init() {

		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[]{android.R.attr.state_focused}, getResources().getDrawable(R.drawable.bg_edittext_on));
		sld.addState(new int[]{-android.R.attr.state_focused}, getResources().getDrawable(R.drawable.bg_edittext_off));

		if(AppInfoUtils.checkMinVersionLimit(16)) {
			this.setBackground(sld);
		} else {
			this.setBackgroundDrawable(sld);
		}
	}
}
