package com.zonecomms.clubcage.classes;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;

public abstract class ZonecommsFragment extends BaseFragment {

	protected String title;
	protected MainActivity mainActivity;
	
	protected ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

	@Override
	public int getCustomFontResId() {

		return R.string.customFont;
	}

	@Override
	public int getLastPageAnimResId() {
		return R.anim.slide_out_to_bottom;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mainActivity = (MainActivity) getActivity();
	}
	
	@Override
	public void setVariables() {

		if(getArguments() != null) {
			title = getArguments().getString("title");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		checkTitleText();
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
		
		mainActivity.hideLoadingView();
	}
	
	public void checkTitleText() {
		
		try {
			mainActivity.getTitleBar().setTitleText(getTitleText());
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public String getTitleText() {
		
		if(title == null) {
			title = getString(R.string.app_name);
		}
		
		return title;
	}
}
