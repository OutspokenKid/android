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
	private static String OAUTH_CLIENT_SECRET = "3DH1ZAMHS7";

	//네이버 테스트 id, secret.
//	private static String OAUTH_CLIENT_ID = "jyvqXeaVOVmV";
//	private static String OAUTH_CLIENT_SECRET = "527300A0_COq1_XV33cf";
	
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
	    	
	    	LogUtils.log("###NaverFragment.mOAuthLoginHandler.run.  success : " + success);
	    	
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
					loginButton.performClick();
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
	public void logout() {

		new DeleteTokenTask().execute();
	}

//////////////////// Custom classes.

	public class NaverUserInfo extends SNSUserInfo {

		public String email;
		public String nickname;
		public String enc_id;
		public String profile_image;
		public String gender;
		
		/*
		 * age는 20대인 것만 나타내고, birthday는 월일만 나와서 제외함.
		 *  
		 * <data> 
		 * <result> 
		 * 		<resultcode>00</resultcode>
		 * 		<message>success</message> </result>
		 * 		<response>
		 * 			<email><![CDATA[rlagudrjs87@naver.com]]></email>
		 * 			<nickname><![CDATA[]]></nickname>
		 * 			<enc_id><![CDATA[97451fd41a1ca2ef522b4a9783c1490b84bb0601866feb3a41583cecbf0050cb]]></enc_id>
		 * 			<profile_image><![CDATA[https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif]]></profile_image>
		 * 			<age><![CDATA[20-29]]></age>
		 * 			<birthday><![CDATA[09-01]]></birthday>
		 * 			<gender>M</gender>
		 * 		</response>
		 * </data>
		 * 
		 * 
		 * <data>
		 * <result>
		 * 		<resultcode>00</resultcode>
		 * 		<message>success</message>
		 * </result>
		 * 
		 * 		<response>
		 * 			<enc_id><![CDATA[97451fd41a1ca2ef522b4a9783c1490b84bb0601866feb3a41583cecbf0050cb]]></enc_id>
		 * 		</response>
		 * </data>
		 */
		public NaverUserInfo(String xmlString) {
			
			if(xmlString.contains("email")) {
				email = xmlString.split("<email>")[1].split("</email>")[0].replace("<![CDATA[", "").replace("]]>", "");
			}
			
			if(xmlString.contains("nickname")) {
				nickname = xmlString.split("<nickname>")[1].split("</nickname>")[0].replace("<![CDATA[", "").replace("]]>", "");
			}

			if(xmlString.contains("enc_id")) {
				enc_id = xmlString.split("<enc_id>")[1].split("</enc_id>")[0].replace("<![CDATA[", "").replace("]]>", "");
			}

			if(xmlString.contains("profile_image")) {
				nickname = xmlString.split("<nickname>")[1].split("</nickname>")[0].replace("<![CDATA[", "").replace("]]>", "");
			}

			if(xmlString.contains("gender")) {
				gender = xmlString.split("<gender>")[1].split("</gender>")[0];
			}
		}
		
		@Override
		public void printSNSUserInfo() {
			
			LogUtils.log("###NaverFragment.printSNSUserInfo.  " +
					"######################################" +
					"\nemail : " + email +
					"\nnickname : " + nickname +
					"\nenc_id : " + enc_id +
					"\nprofile_image : " + profile_image +
					"\ngender : " + gender +
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
