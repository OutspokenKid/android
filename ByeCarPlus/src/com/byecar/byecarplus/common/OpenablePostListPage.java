package com.byecar.byecarplus.common;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPAdapter;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.OpenablePost;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;

public class OpenablePostListPage extends BCPFragment {

	public static int TYPE_NOTICE = 0;
	public static int TYPE_FAQ = 1;
	
	private ListView listView;
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.openablePostList_titleBar);
		
		listView = (ListView) mThisView.findViewById(R.id.openablePostList_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(null);
		listView.setDividerHeight(0);
	}

	@Override
	public void setListeners() {

		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
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
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_openable_post_list;
	}

	@Override
	public int getBackButtonResId() {

		if(type == TYPE_NOTICE) {
			return R.drawable.noticeboard_back_btn;
		} else {
			
			return R.drawable.faq_back_btn;
		}
	}

	@Override
	public int getBackButtonWidth() {
		
		return 235;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public void downloadInfo() {

		if(type == TYPE_NOTICE) {
			url = BCPAPIs.NOTICE_URL;
		} else {
			url = BCPAPIs.FAQ_URL;
		}
		
		url += "?num=0";
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		int size = 0;
		String keyword = type == TYPE_NOTICE? "notices": "faqs";
		int itemType = (type == TYPE_NOTICE? BCPConstants.ITEM_NOTICE : BCPConstants.ITEM_FAQ);
		try {
			JSONArray arJSON = objJSON.getJSONArray(keyword);
			
			size = arJSON.length();
			for(int i=0; i<size; i++) {
				OpenablePost op = new OpenablePost(arJSON.getJSONObject(i));
				op.setItemCode(itemType);
				models.add(op);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		if(size < NUMBER_OF_LISTITEMS) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			
			downloadInfo();
		}
	}

	@Override
	public void onDestroyView() {

		listView.setOnScrollListener(null);
		
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.openablePostList_mainLayout;
	}
}
