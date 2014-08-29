package com.zonecomms.napp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.models.GridMenu;
import com.zonecomms.common.models.GridMenuGroup;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.views.ViewForCategoryGroup;
import com.zonecomms.common.views.ViewForCategoryGroup.OnMenuClickedListener;
import com.zonecomms.napp.IntentHandlerActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.BaseFragment;
import com.zonecomms.napp.classes.ZoneConstants;

public class CategoryPage extends BaseFragment {

	private LinearLayout mainLinear;
	private int[] colors;
	private ArrayList<GridMenuGroup> menuGroups = new ArrayList<GridMenuGroup>();
	private int type;
	private boolean forPost; 
	
	@Override
	protected void bindViews() {
		mainLinear = (LinearLayout) mThisView.findViewById(R.id.categoryPage_mainLinear);
	}

	@Override
	protected void setVariables() {
		
		if(getArguments() != null) {
			type = getArguments().getInt("type");
			forPost = getArguments().getBoolean("forPost");
		}

		colors = new int[]{
				Color.rgb(125, 125, 125),
				Color.rgb(100, 100, 100),
				Color.rgb(75, 75, 75),
				Color.rgb(50, 50, 50),
				};
	}

	@Override
	protected void createPage() {
		
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {

		super.downloadInfo();
		
		if(forPost) {
			if(type == ZoneConstants.TYPE_CATEGORY_THEMA) {
				title = "주제별 최신글";
			} else {
				title = "지역별 최신글";
			}
			
			mActivity.setTitleText(title);
		}
		
		String url = ZoneConstants.BASE_URL + "common/cate_list_all" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
				"&l_cate_id=" + type;
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				ToastUtils.showToast(R.string.failToLoadCategory);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("CategoryPage.downloadInfo.onCompleted.  url : " + url + "\nresult : " + result);
				
				try {
					JSONArray arJSON = (new JSONObject(result)).getJSONArray("data");
					
					int length = arJSON.length();
					for(int i=0; i<length; i++) {

						JSONObject objGroup = arJSON.getJSONObject(i);
						GridMenuGroup menuGroup = new GridMenuGroup(
								type == ZoneConstants.TYPE_CATEGORY_THEMA? colors[i]:colors[colors.length - 1 - i],
								objGroup.getString("m_cate_nm"));
						menuGroups.add(menuGroup);
						
						JSONArray arMenu = objGroup.getJSONArray("s_cate");
						for(int j=0; j<arMenu.length(); j++) {
							
							JSONObject objMenu = arMenu.getJSONObject(j);
							menuGroup.addMenu(objMenu.getString("s_cate_nm"), objMenu.getInt("s_cate_id"));
						}
					}
					
					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadCategory);
				}
			}
		};
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}

	@Override
	protected void setPage(boolean successDownload) {

		mActivity.hideLoadingView();
		
		if(successDownload) {
			int size = menuGroups.size();
			for(int i=0; i<size; i++) {
				
				if(menuGroups.get(i).getGridMenus().size() != 0) {
					ViewForCategoryGroup vfcg = new ViewForCategoryGroup(mContext);
					vfcg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					int p = ResizeUtils.getSpecificLength(8);
					vfcg.setPadding(p, p, p, i==size-1?p:0);
					vfcg.setGridMenuGroup(menuGroups.get(i), i%2==0);
					vfcg.setOnMenuClickedListener(new OnMenuClickedListener() {
						
						@Override
						public void onMenuClicked(int index, GridMenu gridMenu) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/";
							
							if(forPost) {
								uriString += "categoryposts";
							} else {
								uriString += "categorypaaps";
							}
							
							uriString += "?s_cate_id=" + gridMenu.getS_cate_id()
									+ "&s_cate_nm=" + gridMenu.getText();
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						}
					});
					mainLinear.addView(vfcg);
				}
			}
			
			mainLinear.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public String getTitleText() {

		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.categoryPage_mainScroll;
	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden && mainLinear.getChildCount() == 0) {
			downloadInfo();
		}

		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			mActivity.getTitleBar().hidePlusAppButton();
			
			if(type == ZoneConstants.TYPE_CATEGORY_REGION) {
				mActivity.getTitleBar().hideRegionButton();
				mActivity.getTitleBar().showThemaButton();
			} else{
				mActivity.getTitleBar().showRegionButton();
				mActivity.getTitleBar().hideThemaButton();
			}
		}
	}
	
	@Override
	public void onRefreshPage() {

		menuGroups.clear();
		mainLinear.removeAllViews();
		downloadInfo();
	}

	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}

/////////////////////// Custom methods.
	
	public int getPageType() {
		
		return type;
	}
	
	public void setPageType(int type) {
		
		this.type = type;
	}

	@Override
	protected String generateDownloadKey() {
		return "CATEGORYPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_category;
	}
}
