package com.zonecomms.festivalwdjf.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.BaseListFragment;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

public class ListPage extends BaseListFragment {

	private PullToRefreshListView listView;
	
	@Override
	protected void bindViews() {
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.listPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {
	}

	@Override
	protected void createPage() {

		ListAdapter listAdapter = new ListAdapter(mContext, models, false);
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.BLACK);
		listView.getRefreshableView().setDividerHeight(0);
		listView.getRefreshableView().setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				onRefreshPage();
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		zoneAdapter = listAdapter;
	}

	@Override
	protected void setListeners() {
	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void downloadInfo() {
		
		if(isDownloading || isLastList) {
			return;
		}
		
		super.downloadInfo();
		
		url = ZoneConstants.BASE_URL;

		switch (type) {
		
		case ZoneConstants.TYPE_NOTICE:
			url += "notice/list"
					+ "?notice_type=1"
					+ "&image_size=640"
					+ "&last_notice_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_EVENT:
			url += "notice/list"
					+ "?notice_type=2"
					+ "&image_size=640"
					+ "&last_notice_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_MUSIC:
			url += "link/list"
					+ "?link_type=3"
					+ "&image_size=150"
					+ "&last_link_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_VIDEO:
			url += "link/list"
					+ "?link_type=2"
					+ "&image_size=640"
					+ "&last_link_nid=" + lastIndexno;
			break;
		}

		url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);
		
		if(!StringUtils.isEmpty(url)) {
			super.downloadInfo();
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {

					LogUtils.log("ListPage.  url : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {

					LogUtils.log("ListPage.  url : " + url + "\nresult : " + result);
					
					try {
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									switch (type) {
									
									case ZoneConstants.TYPE_NOTICE:
									case ZoneConstants.TYPE_EVENT:
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_NOTICE);
										models.add(notice);
										break;
										
									case ZoneConstants.TYPE_MUSIC:
										Link link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_MUSIC);
										models.add(link);
										break;
										
									case ZoneConstants.TYPE_VIDEO:
										link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_VIDEO);
										models.add(link);
										break;
									}
								} catch(Exception e) {
								}
							}
						} else {
							isLastList = true;
							ToastUtils.showToast(R.string.lastPage);
						}

						setPage(true);
					} catch(Exception e) {
						setPage(false);
					}
				}
			};
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		}
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(isRefreshing && listView != null) {
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					listView.onRefreshComplete();					
				}
			}, 500);
		}
		
		super.setPage(successDownload);
		
		if(models != null && models.size() < MAX_LOADING_COUNT) {
			isLastList = true;
		}
	}

	@Override
	public void onRefreshPage() {

		super.onRefreshPage();
		downloadInfo();
	}
	
	@Override
	protected int getContentViewId() {

		return R.id.listPage_pullToRefreshView;
	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().downloadBanner();
			}
		}
	}

	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String generateDownloadKey() {
		
		return "LISTPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {

		return R.layout.page_list;
	}
}
