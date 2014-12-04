package com.byecar.byecarplus.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.ResizeUtils;

public class TitleBar extends RelativeLayout {

	private View bg;
	private Button btnMenu;
	private Button btnBack;
	
	private Button btnNotice;
	
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
		bg.setBackgroundColor(Color.rgb(254, 188, 42));
		this.addView(bg);
		
		//btnMenu.
		btnMenu = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(220), 
				ResizeUtils.getSpecificLength(60));
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnMenu.setLayoutParams(rp);
		btnMenu.setBackgroundResource(R.drawable.main_menu_btn);
		btnMenu.setVisibility(View.INVISIBLE);
		this.addView(btnMenu);
		
		//btnBack.
		btnBack = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(0, 0);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		btnBack.setLayoutParams(rp);
		btnBack.setVisibility(View.INVISIBLE);
		this.addView(btnBack);
	}

	public Button getMenuButton() {
		
		return btnMenu;
	}
	
	public Button getBackButton() {
		
		return btnBack;
	}
	
	public Button getBtnNotice() {

		if(btnNotice == null) {
			btnNotice = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(60), 
					ResizeUtils.getSpecificLength(60));
			rp.addRule(RelativeLayout.CENTER_VERTICAL);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.rightMargin = ResizeUtils.getSpecificLength(30);
			btnNotice.setLayoutParams(rp);
			btnNotice.setBackgroundResource(R.drawable.main_notice_btn);
			this.addView(btnNotice);
		}
		
		return btnNotice;
	}

	public void setBgColor(int color) {
		
		bg.setBackgroundColor(color);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setBgAlpha(float alpha) {

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			bg.setAlpha(alpha);
		}
	}
}
