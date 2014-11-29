package com.byecar.byecarplus.classes;

import android.os.Bundle;

import com.byecar.byecarplus.SignActivity;

public abstract class BCPFragmentForSign extends BCPFragment {

	protected SignActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (SignActivity) getActivity();
		}
	}
}
