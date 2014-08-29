package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.outspoken_kid.model.SNSUserInfo;

public abstract class SNSFragment extends Fragment {
	
	protected View loginButton;
	protected View coverImage;

	protected OnAfterSignInListener onAfterSignInListener;
	
	public abstract boolean isLoggedIn();
	protected abstract int getLoginImageResId();
	protected abstract View getLoginButton(Activity activity);
	public abstract void logout();
	
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
		return frame;
	}

	public View getCoverImage() {
		
		return coverImage;
	}

	public void setOnAfterSignInListener(OnAfterSignInListener onAfterSignInListener) {
		
		this.onAfterSignInListener = onAfterSignInListener;
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterSignInListener {
		
		public void OnAfterSignIn(SNSUserInfo userInfo);
	}
}
