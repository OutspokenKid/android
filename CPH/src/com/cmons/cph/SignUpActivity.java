package com.cmons.cph;

import android.os.Bundle;

import com.cmons.classes.BaseFragmentActivity;
import com.cmons.cph.fragments.SignUpForBusinessPage;
import com.cmons.cph.fragments.SignUpForCategoryPage;
import com.cmons.cph.fragments.SignUpForPersonalPage;
import com.cmons.cph.fragments.SignUpForPositionPage;
import com.cmons.cph.fragments.SignUpForSearchPage;
import com.cmons.cph.fragments.SignUpForTermsPage;
import com.cmons.cph.fragments.SignUpForWritePage;
import com.cmons.cph.models.Wholesale;

public class SignUpActivity extends BaseFragmentActivity {

	/**
	 * type
	 * 0 : 도매 - 대표.
	 * 1 : 도매 - 직원.
	 * 2 : 도매 - 디자이너.
	 * 
	 * 3 : 소매(오프라인) - 대표.
	 * 4 : 소매(오프라인) - 직원.
	 * 5 : 소매(오프라인) - MD.
	 * 
	 * 6 : 소매(온라인) - 대표.
	 * 7 : 소매(온라인) - 직원.
	 * 8 : 소매(온라인) - MD.
	 */
	
	public static final int BUSINESS_WHOLESALE = 0;
	public static final int BUSINESS_RETAIL_OFFLINE = 3;
	public static final int BUSINESS_RETAIL_ONLINE = 6;

	public static final int POSITION_OWNER = 0;
	public static final int POSITION_EMPLOYEE1 = 1;
	public static final int POSITION_EMPLOYEE2 = 2;
	
	@Override
	protected void bindViews() {

	}

	@Override
	protected void setVariables() {
	}

	@Override
	protected void createPage() {

	}

	@Override
	protected void setSizes() {

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {

		setPage(true);
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showTermsPage();
		}
	}

	@Override
	protected int getXmlResId() {

		return R.layout.activity_sign_up;
	}

	@Override
	protected int getFragmentFrameId() {

		return R.id.signUpActivity_fragmentFrame;
	}
	
	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitleText(String title) {

	}
	
	@Override
	public void onBackPressed() {
		
		if(getFragmentsSize() > 1){
			closeTopPage();
		} else {
			super.onBackPressed();
		}
	}
	
/////////////////////////// Custom methods.
	
	public void showTermsPage() {
		
		startPage(new SignUpForTermsPage(), null);
	}
	
	public void showBusinessPage() {
		
		startPage(new SignUpForBusinessPage(), null);
	}
	
	public void showPositionPage(int type) {

		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		startPage(new SignUpForPositionPage(), bundle);
	}
	
	public void setPosition(int type) {
		
		switch(type) {

		case BUSINESS_WHOLESALE + POSITION_OWNER:
			showCategoryPage(type);
			break;
			
		case BUSINESS_WHOLESALE + POSITION_EMPLOYEE1:
		case BUSINESS_WHOLESALE + POSITION_EMPLOYEE2:
		case BUSINESS_RETAIL_OFFLINE + POSITION_EMPLOYEE1:
		case BUSINESS_RETAIL_OFFLINE + POSITION_EMPLOYEE2:
		case BUSINESS_RETAIL_ONLINE + POSITION_EMPLOYEE1:
		case BUSINESS_RETAIL_ONLINE + POSITION_EMPLOYEE2:
			showSearchPage(type, null);
			break;
		
		case BUSINESS_RETAIL_OFFLINE + POSITION_OWNER:
		case BUSINESS_RETAIL_ONLINE + POSITION_OWNER:
			showWritePage(type);
			break;
		}
	}
	
	public void showCategoryPage(int type) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		startPage(new SignUpForCategoryPage(), bundle);
	}
	
	public void showSearchPage(int type, String categoryString) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putString("categoryString", categoryString);
		
		startPage(new SignUpForSearchPage(), bundle);
	}
	
	public void showWritePage(int type) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		startPage(new SignUpForWritePage(), bundle);
	}
	
	public void showPersonalPage(int type, Wholesale wholesale, String categoryString) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putSerializable("wholesale", wholesale);
		bundle.putString("categoryString", categoryString);
		
		startPage(new SignUpForPersonalPage(), bundle);
	}
	
	public void launchMainActivity() {
		
//		Intent intent = new Intent(this, WholesaleActivity.class);
//		startActivity(intent);
	}

	public void showLoadingView() {
		
	}
	
	public void hideLoadingView() {
		
	}
	
//	public int getType() {
//		
//		return businessType + positionType;
//	}
}
