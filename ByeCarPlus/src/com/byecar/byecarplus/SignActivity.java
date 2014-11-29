package com.byecar.byecarplus;

import android.content.Intent;
import android.net.Uri;

import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.byecar.byecarplus.fragments.FindPwPage;
import com.byecar.byecarplus.fragments.SignInPage;
import com.byecar.byecarplus.fragments.SignPage;
import com.byecar.byecarplus.fragments.SignUpForCommonPage;
import com.byecar.byecarplus.fragments.TermOfUsePage;

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
			showPage(BCPConstants.PAGE_SIGN, null);
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

	@Override
	public BCPFragment getFragmentByPageCode(int pageCode) {
		
		switch(pageCode) {
		
		case BCPConstants.PAGE_SIGN:
			return new SignPage();
		
		case BCPConstants.PAGE_SIGN_IN:
			return new SignInPage();
		
		case BCPConstants.PAGE_SIGN_UP_FOR_COMMON:
			return new SignUpForCommonPage();
		
		case BCPConstants.PAGE_FIND_PW:
			return new FindPwPage();
			
		case BCPConstants.PAGE_TERM_OF_USE:
			return new TermOfUsePage();
			
		}
		return null;
	}

	@Override
	public void handleUri(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.

	public void launchMainForUserActivity() {
		
		Intent intent = new Intent(this, MainForUserActivity.class);
		
		if(getIntent() != null && getIntent().getData() != null) {
			intent.setData(getIntent().getData());
		}
		
		startActivity(intent);
		finish();
	}
}
