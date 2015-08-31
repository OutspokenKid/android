package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kakao.auth.APIErrorResult;
import com.kakao.auth.Session;
import com.kakao.auth.SessionCallback;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.LogoutResponseCallback;
import com.kakao.usermgmt.MeResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.LogUtils;

public abstract class KakaoFragment extends SNSFragment {
	
	private LoginButton loginButton;
	private KakaoUserInfo kakaoUserInfo;
	private boolean loggedIn;
    private final SessionCallback mySessionCallback = new MySessionStatusCallback();
    
    private Session session;
    
    private boolean wasClicked;
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	
    	loginButton = new LoginButton(getActivity());
    	Session.initialize(getActivity());
        session = Session.getCurrentSession();
        session.addCallback(mySessionCallback);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	getCoverImage().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				LogUtils.log("###KakaoFragment.onClick.  isLoggedIn : " + isLoggedIn());
				
				if(isLoggedIn()) {
					
					if(kakaoUserInfo != null) {
						
						if(onAfterSignInListener != null) {
							onAfterSignInListener.OnAfterSignIn(kakaoUserInfo);
						}
					} else {
						wasClicked = true;
						requestMe();
					}
					
				} else {
					wasClicked = true;
					loginButton.performClick();
				}
			}
		});
    }
    
    @Override
    public void onResume() {
    	super.onResume();

        if(session.isOpened()) {
        	//세션이 오픈된된 상태이면, 다음 activity로 이동한다.
			LogUtils.log("###KakaoFragment.onResume.  세션 열린 상태.");
        	onSessionOpened();
        } else {
        	LogUtils.log("###KakaoFragment.onResume.  모르는 상태.");
        }
    }
    
	@Override
	public boolean isLoggedIn() {

		return loggedIn;
	}

	@Override
	protected View getLoginButton(Activity activity) {
		
		return loginButton;
	}

	@Override
	public void logout() {

		UserManagement.requestLogout(new LogoutResponseCallback() {
	        @Override
			public void onSuccess(final long userId) {
	        	loggedIn = false;
	        }

	        @Override
			public void onFailure(final APIErrorResult apiErrorResult) {
	        }
	    });
	}

	private void requestMe() {
		
		LogUtils.log("###KakaoFragment.requestMe.  ");
		
		UserManagement.requestMe(new MeResponseCallback() {
	        @Override
			public void onSuccess(final UserProfile userProfile) {
	        	
	        	kakaoUserInfo = new KakaoUserInfo(userProfile);
	        	
	        	if(wasClicked) {
	        		wasClicked = false;
	        		
	        		if(onAfterSignInListener != null) {
						onAfterSignInListener.OnAfterSignIn(kakaoUserInfo);
					}
	        	}
	        }

	        @Override
			public void onNotSignedUp() {
	        }

	        @Override
			public void onSessionClosedFailure(final APIErrorResult errorResult) {
	        }

	        @Override
			public void onFailure(final APIErrorResult errorResult) {
	        }
	    });
	}
	
    protected void onSessionOpened(){

    	LogUtils.log("###KakaoFragment.onSessionOpened.  ");
    	
    	loggedIn = true;
    	requestMe();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	session.removeCallback(mySessionCallback);
    }
    
//////////////////// Classes.

	private class MySessionStatusCallback implements SessionCallback {
		
        @Override
        public void onSessionOpened() {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동

        	LogUtils.log("###KakaoFragment.onSessionOpened.  ");
        	
        	KakaoFragment.this.onSessionOpened();
        }

        @Override
        public void onSessionClosed(final KakaoException exception) {
        	
        	if(exception != null) {
        		LogUtils.log("###KakaoFragment.onSessionClosed.  exception : " + exception.toString());
        	} else {
        		LogUtils.log("###KakaoFragment.onSessionClosed.  ");
        	}
        	
        	loggedIn = false;
        }
        
		@Override
		public void onSessionOpening() {
			
			LogUtils.log("###KakaoFragment.onSessionOpening.  ");
			
		}
    }

    public class KakaoUserInfo extends SNSUserInfo {

    	public long id;
    	public String nickname;
    	public String profile_image;
    	public String thumbnail_image;
    	
    	public KakaoUserInfo(UserProfile userProfile) {

    		id = userProfile.getId();
    		nickname =  userProfile.getNickname();
    		profile_image = userProfile.getProfileImagePath();
    		thumbnail_image = userProfile.getThumbnailImagePath();
    	}
    	
		@Override
		public void printSNSUserInfo() {

			LogUtils.log("###KakaoFragment.printSNSUserInfo.  " +
					"######################################" +
					"\nid : " + id +
					"\nnickname : " + nickname +
					"\nprofile_image : " + profile_image +
					"\nthumbnail_image : " + thumbnail_image +
					"\n######################################");
		}
    }
}
