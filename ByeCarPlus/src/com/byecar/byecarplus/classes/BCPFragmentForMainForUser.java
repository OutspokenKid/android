package com.byecar.byecarplus.classes;

import android.os.Bundle;

import com.byecar.byecarplus.MainForUserActivity;

public abstract class BCPFragmentForMainForUser extends BCPFragment {

	protected MainForUserActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (MainForUserActivity) getActivity();
		}
	}
}
