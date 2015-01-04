package com.outspoken_kid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public class OffsetScrollView extends ScrollView {

	private OnScrollChangedListener onScrollChangedListener;
	
	public OffsetScrollView(Context context) {
		this(context, null, 0);
	}
	
	public OffsetScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OffsetScrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		this.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged() {

				if(onScrollChangedListener != null) {
					onScrollChangedListener.onScrollChanged(getScrollY());
				}
			}
		});
	}

	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		this.onScrollChangedListener = onScrollChangedListener;
	}
	
//////////////////// Interfaces.

	public interface OnScrollChangedListener {
		
		public void onScrollChanged(int offset);
	}
}
