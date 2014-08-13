package com.cmons.cph;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.fragments.wholesale.WholesaleForCustomerPage;
import com.cmons.cph.fragments.wholesale.WholesaleForManagementPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.fragments.wholesale.WholesaleForSamplePage;
import com.cmons.cph.fragments.wholesale.WholesaleForSettingPage;
import com.cmons.cph.fragments.wholesale.WholesaleForShopPage;
import com.cmons.cph.fragments.wholesale.WholesaleMainPage;
import com.cmons.cph.models.User;

public class WholesaleActivity extends CmonsFragmentActivity {

	private User user;
	
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
	
	public void showOrderPage() {
		
		startPage(new WholesaleForOrderPage(), null);
	}
	
	public void showSamplePage() {
		
		startPage(new WholesaleForSamplePage(), null);
	}
	
	public void showCustomerPage() {
		
		startPage(new WholesaleForCustomerPage(), null);
	}
	
	public void showStaffPage() {
		
		startPage(new WholesaleForCustomerPage(), null);
	}
	
	public void showSettingPage() {
		
		startPage(new WholesaleForSettingPage(), null);
	}
	
	public void showWritePage() {
		
		//.
	}
}
