package com.cmons.cph;

import org.json.JSONObject;

import android.os.Handler;

import com.cmons.cph.classes.CmonsFragment;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.common.ChangeInfoPage;
import com.cmons.cph.fragments.common.ChangePasswordPage;
import com.cmons.cph.fragments.common.ChangePhoneNumberPage;
import com.cmons.cph.fragments.common.StaffPage;
import com.cmons.cph.fragments.retail.RetailForAddPartner;
import com.cmons.cph.fragments.retail.RetailForCustomerListPage;
import com.cmons.cph.fragments.retail.RetailForManagementPage;
import com.cmons.cph.fragments.retail.RetailForProductPage;
import com.cmons.cph.fragments.retail.RetailForSettingPage;
import com.cmons.cph.fragments.retail.RetailMainPage;
import com.cmons.cph.models.Retail;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;

public class RetailActivity extends ShopActivity {
	
	public Retail retail;
	
	@Override
	public void bindViews() {
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

		downloadCategory();
		downloadRetail();
		
		checkDownload();
	}

	@Override
	public int getContentViewId() {

		return R.layout.activity_retail;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFragmentFrameResId() {

		return R.id.retailActivity_fragmentFrame;
	}

	@Override
	public CmonsFragment getFragmentByPageCode(int pageCode) {

		switch(pageCode) {

		case CphConstants.PAGE_RETAIL_MAIN:
			return new RetailMainPage();
			
		case CphConstants.PAGE_RETAIL_ADD_PARTNER:
			return new RetailForAddPartner();

		case CphConstants.PAGE_RETAIL_CUSTOMERLIST:
			return new RetailForCustomerListPage();

		case CphConstants.PAGE_RETAIL_SEARCH:
//			 return new RetailForSearchPage();

		case CphConstants.PAGE_RETAIL_BASKET:
//			 return new RetailForBakset();

		case CphConstants.PAGE_RETAIL_FAVORITE_SHOP:
//			 return new RetailForFavoriteShopPage();

		case CphConstants.PAGE_RETAIL_FAVORITE_PRODUCT:
//			 return new RetailForFavoriteProductPage();

		case CphConstants.PAGE_RETAIL_MANAGEMENT:
			return new RetailForManagementPage();
			
		case CphConstants.PAGE_RETAIL_SETTING:
			return new RetailForSettingPage();
			
		case CphConstants.PAGE_RETAIL_NOTICE_LIST:
			return new RetailForSettingPage();
			
		case CphConstants.PAGE_RETAIL_NOTIFICATION_SETTING:
			return new RetailForSettingPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_INFO:
			return new ChangeInfoPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PASSWORD:
			return new ChangePasswordPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PHONENUMBER:
			return new ChangePhoneNumberPage();
			
		case CphConstants.PAGE_COMMON_STAFF:
			return new StaffPage();
			
		case CphConstants.PAGE_RETAIL_PRODUCT:
			return new RetailForProductPage();
		}
		
		return null;
	}
	
//////////////////// Custom methods.
	
	public void downloadRetail() {
		
		String url = CphConstants.BASE_API_URL + "retails/show" +
				"?retail_id=" + user.getRetail_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailActivity.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadRetail();
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

//					retail = new Retail(objJSON.getJSONObject("retail"));
					retail = new Retail(objJSON);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void checkDownload() {
		
		if(categories != null && categories.length > 0
				&& retail != null) {
			showPage(CphConstants.PAGE_RETAIL_MAIN, null);
		} else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					checkDownload();
				}
			}, 50);
		}
	}

}
