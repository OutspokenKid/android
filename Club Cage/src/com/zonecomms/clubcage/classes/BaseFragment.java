package com.zonecomms.clubcage.classes;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;

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
	protected int boardIndex;		// 1:왁자지껄, 2:생생후기, 3:함께가기, 4:공개수배
	protected boolean isDownloading;
	protected boolean isRefreshing;

	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();

	protected abstract void setListeners();
	protected abstract void setSizes();

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
	}

	@Override
	public void onResume() {
		
		super.onResume();
		
		if(this == ZonecommsApplication.getTopFragment()) {
			ZonecommsApplication.getTopFragment().onHiddenChanged(false);
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		mActivity.hideLoadingView();
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
	
	public int getBoardIndex() {
		
		return boardIndex;
	}
}
