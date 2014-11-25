package com.cmons.byecarplus;

import com.cmons.byecarplus.classes.BCPFragmentActivity;
import com.cmons.byecarplus.fragments.SignPage;

public class SignActivity extends BCPFragmentActivity {

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showSignPage();
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_sign;
	}

	@Override
	public int getFragmentFrameResId() {
		return R.id.signActivity_fragmentFrame;
	}

//////////////////// Custom methods.
	
	public void showSignPage() {
		
		startPage(new SignPage(), null);
	}
	
	public void showSignUpPage() {
		
	}
}
