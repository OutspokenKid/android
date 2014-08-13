package com.cmons.cph.fragments.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.classes.CmonsFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForTermsPage extends CmonsFragmentForSignUp {

	private TitleBar titleBar;
	private Button btnAgree;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForTermsPage_titleBar);
		btnAgree = (Button) mThisView.findViewById(R.id.signUpForTermsPage_btnAgree);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_login, 162, 92);
		titleBar.setTitleText(R.string.terms);
	}

	@Override
	public void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().finish();
			}
		});
		
		btnAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showBusinessPage();
			}
		});
	}

	@Override
	public void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForTermsPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);

		//ScrollView.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForTermsPage_scrollView).getLayoutParams();
		rp.bottomMargin = ResizeUtils.getSpecificLength(200);
		int scrollPadding = ResizeUtils.getSpecificLength(26);
		mThisView.findViewById(R.id.signUpForTermsPage_scrollView).setPadding(
				scrollPadding, scrollPadding, scrollPadding, 0);
		
		//btnAgree.
		rp = (RelativeLayout.LayoutParams) btnAgree.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.bottomMargin = ResizeUtils.getSpecificLength(100);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForTermsPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_terms;
	}

	@Override
	public void onRefreshPage() {
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
}
