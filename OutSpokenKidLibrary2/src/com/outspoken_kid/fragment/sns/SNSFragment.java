package com.outspoken_kid.fragment.sns;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public abstract class SNSFragment extends Fragment {
	
	protected static final int PUBLISH_RESULT_SUCCESS = 1;
	protected static final int PUBLISH_RESULT_FAIL = 0;
	
	protected Button loginButton;
	protected Button publishButton;
	protected View coverImage;
	protected boolean forPosting;
	protected boolean needPosting;
	
	private OnAfterLoginListener onAfterLoginListener;
	protected UserInfo userInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(getArguments() != null 
				&& getArguments().containsKey("forPosting") 
				&& getArguments().getBoolean("forPosting")) {
			forPosting = true;
		} else {
			forPosting = false;
		}
		
		FrameLayout frame = new FrameLayout(getActivity());
		
		loginButton = getLoginButton(getActivity());
		frame.addView(loginButton);
		
		coverImage = new View(getActivity());
		frame.addView(coverImage);

		if(forPosting) {
			publishButton = new Button(getActivity());
			publishButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					needPosting = !needPosting;
					
					if(needPosting) {
						coverImage.setBackgroundResource(getPostableImageResId());
					} else {
						coverImage.setBackgroundResource(getUnPostableImageResId());
					}
				}
			});
			publishButton.setBackgroundColor(Color.TRANSPARENT);
			frame.addView(publishButton);
			
			if(isLoggedIn()) {
				needPosting = true;
				publishButton.setVisibility(View.VISIBLE);
				coverImage.setBackgroundResource(getPostableImageResId());
			} else {
				publishButton.setVisibility(View.INVISIBLE);
				coverImage.setBackgroundResource(getUnPostableImageResId());
			}
		} else {
			coverImage.setBackgroundResource(getLoginImageResId());
			logout();
		}
		
		return frame;
	}
	
	public void setOnAfterLoginListener(OnAfterLoginListener onAfterLoginListener) {
		
		this.onAfterLoginListener = onAfterLoginListener;
	}
	
	public abstract boolean isLoggedIn();
	public abstract int posting(String[] params);
	protected abstract int getLoginImageResId();
	protected abstract int getPostableImageResId();
	protected abstract int getUnPostableImageResId();
	protected abstract Button getLoginButton(Activity activity);
	protected abstract void logout(); 
	public boolean needPosting() {
		return needPosting;
	}

	protected void clear() {
		
		if(publishButton != null) {
			publishButton.setVisibility(View.INVISIBLE);
		}
		
		if(coverImage != null) {
			coverImage.setBackgroundResource(getUnPostableImageResId());
		}
	}
	
	protected void executeListener() {
		
		if(onAfterLoginListener != null && userInfo != null) {
			onAfterLoginListener.onAfterLogin(userInfo);
		}
		
		onAfterLoginListener = null;
	}
	
///////////////////////////////////// Classes.
	
	public abstract class UserInfo {
		
		protected String email;
		protected String location;
		protected String name;
		protected String profileUrl;
		
		public abstract String toString();
		
		public UserInfo() {
		}
		
		public String getEmail() {
			return email;
		}
		
		public String getLocation() {
			return location;
		}
		
		public String getName() {
			return name;
		}
		
		public String getProfileUrl() {
			return profileUrl;
		}
	}

///////////////////////////////////// Interfaces.
	
	public interface OnAfterLoginListener {
		
		public void onAfterLogin(UserInfo userInfo);
	}
}
