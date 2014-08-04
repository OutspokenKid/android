package com.cmons.cph.views;

import com.cmons.cph.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBar extends RelativeLayout {

	private View titleBg;
	private Button btnBack;
	private TextView tvTitle;
	private Button btnNotice;
	private Button btnAdd;
	
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
		
		RelativeLayout.LayoutParams rp = null;
		
		//titleBg.
		titleBg = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		titleBg.setBackgroundResource(R.drawable.bg_titlebar);
		this.addView(titleBg);
		
		//tvTitle.
		tvTitle = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvTitle.setLayoutParams(rp);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		tvTitle.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvTitle, 30);
		this.addView(tvTitle);
	}
	
	public void addBackButton(int resId, int width, int height) {
		
		btnBack = new Button(getContext());
		
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(width), 
				ResizeUtils.getSpecificLength(height));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnBack.setLayoutParams(rp);
		btnBack.setBackgroundResource(resId);
		this.addView(btnBack);
	}
	
	public Button getBackButton() {
		
		return btnBack;
	}
	
	public void setTitleText(String titleText) {
		
		tvTitle.setText(titleText);
	}
	
	public void setTitleText(int resId) {
		
		tvTitle.setText(resId);
	}
	
	public void addButtons(boolean isWholesale) {

//		btnNotice = new Button(getContext());
//		
//		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
//				ResizeUtils.getSpecificLength(width), 
//				ResizeUtils.getSpecificLength(height));
//		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		btnNotice.setLayoutParams(rp);
//		btnNotice.setNoticegroundResource(resId);
//		this.addView(btnNotice);
//		
//		btnAdd = new Button(getContext());
//		
//		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
//				ResizeUtils.getSpecificLength(width), 
//				ResizeUtils.getSpecificLength(height));
//		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		btnAdd.setLayoutParams(rp);
//		btnAdd.setAddgroundResource(resId);
//		this.addView(btnAdd);
	}
	
	public Button getBtnNotice() {
		
		return btnNotice;
	}
	
	public Button getBtnAdd() {
		
		return btnAdd;
	}
}
