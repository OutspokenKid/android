package com.zonecomms.clubcage.classes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.fragments.MainPage;

public abstract class RecyclingActivity extends Activity {

	protected String downloadKey;
	protected View loadingView;
	
	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();
	protected abstract void setSizes();
	protected abstract void setListeners();
	protected abstract void downloadInfo();
	protected abstract void setPage();
	protected abstract int getContentViewId();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		FontUtils.setGlobalFont(this, layoutResID, getString(R.string.customFont));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void finish() {
		
		super.finish();
		
		if(ZonecommsApplication.getFragmentsSize() != 0
				&& ZonecommsApplication.getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
		} else {
			overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
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
