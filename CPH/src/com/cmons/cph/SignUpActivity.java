package com.cmons.cph;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.signup.SignUpForBusinessPage;
import com.cmons.cph.fragments.signup.SignUpForCategoryPage;
import com.cmons.cph.fragments.signup.SignUpForPersonalPage;
import com.cmons.cph.fragments.signup.SignUpForPositionPage;
import com.cmons.cph.fragments.signup.SignUpForSearchPage;
import com.cmons.cph.fragments.signup.SignUpForTermsPage;
import com.cmons.cph.fragments.signup.SignUpForWritePage;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.User;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpActivity extends CmonsFragmentActivity {

	/**
	 * type
	 * 100 : 도매 - 대표.
	 * 101 : 도매 - 직원.
	 * 102 : 도매 - 디자이너.
	 * 
	 * 200 : 소매(오프라인) - 대표.
	 * 201 : 소매(오프라인) - 직원.
	 * 202 : 소매(오프라인) - MD.
	 * 
	 * 300 : 소매(온라인) - 대표.
	 * 301 : 소매(온라인) - 직원.
	 * 302 : 소매(온라인) - MD.
	 */
	
	public static final int BUSINESS_WHOLESALE = 100;
	public static final int BUSINESS_RETAIL_OFFLINE = 200;
	public static final int BUSINESS_RETAIL_ONLINE = 300;

	public static final int POSITION_OWNER = 0;
	public static final int POSITION_EMPLOYEE1 = 1;
	public static final int POSITION_EMPLOYEE2 = 2;
	
	@Override
	public void bindViews() {

	}

	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {

	}

	@Override
	public void setSizes() {

	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showTermsPage();
		}
	}

	@Override
	public int getContentViewId() {

		return R.layout.activity_sign_up;
	}

	@Override
	public int getFragmentFrameResId() {

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

			if(!getTopFragment().onBackPressed()) {
				closeTopPage();
			}
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
	
	//도매 대표, 직원, 디자이너, 소매 직원, MD.
	public void showPersonalPage(int type, Shop shop, String categoryString) {
			
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			bundle.putSerializable("shop", shop);
			bundle.putString("categoryString", categoryString);
			
			startPage(new SignUpForPersonalPage(), bundle);
		}
	
	//소매 대표.
	public void showPersonalPage(int type, String userName, String retailName, 
			String mallUrl, String address, String regNumber) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putString("userName", userName);
		bundle.putString("retailName", retailName);
		bundle.putString("mallUrl", mallUrl);
		bundle.putString("address", address);
		bundle.putString("regNumber", regNumber);
		
		startPage(new SignUpForPersonalPage(), bundle);
	}
	
	public void launchWholesaleActivity(User user) {
		
		Intent intent = new Intent(this, WholesaleActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}
	
	public void launchRetailActivity(User user) {
		
		Intent intent = new Intent(this, RetailActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}

	public void signUpForWholesale(String id, String pw, String role, String userName, 
			String wholesale_id, String categoryString, String phone_auth_key) {
	
		try {
			String url = CphConstants.BASE_API_URL + "users/join" +
					"?user[id]=" + URLEncoder.encode(id, "utf-8") + 
					"&user[pw]=" + URLEncoder.encode(pw, "utf-8") +
					"&user[role]=" + URLEncoder.encode(role, "utf-8") +
					"&user[name]=" + URLEncoder.encode(userName, "utf-8") +
					"&user[wholesale_id]=" + URLEncoder.encode(wholesale_id, "utf-8") +
					"&wholesale_category_id[]=" + URLEncoder.encode(categoryString, "utf-8") +
					"&phone_auth_key=" + URLEncoder.encode(phone_auth_key, "utf-8");
		
			ToastUtils.showToast(R.string.signingUp);
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpActivity.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToSignUp);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpActivity.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						if(objJSON.getInt("result") == 1) {
							User user = new User(objJSON.getJSONObject("user"));
							launchWholesaleActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSignUp);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSignUp);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		}
	}
	
	public void signUpForRetailOwner(String id, String pw, String role, String userName,
			String retailName, String address, String mallUrl, 
			String regNumber, String phone_auth_key) {
		
		try {
			String url = CphConstants.BASE_API_URL + "users/join" +
					"?user[id]=" + URLEncoder.encode(id, "utf-8") + 
					"&user[pw]=" + URLEncoder.encode(pw, "utf-8") +
					"&user[role]=" + URLEncoder.encode(role, "utf-8") +
					"&user[name]=" + URLEncoder.encode(userName, "utf-8") +
					"&retail[name]=" + URLEncoder.encode(retailName, "utf-8") +
					"&retail[address]=" + URLEncoder.encode(address, "utf-8") +
					"&retail[mall_url]=" + URLEncoder.encode(mallUrl, "utf-8") +
					"&retail[corp_reg_number]=" + URLEncoder.encode(regNumber, "utf-8") +
					"&phone_auth_key=" + URLEncoder.encode(phone_auth_key, "utf-8");
		
			ToastUtils.showToast(R.string.signingUp);
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpActivity.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToSignUp);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpActivity.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						if(objJSON.getInt("result") == 1) {
							User user = new User(objJSON.getJSONObject("user"));
							launchRetailActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSignUp);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSignUp);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		}
	}
	
	public void signUpForRetailEmployee(String id, String pw, String role, String userName,
			String retailId, String phone_auth_key) {
		
		try {
			String url = CphConstants.BASE_API_URL + "users/join" +
					"?user[id]=" + URLEncoder.encode(id, "utf-8") + 
					"&user[pw]=" + URLEncoder.encode(pw, "utf-8") +
					"&user[role]=" + URLEncoder.encode(role, "utf-8") +
					"&user[name]=" + URLEncoder.encode(userName, "utf-8") +
					"&user[retail_id]=" + URLEncoder.encode(retailId, "utf-8") +
					"&phone_auth_key=" + URLEncoder.encode(phone_auth_key, "utf-8");
		
			ToastUtils.showToast(R.string.signingUp);
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpActivity.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToSignUp);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpActivity.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						if(objJSON.getInt("result") == 1) {
							User user = new User(objJSON.getJSONObject("user"));
							launchRetailActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSignUp);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSignUp);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignUp);
		}
	}
}
