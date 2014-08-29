package com.zonecomms.napp.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Papp;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.BaseListFragment;
import com.zonecomms.napp.classes.ZoneConstants;

public class GridPage extends BaseListFragment {

	private PullToRefreshGridView gridView;
	private HoloStyleEditText editText;

	private int numOfColumn;
	private String concern_kind;
	private int s_cate_id;
	
	private AsyncSearchTask currentTask;
	
	@Override
	protected void bindViews() {
		gridView = (PullToRefreshGridView) mThisView.findViewById(R.id.gridPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			numOfColumn = getArguments().getInt("numOfColumn");
			concern_kind = getArguments().getString("concern_kind");
			s_cate_id = getArguments().getInt("s_cate_id");
		}
	}

	@Override
	protected void createPage() {

		if(numOfColumn == 0) {
			numOfColumn = 2;
		}
		
		if(type == ZoneConstants.TYPE_GRID_MEMBER_SEARCH) {
			addSearchBar();
		}
		
		GridAdapter gridAdapter = new GridAdapter(mContext, models, false);
		gridView.setAdapter(gridAdapter);
		gridView.getRefreshableView().setNumColumns(numOfColumn);
		gridView.getRefreshableView().setPadding(0, ResizeUtils.getSpecificLength(8), 0, 0);
		gridView.getRefreshableView().setSelector(R.drawable.list_selector);
		
	    if (android.os.Build.VERSION.SDK_INT >= 9) {
	    	gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
	    }
		
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				onRefreshPage();
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {
			
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
		zoneAdapter = gridAdapter;
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
		
		url = ZoneConstants.BASE_URL;

		switch(type) {
		
		case ZoneConstants.TYPE_GRID_PAPP:
			int s_cate_id = 0;
			
			if(getArguments() != null) {
				s_cate_id = getArguments().getInt("s_cate_id");
			}

			url += "sb/category_papp_list" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
					"&s_cate_id=" + s_cate_id +
					"&image_size=" + ResizeUtils.getSpecificLength(150);
			break;
			
		case ZoneConstants.TYPE_GRID_POST:
			url += "spot/list" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
					"&concern_kind=" + concern_kind +
					"&image_size=" + ResizeUtils.getSpecificLength(308) +
					"&last_spot_nid=" + lastIndexno;
			
			break;

		case ZoneConstants.TYPE_GRID_POST_FRIENDS:
			url += "spot/latestSpotListByFriends" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&image_size=" + ResizeUtils.getSpecificLength(308) +
					"&last_spot_nid=" + lastIndexno;
			break;
		
		case ZoneConstants.TYPE_GRID_POST_CATEGORY:
			s_cate_id = 0;
			
			if(getArguments() != null) {
				s_cate_id = getArguments().getInt("s_cate_id");
			}
			
			url += "spot/list/with_category" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
					"&s_cate_id=" + s_cate_id +
					"&image_size=" + ResizeUtils.getSpecificLength(308) +
					"&last_spot_nid=" + lastIndexno;
			break;

		case ZoneConstants.TYPE_GRID_MEMBER:
			
			url += "member/friendList";
			
			if(getArguments() != null && getArguments().containsKey("userId")) {
				url += "?member_id=" + getArguments().getString("userId")
						+ "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
			} else {
				url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);
			}
			
			url += "&image_size=" + ResizeUtils.getSpecificLength(150) +
					"&last_friend_plus_nid=" + lastIndexno;
			break;
		
		case ZoneConstants.TYPE_GRID_MEMBER_SEARCH:
			String keyword = "";
			
			if(editText != null && editText.getEditText().getText() != null 
					&& !StringUtils.isEmpty(editText.getEditText().getText().toString())) {
				try {
					keyword = URLEncoder.encode(editText.getEditText().getText().toString(), "UTF-8");
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			} else {
				models.clear();
				setPage(true);
				return;
			}
			
			url += "member/search/user" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
					"&keyword=" + keyword +
					"&image_size=" + ResizeUtils.getSpecificLength(150);
			
			if(lastIndexno == 0) {
				url += "&last_member_nid="; 
			} else {
				url += "&last_member_nid=" + lastIndexno;
			}
			break;
			
		case ZoneConstants.TYPE_GRID_POST_GETHERING:
			url +=  "boardspot/list" +
					"?image_size=" + ResizeUtils.getSpecificLength(308) +
					"&last_spot_nid=" + lastIndexno +
					"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID);
			break;
			
		case ZoneConstants.TYPE_GRID_POST_GETHERING_INTRO:
			url += "spot/list" +
					"?concern_kind=050" +
					"&image_size=" + ResizeUtils.getSpecificLength(308) +
					"&last_spot_nid=" + lastIndexno;
			break;
		}
		
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
						LogUtils.log("GridPage.onCompleted.  url : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									switch(type) {
									case ZoneConstants.TYPE_GRID_PAPP:
										Papp papp = new Papp(arJSON.getJSONObject(i));
										papp.setItemCode(ZoneConstants.ITEM_PAPP);
										papp.setSelectable(true);
										papp.setCheckable(false);
										models.add(papp);
										break;
									case ZoneConstants.TYPE_GRID_POST:
									case ZoneConstants.TYPE_GRID_POST_FRIENDS:
									case ZoneConstants.TYPE_GRID_POST_CATEGORY:
									case ZoneConstants.TYPE_GRID_POST_GETHERING:
									case ZoneConstants.TYPE_GRID_POST_GETHERING_INTRO:
										Post post = new Post(arJSON.getJSONObject(i));
										post.setItemCode(ZoneConstants.ITEM_POST);
										
										if(type == ZoneConstants.TYPE_GRID_POST_GETHERING) {
											post.setFromGethering(true);
										} else if(type == ZoneConstants.TYPE_GRID_POST_GETHERING_INTRO) {
											post.setFromGethering(true);
											post.setHideOrigin(true);
										}
										
										models.add(post);
										break;
									case ZoneConstants.TYPE_GRID_MEMBER:
									case ZoneConstants.TYPE_GRID_MEMBER_SEARCH:
										Member member = new Member(arJSON.getJSONObject(i));
										member.setItemCode(ZoneConstants.ITEM_USER);
										models.add(member);
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
		
		if(isRefreshing && gridView != null) {
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					gridView.onRefreshComplete();
				}
			}, 500);
		}
		
