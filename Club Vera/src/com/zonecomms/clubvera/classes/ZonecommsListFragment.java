package com.zonecomms.clubvera.classes;

import java.util.ArrayList;

import android.widget.BaseAdapter;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubvera.R;
import com.zonecomms.common.adapters.GridAdapter;

public abstract class ZonecommsListFragment extends ZonecommsFragment {

	protected int lastIndexno;
	protected String url;
	
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected BaseAdapter targetAdapter;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(!isDownloading && !isRefreshing && models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void setPage(boolean successDownload) {

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
	public void refreshPage() {
		
		if(isRefreshing) {
			return;
		}
		
		super.refreshPage();
		
		lastIndexno = 0;
		models.clear();
		
		if(targetAdapter instanceof GridAdapter) {
			((GridAdapter)targetAdapter).clearHardCache();
		}
	}
}
