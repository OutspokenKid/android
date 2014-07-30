package com.cmons.cph.fragments;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForBusinessPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	private Button btnWholesale;
	private Button btnRetailOffline;
	private Button btnRetailOnline;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForTermPage_titleBar);
		btnWholesale = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnWholesale);
		btnRetailOffline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOffline);
		btnRetailOnline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOnline);
	}

	@Override
	protected void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_login, 162, 92);
		titleBar.setTitleText(R.string.findId);
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		btnWholesale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setBusiness(SignUpActivity.BUSINESS_WHOLESALE);
			}
		});
		
		btnRetailOffline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setBusiness(SignUpActivity.BUSINESS_RETAIL_OFFLINE);
			}
		});
		
		btnRetailOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.setBusiness(SignUpActivity.BUSINESS_RETAIL_ONLINE);
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
		
		//btnWholesale.
		rp = (RelativeLayout.LayoutParams) btnWholesale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//btnRetailOffline.
		rp = (RelativeLayout.LayoutParams) btnRetailOffline.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//btnRetailOnline.
		rp = (RelativeLayout.LayoutParams) btnRetailOnline.getLayoutParams();
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

		return R.layout.fragment_sign_up_business;
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
