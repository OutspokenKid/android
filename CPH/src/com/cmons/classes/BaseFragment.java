package com.cmons.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.outspoken_kid.utils.SoftKeyboardUtils;

public abstract class BaseFragment extends Fragment {

	private static int madeCount;
	
	protected View mThisView;
	protected Context mContext;
	
	protected String fragmentTag;
	protected String title;
	protected boolean disableExitAnim;

	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();

	protected abstract void setListeners();
	protected abstract void setSizes();

	protected abstract int getXmlResId();

	public abstract void onRefreshPage();
	public abstract boolean onBackKeyPressed();

	public BaseFragment() {
		
		madeCount ++;
		fragmentTag = "page" + madeCount;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getActivity();
		setVariables();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}
		
		mThisView = inflater.inflate(getXmlResId(), null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindViews();
		createPage();
		
		setListeners();
		setSizes();
	}

	@Override
	public void onResume() {
		super.onResume();

		SoftKeyboardUtils.hideKeyboard(mContext, mThisView);
	}
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		
		if(disableExitAnim) {
			disableExitAnim = false;
			Animation a = new Animation() {};
			a.setDuration(0);
			return a;
		}
		
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	
	public String getTitleText() {
		return title;
	}
	
	public void disableExitAnim() {
		
		disableExitAnim = true;
	}
	
	public String getFragmentTag() {
		
		return fragmentTag;
	}
}
