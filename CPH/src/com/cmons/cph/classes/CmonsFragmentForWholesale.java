package com.cmons.cph.classes;

import android.os.Bundle;

import com.cmons.cph.WholesaleActivity;
import com.cmons.cph.views.TitleBar;

public abstract class CmonsFragmentForWholesale extends CmonsFragment {

	protected TitleBar titleBar;
	protected WholesaleActivity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (WholesaleActivity) getActivity();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();

		titleBar.setTitleText(title);
		mActivity.setTitleText(title);
	}
}