package com.cmons.cph;

import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.cmons.cph.classes.CmonsFragment;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.common.ChangeInfoPage;
import com.cmons.cph.fragments.common.ChangePasswordPage;
import com.cmons.cph.fragments.common.ChangePhoneNumberPage;
import com.cmons.cph.fragments.common.GuidePage;
import com.cmons.cph.fragments.common.NoticeListPage;
import com.cmons.cph.fragments.common.NoticePage;
import com.cmons.cph.fragments.common.NotificationSettingPage;
import com.cmons.cph.fragments.common.ProductPage;
import com.cmons.cph.fragments.common.ReplyPage;
import com.cmons.cph.fragments.common.SettingPage;
import com.cmons.cph.fragments.common.StaffPage;
import com.cmons.cph.fragments.retail.RetailForAddPartner;
import com.cmons.cph.fragments.retail.RetailForCustomerListPage;
import com.cmons.cph.fragments.retail.RetailForFavoriteProductPage;
import com.cmons.cph.fragments.retail.RetailForFavoriteShopPage;
import com.cmons.cph.fragments.retail.RetailForManagementPage;
import com.cmons.cph.fragments.retail.RetailForOrderPage;
import com.cmons.cph.fragments.retail.RetailForOrderProductPage;
import com.cmons.cph.fragments.retail.RetailForSampleListPage;
import com.cmons.cph.fragments.retail.RetailForSearchPage;
import com.cmons.cph.fragments.retail.RetailForShopPage;
import com.cmons.cph.fragments.retail.RetailForWishListPage;
import com.cmons.cph.fragments.retail.RetailForWishPage;
import com.cmons.cph.fragments.retail.RetailMainPage;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class RetailActivity extends ShopActivity {
	
	public Retail retail;
	
	@Override
	public void bindViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPage() {
		
		getRetailFromSharedPrefs();
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

		case CphConstants.PAGE_RETAIL_CUSTOMER_LIST:
			return new RetailForCustomerListPage();

		case CphConstants.PAGE_RETAIL_SEARCH:
			 return new RetailForSearchPage();

		case CphConstants.PAGE_RETAIL_WISH_LIST:
			 return new RetailForWishListPage();

		case CphConstants.PAGE_RETAIL_FAVORITE_SHOP:
			 return new RetailForFavoriteShopPage();

		case CphConstants.PAGE_RETAIL_FAVORITE_PRODUCT:
			 return new RetailForFavoriteProductPage();

		case CphConstants.PAGE_RETAIL_MANAGEMENT:
			return new RetailForManagementPage();
			
		case CphConstants.PAGE_COMMON_SETTING:
			return new SettingPage();
			
		case CphConstants.PAGE_COMMON_NOTICE_LIST:
			return new NoticeListPage();
			
		case CphConstants.PAGE_COMMON_NOTICE:
			return new NoticePage();
			
		case CphConstants.PAGE_COMMON_NOTIFICATION_SETTING:
			return new NotificationSettingPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_INFO:
			return new ChangeInfoPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PASSWORD:
			return new ChangePasswordPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PHONENUMBER:
			return new ChangePhoneNumberPage();
			
		case CphConstants.PAGE_COMMON_STAFF:
			return new StaffPage();
			
		case CphConstants.PAGE_RETAIL_WISH:
			 return new RetailForWishPage();
			 
		case CphConstants.PAGE_RETAIL_SHOP:
			 return new RetailForShopPage();
			 
		case CphConstants.PAGE_COMMON_PRODUCT:
			 return new ProductPage();
			 
		case CphConstants.PAGE_RETAIL_ORDER_PRODUCT:
			 return new RetailForOrderProductPage();
			 
		case CphConstants.PAGE_COMMON_REPLY:
			 return new ReplyPage();
			 
		case CphConstants.PAGE_RETAIL_ORDER:
			 return new RetailForOrderPage();
			 
		case CphConstants.PAGE_RETAIL_SAMPLE:
			 return new RetailForSampleListPage();
			 
		case CphConstants.PAGE_COMMON_GUIDE:
			return new GuidePage();
		}
		
		return null;
	}
	
	@Override
	public void handleUri(Uri uri) {

		String url = uri.getHost() + uri.getPath();
		
		LogUtils.log("###RetailActivity.handleIntent.clickOK " +
				"\nurl : " + url);
		
		//상품 상태값 변경 (거래완료)
		if(url.equals("retails/orders")) {
			showPage(CphConstants.PAGE_RETAIL_CUSTOMER_LIST, null);
			
		//상품 댓글 대댓글 작성
		} else if(url.equals("products/replies")) {
			int product_id = Integer.parseInt(uri.getQueryParameter("product_id"));
			Bundle bundle = new Bundle();
			bundle.putInt("product_id", product_id);
			showPage(CphConstants.PAGE_COMMON_REPLY, bundle);
			
		//전체 공지 작성 (need_push:1)
		} else if(url.equals("notices")) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("isAppNotice", true);
			bundle.putBoolean("isOurNotice", false);
			showPage(CphConstants.PAGE_COMMON_NOTICE_LIST, bundle);
			
		//도매 공지 작성 (need_push:1)
		} else if(url.equals("wholesales/notices")) {
			int wholesale_id = Integer.parseInt(uri.getQueryParameter("wholesale_id"));
			Bundle bundle = new Bundle();
			bundle.putBoolean("isAppNotice", false);
			bundle.putBoolean("isOurNotice", false);
			bundle.putInt("wholesale_id", wholesale_id);
			showPage(CphConstants.PAGE_COMMON_NOTICE_LIST, bundle);
			
		//상품 등록
		} else if(url.equals("products")) {
			int wholesale_id = Integer.parseInt(uri.getQueryParameter("wholesale_id"));
			String wholesaleInfoUrl = CphConstants.BASE_API_URL + "wholesales/show" +
					"?wholesale_id=" + wholesale_id;
			
			DownloadUtils.downloadJSONString(wholesaleInfoUrl,
					new OnJSONDownloadListener() {

						@Override
						public void onError(String url) {

							LogUtils.log("RetailActivity.handleIntent.onError."
									+ "\nurl : " + url);

						}

						@Override
						public void onCompleted(String url,
								JSONObject objJSON) {

							try {
								LogUtils.log("RetailActivity.handleIntent.onCompleted."
										+ "\nurl : "
										+ url
										+ "\nresult : "
										+ objJSON);

								if(objJSON.getInt("result") == 1) {
									Wholesale wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
									Bundle bundle = new Bundle();
									bundle.putSerializable("wholesale", wholesale);
									showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
								}
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					});
			
		//샘플 상태값 변경 (반납요청)
		} else if(url.equals("retails/samples")) {
			int status = Integer.parseInt(uri.getQueryParameter("status"));
			Bundle bundle = new Bundle();
			bundle.putInt("menuIndex", status);
			showPage(CphConstants.PAGE_RETAIL_SAMPLE, bundle);
			
		//직원 상태값 변경 (승인)
		//직원 상태값 변경 (거절)
		} else if(url.equals("home")) {
			//Do nothing.
			
		//거래처 승인
		//거래처 삭제
		} else if(url.equals("retails/customers")) {
			Bundle bundle = new Bundle();
			bundle.putInt("menuIndex", 1);
			showPage(CphConstants.PAGE_RETAIL_CUSTOMER_LIST, bundle);
			
		//회원가입
		} else if(url.equals("users/staffs")) {
			showPage(CphConstants.PAGE_COMMON_STAFF, null);
			
		//소매 매장 사용불가 처리
		} else if(url.equals("retails/disable")) {
			signOut();
		}
	}

	public void getRetailFromSharedPrefs() {

		try {
			if(!SharedPrefsUtils.checkPrefs(CphConstants.PREFS_SHOP, "retail")) {
				JSONObject objJSON = new JSONObject(
						SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_SHOP, "retail")); 
				retail = new Retail(objJSON);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
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

					retail = new Retail(objJSON.getJSONObject("retail"));
					SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_SHOP, "retail", retail.getJsonString());
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void checkDownload() {
		
		if(categories != null 
				&& categories.length > 0
				&& retail != null
				&& getFragmentsSize() == 0) {
			showMainPage();
		} else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					checkDownload();
				}
			}, 50);
		}
	}
	
	public void showGuidePage() {

		Bundle bundle = new Bundle();
		bundle.putBoolean("isWholesale", false);
		showPage(CphConstants.PAGE_COMMON_GUIDE, bundle);
	}
	
	public void showMainPage() {
		
		showPage(CphConstants.PAGE_RETAIL_MAIN, null);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				checkIntent();
			}
		}, 500);
	}
}
