package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class CircleHeaderView extends FrameLayout {

	private static final int ANIM_DURATION = 500;

	public static final int HEADER_VIEW_HEIGHT = 708;
	public static final int TITLE_BAR_HEIGHT = 82;
	public static int diffLength;
	
	private NetworkImageView ivTitle;
	private TextView[] tvTitles = new TextView[2];
	
	private AlphaAnimation aaIn, aaOut;
	private int titleBarIndex = 1;
	
	public CircleHeaderView(Context context) {
		this(context, null, 0);
	}
	
	public CircleHeaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircleHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {

		diffLength = ResizeUtils.getSpecificLength(HEADER_VIEW_HEIGHT - TITLE_BAR_HEIGHT);
		
		this.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(HEADER_VIEW_HEIGHT)));
		this.setVisibility(View.INVISIBLE);
		this.setClickable(true);
		
		ivTitle = new NetworkImageView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 200, ivTitle, 2, 
				Gravity.BOTTOM, new int[]{0, 0, 0, TITLE_BAR_HEIGHT});
		ivTitle.setScaleType(ScaleType.FIT_XY);
		this.addView(ivTitle);
		
		tvTitles[0] = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, TITLE_BAR_HEIGHT, tvTitles[0], 2, 
				Gravity.BOTTOM, null);
		tvTitles[0].setTextColor(Color.WHITE);
		tvTitles[0].setGravity(Gravity.CENTER);
		FontUtils.setGlobalFont(tvTitles[0]);
		FontUtils.setFontSize(tvTitles[0], 36);
		this.addView(tvTitles[0]);
		
		tvTitles[1] = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, TITLE_BAR_HEIGHT, tvTitles[1], 2, 
				Gravity.BOTTOM, null);
		tvTitles[1].setTextColor(Color.WHITE);
		tvTitles[1].setGravity(Gravity.CENTER);
		tvTitles[1].setVisibility(View.INVISIBLE);
		FontUtils.setGlobalFont(tvTitles[1]);
		FontUtils.setFontSize(tvTitles[1], 36);
		this.addView(tvTitles[1]);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(ANIM_DURATION);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(ANIM_DURATION);
	}
	
	public NetworkImageView getIvTitle() {
		
		return ivTitle;
	}
	
	public void showHeader() {
		
		this.startAnimation(aaIn);
		this.setVisibility(View.VISIBLE);
	}

	public void setTitleBarText(String text) {
		
		tvTitles[0].setText(text);
		tvTitles[1].setText(text);
	}
	
	public void setTitleBarColor(int index, int color) {

		LogUtils.log("###CircleHeaderView.setTitleBarColor.  index : " + (titleBarIndex));
		
		tvTitles[titleBarIndex].setBackgroundColor(color);
	}
	
	public void changeTitleBarColor(int color, boolean needToPlay) {

		titleBarIndex++;
		
		int in = titleBarIndex % 2;
		int out = (in + 1) % 2;
		
		LogUtils.log("###CircleHeaderView.changeTitleBarColor.  needToPlay : " + needToPlay + ", " + in + "번째 타이틀 보여주고, " + out + "번째 타이틀 숨기기");
		
		if(needToPlay) {
			tvTitles[in].setBackgroundColor(color);
			tvTitles[in].setVisibility(View.VISIBLE);
//			tvTitles[in].startAnimation(aaIn);
			tvTitles[out].setVisibility(View.INVISIBLE);
//			tvTitles[out].startAnimation(aaOut);
		} else {
			tvTitles[in].setBackgroundColor(color);
		}
	}
	
	public void showTitleBar() {
		
		LogUtils.log("###CircleHeaderView.showTitleBar.  " + (titleBarIndex % 2) + "번째 타이틀 보여주고, " + ((titleBarIndex + 1) %2) + "번째 타이틀 숨기기");
		
		tvTitles[titleBarIndex % 2].setVisibility(View.VISIBLE);
		tvTitles[(titleBarIndex + 1) % 2].setVisibility(View.INVISIBLE);
	}
	
	public void hideTitleBar() {
		
		LogUtils.log("###CircleHeaderView.hideTitleBar.  둘 다 숨기기.");
		
		tvTitles[0].setVisibility(View.INVISIBLE);
		tvTitles[1].setVisibility(View.INVISIBLE);
	}
}
