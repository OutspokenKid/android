package com.zonecomms.common.wrapperviews;

import com.zonecomms.common.wrappers.ViewWrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

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
