package com.cmons.classes;

import android.os.Bundle;

import com.cmons.cph.SignUpActivity;

public abstract class BaseFragmentForSignUp extends BaseFragment {

	protected SignUpActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (SignUpActivity) getActivity();
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			mActivity.setTitleText(title);
		}
	}
}
