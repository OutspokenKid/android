package com.cmons.cph.classes;

import android.app.Activity;

import com.cmons.cph.ShopActivity;

public abstract class CmonsFragmentForShop extends CmonsFragment {

	protected ShopActivity mActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if(mActivity == null) {
			mActivity = (ShopActivity) activity;
		}
	}
}