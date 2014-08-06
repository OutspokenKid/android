package com.outspoken_kid.classes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.interfaces.OutspokenFragmentInterface;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;

public abstract class BaseFragment extends Fragment 
		implements OutspokenFragmentInterface {
	
	protected static int madeCount;

	protected View mThisView;
	protected Context mContext;
	protected BaseFragmentActivity mActivity;
	
	protected String fragmentTag;
	protected boolean isDownloading;
	protected boolean isRefreshing;
	protected boolean disableExitAnim;
	protected boolean isLastFragment;
	
	public abstract boolean onMenuPressed();
	public abstract boolean onBackPressed();
	
	public BaseFragment() {
		
		fragmentTag = "page" + ++madeCount;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		LogUtils.log("###BaseFragment.onAttach.");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LogUtils.log("###BaseFragment.onCreate.  ");
		setVariables();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LogUtils.log("###BaseFragment.onCreateView.  ");
		
		if(container == null) {
			return null;
		}
	
		mContext = getActivity();
		mActivity = (BaseFragmentActivity) getActivity();
		
		mThisView = inflater.inflate(getContentViewId(), null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		LogUtils.log("###BaseFragment.onActivityCreated.  ");
		
		bindViews();
		createPage();
		
		setListeners();
		setSizes();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		LogUtils.log("###BaseFragment.onStart.  ");
		
		if(getCustomFontResId() != 0) {
			FontUtils.setGlobalFont(getActivity(), mThisView, getString(getCustomFontResId()));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		LogUtils.log("###BaseFragment.onResume.  ");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		LogUtils.log("###BaseFragment.onPause.  ");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		LogUtils.log("###BaseFragment.onStop.  ");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		LogUtils.log("###BaseFragment.onDestroyView.  ");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		LogUtils.log("###BaseFragment.onDestroy.  ");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		LogUtils.log("###BaseFragment.onDetach.  ");
	}
	
	@Override
	public void downloadInfo() {

		if(!isRefreshing) {
			mActivity.showLoadingView();
		}
		
		isDownloading = true;
	}
	
	@Override
	public void setPage(boolean successDownload) {

		mActivity.hideLoadingView();
		isRefreshing = false;
		isDownloading = false;
	}
	
	@Override
	public void refreshPage() {

		if(isRefreshing) {
			return;
		}
		
		isRefreshing = true;
		isDownloading = false;
		
		downloadInfo();
	}
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

		if(disableExitAnim) {
			
			if(isLastFragment && !enter) {
				return AnimationUtils.loadAnimation(mContext, getLastPageAnimResId());
				
			} else {
				disableExitAnim = false;
				Animation a = new Animation() {};
				a.setDuration(0);
				return a;
			}
		}
		
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	
	@Override
	public void disableExitAnim(boolean isLastPage) {

		isLastFragment = isLastPage;
		disableExitAnim = true;
	}
	
	@Override
	public String getFragmentTag() {
		
		return fragmentTag;
	}
}
