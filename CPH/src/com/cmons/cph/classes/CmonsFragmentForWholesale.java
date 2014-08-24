package com.cmons.cph.classes;

import android.app.Activity;

import com.cmons.cph.WholesaleActivity;

public abstract class CmonsFragmentForWholesale extends CmonsFragment {

	protected WholesaleActivity mActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if(mActivity == null) {
			mActivity = (WholesaleActivity) activity;
		}
	}
}