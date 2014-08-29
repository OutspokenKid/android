package com.outspoken_kid.classes;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubmania.R;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.BaseModel;

public abstract class BaseListFragment extends BaseFragment {

	protected int lastIndexno;
	protected boolean isLastList;
	protected String url;
	protected int type;
	
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected BaseAdapter targetAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		type = getArguments().getInt("type");
		
		bindViews();
		setVariables();
		createPage();
		
		setListener();
		setSize();
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
