package com.outspoken_kid.fragment.sns;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.LogUtils;

public abstract class FacebookFragment extends SNSFragment {

	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;
	private FBUserInfo fbUserInfo;
	private boolean loggedIn;
	private boolean wasClicked;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	  
		@Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getCoverImage().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {

				LogUtils.log("###FacebookFragment.onClick.  isLoggedIn : " + isLoggedIn());
				
				if(isLoggedIn()) {
					
					if(onAfterSignInListener != null) {
						onAfterSignInListener.OnAfterSignIn(fbUserInfo);
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
		
		Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
		
		uiHelper.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.log("###FacebookFragment.onActivityResult.  " +
				"\nrequestCode : " + requestCode + 
				"\nresultCode : " + resultCode +
				"\ndata : " + data);
		
		/*
		취소한 경우.
08-25 12:57:36.590: I/notice(3711): ###FacebookFragment.onActivityResult.  
08-25 12:57:36.590: I/notice(3711): requestCode : 64206
08-25 12:57:36.590: I/notice(3711): resultCode : 0
08-25 12:57:36.590: I/notice(3711): data : Intent { (has extras) }

		성공한 경우.
08-25 13:00:14.958: I/notice(3711): ###FacebookFragment.onActivityResult.  
08-25 13:00:14.958: I/notice(3711): requestCode : 64206
08-25 13:00:14.958: I/notice(3711): resultCode : -1
08-25 13:00:14.958: I/notice(3711): data : Intent { (has extras) }
		*/

		uiHelper.onActivityResult(requestCode, resultCode, data);
		
		//로그인 성공.
		if(resultCode == Activity.RESULT_OK
				&& requestCode == 64206) {
			
//			if(onAfterSignInListener != null) {
//				onAfterSignInListener.OnAfterSignIn(fbUserInfo);
//			}
		}
	}
	
	@Override
	public boolean isLoggedIn() {

		return loggedIn;
	}

	@Override
	protected Button getLoginButton(Activity activity) {

		if(loginButton == null) {
			loginButton = new LoginButton(getActivity());
			loginButton.setFragment(this);
			loginButton.setReadPermissions(Arrays.asList(
	                new String[] { "email"}));
		}
		
		return loginButton;
	}

	@Override
	public void logout() {

		if(isLoggedIn()) {
			fbUserInfo = null;
			
			Session session = Session.getActiveSession();
		    if (session != null) {

		        if (!session.isClosed()) {
		            session.closeAndClearTokenInformation();
		            //clear your preferences if saved
		        }
		    } else {
		        session = new Session(getActivity());
		        Session.setActiveSession(session);
		        session.closeAndClearTokenInformation();
		            //clear your preferences if saved
		    }
		}
	}
	
//////////////////// Custom methods.
	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		
		if (state.isOpened()) {
			LogUtils.log("###FacebookFragment.onSessionStateChange.  세션 열린 상태.");
			
			loggedIn = true;
			Request request =  Request.newMeRequest(session, new GraphUserCallback() {       

				@Override
				public void onCompleted(GraphUser user, Response response) {

					if(user != null) {
						fbUserInfo = new FBUserInfo(user);
						loggedIn = true;
						
						if(wasClicked) {
							wasClicked = false;
							
							if(onAfterSignInListener != null) {
								onAfterSignInListener.OnAfterSignIn(fbUserInfo);
							}
						}
						
					} else {
						loggedIn = false;
					}
				}
            });
			Request.executeBatchAsync(request);
		} else if (state.isClosed()) {
			LogUtils.log("###FacebookFragment.onSessionStateChange.  세션 닫힌 상태.");
			loggedIn = false;
		} else {
			LogUtils.log("###FacebookFragment.onSessionStateChange.  모르는 상태.");
		}
	}

	public FBUserInfo getUserInfo() {
		
		return fbUserInfo;
	}
	
////////////////////Custom classes.
	
	public class FBUserInfo extends SNSUserInfo {
		
		public String id;
		public String userName;
		public String firstName;
		public String lastName;
		public String birthDay;
		public String linkUrl;
		
		public String email;
		public String profileImage;
		
		public FBUserInfo(GraphUser user) {
			
			id = user.getId();
			userName = user.getUsername();
			firstName = user.getFirstName();
			lastName = user.getLastName();
			birthDay = user.getBirthday();
			linkUrl = user.getLink();

			if(userName == null) {
				userName = firstName + lastName;
			}
			
			if(user.getProperty("email") != null) {
				//email.
				email = user.getProperty("email").toString();
			}
			
			//profileImage.
			profileImage = "http://graph.facebook.com/" + id + "/picture?type=large";
		}
		
		@Override
		public void printSNSUserInfo() {

			LogUtils.log("###FacebookFragment.printSNSUserInfo.  " +
					"######################################" +
					"\nid : " + id +
					"\nuserName : " + userName +
					"\nfirstName : " + firstName +
					"\nlastName : " + lastName +
					"\nbirthDay : " + birthDay +
					"\nlinkUrl : " + linkUrl +
					"\nprofileImage : " + profileImage +
					"\nemail : " + email +
					"\n######################################");
		}
	}
}
