package com.outspoken_kid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;

public abstract class BaseRecyclingActivity extends Activity {

	//onCreate.
	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();
	protected abstract void setListeners();
	protected abstract void setSizes();
	
	//onResume.
	protected abstract void downloadInfo();
	
	protected abstract void setPage();
	protected abstract int getContentViewId();
	protected abstract int getCustomFontResId();
	protected abstract View getLoadingView();
	protected abstract Animation getLoadingViewAnimIn();
	protected abstract Animation getLoadingViewAnimOut();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bindViews();
		setVariables();
		createPage();
		setListeners();
		setSizes();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		downloadInfo();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		try {
			if(getCustomFontResId() != 0) {
				FontUtils.setGlobalFont(this, layoutResID, getString(getCustomFontResId()));
			}			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void showLoadingView() {
		
		if(getLoadingView() != null && getLoadingView().getVisibility() != View.VISIBLE) {
			getLoadingView().setVisibility(View.VISIBLE);
			
			if(getLoadingViewAnimIn() != null) {
				getLoadingView().startAnimation(getLoadingViewAnimIn());
			}
		}
	}
	
	public void hideLoadingView() {
		
		if(getLoadingView() != null && getLoadingView().getVisibility() == View.VISIBLE) {
			getLoadingView().setVisibility(View.INVISIBLE);
			
			if(getLoadingViewAnimOut() != null) {
				getLoadingView().startAnimation(getLoadingViewAnimOut());
			}
		}
	}
}
