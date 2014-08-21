package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public abstract class FacebookFragment extends SNSFragment {

	private LoginButton loginButton;
	private UiLifecycleHelper uiHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Button getLoginButton(Activity activity) {

		if(loginButton == null) {
			loginButton = new LoginButton(getActivity());
		}
		
		return loginButton;
	}

	@Override
	protected void logout() {
		// TODO Auto-generated method stub

	}
}
