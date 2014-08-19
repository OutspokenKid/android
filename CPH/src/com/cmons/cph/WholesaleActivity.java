package com.cmons.cph;

import android.content.Intent;
import android.os.Bundle;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.fragments.wholesale.WholesaleForChangeInfoPage;
import com.cmons.cph.fragments.wholesale.WholesaleForChangePasswordPage;
import com.cmons.cph.fragments.wholesale.WholesaleForChangePhoneNumberPage;
import com.cmons.cph.fragments.wholesale.WholesaleForCustomerListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForManagementPage;
import com.cmons.cph.fragments.wholesale.WholesaleForNoticeListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForNoticePage;
import com.cmons.cph.fragments.wholesale.WholesaleForNotificationSettingPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.fragments.wholesale.WholesaleForSamplePage;
import com.cmons.cph.fragments.wholesale.WholesaleForSettingPage;
import com.cmons.cph.fragments.wholesale.WholesaleForShopPage;
import com.cmons.cph.fragments.wholesale.WholesaleForStaffPage;
import com.cmons.cph.fragments.wholesale.WholesaleMainPage;
import com.cmons.cph.models.Notice;
import com.cmons.cph.models.User;

public class WholesaleActivity extends CmonsFragmentActivity {

	public User user;
	
	@Override
	public void bindViews() {
		
	}

	@Override
	public void setVariables() {

		if(getIntent() != null) {
			user = (User) getIntent().getSerializableExtra("userInfo");
		}
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

	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		showMainPage();
	}

	@Override
	public int getContentViewId() {

		return R.layout.activity_wholesale;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTitleText(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFragmentFrameResId() {

		return R.id.wholesaleActivity_fragmentFrame;
	}
	
	@Override
	public void onBackPressed() {
		
		if(getTopFragment().onBackPressed()) {
			//Do nothing.
		} else {
			super.onBackPressed();
		}
	}
	
//////////////////// Custom methods.
	
	public void showMainPage() {
		
		startPage(new WholesaleMainPage(), null);
	}
	
	public void showShopPage() {
		
		startPage(new WholesaleForShopPage(), null);
	}
	
	public void showManagementPage() {
		
		startPage(new WholesaleForManagementPage(), null);
	}
	
	public void showOrderListPage() {
		
		startPage(new WholesaleForOrderListPage(), null);
	}
	
	public void showOrderPage() {
		
		startPage(new WholesaleForOrderPage(), null);
	}
	
	public void showSamplePage() {
		
		startPage(new WholesaleForSamplePage(), null);
	}
	
	public void showCustomerPage() {
		
		startPage(new WholesaleForCustomerListPage(), null);
	}
	
	public void showStaffPage() {
		
		startPage(new WholesaleForStaffPage(), null);
	}
	
	public void showSettingPage() {
		
		startPage(new WholesaleForSettingPage(), null);
	}
	
	public void showWritePage() {
		
		//.
	}

	public void showNoticeListPage() {
		
		startPage(new WholesaleForNoticeListPage(), null);
	}
	
	public void showNoticePage(Notice notice) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("notice", notice);
		
		startPage(new WholesaleForNoticePage(), bundle);
	}
	
	public void showNotificationSettingPage() {
		
		startPage(new WholesaleForNotificationSettingPage(), null);
	}
	
	public void showChangeInfoPage() {
		
		startPage(new WholesaleForChangeInfoPage(), null);
	}
	
	public void showChangePasswordPage() {
		
		startPage(new WholesaleForChangePasswordPage(), null);
	}
	
	public void showChangePhoneNumberPage() {
		
		startPage(new WholesaleForChangePhoneNumberPage(), null);
	}

	public void launchSignInActivity() {
		
		Intent intent = new Intent(this, SignInActivity.class);
		startActivity(intent);
		finish();
	}
}
