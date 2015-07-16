package com.byecar.classes;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.byecar.byecarplusfordealer.R;
import com.byecar.models.BCPBaseModel;
import com.byecar.views.TitleBar;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;

public abstract class BCPFragment extends BaseFragment {
	
	public static final int NUMBER_OF_LISTITEMS = 10;

	protected BCPFragmentActivity mActivity;
	
	protected TitleBar titleBar;
	protected String title;

	protected long last_priority = 0;
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected BCPAdapter adapter;
	protected String url;
	protected String lastUrl;
	protected boolean isMain = false;
	
	public abstract int getPageTitleTextResId();
	public abstract int getRootViewResId();
	
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
		
		if(getActivity() != null && mActivity == null) {
			mActivity = (BCPFragmentActivity) getActivity();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(getActivity() != null && mActivity == null) {
			mActivity = (BCPFragmentActivity) getActivity();
		}
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(titleBar != null) {
			
			if(!isMain) {
				titleBar.getBackButton().setVisibility(View.VISIBLE);
			}
			
			if(titleBar.getLayoutParams() != null) {
				titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(88);
			}
			
			if(getPageTitleTextResId() != 0) {
				titleBar.setTitle(getPageTitleTextResId());
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
	public void onDetach() {

		mActivity = null;
		titleBar = null;
		
		if(getRootViewResId() != 0 && mThisView != null) {
			ViewUnbindHelper.unbindReferences(mThisView.findViewById(getRootViewResId()));
		}
		
		super.onDetach();
	}
	
	@Override
	public void downloadInfo() {
		
		try {
			if(isDownloading || isLastList || url == null) {
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

			url += "last_priority=";
					
			if(last_priority != 0) {
				 url += last_priority;
			}
			
			if(!url.contains("num=")) {
				url += "&num=" + NUMBER_OF_LISTITEMS;
			}
			
			lastUrl = url;
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
						
						if(isLastList && last_priority != 0) {
//							ToastUtils.showToast(R.string.lastList);
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
				
				if(models.size() > 0) {
					last_priority = ((BCPBaseModel)models.get(models.size() - 1)).getPriority();
				}
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
		last_priority = 0;
		
		try {
			models.clear();
			adapter.notifyDataSetChanged();
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
		last_priority = 0;
		
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