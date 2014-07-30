package com.cmons.cph.fragments;

import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForPersonalPage extends BaseFragmentForSignUp {

	private TextView tvCompanyName;
	private EditText etCompanyName;
	
	private TextView tvCompanyPhone;
	private EditText etCompanyPhone;
	
	private TextView tvCompanyLocation;
	private EditText etCompanyLocation;
	
	private TextView tvId;
	private EditText etId;
	private Button btnCheckId;
	
	private TextView tvPw;
	private EditText etPw;
	
	private TextView tvPwConfirm;
	private EditText etPwConfirm;
	
	private TextView tvPhone;
	private EditText etPhone;
	private EditText etVerification;
	private Button btnSendVerification;
	
	private Button btnComplete;
	
	private int type;
	
	@Override
	protected void bindViews() {

		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etCompanyName);
		
		tvCompanyPhone = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyPhone);
		etCompanyPhone = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etCompanyPhone);
		
		tvCompanyLocation = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyLocation);
		etCompanyLocation = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etCompanyLocation);
		
		tvId = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvId);
		etId = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etId);
		btnCheckId = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnCheckId);
		
		tvPw = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvPw);
		etPw = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etPw);
		
		tvPwConfirm = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvConfirmPw);
		etPwConfirm = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etConfirmPw);
		
		tvPhone = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvPhone);
		etPhone = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etPhone);
		
		etVerification = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etVerification);
		btnSendVerification = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnSendVerification);
		
		btnComplete = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnComplete);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {

		switch(type) {
		
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_OWNER:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE1:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE2:
			tvCompanyPhone.setText(R.string.phoneNumberForWholesale);
			tvCompanyLocation.setText(R.string.addressForWholesale);
			break;
		
		default:
			tvCompanyPhone.setText(R.string.phoneNumberForRetail);
			tvCompanyLocation.setText(R.string.addressForRetail);
		}
		
		etCompanyName.setText("C-MONS");
		etCompanyPhone.setText("02-0000-0000");
		etCompanyLocation.setText("강남구 역삼동 개나리아파트 201호");
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		int textViewWidth = LayoutParams.WRAP_CONTENT;
		int textViewHeight = ResizeUtils.getSpecificLength(80);
		int textViewMarginLeft = ResizeUtils.getSpecificLength(40);
		int textViewMarginTop = ResizeUtils.getSpecificLength(40);
		
		int editTextWidth = ResizeUtils.getSpecificLength(500);
		int editTextHeight = ResizeUtils.getSpecificLength(80);
		int editTextMarginTop = ResizeUtils.getSpecificLength(40);

		int smallEditTextWidth = ResizeUtils.getSpecificLength(300);
		int smallEditTextHeight = ResizeUtils.getSpecificLength(80);
		
		int buttonWidth = ResizeUtils.getSpecificLength(240);
		int buttonHeight = ResizeUtils.getSpecificLength(80);
		int buttonMarginTop = ResizeUtils.getSpecificLength(40);
		
		int smallButtonWidth = ResizeUtils.getSpecificLength(220);
		int smallButtonHeight = ResizeUtils.getSpecificLength(80);
		int smallButtonMarginLeft = ResizeUtils.getSpecificLength(20);
		
		//tvCompanyName.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.leftMargin = textViewMarginLeft;
		rp.topMargin = textViewMarginTop * 2;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvCompanyName.setLayoutParams(rp);
		
		//etCompanyName.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvCompanyName);
		etCompanyName.setLayoutParams(rp);
		
		//tvCompanyPhone.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etCompanyName);
		tvCompanyPhone.setLayoutParams(rp);
				
		//etCompanyPhone.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvCompanyPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvCompanyPhone);
		etCompanyPhone.setLayoutParams(rp);
		
		//tvCompanyLocation.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etCompanyPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etCompanyPhone);
		tvCompanyLocation.setLayoutParams(rp);
		
		//etCompanyLocation.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvCompanyLocation);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvCompanyLocation);
		etCompanyLocation.setLayoutParams(rp);
		
		//tvId.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etCompanyLocation);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etCompanyLocation);
		tvId.setLayoutParams(rp);
		
		//etId.
		rp = new RelativeLayout.LayoutParams(smallEditTextWidth, smallEditTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvId);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvId);
		etId.setLayoutParams(rp);
		
		//btnCheckId.
		rp = new RelativeLayout.LayoutParams(smallButtonWidth, smallButtonHeight);
		rp.leftMargin = smallButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForPersonalPage_etId);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForPersonalPage_etId);
		btnCheckId.setLayoutParams(rp);
		
		//tvPw.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etId);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etId);
		tvPw.setLayoutParams(rp);
		
		//etPw.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvPw);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvPw);
		etPw.setLayoutParams(rp);
		
		//tvPwConfirm.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etPw);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etPw);
		tvPwConfirm.setLayoutParams(rp);
		
		//etPwConfirm.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvConfirmPw);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvConfirmPw);
		etPwConfirm.setLayoutParams(rp);
		
		//tvPhone.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etConfirmPw);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etConfirmPw);
		tvPhone.setLayoutParams(rp);
		
		//etPhone.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_tvPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_tvPhone);
		etPhone.setLayoutParams(rp);
		
		//etVerification.
		rp = new RelativeLayout.LayoutParams(smallEditTextWidth, smallEditTextHeight);
		rp.topMargin = editTextMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForPersonalPage_etPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_etPhone);
		etVerification.setLayoutParams(rp);
		
		//btnSendVerification.
		rp = new RelativeLayout.LayoutParams(smallButtonWidth, smallButtonHeight);
		rp.leftMargin = smallButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForPersonalPage_etVerification);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForPersonalPage_etVerification);
		btnSendVerification.setLayoutParams(rp);
		
		//btnComplete.
		rp = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
		rp.topMargin = buttonMarginTop;
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.signUpForPersonalPage_btnSendVerification);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_btnSendVerification);
		btnComplete.setLayoutParams(rp);
		
		FontInfo.setFontSize(btnSendVerification, 26);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_personal;
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
