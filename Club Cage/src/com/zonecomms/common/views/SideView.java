package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class SideView extends FrameLayout {

	private ImageView icon;
	private TextView tvTitle;
	
	public SideView(Context context) {
		this(context, null, 0);
	}
	
	public SideView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		
		this.setBackgroundColor(Color.rgb(110, 110, 110));
		
		View topLine = new View(getContext());
		topLine.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2, Gravity.TOP));
		topLine.setBackgroundColor(Color.rgb(147, 149, 152));
		this.addView(topLine);
		
		View bottomLine = new View(getContext());
		bottomLine.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2, Gravity.BOTTOM));
		bottomLine.setBackgroundColor(Color.rgb(88, 88, 88));
		this.addView(bottomLine);
		
		icon = new ImageView(getContext());
		ResizeUtils.viewResize(80, 80, icon, 2, Gravity.LEFT|Gravity.CENTER_VERTICAL, new int[]{20, 0, 0, 0});
		icon.setScaleType(ScaleType.CENTER_CROP);
		this.addView(icon);
		
		tvTitle = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, tvTitle, 2, Gravity.LEFT, 
				new int[]{120, 0, 0, 0}, new int[]{0, 0, 20, 0});
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setEllipsize(TruncateAt.END);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvTitle, 30);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		this.addView(tvTitle);
	}
	
	public ImageView getIcon() {
		
		return icon;
	}
	
	public void setIcon(int resId) {
		
		if(icon != null && resId != 0) {
			icon.setBackgroundResource(resId);
		}
	}
	
	public void setTitle(String title) {
		
		if(tvTitle != null && !StringUtils.isEmpty(title)) {
			tvTitle.setText(title);
		}
	}
	
	public void setTitle(int resId) {
		
		if(tvTitle != null && resId != 0) {
			tvTitle.setText(resId);
		}
	}
}
