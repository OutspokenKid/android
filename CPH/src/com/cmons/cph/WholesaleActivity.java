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
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;

public class WholesaleActivity extends ShopActivity {

	public Wholesale wholesale;
	
	@Override
	public void bindViews() {
		
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

		downloadCategory();
		downloadWholesale();
		
		checkDownload();
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
			
		case CphConstants.PAGE_COMMON_NOTICE_LIST:
			return new NoticeListPage();
			
		case CphConstants.PAGE_COMMON_NOTICE:
			return new NoticePage();
			
		case CphConstants.PAGE_COMMON_NOTIFICATION_SETTING:
			return new NotificationSettingPage();
			
		case CphConstants.PAGE_WHOLESALE_ORDER_LIST:
			return new WholesaleForOrderListPage();
			
		case CphConstants.PAGE_WHOLESALE_ORDER:
			return new WholesaleForOrderPage();
			
		case CphConstants.PAGE_COMMON_PRODUCT:
			return new ProductPage();
			
		case CphConstants.PAGE_WHOLESALE_PROFILE_IMAGE:
			return new WholesaleForProfileImagePage();
			
		case CphConstants.PAGE_WHOLESALE_SAMPLE_LIST:
			return new WholesaleForSampleListPage();
			
		case CphConstants.PAGE_COMMON_SETTING:
			return new SettingPage();
			
		case CphConstants.PAGE_WHOLESALE_SHOP:
			return new WholesaleForShopPage();
			
		case CphConstants.PAGE_WHOLESALE_WRITE:
			return new WholesaleForWritePage();
			
		case CphConstants.PAGE_COMMON_CHANGE_INFO:
			return new ChangeInfoPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PASSWORD:
			return new ChangePasswordPage();
			
		case CphConstants.PAGE_COMMON_CHANGE_PHONENUMBER:
			return new ChangePhoneNumberPage();
			
		case CphConstants.PAGE_COMMON_STAFF:
			return new StaffPage();
			
		case CphConstants.PAGE_COMMON_REPLY:
			return new ReplyPage();
			
		case CphConstants.PAGE_WHOLESALE_CUSTOMER:
			return new WholesaleForCustomerPage();
		}
		
		return null;
	}

	/**
	 * id : push id. 
	 * receiver_id : 받는 사람의 id.
	 * message : 유저에게 보여줄 메세지.
	 * uri : 연결할 uri.
	 * created_at : 생성된 시간.
	 * pushed_at : 보내진 시간.
	 * read_at : 유저가 읽은 시간.
	 */
	@Override
	public void handleUri(Uri uri) {

		try {
			String scheme = uri.getScheme();
			String host = uri.getHost();
			String url = host + uri.getPath();
			
			LogUtils.log("WholesaleActivity.actionByUri. ========" +
					"\nuri : " + uri + 
					"\nscheme : "+ scheme +
					"\nhost : " + host + 
					"\nurl : " + url);
			
			if(scheme.equals("http")||scheme.equals("https")) {
				IntentUtils.showDeviceBrowser(this, url);
				
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				IntentUtils.showMarket(this, uri);
				
			} else if(scheme.equals("cph")) {
				
				//상품 주문
				if(url.equals("cph://wholesales/orders")) {
					
				//상품 상태값 변경 (입금완료)
				} else if(url.equals("cph://retails/orders")) {
					
				//상품에서 댓글 작성
				} else if(url.equals("cph://products/replies?product_id=100001&post_id=1")) {
					
				//전체 공지 작성 (need_push:1)
				} else if(url.equals("cph://notices")) {
					
				//샘플 요청
				} else if(url.equals("cph://wholesales/samples")) {
					
				//직원 상태값 변경 (승인)
				} else if(url.equals("cph://home")) {
					
				//직원 상태값 변경 (거절)
				} else if(url.equals("cph://home")) {
					
				//거래처 요청
				} else if(url.equals("cph://wholesales/customers/requested")) {
					
				//회원가입
				} else if(url.equals("cph://users/staffs")) {
					
				//대표 승인
				} else if(url.equals("cph://home")) {
					
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("wholesale", wholesale);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		if(savedInstanceState != null) {
			wholesale = (Wholesale) savedInstanceState.getSerializable("wholesale");
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
		
		if(categories != null && categories.length > 0
				&& wholesale != null) {
			
			if(getFragmentsSize() == 0) {
				showPage(CphConstants.PAGE_WHOLESALE_MAIN, null);
			}
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
