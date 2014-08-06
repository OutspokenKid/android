package com.zonecomms.clubcage.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsListFragment;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.utils.AppInfoUtils;

public class ListPage extends ZonecommsListFragment {

	private static int MAX_LOADING_COUNT = 12;
	
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	
	@Override
	public void bindViews() {
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.listPage_mainLayout);
		listView = (ListView) mThisView.findViewById(R.id.listPage_listView);
	}

	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {

		swipeRefreshLayout.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
        swipeRefreshLayout.setEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				refreshPage();
			}
		});
        
		ListAdapter listAdapter = new ListAdapter(mContext, mainActivity, models, false);
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.BLACK);
		listView.setDividerHeight(0);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
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
	public void setListeners() {
		
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub
	}

	@Override
	public void downloadInfo() {
		
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
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ListPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

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
					} catch (Exception e) {
						LogUtils.trace(e);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						setPage(false);
					}
				}
			});
		}
	}

	@Override
	public void setPage(boolean successDownload) {

		if(isRefreshing && listView != null) {
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					swipeRefreshLayout.setRefreshing(false);
				}
			}, 1000);
		}
		
		super.setPage(successDownload);
		
		if(models != null && models.size() < MAX_LOADING_COUNT) {
			isLastList = true;
		}
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_list;
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().downloadBanner();
		}
	}
}
