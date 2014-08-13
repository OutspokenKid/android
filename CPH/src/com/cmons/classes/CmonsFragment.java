package com.cmons.classes;

import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.utils.SoftKeyboardUtils;

public abstract class CmonsFragment extends BaseFragment {

	protected String title;

	public abstract void onRefreshPage();

	@Override
	public void onResume() {
		super.onResume();

		SoftKeyboardUtils.hideKeyboard(mContext, mThisView);
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastPageAnimResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showLoadingView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideLoadingView() {
		// TODO Auto-generated method stub

	}
}