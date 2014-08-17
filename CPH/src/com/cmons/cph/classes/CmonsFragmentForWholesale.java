package com.cmons.cph.classes;

import android.os.Bundle;

import com.cmons.cph.WholesaleActivity;

public abstract class CmonsFragmentForWholesale extends CmonsFragment {

	protected WholesaleActivity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mActivity == null) {
			mActivity = (WholesaleActivity) getActivity();
		}
	}
}