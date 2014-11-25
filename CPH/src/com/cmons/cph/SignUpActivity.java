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
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpActivity extends CmonsFragmentActivity {

	/**
	 * role.
	 * 100 : 도매 - 대표.
	 * 101 : 도매 - 직원.
	 * 102 : 도매 - 디자이너.
	 * 
	 * 210 : 소매(오프라인) - 대표.
	 * 220 : 소매(온라인) - 대표.
	 * 
	 * 201 : 소매 - 직원.
	 * 202 : 소매 - MD.
	 */
	
	public static final int BUSINESS_WHOLESALE = 0;
	public static final int BUSINESS_RETAIL_OFFLINE = 3;
	public static final int BUSINESS_RETAIL_ONLINE = 6;

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
	public void onBackPressed() {
		
		if(getFragmentsSize() > 1){

			if(!getTopFragment().onBackPressed()) {
				closeTopPage();
			}
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
	
	public void showPersonalPage(int type, Shop shop, String categoryString, String userName) {
			
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putSerializable("shop", shop);
		bundle.putString("categoryString", categoryString);
		bundle.putString("userName", userName);
		
		startPage(new SignUpForPersonalPage(), bundle);
	}
	
	public void launchWholesaleActivity(User user) {
		
		Intent intent = new Intent(this, WholesaleActivity.class);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}
	
	public void launchRetailActivity(User user) {
		
		Intent intent = new Intent(this, RetailActivity.class);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}

	public void signUpForWholesale(String id, String pw, int type, String userName, 
			String wholesale_id, String categoryString, String phone_auth_key) {
	
		try {
			if(categoryString == null) {
				categoryString = "";
			}
			
			String role = null;
			
			if(type % 3 == 0) {
				role = "100";
			} else if(type % 3 == 1) {
				role = "101";
			} else {
				role = "102";
			}
			
			final String ROLE = role;
			
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
							
							if(ROLE.equals("100")) {
								ToastUtils.showToast(R.string.complete_signUpWholesaleOwner);
							} else {
								ToastUtils.showToast(R.string.complete_signUpEmployee);
							}
							
							saveCookies();
							User user = new User(objJSON.getJSONObject("user"));
							launchWholesaleActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
						
						if(objJSON.getInt("result") == 2
								|| objJSON.getInt("result") == 3) {
							SoftKeyboardUtils.hideKeyboard(context, getTopFragment().getView());
							finish();
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
	
	public void signUpForRetailOwner(String id, String pw, int type, String userName,
			String retailName, String address, String mallUrl, String companyPhone, 
			String regNumber, String phone_auth_key) {
		
		String role = null;
		
		if(type == 3) {
			role = "220";
		} else {
			role = "210";
		}
		
		try {
			String url = CphConstants.BASE_API_URL + "users/join" +
					"?user[id]=" + URLEncoder.encode(id, "utf-8") + 
					"&user[pw]=" + URLEncoder.encode(pw, "utf-8") +
					"&user[role]=" + URLEncoder.encode(role, "utf-8") +
					"&user[name]=" + URLEncoder.encode(userName, "utf-8") +
					"&retail[name]=" + URLEncoder.encode(retailName, "utf-8") +
					"&retail[address]=" + URLEncoder.encode(address, "utf-8") +
					"&retail[mall_url]=" + URLEncoder.encode(mallUrl, "utf-8") +
					"&retail[phone_number]=" + URLEncoder.encode(companyPhone, "utf-8") +
					"&retail[corp_reg_number]=" + URLEncoder.encode(regNumber, "utf-8") +
					"&phone_auth_key=" + URLEncoder.encode(phone_auth_key, "utf-8");
		
			/*
			user[id] : 아이디 (6 ~ 64)
			user[pw] : 비밀번호 (6 ~ 64)
			user[role] : 인터넷쇼핑몰 대표(210), 오프라인 대표(220)
			user[name] : 가입자 이름
			retail[name] : 상호명
			retail[address] : 사업장 주소(오프라인)
			retail[mall_url] : 쇼핑몰 URL(인터넷 쇼핑몰)
			retail[phone_number] : 매장 전화번호
			retail[corp_reg_number] : 사업자 등록번호
			phone_auth_key : 전화번호 인증키
			*/
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
							ToastUtils.showToast(R.string.complete_signUpRetailOwner);
							saveCookies();
							User user = new User(objJSON.getJSONObject("user"));
							launchRetailActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
						
						if(objJSON.getInt("result") == 2) {
							SoftKeyboardUtils.hideKeyboard(context, getTopFragment().getView());
							finish();
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
	
	public void signUpForRetailEmployee(String id, String pw, int type, String userName,
			String retailId, String phone_auth_key) {
		
		try {
			String role = null;
					
			if(type % 3 == 1) {
				role = "201";
			} else {
				role = "202";
			}
			
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
							ToastUtils.showToast(R.string.complete_signUpEmployee);
							saveCookies();
							User user = new User(objJSON.getJSONObject("user"));
							launchRetailActivity(user);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
						
						if(objJSON.getInt("result") == 3) {
							SoftKeyboardUtils.hideKeyboard(context, getTopFragment().getView());
							finish();
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
