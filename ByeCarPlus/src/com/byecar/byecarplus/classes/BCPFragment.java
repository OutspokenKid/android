package com.byecar.byecarplus.classes;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class BCPFragment extends BaseFragment {
	
	public static final int NUMBER_OF_LISTITEMS = 10;

	protected BCPFragmentActivity mActivity;
	
	protected TitleBar titleBar;
	protected String title;

	protected int pageIndex = 1;
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected BCPAdapter adapter;
	protected String url;

	public abstract int getBackButtonResId();
	public abstract int getBackButtonWidth();
	public abstract int getBackButtonHeight();
	
	/**
	 * BaseModel로 파싱해서 models에 넣고, 조건에 따라 isLastList 설정.
	 * 
	 * @param objJSON Result of downloading.
	 * @return isLastList.
	 */
	public abstract boolean parseJSON(JSONObject objJSON);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = (BCPFragmentActivity) getActivity();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(titleBar != null) {
			
			if(titleBar.getLayoutParams() != null) {
				titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(88);
			}
			
			if(getBackButtonResId() != 0) {
				titleBar.getBackButton().setBackgroundResource(getBackButtonResId());
				titleBar.getBackButton().getLayoutParams().width = ResizeUtils.getSpecificLength(getBackButtonWidth());
				titleBar.getBackButton().getLayoutParams().height = ResizeUtils.getSpecificLength(getBackButtonHeight());
				((RelativeLayout.LayoutParams)titleBar.getBackButton().getLayoutParams()).
						leftMargin = ResizeUtils.getSpecificLength(8);
				titleBar.getBackButton().setVisibility(View.VISIBLE);
				
				titleBar.getBackButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						getActivity().getSupportFragmentManager().popBackStack();
					}
				});
			} else {
				titleBar.getBackButton().setVisibility(View.INVISIBLE);
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SoftKeyboardUtils.hideKeyboard(mContext, mThisView);
	}
	
	@Override
	public void downloadInfo() {
		
		try {
			if(isDownloading || isLastList) {
				return;
			}
			
			if(!isRefreshing) {
				showLoadingView();
			}
			
			isDownloading = true;
			
			if(url.contains("?")) {
				url += "&";
			} else {
				url += "?";
			}

			url += "page=" + pageIndex;
			
			if(!url.contains("num=")) {
				url += "&num=" + NUMBER_OF_LISTITEMS;
			}
			
			final String lastUrl = url;
			DownloadUtils.downloadJSONString(lastUrl, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("BCPFragment.onError." + "\nurl : " + url);
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						if(!url.equals(lastUrl)) {
							return;
						}
						
						LogUtils.log("BCPFragment.downloadInfo.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						isLastList = parseJSON(objJSON);
						
						if(isLastList && pageIndex > 2) {
							ToastUtils.showToast(R.string.lastList);
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
		} catch (Exception e) {
			LogUtils.trace(e);
			setPage(false);
		} catch (Error e) {
			LogUtils.trace(e);
			setPage(false);
		}
	}
	
	@Override
	public void setPage(boolean successDownload) {

		isRefreshing = false;
		isDownloading = false;
		
		try {
			hideLoadingView();
			
			if(successDownload && adapter != null) {
				adapter.notifyDataSetChanged();
				pageIndex++;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void refreshPage() {

		if(isRefreshing) {
			return;
		}

		isRefreshing = true;
		isDownloading = false;
		isLastList = false;
		pageIndex = 1;
		
		try {
			models.clear();
			
			if(adapter != null) {
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		downloadInfo();
	}
	
	public void refreshPageWithoutClearItem() {
		
		LogUtils.log("###BCPFragment.refreshPageWithoutClearItem.  ");
		
		if(isRefreshing) {
			return;
		}
		
		isRefreshing = true;
		isDownloading = false;
		isLastList = false;
		pageIndex = 1;
		
		try {
			models.clear();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		downloadInfo();
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastPageAnimResId() {

		return R.anim.abc_fade_in;
	}

	@Override
	public void showLoadingView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideLoadingView() {
		// TODO Auto-generated method stub

	}
}