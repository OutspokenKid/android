package com.cmons.cph.fragments.signup;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForTermsPage extends CmonsFragmentForSignUp {
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForTermsPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForTermsPage_ivBg);
	}

	@Override
	public void setVariables() {

		title = getString(R.string.terms);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().finish();
			}
		});
		
		titleBar.getBtnAgree().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showBusinessPage();
			}
		});
	}

	@Override
	public void setSizes() {
		
		//ScrollView.
		int scrollPadding = ResizeUtils.getSpecificLength(26);
		mThisView.findViewById(R.id.signUpForTermsPage_scrollView).setPadding(
				scrollPadding, scrollPadding, scrollPadding, scrollPadding);
		
		//shadow.
		RelativeLayout.LayoutParams rp = null;
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForTermsPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_terms;
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
