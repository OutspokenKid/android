package com.cmons.cph.fragments;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;

public class SignUpForTermsPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	private ScrollView scrollView;
	private TextView tvTerms;
	private Button btnAgree;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForTermPage_titleBar);
		scrollView = (ScrollView) mThisView.findViewById(R.id.signUpForTermsPage_scrollView);
		tvTerms = (TextView) mThisView.findViewById(R.id.signUpForTermsPage_tvTerms);
		btnAgree = (Button) mThisView.findViewById(R.id.signUpForTermsPage_btnAgree);
	}

	@Override
	protected void setVariables() {

		String terms = "";
		
		for(int i=0; i<100; i++) {
			terms += "Terms of use\n";
		}
		
		tvTerms.setText(terms);
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
		
		btnAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.agree();
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

		//ScrollView.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_scrollView).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//btnAgree.
		rp = (RelativeLayout.LayoutParams) btnAgree.getLayoutParams();
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

		return R.layout.fragment_sign_up_terms;
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
