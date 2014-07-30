package com.cmons.cph.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForWritePage extends BaseFragmentForSignUp {

	TextView tvCompanyName;
	EditText etCompanyName;
	
	TextView tvCompanyAddress;
	EditText etCompanyAddress;
	
	TextView tvCompanyOwnerName;
	EditText etCompanyOwnerName;
	
	TextView tvCompanyRegistration;
	EditText etCompanyRegistration1;
	EditText etCompanyRegistration2;
	EditText etCompanyRegistration3;
	
	Button btnNext;
	
	private int type;
	
	@Override
	protected void bindViews() {

		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyName);
		
		tvCompanyAddress = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyAddress);
		etCompanyAddress = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyAddress);
		
		tvCompanyOwnerName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyOwnerName);
		etCompanyOwnerName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyOwnerName);
		
		tvCompanyRegistration = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyRegistration);
		etCompanyRegistration1 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration1);
		etCompanyRegistration2 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration2);
		etCompanyRegistration3 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration3);
		
		btnNext = (Button) mThisView.findViewById(R.id.signUpForWritePage_btnNext);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListeners() {

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPersonalPage(type);
			}
		});
	}

	@Override
	protected void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		int textViewWidth = LayoutParams.WRAP_CONTENT;
		int textViewHeight = ResizeUtils.getSpecificLength(80);
		int textViewMarginLeft = ResizeUtils.getSpecificLength(80);
		int textViewMarginTop = ResizeUtils.getSpecificLength(40);
		
		int editTextWidth = ResizeUtils.getSpecificLength(500);
		int editTextHeight = ResizeUtils.getSpecificLength(80);

		int smallEditTextWidth = ResizeUtils.getSpecificLength(150);
		int smallEditTextHeight = ResizeUtils.getSpecificLength(80);
		int smallEditTextMarginLeft = ResizeUtils.getSpecificLength(25);
		
		int buttonWidth = ResizeUtils.getSpecificLength(240);
		int buttonHeight = ResizeUtils.getSpecificLength(80);
		int buttonMarginTop = ResizeUtils.getSpecificLength(40);
		
		//tvCompanyName.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.leftMargin = textViewMarginLeft;
		rp.topMargin = textViewMarginTop * 2;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvCompanyName.setLayoutParams(rp);
		
		//etCompanyName.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_tvCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_tvCompanyName);
		etCompanyName.setLayoutParams(rp);
		
		//tvCompanyAddress.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_etCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyName);
		tvCompanyAddress.setLayoutParams(rp);
		
		//etCompanyAddress.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_tvCompanyAddress);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_tvCompanyAddress);
		etCompanyAddress.setLayoutParams(rp);
		
		//tvCompanyOwnerName.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_etCompanyAddress);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyAddress);
		tvCompanyOwnerName.setLayoutParams(rp);
		
		//etCompanyOwnerName.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_tvCompanyOwnerName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_tvCompanyOwnerName);
		etCompanyOwnerName.setLayoutParams(rp);
		
		//tvCompanyRegistration.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_etCompanyOwnerName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyOwnerName);
		tvCompanyRegistration.setLayoutParams(rp);
		
		//etCompanyRegistration1.
		rp = new RelativeLayout.LayoutParams(smallEditTextWidth, smallEditTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForWritePage_tvCompanyRegistration);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_tvCompanyRegistration);
		etCompanyRegistration1.setLayoutParams(rp);
		
		//etCompanyRegistration2.
		rp = new RelativeLayout.LayoutParams(smallEditTextWidth, smallEditTextHeight);
		rp.leftMargin = smallEditTextMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForWritePage_etCompanyRegistration1);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForWritePage_etCompanyRegistration1);
		etCompanyRegistration2.setLayoutParams(rp);
		
		//etCompanyRegistration3.
		rp = new RelativeLayout.LayoutParams(smallEditTextWidth, smallEditTextHeight);
		rp.leftMargin = smallEditTextMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForWritePage_etCompanyRegistration2);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForWritePage_etCompanyRegistration2);
		etCompanyRegistration3.setLayoutParams(rp);
		
		//btnNext.
		rp = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
		rp.topMargin = buttonMarginTop;
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyRegistration1);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		btnNext.setLayoutParams(rp);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_write;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
