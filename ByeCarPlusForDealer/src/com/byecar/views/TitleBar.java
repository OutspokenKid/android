package com.byecar.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class TitleBar extends RelativeLayout {

	private View bg;
	private Button btnMenu;		//메인에서만.
	private Button btnBack;		//메인 제외한 모든 페이지.
	private TextView tvTitle;
	private View bottomLine;
	
	private TextView tvNotificationCount;
	
	public TitleBar(Context context) {
		this(context, null, 0);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}
	
	public void init() {

		if(this.getChildCount() != 0) {
			return;
		}
		
		RelativeLayout.LayoutParams rp = null;
		
		//bg.
		bg = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
		bg.setBackgroundColor(getContext().getResources().getColor(R.color.titlebar_bg_brown));
		this.addView(bg);
		
		//btnMenu.
		btnMenu = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(251), 
				ResizeUtils.getSpecificLength(60));
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnMenu.setLayoutParams(rp);
		btnMenu.setBackgroundResource(R.drawable.main_menu_btn);
		btnMenu.setVisibility(View.INVISIBLE);
		this.addView(btnMenu);
		
		//btnBack.
		btnBack = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(161), ResizeUtils.getSpecificLength(60));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		btnBack.setLayoutParams(rp);
		btnBack.setVisibility(View.INVISIBLE);
		btnBack.setBackgroundResource(R.drawable.back_btn);
		this.addView(btnBack);
		
		//Bottom line.
		bottomLine = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bottomLine.setLayoutParams(rp);
		bottomLine.setBackgroundColor(Color.rgb(186, 186, 186));
		this.addView(bottomLine);
	}

	public Button getMenuButton() {
		
		return btnMenu;
	}
	
	public Button getBackButton() {
		
		return btnBack;
	}

	public void setBgColor(int color) {
		
		bg.setBackgroundColor(color);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setBgAlpha(float alpha) {

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			bg.setAlpha(alpha);
			bottomLine.setAlpha(alpha);
		}
	}

	public void setTitle(int titleResId) {
		
		if(tvTitle == null) {
			tvTitle = new TextView(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(320), 
					LayoutParams.MATCH_PARENT);
			rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			tvTitle.setLayoutParams(rp);
			tvTitle.setTextColor(Color.WHITE);
			FontUtils.setFontSize(tvTitle, 32);
			FontUtils.setFontStyle(tvTitle, FontUtils.BOLD);
			tvTitle.setGravity(Gravity.CENTER);
			this.addView(tvTitle);
		}
		
		tvTitle.setText(titleResId);
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setNotificationCount(int count) {
		
		if(tvNotificationCount == null) {
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.topMargin = ResizeUtils.getSpecificLength(16);
			rp.leftMargin = ResizeUtils.getSpecificLength(24);
			tvNotificationCount = new TextView(getContext());
			tvNotificationCount.setLayoutParams(rp);
			tvNotificationCount.setTextColor(Color.WHITE);
			tvNotificationCount.setGravity(Gravity.CENTER);
			
			int pv = ResizeUtils.getSpecificLength(2);
			int ph = ResizeUtils.getSpecificLength(6);
			tvNotificationCount.setPadding(ph, pv, ph, pv);
			
			FontUtils.setFontSize(tvNotificationCount, 20);
			this.addView(tvNotificationCount);
			
			PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.progress_guage_red));
	        pd.setCornerRadius(ResizeUtils.getSpecificLength(10));
	        
	        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	        	tvNotificationCount.setBackground(pd);
			} else {
				tvNotificationCount.setBackgroundDrawable(pd);
			}
		}
		
		if(count > 100) {
			tvNotificationCount.setText("+99");
			tvNotificationCount.setVisibility(View.VISIBLE);
		} else if(count <= 0){
			tvNotificationCount.setVisibility(View.INVISIBLE);
		} else {
			tvNotificationCount.setText("" + count);
			tvNotificationCount.setVisibility(View.VISIBLE);
		}
	}
}
