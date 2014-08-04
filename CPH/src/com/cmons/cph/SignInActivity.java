package com.cmons.cph;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.cmons.classes.BaseFragmentActivity;
import com.cmons.classes.CphConstants;
import com.cmons.cph.fragments.FindIdPwPage;
import com.cmons.cph.fragments.SignInPage;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignInActivity extends BaseFragmentActivity {
	
	private boolean signingIn;
	
	@Override
	protected void bindViews() {

	}

	@Override
	protected void setVariables() {

	}

	@Override
	protected void createPage() {

	}

	@Override
	protected void setSizes() {

	}

	@Override
	protected void setListeners() {

	}

	@Override
	protected void downloadInfo() {

		setPage(true);
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showSignInPage();
		}
	}

	@Override
	protected int getXmlResId() {
		
		return R.layout.activity_sign_in;
	}
	
	@Override
	protected int getFragmentFrameId() {

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
	
	public void signIn(String id, String pw) {
		
		if(!signingIn) {
			ToastUtils.showToast(R.string.signingIn);
			signingIn = true;
		}

		try {
			String url = CphConstants.BASE_API_URL + "/users/login" +
					"?user[id]=" + id +
					"&user[pw]=" + pw;
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					signingIn = false;
					
					LogUtils.log("from.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					signingIn = false;
					
					try {
						LogUtils.log("from.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						//Create user object.
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			signingIn = false;
		} catch (Error e) {
			LogUtils.trace(e);
			signingIn = false;
		}
	}
	
	public void launchMainActivity() {
		
//		Intent intent = new Intent(this, WholesaleActivity.class);
//		startActivity(intent);
	}
}
