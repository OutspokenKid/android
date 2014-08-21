package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.outspoken_kid.model.SNSUserInfo;

public abstract class SNSFragment extends Fragment {
	
	protected Button loginButton;
	protected View coverImage;
	
	private OnAfterLoginListener onAfterLoginListener;
	protected SNSUserInfo snsUserInfo;

	public abstract boolean isLoggedIn();
	protected abstract int getLoginImageResId();
	protected abstract Button getLoginButton(Activity activity);
	protected abstract void logout();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		FrameLayout frame = new FrameLayout(getActivity());
		
		loginButton = getLoginButton(getActivity());
		loginButton.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frame.addView(loginButton);
		
		coverImage = new View(getActivity());
		coverImage.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		coverImage.setBackgroundResource(getLoginImageResId());
		frame.addView(coverImage);
		logout();
		
		return frame;
	}
	
	public void setOnAfterLoginListener(OnAfterLoginListener onAfterLoginListener) {
		
		this.onAfterLoginListener = onAfterLoginListener;
	}
	
	protected void executeListener() {
		
		if(onAfterLoginListener != null && snsUserInfo != null) {
			onAfterLoginListener.onAfterLogin(snsUserInfo);
		}
		
		onAfterLoginListener = null;
	}

///////////////////////////////////// Interfaces.
	
	public interface OnAfterLoginListener {
		
		public void onAfterLogin(SNSUserInfo snsUserInfo);
	}
}
