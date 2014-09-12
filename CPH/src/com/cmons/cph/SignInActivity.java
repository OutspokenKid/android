package com.cmons.cph;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.signin.FindIdPwPage;
import com.cmons.cph.fragments.signin.SignInPage;
import com.cmons.cph.models.User;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignInActivity extends CmonsFragmentActivity {
	
	private boolean signingIn;
	
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

	}

	@Override
	public void downloadInfo() {
		
		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {
		
		if(getFragmentsSize() == 0) {
			showSignInPage();
		}
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.activity_sign_in;
	}
	
	@Override
	public int getFragmentFrameResId() {

		return R.id.signInActivity_fragmentFrame;
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
	public void onBackPressed() {
		
		if(getFragmentsSize() > 1){
			closeTopPage();
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
	
///////////////////////// Custom methods.
	
	public void showSignInPage() {
		
		startPage(new SignInPage(), null);
	}
	
	public void showFindPage(int type) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		startPage(new FindIdPwPage(), bundle);
	}
	
	public void launchSignUpActivity() {
		
		Intent intent = new Intent(this, SignUpActivity.class);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void signIn(final String id, final String pw) {
		
		if(!signingIn) {
			ToastUtils.showToast(R.string.signingIn);
			signingIn = true;
		}

		try {
			String url = CphConstants.BASE_API_URL + "users/login" +
					"?user[id]=" + URLEncoder.encode(id, "utf-8") +
					"&user[pw]=" + URLEncoder.encode(pw, "utf-8");
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					signingIn = false;
					LogUtils.log("SignInActivity.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToSignIn);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					signingIn = false;
					
					try {
						LogUtils.log("SignInActivity.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_signIn);
							
							User user = new User(objJSON.getJSONObject("user"));

							LogUtils.log("###SignInActivity.onResume.  save Cookies =====================");
							List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
							
							int size = cookies.size();
							for(int i=0; i<size; i++) {

								String prefsName = null;
								
								if("CPH_D1".equals(cookies.get(i).getName())) {
									prefsName = CphConstants.PREFS_COOKIE_CPH_D1;
								} else if("CPH_S".equals(cookies.get(i).getName())) {
									prefsName = CphConstants.PREFS_COOKIE_CPH_S;
								} else {
									continue;
								}
								
								SharedPrefsUtils.saveCookie(prefsName, cookies.get(i));
								LogUtils.log("		key : " + cookies.get(i).getName());
							}
							
							//Check user type.
							if(user.getRole() < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
								launchWholesaleActivity(user);
							} else {
								launchRetailActivity(user);
							}
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignIn);
			signingIn = false;
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignIn);
			signingIn = false;
		}
	}
	
	public void launchWholesaleActivity(User user) {

		Intent intent = new Intent(this, WholesaleActivity.class);
		intent.putExtra("user", user);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	public void launchRetailActivity(User user) {

		Intent intent = new Intent(this, RetailActivity.class);
		intent.putExtra("user", user);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
}
