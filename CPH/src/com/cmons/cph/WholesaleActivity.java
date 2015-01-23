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
import com.cmons.cph.fragments.wholesale.WholesaleForAccountPage;
import com.cmons.cph.fragments.wholesale.WholesaleForCustomerListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForCustomerPage;
import com.cmons.cph.fragments.wholesale.WholesaleForManagementPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.fragments.wholesale.WholesaleForProfileImagePage;
import com.cmons.cph.fragments.wholesale.WholesaleForSampleListPage;
import com.cmons.cph.fragments.wholesale.WholesaleForShopPage;
import com.cmons.cph.fragments.wholesale.WholesaleForWritePage;
import com.cmons.cph.fragments.wholesale.WholesaleMainPage;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;

public class WholesaleActivity extends ShopActivity {
	
	public Wholesale wholesale;
	
	@Override
	public void bindViews() {
		
	}

	@Override
	public void createPage() {

		getWholesaleFromSharedPrefs();
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

		downloadCategory();
		downloadWholesale();
		
		checkDownload();
	}

	@Override
	public int getContentViewId() {

		return R.layout.activity_wholesale;
	}

	@Override
	public int getFragmentFrameResId() {

		return R.id.wholesaleActivity_fragmentFrame;
	}
	
	@Override
	public CmonsFragment getFragmentByPageCode(int pageCode) {

		switch(pageCode) {

		case CphConstants.PAGE_WHOLESALE_MAIN:
			return new WholesaleMainPage();
			
		case CphConstants.PAGE_WHOLESALE_ACCOUNT:
			return new WholesaleForAccountPage();
			
		case CphConstants.PAGE_WHOLESALE_CUSTOMER_LIST:
			return new WholesaleForCustomerListPage();
			
		case CphConstants.PAGE_WHOLESALE_MANAGEMENT:
			return new WholesaleForManagementPage();
			
		case CphConstants.PAGE_NOTICE_LIST:
			return new NoticeListPage();
			
		case CphConstants.PAGE_NOTICE:
			return new NoticePage();
			
		case CphConstants.PAGE_NOTIFICATION_SETTING:
			return new NotificationSettingPage();
			
		case CphConstants.PAGE_WHOLESALE_ORDER_LIST:
			return new WholesaleForOrderListPage();
			
		case CphConstants.PAGE_WHOLESALE_ORDER:
			return new WholesaleForOrderPage();
			
		case CphConstants.PAGE_PRODUCT:
			return new ProductPage();
			
		case CphConstants.PAGE_WHOLESALE_PROFILE_IMAGE:
			return new WholesaleForProfileImagePage();
			
		case CphConstants.PAGE_WHOLESALE_SAMPLE_LIST:
			return new WholesaleForSampleListPage();
			
		case CphConstants.PAGE_SETTING:
			return new SettingPage();
			
		case CphConstants.PAGE_WHOLESALE_SHOP:
			return new WholesaleForShopPage();
			
		case CphConstants.PAGE_WHOLESALE_WRITE:
			return new WholesaleForWritePage();
			
		case CphConstants.PAGE_CHANGE_INFO:
			return new ChangeInfoPage();
			
		case CphConstants.PAGE_CHANGE_PASSWORD:
			return new ChangePasswordPage();
			
		case CphConstants.PAGE_CHANGE_PHONENUMBER:
			return new ChangePhoneNumberPage();
			
		case CphConstants.PAGE_STAFF:
			return new StaffPage();
			
		case CphConstants.PAGE_REPLY:
			return new ReplyPage();
			
		case CphConstants.PAGE_WHOLESALE_CUSTOMER:
			return new WholesaleForCustomerPage();
			
		case CphConstants.PAGE_GUIDE:
			return new GuidePage();
		}
		
		return null;
	}
	
