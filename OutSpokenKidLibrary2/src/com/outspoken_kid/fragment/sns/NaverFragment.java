package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class NaverFragment extends SNSFragment {

	private static String OAUTH_CLIENT_ID = "R9zCvAtCmbj5yhXjmxc9";
	private static String OAUTH_CLIENT_SECRET = "iw_Hf14ujB";
	private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
	private static String OAUTH_CALLBACK_URL = "http://static.nid.naver.com/oauth/naverOAuthExp.nhn";
	
	private OAuthLogin mOAuthLoginInstance;
	private OAuthLoginButton mOAuthLoginButton;
	private NaverUserInfo naverUserInfo;
	
	/** 
	* startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다. 
	*/ 
	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() { 
	    @Override
	    public void run(boolean success) { 
	        if (success) { 
	        	new RequestApiTask().execute();
	        	
	         } else { 
	            String errorCode = mOAuthLoginInstance.getLastErrorCode(getActivity()).getCode(); 
	            String errorDesc = mOAuthLoginInstance.getLastErrorDesc(getActivity()); 

	            String toastMsg = "errorCode:" + errorCode + ", errorDesc:" + errorDesc;
	            ToastUtils.showToast(toastMsg);
	        } 
	    }; 
	}; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mOAuthLoginInstance = OAuthLogin.getInstance();
		mOAuthLoginInstance.init(getActivity(), OAUTH_CLIENT_ID, 
				OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_CALLBACK_URL);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getCoverImage().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(isLoggedIn()) {
					if(onAfterSignInListener != null) {
						onAfterSignInListener.OnAfterSignIn(naverUserInfo);
					}
				} else {
					mOAuthLoginInstance.startOauthLoginActivity(getActivity(), mOAuthLoginHandler);
				}
			}
		});
	}
	
	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Button getLoginButton(Activity activity) {

		if(mOAuthLoginButton == null) {
			mOAuthLoginButton = new OAuthLoginButton(getActivity());
			mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);	
			mOAuthLoginButton.setBgType("green", "icon");
		}
		
		return mOAuthLoginButton;
	}

	@Override
	protected void logout() {

		new DeleteTokenTask().execute();
	}

//////////////////// Custom classes.

	public class NaverUserInfo extends SNSUserInfo {

		public String content; 
		
		public NaverUserInfo(String content) {
			this.content = content;
		}
		
		@Override
		public void printSNSUserInfo() {
			
			LogUtils.log("###NaverFragment.printSNSUserInfo.  " +
					"######################################" +
					"\ncontent : " + content +
					"\n######################################");
		}
	}
	
	private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// 서버에 삭제 요청한 결과를 알고 싶다면 res.getResultValue() 로 확인한다.
			mOAuthLoginInstance.logoutAndDeleteToken(getActivity());
			return null;
		}
	}
	
	private class RequestApiTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
//			mApiResultText.setText((String) "");
		}
		@Override
		protected String doInBackground(Void... params) {
			String url = "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml";
			String at = mOAuthLoginInstance.getAccessToken(getActivity());
			return mOAuthLoginInstance.requestApi(getActivity(), at, url);
		}
		protected void onPostExecute(String content) {

			naverUserInfo = new NaverUserInfo(content);
			
			if(onAfterSignInListener != null) {
				onAfterSignInListener.OnAfterSignIn(naverUserInfo);
			}
		}
	}
}
