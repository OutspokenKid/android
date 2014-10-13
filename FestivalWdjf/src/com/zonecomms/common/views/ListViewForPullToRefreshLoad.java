package com.zonecomms.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewForPullToRefreshLoad extends ListView {
	
	public ListViewForPullToRefreshLoad(Context context) {
		this(context, null, 0);
	}
	
	public ListViewForPullToRefreshLoad(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ListViewForPullToRefreshLoad(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		
		setDividerHeight(0);
		setClickable(true);
	}
}
