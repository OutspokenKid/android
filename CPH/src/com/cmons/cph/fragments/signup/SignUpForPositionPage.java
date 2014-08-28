package com.cmons.cph.fragments.signup;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForPositionPage extends CmonsFragmentForSignUp {

	private Button btnPosition1;
	private Button btnPosition2;
	private Button btnPosition3;
	
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForPositionPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForPositionPage_ivBg);
		
		btnPosition1 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition1);
		btnPosition2 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition2);
		btnPosition3 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition3);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
		
		title = getString(R.string.selectPosition);
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		switch(type) {
		
		case SignUpActivity.BUSINESS_WHOLESALE:
			btnPosition1.setBackgroundResource(R.drawable.class1_1_btn);
			btnPosition2.setBackgroundResource(R.drawable.class1_2_btn);
			btnPosition3.setBackgroundResource(R.drawable.class1_3_btn);
			break;
			
		case SignUpActivity.BUSINESS_RETAIL_OFFLINE:
		case SignUpActivity.BUSINESS_RETAIL_ONLINE:
			btnPosition1.setBackgroundResource(R.drawable.class2_1_btn);
			btnPosition2.setBackgroundResource(R.drawable.class2_2_btn);
			btnPosition3.setBackgroundResource(R.drawable.class2_3_btn);
			break;
		}
	}

	@Override
	public void setListeners() {
		
		btnPosition1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(type + SignUpActivity.POSITION_OWNER);
			}
		});
		
		btnPosition2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(type + SignUpActivity.POSITION_EMPLOYEE1);
			}
		});
		
		btnPosition3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(type + SignUpActivity.POSITION_EMPLOYEE2);
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPositionPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//btnOwner.
		rp = (RelativeLayout.LayoutParams) btnPosition1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		
		//btnEmployee1.
		rp = (RelativeLayout.LayoutParams) btnPosition2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//btnEmployee2.
		rp = (RelativeLayout.LayoutParams) btnPosition3.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}
	
	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_sign_up_position;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}
