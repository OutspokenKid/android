package com.zonecomms.clubcage.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import com.outspoken_kid.utils.StringUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.BaseListFragment;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.zonecomms.common.utils.AppInfoUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.models.Notice;

public class ListPage extends BaseListFragment {

	private static int MAX_LOADING_COUNT = 12;
	
	private PullToRefreshListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}
	
		mThisView = inflater.inflate(R.layout.page_list, null);
		return mThisView;
	}
	
	@Override
	protected void bindViews() {
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.listPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {
		
		setDownloadKey("LISTPAGE" + madeCount);
	}

	@Override
	protected void createPage() {

		ListAdapter listAdapter = new ListAdapter(mContext, mActivity, models, false);
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
		targetAdapter = listAdapter;
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void setSize() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void downloadInfo() {
		
		if(isDownloading || isLastList) {
			return;
		}
		
		super.downloadInfo();
		
		url = null;

		if(title.equals("VIDEO")) {
			url = ZoneConstants.BASE_URL + "link/list"
					+ "?link_type=2"
					+ "&image_size=" + ResizeUtils.getSpecificLength(640)
					+ "&last_link_nid=" + lastIndexno;
		} else if(title.equals("NOTICE")) {
			url = ZoneConstants.BASE_URL + "notice/list"
					+ "?notice_type=1"
					+ "&image_size=" + ResizeUtils.getSpecificLength(640)
					+ "&last_notice_nid=" + lastIndexno;
		} else if(title.equals("EVENT")) {
			url = ZoneConstants.BASE_URL + "notice/list"
					+ "?notice_type=2"
					+ "&image_size=" + ResizeUtils.getSpecificLength(640)
					+ "&last_notice_nid=" + lastIndexno;
		} else if(title.equals("MUSIC")) {
			url = ZoneConstants.BASE_URL + "link/list"
					+ "?link_type=3"
					+ "&image_size=" + ResizeUtils.getSpecificLength(150)
					+ "&last_link_nid=" + lastIndexno;
		}

		url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);
		
		if(!StringUtils.isEmpty(url)) {
			super.downloadInfo();
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					try {
						LogUtils.log("ListPage.  url : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									if(title.equals("VIDEO")) {
										Link link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_VIDEO);
										models.add(link);
									} else if(title.equals("NOTICE")) {
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_NOTICE);
										models.add(notice);
									} else if(title.equals("EVENT")) {
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_NOTICE);		//공지사항과 같다.
										models.add(notice);
									} else if(title.equals("MUSIC")) {
										Link link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_MUSIC);
										models.add(link);
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
						e.printStackTrace();
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
}
