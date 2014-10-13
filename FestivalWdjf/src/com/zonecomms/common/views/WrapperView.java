package com.zonecomms.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zonecomms.festivalwdjf.classes.ViewWrapper;

public class WrapperView extends FrameLayout {

	protected ViewWrapper viewWrapper;
	
	public WrapperView(Context context) {
		this(context, null);
	}
	
	public WrapperView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ViewWrapper getViewWrapper() {
		return viewWrapper;
	}

	public void setViewWrapper(ViewWrapper viewWrapper) {
		this.viewWrapper = viewWrapper;
	}
}
