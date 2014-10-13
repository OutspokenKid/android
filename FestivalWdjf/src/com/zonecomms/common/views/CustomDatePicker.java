package com.zonecomms.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

public class CustomDatePicker extends DatePicker {

	private boolean canHolo;
	ViewParent parent;
	
	public CustomDatePicker(Context context) {
		this(context, null, 0);
	}
	
	public CustomDatePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CustomDatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	    if (currentapiVersion >= 11) {
	    	canHolo = true;
	    }
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if(canHolo) {
			if(parent == null) {
				parent = getParent();
			}
			
			if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
				parent.requestDisallowInterceptTouchEvent(true);
			} else if(ev.getActionMasked() == MotionEvent.ACTION_UP
					|| ev.getActionMasked() == MotionEvent.ACTION_CANCEL){
				parent.requestDisallowInterceptTouchEvent(false);
			}
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
}
