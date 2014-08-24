package com.cmons.cph.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class TitleBar extends RelativeLayout {

	private View titleBg;
	private Button btnBack;
	private Button btnHome;
	private TextView tvTitle;
	private View titleView;
	private Button btnNotice;
	private Button btnAdd;
	private Button btnSubmit;
	private Button btnWrite;
	private Button btnReply;
	private Button btnEdit;
	
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
		titleBg.setBackgroundResource(R.drawable.navigation_bar);
		this.addView(titleBg);
		
		//btnBack.
		btnBack = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(92), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnBack.setLayoutParams(rp);
		btnBack.setBackgroundResource(R.drawable.back1_btn);
		this.addView(btnBack);
		
		//btnHome.
		btnHome = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(92), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.leftMargin = ResizeUtils.getSpecificLength(92); 
		btnHome.setLayoutParams(rp);
		btnHome.setBackgroundResource(R.drawable.home_btn);
		this.addView(btnHome);
		
		//tvTitle.
		tvTitle = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = ResizeUtils.getSpecificLength(184);
		rp.rightMargin = ResizeUtils.getSpecificLength(184);
		tvTitle.setLayoutParams(rp);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setSingleLine();
		tvTitle.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvTitle, 28);
		this.addView(tvTitle);
		
		//titleView.
		titleView = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(121), 
				ResizeUtils.getSpecificLength(71));
		rp.addRule(RelativeLayout.CENTER_IN_PARENT);
		titleView.setLayoutParams(rp);
		titleView.setBackgroundResource(R.drawable.logo2);
		this.addView(titleView);
	}
	
	public Button getBackButton() {
		
		return btnBack;
	}
	
	public Button getHomeButton() {
		
		return btnHome;
	}
	
	public void setTitleText(String titleText) {
		
		if(titleText == null) {
			tvTitle.setVisibility(View.INVISIBLE);
			titleView.setVisibility(View.VISIBLE);
		} else{
			tvTitle.setText(titleText);
			tvTitle.setVisibility(View.VISIBLE);
			titleView.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setTitleText(int resId) {
		
		if(resId == 0) {
			tvTitle.setVisibility(View.INVISIBLE);
			titleView.setVisibility(View.VISIBLE);
		} else {
			tvTitle.setText(resId);
			tvTitle.setVisibility(View.VISIBLE);
			titleView.setVisibility(View.INVISIBLE);
		}
	}
	
	public Button getBtnNotice() {
		
		if(btnNotice == null) {
			btnNotice = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(92), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.rightMargin = ResizeUtils.getSpecificLength(92);
			btnNotice.setLayoutParams(rp);
			btnNotice.setBackgroundResource(R.drawable.notice2_btn);
			btnNotice.setVisibility(View.INVISIBLE);
			this.addView(btnNotice);
		}
		
		return btnNotice;
	}
	
	public Button getBtnAdd() {

		if(btnAdd == null) {
			btnAdd = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(92), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btnAdd.setLayoutParams(rp);
			btnAdd.setBackgroundResource(R.drawable.add_btn);
			this.addView(btnAdd);
		}
		
		return btnAdd;
	}
	
	public Button getBtnSubmit() {
		
		if(btnSubmit == null) {
			btnSubmit = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btnSubmit.setLayoutParams(rp);
			btnSubmit.setBackgroundResource(R.drawable.completion_btn);
			this.addView(btnSubmit);
		}
		
		return btnSubmit;
	}

	public Button getBtnWrite() {

		if(btnWrite == null) {
			btnWrite = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btnWrite.setLayoutParams(rp);
			btnWrite.setBackgroundResource(R.drawable.add_notice_btn);
			this.addView(btnWrite);
		}
		
		return btnWrite;
	}

	public Button getBtnReply() {

		if(btnReply == null) {
			btnReply = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(92), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.rightMargin = ResizeUtils.getSpecificLength(92);
			btnReply.setLayoutParams(rp);
			btnReply.setBackgroundResource(R.drawable.myshop_viewer_comment_btn);
			this.addView(btnReply);
		}
		
		return btnReply;
	}
	
	public Button getBtnEdit() {

		if(btnEdit == null) {
			btnEdit = new Button(getContext());
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(92), 
					ResizeUtils.getSpecificLength(92));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			btnEdit.setLayoutParams(rp);
			btnEdit.setBackgroundResource(R.drawable.myshop_viewer_setting_btn);
			this.addView(btnEdit);
		}
		
		return btnEdit;
	}
}
