package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForPullToRefreshLoad extends GridView {

	public GridViewForPullToRefreshLoad(Context context) {
		this(context, null, 0);
	}
	
	public GridViewForPullToRefreshLoad(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public GridViewForPullToRefreshLoad(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		
		setClickable(true);
		setBackgroundColor(Color.BLACK);
		setSelector(new ColorDrawable(Color.argb(0, 0, 0, 0)));
	}
}