	@Override
	public void handleUri(Uri uri) {
		
		String url = uri.getHost() + uri.getPath();
		
		LogUtils.log("###WholesaleActivity.handleUri" +
				"\nuri : " + uri);
		
		//상품 주문
		if(url.contains("wholesales/orders")) {
			Bundle bundle = new Bundle();
			bundle.putInt("menuIndex", 0);
			showPage(CphConstants.PAGE_WHOLESALE_ORDER_LIST, bundle);
			
		//상품 상태값 변경 (입금완료)
		} else if(url.contains("retails/orders")) {
			Bundle bundle = new Bundle();
			bundle.putInt("menuIndex", 0);
			showPage(CphConstants.PAGE_WHOLESALE_ORDER_LIST, bundle);
		
		//상품 상태값 변경 (입금완료)
		} else if(url.contains("retails/orders")) {
			Bundle bundle = new Bundle();
			bundle.putInt("menuIndex", 0);
			showPage(CphConstants.PAGE_WHOLESALE_ORDER_LIST, bundle);
			
			//products/replies

		//상품에서 댓글 작성
		} else if(url.contains("products/replies")) {
			int product_id = Integer.parseInt(uri.getQueryParameter("product_id"));
			Bundle bundle = new Bundle();
			bundle.putInt("product_id", product_id);
			showPage(CphConstants.PAGE_REPLY, bundle);
			
		//전체 공지 작성 (need_push:1)
		} else if(url.contains("notices")) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("isAppNotice", true);
			bundle.putBoolean("isOurNotice", false);
			showPage(CphConstants.PAGE_NOTICE_LIST, bundle);
			
		//샘플 요청
		} else if(url.contains("wholesales/samples")) {
			showPage(CphConstants.PAGE_WHOLESALE_SAMPLE_LIST, null);
			
		//직원 상태값 변경 (승인)
		} else if(url.contains("home")) {
			//Do nothing.
			
		//직원 상태값 변경 (거절)
		} else if(url.contains("home")) {
			//Do nothing.
			
		//거래처 요청
		} else if(url.contains("wholesales/customers/requested")) {
			showPage(CphConstants.PAGE_WHOLESALE_CUSTOMER_LIST, null);
			
		//회원가입
		} else if(url.contains("users/staffs")) {
			showPage(CphConstants.PAGE_STAFF, null);
			
		//대표 승인
		} else if(url.contains("home")) {
			//Do nothing.
			
		//도매 매장 사용불가 처리
		} else if(url.equals("wholesales/disable")) {
			signOut();
			
		//주문 댓글.
		} else if(url.equals("orders/replies")) {
			//"uri": "cph://orders/replies?order_collapse_key=WgncFfghjmRbSbgSWhIfnxCFaurjiPhZ&post_id=119",
			
			String order_collapse_key = uri.getQueryParameter("order_collapse_key"); 
			
			//http://cph-app.co.kr/wholesales/orders/show?collapse_key=KBxyqdgxprPeFnAOrvRDomOaEprZiGMH
			String apiUrl = CphConstants.BASE_API_URL + "wholesales/orders/show"
					+ "?collapse_key=" + order_collapse_key;
			DownloadUtils.downloadJSONString(apiUrl, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("WholesaleActivity.handleUri.orders/replies.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleActivity.handleUri.orders/replies.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						OrderSet orderSet = new OrderSet(objJSON.getJSONObject("order"));
						Bundle bundle = new Bundle();
						bundle.putSerializable("orderSet", orderSet);
						showPage(CphConstants.PAGE_WHOLESALE_ORDER, bundle);
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		}
	}

	public void getWholesaleFromSharedPrefs() {

		try {
			String jsonString = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_SHOP, "wholesale");
			
			if(!StringUtils.isEmpty(jsonString)) {
				LogUtils.log("###WholesaleActivity.getWholesaleFromSharedPrefs.  " +
						"has Wholesale.");
				JSONObject objJSON = new JSONObject(jsonString); 
				wholesale = new Wholesale(objJSON);
			} else {
				LogUtils.log("###WholesaleActivity.getWholesaleFromSharedPrefs.  " +
						"has not Wholesale.");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
//////////////////// Custom methods.

	public void downloadWholesale() {
		
		String url = CphConstants.BASE_API_URL + "wholesales/show" +
				"?wholesale_id=" + user.getWholesale_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleActivity.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadWholesale();
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
					SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_SHOP, "wholesale", wholesale.getJsonString());
					
					LogUtils.log("###where.onCompleted.  sampleav : " + wholesale.getSample_available());
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
				&& wholesale != null
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
	
	public void showMainPage() {
		
		showPage(CphConstants.PAGE_WHOLESALE_MAIN, null);

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				checkIntent();
			}
		}, 500);
	}
}
