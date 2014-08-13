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
		startActivity(intent);
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

						if(objJSON.has("message")) {
							ToastUtils.showToast(objJSON.getString("message"));
						}
						
						if(objJSON.getInt("result") == 1) {
							User user = new User(objJSON.getJSONObject("user"));
							
							SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_SIGN, "id", id);
							SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_SIGN, "pw", pw);
							
							//Check user type.
							
							launchWholesaleActivity(user);
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
		startActivity(intent);
		finish();
	}
	
	public void launchRetailActivity(User user) {

		Intent intent = new Intent(this, RetailActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}
}
