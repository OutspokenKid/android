package com.zonecomms.golfn.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Gethering;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.BaseListFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class GetheringListPage extends BaseListFragment {

	HoloStyleEditText editText;
	private Button[] buttons;
	private PullToRefreshListView listView;

	private AsyncSearchTask currentTask;
	
	/**
	 * type
	 * -1 : 모임 검색
	 * 0 : 전체 모임
	 * 1 : 활동 모임
	 * 2 : 승인 대기
	 * 3 : 모임 생성
	 */
	
	@Override
	protected void bindViews() {

		buttons = new Button[4];
		buttons[0] = (Button) mThisView.findViewById(R.id.getheringlistPage_btnAll);
		buttons[1] = (Button) mThisView.findViewById(R.id.getheringlistPage_btnActive);
		buttons[2] = (Button) mThisView.findViewById(R.id.getheringlistPage_btnStandBy);
		buttons[3] = (Button) mThisView.findViewById(R.id.getheringlistPage_btnOpen);
		
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.getheringListPage_listView);
	}

	@Override
	protected void setVariables() {
		
		//모임 검색.
		if(type == -1) {
			addSearchBar();
		} else {
			for(int i=0; i<4; i++) {
				if(i == type) {
					buttons[i].setBackgroundColor(Color.BLACK);
				} else {
					buttons[i].setBackgroundColor(Color.rgb(55, 55, 55));
				}
				FontInfo.setFontSize(buttons[i], 28);
				FontInfo.setFontStyle(buttons[i], FontInfo.BOLD);
			}
		}
		
		zoneAdapter = new ListAdapter(mContext, models, false);
		listView.setAdapter(zoneAdapter);
		listView.getRefreshableView().setDividerHeight(0);
		listView.setBackgroundColor(Color.BLACK);
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
	}

	@Override
	protected void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListeners() {

		for(int i=0; i<4; i++) {
			
			buttons[i].setTag("" + i);
			buttons[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					int clickedIndex = Integer.parseInt(v.getTag().toString());
					
					if(clickedIndex != 3) {
						buttons[clickedIndex].setBackgroundColor(Color.BLACK);
						buttons[type].setBackgroundColor(Color.rgb(55, 55, 55));
						type = clickedIndex;
					}
					
					switch(clickedIndex) {
					case 0:
					case 1:
					case 2:
						isLastList = false;
						lastIndexno = 0;
						models.clear();
						zoneAdapter.notifyDataSetChanged();
						downloadInfo();
						break;
					case 3:
						mActivity.showGetheringOpenPage();
						break;
					}
				}
			});
		}
	}

	@Override
	protected void setSizes() {

		int l1 = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);
		
		RelativeLayout.LayoutParams rp;
		
		for(int i=0; i<4; i++) {
			rp = (RelativeLayout.LayoutParams) buttons[i].getLayoutParams();
			rp.width = l1;
			rp.height = l1;
			rp.leftMargin = s;
			
			if(i==0) {
				rp.topMargin = s;
			}
			
			FontInfo.setFontSize(buttons[i], 30);
		}
	}

	@Override
	protected void downloadInfo() {
		
		if(isLastList || isDownloading) {
			return;
		}
		
		super.downloadInfo();
		
		//모임 검색.
		if(type == -1) {
			
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
			
			url = ZoneConstants.BASE_URL + "sb/search/list" +
					"?last_sb_nid=" + lastIndexno +
					"&image_size=150" +
					"&origin_sb_id=" + ZoneConstants.PAPP_ID +
					"&keyword=" + keyword +
					"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID|AppInfoUtils.WITHOUT_SB_ID);
		} else{
			/*
			활동 모임
			http://112.169.61.103/externalapi/public/
			sb/board_list
			?image_size=187
			&last_sb_nid=0
			&status=1
			&member_id=aaaaa
			
			승인 대기
			http://112.169.61.103/externalapi/public
			/sb/board_list
			?image_size=187
			&last_sb_nid=0
			&status=0
			&member_id=aaaaa

			*/
			url = ZoneConstants.BASE_URL + "sb/board_list" +
					"?image_size=150" +
					"&last_sb_nid=" + lastIndexno +
					"&origin_sb_id=" + ZoneConstants.PAPP_ID +
					"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID|AppInfoUtils.WITHOUT_SB_ID);
			
			switch(type) {
			
			case 0:
				break;
			case 1:
				url += "&status=1&member_id=" + MainActivity.myInfo.getMember_id();
				break;
			case 2:
				url += "&status=0&member_id=" + MainActivity.myInfo.getMember_id();
				break;
			}
		}
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("onError. url : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("onCompleted. url : " + url + "\nresult : "+ result);
				
				try {
					JSONObject objJSON = new JSONObject(result);
					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();

					final String memberId = MainActivity.myInfo.getMember_id();
					
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								Gethering gethering = new Gethering(arJSON.getJSONObject(i));
								gethering.setItemCode(ZoneConstants.ITEM_GETHERING);
								
								if(memberId.equals(gethering.getReg_id())) {
									gethering.setOwner(true);
								}
								
								models.add(gethering);
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

		return R.id.getheringlistPage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {

		return false;
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
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(hidden) {
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			if(!hidden) {
				mActivity.getTitleBar().showHomeButton();
				mActivity.getTitleBar().hideWriteButton();
			}
		}
	}

	@Override
	protected String generateDownloadKey() {
		return "GETHERINGLISTPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_getheringlist;
	}
	
	public void addSearchBar() {
		
		editText = (HoloStyleEditText) mThisView.findViewById(R.id.getheringlistPage_editText);
		int p = ResizeUtils.getSpecificLength(8);
		RelativeLayout.LayoutParams rp;
		rp = (RelativeLayout.LayoutParams) editText.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = p;
		editText.getEditText().setSingleLine();
		FontInfo.setFontSize(editText.getEditText(), 20);
		editText.getEditText().addTextChangedListener(new TextWatcher() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(StringUtils.isEmpty(editText.getEditText().getText())) {
					
					if(editText.getVisibility() == View.VISIBLE) {
						isLastList = false;
						isRefreshing = true;
						lastIndexno = 0;
						models.clear();
						zoneAdapter.notifyDataSetChanged();
						((ListAdapter)zoneAdapter).clearHardCache();
					}
				} else {
					AsyncSearchTask ast = new AsyncSearchTask();
					
					if(currentTask != null) {
						currentTask.cancel(true);
						currentTask = null;
					}
					
					currentTask = ast;
					
					if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
						ast.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						ast.execute();
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		rp = (RelativeLayout.LayoutParams) buttons[0].getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(90);
		
		for(int i=0; i<4; i++) {
			buttons[i].setVisibility(View.GONE);
		}

		editText.setVisibility(View.VISIBLE);
		editText.getEditText().requestFocus();
		editText.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			}
		}, 500);
	}
	
////////////////////////Classes.
	
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
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			onRefreshPage();
		}
	}
}
