package com.outspoken_kid.classes;

import java.util.ArrayList;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.zonecomms.clubmania.MainActivity;
import com.zonecomms.clubmania.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

public abstract class BaseFragment extends Fragment {
	
	public final int LEFT = -1;
	public final int FADE = 0;
	public final int RIGHT = 1;
	
	protected static int madeCount = 0;
	
	protected View mThisView;
	
	protected Context mContext;
	protected MainActivity mActivity;
	
	protected String title;
	protected String downloadKey;
	protected boolean isDownloading;
	protected boolean isRefreshing;
	protected int s_cate_id;

	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();

	protected abstract void setListener();
	protected abstract void setSize();

	protected abstract String getTitleText();
	protected abstract int getContentViewId();

	public abstract void onRefreshPage();
	public abstract boolean onBackKeyPressed();
	public abstract void onSoftKeyboardShown();
	public abstract void onSoftKeyboardHidden();
	
	protected ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		madeCount++;
		
		if(getArguments() != null) {
			title = getArguments().getString("title");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = (MainActivity) getActivity();
		SetupClass.setupApplication(mActivity);

		addFragmentToManager();
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
			checkTitleText();
		}
	}
	
	@Override
	public void onDetach() {

		super.onDetach();
		
		try {
			for(int i=0; i<imageViews.size(); i++) {
				
				if(imageViews.get(i) != null) {
					ViewUnbindHelper.unbindReferences(imageViews.get(i));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		super.onDestroyView();
		
		try {
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			
			if(isBeforeMain) {
				ft.setCustomAnimations(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
			} else if(needAnim) {
				ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
			
			ft.remove(this);
			ft.commitAllowingStateLoss();
			removeFragmentFromManager();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		mActivity.hideLoadingView();
		AsyncStringDownloader.cancelAllTasksByKey(downloadKey);
		BitmapDownloader.removeTasksByKey(downloadKey);
	}
	
	protected void downloadInfo() {

		if(!isRefreshing) {
			mActivity.showLoadingView();
		}
		
		isDownloading = true;
	}
	
	protected void setPage(boolean downloadSuccess) {
		
		mActivity.hideLoadingView();
		mActivity.hideCover();
		isRefreshing = false;
		isDownloading = false;
	}
	
	//Add to Manager.
	protected void addFragmentToManager() {
		ApplicationManager.getInstance().addFragment(this);
	}
	
	//Remove from manager.
	protected void removeFragmentFromManager() {
		ApplicationManager.removeFragment(this);
	}
	
	public void checkTitleText() {
		
		try {
			if(mActivity != null && mActivity.getTitleBar() != null
					&& getTitleText() != null) {
				mActivity.getTitleBar().setTitleText(getTitleText());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDownloadKey(String downloadKey) {
		
		this.downloadKey = downloadKey;
	}
	
	public String getDownloadKey() {
		return downloadKey;
	}

	public int getS_cate_id() {
		
		return s_cate_id;
	}

	public String getTitle() {
		
		return title;
	}
}
