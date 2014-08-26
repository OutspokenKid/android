package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.MeResponseCallback;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.LogUtils;

public abstract class KakaoFragment extends SNSFragment {

	private LoginButton loginButton;
	private KakaoUserInfo kakaoUserInfo;
	private boolean loggedIn;
    private final SessionCallback mySessionCallback = new MySessionStatusCallback();
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	loginButton.setLoginSessionCallback(mySessionCallback);
    	
    	getCoverImage().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(isLoggedIn()) {
					if(onAfterSignInListener != null) {
						onAfterSignInListener.OnAfterSignIn(kakaoUserInfo);
					}
					
				} else {
					loginButton.performClick();
				}
			}
		});
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	 if(Session.initializeSession(getActivity(), mySessionCallback)){
             // 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
    		 LogUtils.log("###KakaoFragment.onResume.  세션 갱신중.");
    		 
         } else if (Session.getCurrentSession().isOpened()){
             // 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
        	 onSessionOpened();
         }
    }
    
	@Override
	public boolean isLoggedIn() {

		return loggedIn;
	}

	@Override
	protected View getLoginButton(Activity activity) {

		if(loginButton == null) {
			loginButton = new LoginButton(getActivity());
	        loginButton.setLoginSessionCallback(mySessionCallback);
		}
		
		return loginButton;
	}

	@Override
	protected void logout() {

		UserManagement.requestLogout(new LogoutResponseCallback() {
	        @Override
	        protected void onSuccess(final long userId) {
	        	loggedIn = false;
	        }

	        @Override
	        protected void onFailure(final APIErrorResult apiErrorResult) {
	        }
	    });
	}

	private void requestMe() {

		UserManagement.requestMe(new MeResponseCallback() {
	        @Override
	        protected void onSuccess(final UserProfile userProfile) {

	        	kakaoUserInfo = new KakaoUserInfo(userProfile);
	        }

	        @Override
	        protected void onNotSignedUp() {
	        }

	        @Override
	        protected void onSessionClosedFailure(final APIErrorResult errorResult) {
	        }

	        @Override
	        protected void onFailure(final APIErrorResult errorResult) {
	        }
	    });
	}
	
    protected void onSessionOpened(){
    	
    	loggedIn = true;
    	requestMe();
    }
    
//////////////////// Classes.

	private class MySessionStatusCallback implements SessionCallback {
        @Override
        public void onSessionOpened() {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
            
        	KakaoFragment.this.onSessionOpened();
        	
        	if(onAfterSignInListener != null) {
				onAfterSignInListener.OnAfterSignIn(kakaoUserInfo);
			}
        }

        @Override
        public void onSessionClosed(final KakaoException exception) {

        	loggedIn = false;
        }
    }
    
    public class KakaoUserInfo extends SNSUserInfo {

    	public String nickname;
    	public String profile_image;
    	public String thumbnail_image;
    	
    	public KakaoUserInfo(UserProfile userProfile) {
    	
    		nickname=  userProfile.getNickname();
    		profile_image = userProfile.getProfileImagePath();
    		thumbnail_image = userProfile.getThumbnailImagePath();
    	}
    	
		@Override
		public void printSNSUserInfo() {

			LogUtils.log("###KakaoFragment.printSNSUserInfo.  " +
					"######################################" +
					"\nnickname : " + nickname +
					"\nprofile_image : " + profile_image +
					"\nthumbnail_image : " + thumbnail_image +
					"\n######################################");
		}
    }
}
