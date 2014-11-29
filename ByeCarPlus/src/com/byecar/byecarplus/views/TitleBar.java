package com.byecar.byecarplus.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.outspoken_kid.utils.ResizeUtils;

public class TitleBar extends RelativeLayout {

	private Button btnMenu;
	private Button btnBack;
	
//	private Button btnAdd;
	
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
		
		setBackgroundColor(Color.rgb(254, 188, 42));
		
		RelativeLayout.LayoutParams rp = null;
		
		//btnMenu.
		btnMenu = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(92), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnMenu.setLayoutParams(rp);
//		btnMenu.setBackgroundResource(R.drawable.retail_menu_btn);
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
	
//	public Button getBtnAdd() {
//
//		if(btnAdd == null) {
//			btnAdd = new Button(getContext());
//			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
//					ResizeUtils.getSpecificLength(92), 
//					ResizeUtils.getSpecificLength(92));
//			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			btnAdd.setLayoutParams(rp);
//			btnAdd.setBackgroundResource(R.drawable.add_btn);
//			this.addView(btnAdd);
//		}
//		
//		return btnAdd;
//	}
}
