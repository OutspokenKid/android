package com.cmons.cph;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.wholesale.WholesaleForAccountPage;
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
import com.cmons.cph.fragments.wholesale.WholesaleForProductPage;
import com.cmons.cph.fragments.wholesale.WholesaleForProfileImagePage;
import com.cmons.cph.fragments.wholesale.WholesaleForSamplePage;
import com.cmons.cph.fragments.wholesale.WholesaleForSettingPage;
import com.cmons.cph.fragments.wholesale.WholesaleForShopPage;
import com.cmons.cph.fragments.wholesale.WholesaleForStaffPage;
import com.cmons.cph.fragments.wholesale.WholesaleForWritePage;
import com.cmons.cph.fragments.wholesale.WholesaleMainPage;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Notice;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.User;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;

public class WholesaleActivity extends CmonsFragmentActivity {

	private static WholesaleActivity instance;
	
	public User user;
	public Wholesale wholesale;
	public Category[] categories;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		instance = this;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	@Override
	public void bindViews() {
		
	}

	@Override
	public void setVariables() {

		if(getIntent() != null) {
			user = (User) getIntent().getSerializableExtra("user");
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
	
	@Override
	public void showLoadingView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideLoadingView() {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.
	
	public static WholesaleActivity getInstance() {
		
		return instance;
	}
	
	public void downloadCategory() {
		
		String url = CphConstants.BASE_API_URL + "categories";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleActivity.onError." + "\nurl : " + url);

				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadCategory();
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("categories");
					
					int size = arJSON.length();
					categories = new Category[size];
					for(int i=0; i<size; i++) {
						categories[i] = new Category(arJSON.getJSONObject(i));
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
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
	
	public void showWritePage(Product product) {

		Bundle bundle = null;
		
		if(product != null) {
			
			bundle = new Bundle();
			bundle.putSerializable("product", product);
		}
		
		startPage(new WholesaleForWritePage(), bundle);
	}

	public void showNoticeListPage(boolean isAppNotice) {
		
		Bundle bundle = new Bundle();
		bundle.putBoolean("isAppNotice", isAppNotice);
		
		startPage(new WholesaleForNoticeListPage(), bundle);
	}
	
	public void showNoticePage(Notice notice, boolean isEdit, boolean isAppNotice) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("notice", notice);
		bundle.putBoolean("isEdit", isEdit);
		bundle.putBoolean("isAppNotice", isAppNotice);
		
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

	public void showProfileImagePage() {
		
		startPage(new WholesaleForProfileImagePage(), null);
	}

	public void showAccountPage() {
		
		startPage(new WholesaleForAccountPage(), null);
	}

	public void showProductPage(Product product) {
		
		Bundle bundle = null;
		
		if(product != null) {
			
			bundle = new Bundle();
			bundle.putSerializable("product", product);
		}
		
		startPage(new WholesaleForProductPage(), bundle);
	}
	
	public void launchSignInActivity() {
		
		Intent intent = new Intent(this, SignInActivity.class);
		startActivity(intent);
		finish();
	}

	public void showCategorySelectPopup(final boolean needWhole,
			final OnAfterSelectCategoryListener onAfterSelectCategoryListener) {
		
		if(categories == null) {
			return;
		}
		
		int size = categories.length;
		String[] strings = null;
		
		if(needWhole) {
			strings = new String[size + 1];
			
			strings[0] = "전체";
			
			for(int i=0; i<size; i++) {
				strings[i + 1] = categories[i].getName();
			}
		} else {
			strings = new String[size];
			
			for(int i=0; i<size; i++) {
				strings[i] = categories[i].getName();
			}
		}
		
		showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(needWhole && which == 0) {
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(null, null);
					}
					return;
				}
				
				int selectedIndex = needWhole? which - 1 : which;
				
				Category selectedCategory = categories[selectedIndex];
				
				if(selectedCategory.getCategories() != null) {
					showSubCategorySelectPopup(needWhole, selectedCategory, 
							onAfterSelectCategoryListener);
				} else {
					//select completed.
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(selectedCategory, null);
					}
				}
			}
		});
	}
	
	public void showSubCategorySelectPopup(
			final boolean needWhole,
			final Category category,
			final OnAfterSelectCategoryListener onAfterSelectCategoryListener) {
		
		int size = category.getCategories().length;
		
		String[] strings = null;
		
		if(needWhole) {
			strings = new String[size + 1];
			
			strings[0] = "전체";
			
			for(int i=0; i<size; i++) {
				strings[i + 1] = category.getCategories()[i].getName();
			}
		} else {
			strings = new String[size];

			for(int i=0; i<size; i++) {
				strings[i] = category.getCategories()[i].getName();
			}
		}
		
		showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(needWhole && which == 0) {
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(category, null);
					}
					
					return;
				}
				
				int selectedIndex = needWhole? which - 1 : which;
				
				Category selectedCategory = category.getCategories()[selectedIndex];
				
				if(onAfterSelectCategoryListener != null) {
					onAfterSelectCategoryListener.onAfterSelectCategory(category, selectedCategory);
				}
			}
		});
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterSelectCategoryListener {
		
		public void onAfterSelectCategory(Category category, Category subCategory);
	}
}
