package com.cmons.cph.fragments.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForBusinessPage extends CmonsFragmentForSignUp {

	private TitleBar titleBar;
	private Button btnWholesale;
	private Button btnRetailOffline;
	private Button btnRetailOnline;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForBusinessPage_titleBar);
		btnWholesale = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnWholesale);
		btnRetailOffline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOffline);
		btnRetailOnline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOnline);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_terms, 162, 92);
		titleBar.setTitleText(R.string.selectBusiness);
	}

	@Override
	public void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		btnWholesale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPositionPage(SignUpActivity.BUSINESS_WHOLESALE);
			}
		});
		
		btnRetailOffline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPositionPage(SignUpActivity.BUSINESS_RETAIL_OFFLINE);
			}
		});
		
		btnRetailOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPositionPage(SignUpActivity.BUSINESS_RETAIL_ONLINE);
			}
		});
	}

	@Override
	public void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForBusinessPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//btnWholesale.
		rp = (RelativeLayout.LayoutParams) btnWholesale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		
		//btnRetailOffline.
		rp = (RelativeLayout.LayoutParams) btnRetailOffline.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnRetailOnline.
		rp = (RelativeLayout.LayoutParams) btnRetailOnline.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(148);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForBusinessPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_business;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
