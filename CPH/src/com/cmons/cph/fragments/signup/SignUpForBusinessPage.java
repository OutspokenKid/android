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

public class SignUpForBusinessPage extends CmonsFragmentForSignUp {

	private Button btnWholesale;
	private Button btnRetailOffline;
	private Button btnRetailOnline;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForBusinessPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForBusinessPage_ivBg);
		
		btnWholesale = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnWholesale);
		btnRetailOffline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOffline);
		btnRetailOnline = (Button) mThisView.findViewById(R.id.signUpForBusinessPage_btnRetailOnline);
	}

	@Override
	public void setVariables() {

		title = getString(R.string.selectBusiness);
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {
		
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
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForBusinessPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//btnWholesale.
		rp = (RelativeLayout.LayoutParams) btnWholesale.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		
		//btnRetailOffline.
		rp = (RelativeLayout.LayoutParams) btnRetailOffline.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//btnRetailOnline.
		rp = (RelativeLayout.LayoutParams) btnRetailOnline.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_business;
	}

	@Override
	public void refreshPage() {
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
