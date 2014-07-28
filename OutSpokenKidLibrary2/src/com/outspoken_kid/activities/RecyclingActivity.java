package com.outspoken_kid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.ResizeUtils;

public abstract class RecyclingActivity extends Activity {

	protected String downloadKey;
	protected View loadingView;
	
	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();
	protected abstract void setListeners();
	protected abstract void setSizes();
	protected abstract void downloadInfo();
	protected abstract void setPage();
	protected abstract int getContentViewId();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ViewUnbindHelper.unbindReferences(this, getContentViewId());
	}

	public void setLoadingView(int resId) {
		
		loadingView = findViewById(resId);
		ResizeUtils.viewResize(120, 150, loadingView, 2, Gravity.CENTER, new int[]{0, 45, 0, 0});
	}
	
	public void showLoadingView() {
		
		if(loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
			loadingView.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideLoadingView() {
		
		if(loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}
}
