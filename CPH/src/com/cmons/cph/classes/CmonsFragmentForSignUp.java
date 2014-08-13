package com.cmons.cph.classes;

import android.os.Bundle;

import com.cmons.cph.SignUpActivity;

public abstract class CmonsFragmentForSignUp extends CmonsFragment {

	protected SignUpActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (SignUpActivity) getActivity();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mActivity.setTitleText(title);
	}
}
