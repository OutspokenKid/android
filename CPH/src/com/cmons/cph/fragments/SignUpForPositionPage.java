package com.cmons.cph.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForPositionPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	private Button btnPosition1;
	private Button btnPosition2;
	private Button btnPosition3;
	
	private int type;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForPositionPage_titleBar);
		btnPosition1 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition1);
		btnPosition2 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition2);
		btnPosition3 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnPosition3);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_business, 162, 92);
		titleBar.setTitleText(R.string.selectPosition);
		
		switch(type) {
		
		case SignUpActivity.BUSINESS_WHOLESALE:
			btnPosition1.setBackgroundResource(R.drawable.btn_owner);
			btnPosition2.setBackgroundResource(R.drawable.btn_employee);
			btnPosition3.setBackgroundResource(R.drawable.btn_designer);
			break;
			
		case SignUpActivity.BUSINESS_RETAIL_OFFLINE:
		case SignUpActivity.BUSINESS_RETAIL_ONLINE:
			btnPosition1.setBackgroundResource(R.drawable.btn_owner);
			btnPosition2.setBackgroundResource(R.drawable.btn_employee);
			btnPosition3.setBackgroundResource(R.drawable.btn_md);
			break;
		}
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
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
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPositionPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//btnOwner.
		rp = (RelativeLayout.LayoutParams) btnPosition1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		
		//btnEmployee1.
		rp = (RelativeLayout.LayoutParams) btnPosition2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnEmployee2.
		rp = (RelativeLayout.LayoutParams) btnPosition3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPositionPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_position;
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
