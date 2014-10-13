package com.zonecomms.festivalwdjf.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.zonecomms.festivalwdjf.MainActivity;

public abstract class BaseFragment extends Fragment {
	
	protected static int madeCount = 0;
	
	protected View mThisView;
	
	protected MainActivity mActivity;
	protected Context mContext;
	
	protected String title;
	protected String downloadKey;
	protected boolean isDownloading;
	protected boolean isRefreshing;

	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();

	protected abstract void setListeners();
	protected abstract void setSizes();

	protected abstract String generateDownloadKey();
	protected abstract int getContentViewId();
	protected abstract int getXmlResId();

	public abstract void onRefreshPage();
	public abstract boolean onBackKeyPressed();
	public abstract void onSoftKeyboardShown();
	public abstract void onSoftKeyboardHidden();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getActivity();
		mActivity = ApplicationManager.getInstance().getActivity();
		SetupClass.setupApplication(mActivity);
		ApplicationManager.getInstance().addFragment(this);
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
		
		madeCount++;
		
		if(getArguments() != null && getArguments().containsKey("title")) {
			title = getArguments().getString("title");
		}
		
		downloadKey = generateDownloadKey();
		
		bindViews();
		setVariables();
		createPage();
		
		setListeners();
		setSizes();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(this == ApplicationManager.getTopFragment()) {
			ApplicationManager.getTopFragment().onHiddenChanged(false);
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			mActivity.setTitleText(title);
		}
	}
	
	@Override
	public void onDetach() {

		super.onDetach();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(mActivity != null) {
			ViewUnbindHelper.unbindReferences(mActivity, getContentViewId());
		}
	}

	public void finish() {

		mActivity.hideLoadingView();
		AsyncStringDownloader.cancelAllTasksByKey(downloadKey);
		BitmapDownloader.removeTasksByKey(downloadKey);
		mActivity.finishFragment(this);
	}
	
	protected void downloadInfo() {

		if(!isRefreshing) {
			mActivity.showLoadingView();
		}
		
		isDownloading = true;
	}
	
	protected void setPage(boolean downloadSuccess) {
		
		mActivity.hideLoadingView();
		isRefreshing = false;
		isDownloading = false;
	}
	
	public void setDownloadKey(String downloadKey) {
		
		this.downloadKey = downloadKey;
	}
	
	public String getDownloadKey() {
		return downloadKey;
	}

	public String getTitleText() {
		return title;
	}
}
