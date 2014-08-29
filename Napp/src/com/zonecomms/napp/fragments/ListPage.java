package com.zonecomms.napp.fragments;

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
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.models.Papp;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.BaseListFragment;
import com.zonecomms.napp.classes.ZoneConstants;

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
				
				if(type == ZoneConstants.TYPE_LIST_PAPP) {
					return;
				}
				
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
		// TODO Auto-generated method stub
	}

	@Override
	protected void downloadInfo() {

		if(isDownloading || isLastList) {
			return;
		}
		
		if(type == ZoneConstants.TYPE_LIST_PAPP && getArguments() != null) {
			url = ZoneConstants.BASE_URL + "sb/category_papp_list" +
					"?s_cate_id=" + getArguments().getInt("s_cate_id") +
					"&image_size=150";
		} else if(type == ZoneConstants.TYPE_LIST_NOTICE){
			url = ZoneConstants.BASE_URL + "notice/list" +
					"?notice_type=1" +
					"&image_size=640" +
					"&last_notice_nid=" + lastIndexno;
		} else if(type == ZoneConstants.TYPE_LIST_GUIDE) {
			url = ZoneConstants.BASE_URL + "notice/list" +
					"?notice_type=6" +
					"&image_size=640" +
					"&last_notice_nid=" + lastIndexno;
		} else if(type == ZoneConstants.TYPE_LIST_QNA) {
			url = ZoneConstants.BASE_URL + "notice/list" +
					"?notice_type=5" +
					"&image_size=640" +
					"&last_notice_nid=" + lastIndexno;
		}
		
		url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID|AppInfoUtils.WITHOUT_SB_ID);
		
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
									if(type == ZoneConstants.TYPE_LIST_NOTICE
											|| type == ZoneConstants.TYPE_LIST_QNA
											|| type == ZoneConstants.TYPE_LIST_GUIDE) {
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_NOTICE);
										models.add(notice);
									} else if(type == ZoneConstants.TYPE_LIST_PAPP) {
										Papp papp = new Papp(arJSON.getJSONObject(i));
										papp.setItemCode(ZoneConstants.ITEM_PAPP);
										papp.setSelectable(true);
										models.add(papp);
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
						LogUtils.trace(e);
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
			mActivity.getTitleBar().showPlusAppButton();
			mActivity.getTitleBar().hideThemaButton();
			mActivity.getTitleBar().hideRegionButton();
			
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
