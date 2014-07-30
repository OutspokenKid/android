package com.cmons.cph.fragments;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForPositionPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	private Button btnOwner;
	private Button btnEmployee1;
	private Button btnEmployee2;
	
	private int type;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForTermPage_titleBar);
		btnOwner = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnOwner);
		btnEmployee1 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnEmployee1);
		btnEmployee2 = (Button) mThisView.findViewById(R.id.signUpForPositionPage_btnEmployee2);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_login, 162, 92);
		titleBar.setTitleText(R.string.findId);
		
		switch(type) {
		
		case SignUpActivity.BUSINESS_WHOLESALE:
			btnEmployee2.setText(R.string.employee_designer);
			break;
			
		case SignUpActivity.BUSINESS_RETAIL_OFFLINE:
		case SignUpActivity.BUSINESS_RETAIL_ONLINE:
			btnEmployee2.setText(R.string.employee_md);
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
		
		btnOwner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(SignUpActivity.POSITION_OWNER);
			}
		});
		
		btnEmployee1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(SignUpActivity.POSITION_EMPLOYEE1);
			}
		});
		
		btnEmployee2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setPosition(SignUpActivity.POSITION_EMPLOYEE2);
			}
		});
	}

	@Override
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//btnOwner.
		rp = (RelativeLayout.LayoutParams) btnOwner.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//btnEmployee1.
		rp = (RelativeLayout.LayoutParams) btnEmployee1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//btnEmployee2.
		rp = (RelativeLayout.LayoutParams) btnEmployee2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_ivCopyright).getLayoutParams();
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
