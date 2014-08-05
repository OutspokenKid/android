package com.zonecomms.clubcage.classes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;

public abstract class BaseFragment extends Fragment {

	private static int madeCount;
	
	protected View mThisView;
	protected Context mContext;
	protected MainActivity mActivity;

	protected String fragmentTag;
	protected String title;
	protected boolean isDownloading;
	protected boolean isRefreshing;
	protected boolean disableExitAnim;
	protected boolean isLast;

	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();

	protected abstract void setListeners();
	protected abstract void setSizes();

	protected abstract String getTitleText();
	protected abstract int getContentViewId();
	protected abstract int getLayoutResId();

	public abstract void onRefreshPage();
	public abstract boolean onBackKeyPressed();
	public abstract void onSoftKeyboardShown();
	public abstract void onSoftKeyboardHidden();
	
	protected ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

	public BaseFragment() {
		
		madeCount ++;
		fragmentTag = "page" + madeCount;
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
		mActivity = (MainActivity) getActivity();
		
		mThisView = inflater.inflate(getLayoutResId(), null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		LogUtils.log("###BaseFragment.onActivityCreated.  ");
		
		if(getArguments() != null) {
			title = getArguments().getString("title");
		}
		
		bindViews();
		createPage();
		
		setListeners();
		setSizes();
	}

	@Override
	public void onStart() {
		super.onStart();
		
		LogUtils.log("###BaseFragment.onStart.  ");
		
		FontUtils.setGlobalFont(getActivity(), mThisView, getString(R.string.customFont));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		LogUtils.log("###BaseFragment.onSaveInstanceState.  ");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		LogUtils.log("###BaseFragment.onResume.  ");
		checkTitleText();
		
		SoftKeyboardUtils.hideKeyboard(mContext, mThisView);
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
		
		try {
			for(int i=0; i<imageViews.size(); i++) {
				
				if(imageViews.get(i) != null) {
					ViewUnbindHelper.unbindReferences(imageViews.get(i));
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
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
			LogUtils.trace(e);
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
			LogUtils.trace(e);
		}
	}
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		
		if(disableExitAnim) {
			
			if(isLast && !enter) {
				return AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_bottom);
				
			} else {
				disableExitAnim = false;
				Animation a = new Animation() {};
				a.setDuration(0);
				return a;
			}
		}
		
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	
	public void disableExitAnim(boolean isLast) {
		
		this.isLast = isLast;
		disableExitAnim = true;
	}

	public String getFragmentTag() {
		
		return fragmentTag;
	}
}