		super.setPage(successDownload);
	}

	@Override
	public void onRefreshPage() {

		super.onRefreshPage();
		downloadInfo();
	}
	
	@Override
	protected int getContentViewId() {

		return R.id.gridPage_pullToRefreshView;
	}

	@Override
	public boolean onBackKeyPressed() {

		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			
			//타입에 따라 타이틀바 버튼 설정.
			if(type == ZoneConstants.TYPE_GRID_POST
				|| type == ZoneConstants.TYPE_GRID_POST_CATEGORY
				|| type == ZoneConstants.TYPE_GRID_POST_GETHERING_INTRO) {
				mActivity.getTitleBar().showWriteButton();
			} else {
				mActivity.getTitleBar().hideWriteButton();
			}

			mActivity.getTitleBar().showPlusAppButton();
			mActivity.getTitleBar().hideThemaButton();
			mActivity.getTitleBar().hideRegionButton();

			if(type == ZoneConstants.TYPE_GRID_MEMBER_SEARCH
					&& editText != null
					&& editText.getVisibility() != View.VISIBLE) {
				editText.setVisibility(View.VISIBLE);
				editText.requestFocus();
				editText.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
					}
				}, 500);
			}
			
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
		return "GRIDPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_grid;
	}
	
///////////////////////// Custom methods.
	
	public void addSearchBar() {
		
		int p = ResizeUtils.getSpecificLength(8);
		editText = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, editText, 1, Gravity.TOP, new int[]{p, 0, p, 0});
		editText.getEditText().setSingleLine();
		editText.setVisibility(View.INVISIBLE);
		FontInfo.setFontSize(editText.getEditText(), 26);
		editText.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(StringUtils.isEmpty(editText.getEditText().getText())) {
					
					if(editText.getVisibility() == View.VISIBLE) {
						isLastList = false;
						isRefreshing = true;
						lastIndexno = 0;
						models.clear();
						zoneAdapter.notifyDataSetChanged();
						((GridAdapter)zoneAdapter).clearHardCache();
					}
				} else {
					AsyncSearchTask ast = new AsyncSearchTask();
					
					if(currentTask != null) {
						currentTask.cancel(true);
						currentTask = null;
					}
					
					currentTask = ast;
					ast.execute();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		((LinearLayout)mThisView.findViewById(R.id.gridPage_mainLayout)).addView(editText, 0);
	}
	
	public String getConcern_kind() {
		
		return concern_kind;
	}

	public int getS_cate_id() {
		
		return s_cate_id;
	}
	
//////////////////////// Classes.
	
	public class AsyncSearchTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			
			mActivity.showLoadingView();
			ToastUtils.showToast(R.string.searching);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			onRefreshPage();
		}
	}

}