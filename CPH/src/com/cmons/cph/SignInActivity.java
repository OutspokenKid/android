package com.cmons.cph;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.signin.FindIdPwPage;
import com.cmons.cph.fragments.signin.SignInPage;
import com.cmons.cph.models.User;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
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
	public void onBackPressed() {
		
		if(getFragmentsSize() > 1){
			closeTopPage();
		} else {
			super.onBackPressed();
		}
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

							saveCookies();
							User user = new User(objJSON.getJSONObject("user"));
							
							//Check user type.
							if(user.getRole() < 200) {
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
