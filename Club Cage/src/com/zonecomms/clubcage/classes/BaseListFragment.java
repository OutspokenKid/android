package com.zonecomms.clubcage.classes;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.common.adapters.GridAdapter;

public abstract class BaseListFragment extends BaseFragment {

	protected int lastIndexno;
	protected boolean isLastList;
	protected String url;
	
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected BaseAdapter targetAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindViews();
		setVariables();
		createPage();
		
		setListeners();
		setSizes();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(!isDownloading && !isRefreshing && models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		super.setPage(successDownload);

		if(successDownload) {

			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
			}
			
			if(targetAdapter != null) {
				targetAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}
	}
	
	@Override
	protected String getTitleText() {
		
		if(title == null) {
			title = getString(R.string.app_name); 
		}
		
		return title;
	}
	
	@Override
	public void onRefreshPage() {

		if(isRefreshing) {
			return;
		}

		isLastList = false;
		isRefreshing = true;
		lastIndexno = 0;
		models.clear();
		targetAdapter.notifyDataSetChanged();
		
		if(targetAdapter instanceof GridAdapter) {
			((GridAdapter)targetAdapter).clearHardCache();
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(!hidden && !isDownloading && !isRefreshing && models.size() == 0) {
			downloadInfo();
		}
	}
}
