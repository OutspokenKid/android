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

public class SignUpForSearchPage extends BaseFragmentForSignUp {

	private TextView tvCompanyName;
	private EditText etCompanyName;
	private Button btnCompanyName;
	
	private TextView tvCompanyPhone;
	private EditText etCompanyPhone;
	private Button btnCompanyPhone;
	
	private TextView tvCompanyLocation;
	private Button btnCompanyFloor;
	private Button btnCompanyRoomNumber;
	private Button btnCompanyLocation;
	
	private TextView tvCompanyRegistration;
	private EditText etCompanyRegistration;
	private Button btnCompanyRegistration;
	
	private int type;
	
	@Override
	protected void bindViews() {

		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyName);
		btnCompanyName = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyName);
		
		tvCompanyPhone = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyPhone);
		etCompanyPhone = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyPhone);
		btnCompanyPhone = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyPhone);
		
		tvCompanyLocation = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyLocation);
		btnCompanyFloor = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyFloor);
		btnCompanyRoomNumber = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyRoomNumber);
		btnCompanyLocation = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyLocation);
		
		tvCompanyRegistration = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyRegistration);
		etCompanyRegistration = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyRegistration);
		btnCompanyRegistration = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyRegistration);
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

		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.showPersonalPage(type);
			}
		};
		
		btnCompanyName.setOnClickListener(ocl);
		btnCompanyPhone.setOnClickListener(ocl);
		btnCompanyLocation.setOnClickListener(ocl);
		btnCompanyRegistration.setOnClickListener(ocl);
	}

	@Override
	protected void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int textViewWidth = LayoutParams.WRAP_CONTENT;
		int textViewHeight = ResizeUtils.getSpecificLength(80);
		int textViewMarginLeft = ResizeUtils.getSpecificLength(10);
		int textViewMarginTop = ResizeUtils.getSpecificLength(40);
		
		int editTextWidth = ResizeUtils.getSpecificLength(500);
		int editTextHeight = ResizeUtils.getSpecificLength(80);
		
		int searchButtonWidth = ResizeUtils.getSpecificLength(120);
		int searchButtonHeight = ResizeUtils.getSpecificLength(80);
		int searchButtonMarginLeft = ResizeUtils.getSpecificLength(10);
		
		int locationButtonWidth = ResizeUtils.getSpecificLength(240);
		int locationButtonHeight = ResizeUtils.getSpecificLength(80);
		int locationButtonMarginLeft = ResizeUtils.getSpecificLength(20);
		
		//tvCompanyName.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.leftMargin = textViewMarginLeft;
		rp.topMargin = textViewMarginTop * 2;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvCompanyName.setLayoutParams(rp);
		
		//etCompanyName.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_tvCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_tvCompanyName);
		etCompanyName.setLayoutParams(rp);
		
		//btnCompanyName.
		rp = new RelativeLayout.LayoutParams(searchButtonWidth, searchButtonHeight);
		rp.leftMargin = searchButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForSearchPage_etCompanyName);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForSearchPage_etCompanyName);
		btnCompanyName.setLayoutParams(rp);
		
		//tvCompanyPhone.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_etCompanyName);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_etCompanyName);
		tvCompanyPhone.setLayoutParams(rp);
		
		//etCompanyPhone.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_tvCompanyPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_tvCompanyPhone);
		etCompanyPhone.setLayoutParams(rp);
		
		//btnCompanyPhone.
		rp = new RelativeLayout.LayoutParams(searchButtonWidth, searchButtonHeight);
		rp.leftMargin = searchButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForSearchPage_etCompanyPhone);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForSearchPage_etCompanyPhone);
		btnCompanyPhone.setLayoutParams(rp);
		
		//tvCompanyLocation.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_etCompanyPhone);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_etCompanyPhone);
		tvCompanyLocation.setLayoutParams(rp);
		
		//btnCompanyFloor.
		rp = new RelativeLayout.LayoutParams(locationButtonWidth, locationButtonHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_tvCompanyLocation);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_tvCompanyLocation);
		btnCompanyFloor.setLayoutParams(rp);
		
		//btnCompanyRoomNumber.
		rp = new RelativeLayout.LayoutParams(locationButtonWidth, locationButtonHeight);
		rp.leftMargin = locationButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForSearchPage_btnCompanyFloor);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForSearchPage_btnCompanyFloor);
		btnCompanyRoomNumber.setLayoutParams(rp);
		
		//btnCompanyLocation.
		rp = new RelativeLayout.LayoutParams(searchButtonWidth, searchButtonHeight);
		rp.leftMargin = searchButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForSearchPage_btnCompanyRoomNumber);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForSearchPage_btnCompanyRoomNumber);
		btnCompanyLocation.setLayoutParams(rp);
		
		//tvCompanyRegistration.
		rp = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
		rp.topMargin = textViewMarginTop;
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_btnCompanyFloor);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_btnCompanyFloor);
		tvCompanyRegistration.setLayoutParams(rp);
		
		//etCompanyRegistration.
		rp = new RelativeLayout.LayoutParams(editTextWidth, editTextHeight);
		rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.signUpForSearchPage_tvCompanyRegistration);
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_tvCompanyRegistration);
		etCompanyRegistration.setLayoutParams(rp);
		
		//btnCompanyRegistration.
		rp = new RelativeLayout.LayoutParams(searchButtonWidth, searchButtonHeight);
		rp.leftMargin = searchButtonMarginLeft;
		rp.addRule(RelativeLayout.ALIGN_TOP, R.id.signUpForSearchPage_etCompanyRegistration);
		rp.addRule(RelativeLayout.RIGHT_OF, R.id.signUpForSearchPage_etCompanyRegistration);
		btnCompanyRegistration.setLayoutParams(rp);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_search;
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
