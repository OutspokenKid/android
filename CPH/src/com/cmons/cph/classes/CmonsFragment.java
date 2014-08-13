package com.cmons.cph.classes;

import java.util.ArrayList;

import org.json.JSONObject;

import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;

public abstract class CmonsFragment extends BaseFragment {

	protected final int NUMBER_OF_LISTITEMS = 10;
	
	protected String title;
	
	protected int pageIndex;
	protected ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	protected CphAdapter adapter;
	protected String url;
	
	/**
	 * BaseModel로 파싱해서 models에 넣고, 조건에 따라 isLastList 설정.
	 * 
	 * @param objJSON Result of downloading.
	 */
	public abstract void parseJSON(JSONObject objJSON);

	@Override
	public void onResume() {
		super.onResume();

		SoftKeyboardUtils.hideKeyboard(mContext, mThisView);
	}
	
	@Override
	public void downloadInfo() {

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
		
		url += "num=" + NUMBER_OF_LISTITEMS +
				"&page=" + pageIndex;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					parseJSON(objJSON);
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
	
	@Override
	public void setPage(boolean successDownload) {

		hideLoadingView();
		isRefreshing = false;
		isDownloading = false;
		
		if(successDownload) {
			adapter.notifyDataSetChanged();
			pageIndex++;
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
		
		downloadInfo();
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastPageAnimResId() {
		// TODO Auto-generated method stub
		return 0;
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