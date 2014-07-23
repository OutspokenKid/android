package com.outspoken_kid.classes;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.fragments.MainPage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

public abstract class RecyclingActivity extends Activity {

	protected String downloadKey;
	protected View loadingView;
	
	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();
	protected abstract void setSize();
	protected abstract void setListener();
	protected abstract void downloadInfo();
	protected abstract void setPage();
	protected abstract int getContentViewId();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ApplicationManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ViewUnbindHelper.unbindReferences(this, getContentViewId());
		AsyncStringDownloader.cancelCurrentDownload(downloadKey);
		BitmapDownloader.removeTasksByKey(downloadKey);
	}
	
	public void finishWithoutAnim() {

		ApplicationManager.removeActivity(this);
		super.finish();
	}
	
	@Override
	public void finish() {
		
		ApplicationManager.removeActivity(this);
		super.finish();
		
		if(ApplicationManager.getFragmentsSize() != 0
				&& ApplicationManager.getTopFragment() instanceof MainPage) {
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
