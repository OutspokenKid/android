package com.zonecomms.golfn.classes;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.adapters.ZoneAdapter;
import com.zonecomms.golfn.R;

public abstract class BaseListFragment extends BaseFragment {

	protected final int MAX_LOADING_COUNT = 12;
	
	protected int lastIndexno;
	protected boolean isLastList;
	protected String url;
	protected int type;
	
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected ZoneAdapter zoneAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.log("BaseListFragment.onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		super.setPage(successDownload);

		if(successDownload) {
			
			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
				LogUtils.log("###BaseListFragment.setPage.  indexno : " + lastIndexno);
			}
			
			if(zoneAdapter != null) {
				zoneAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(getString(R.string.failToLoadList));
		}
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
		
		if(zoneAdapter != null) {
			zoneAdapter.notifyDataSetChanged();
			zoneAdapter.clearHardCache();
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
