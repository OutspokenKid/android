package com.byecar.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Post;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MyForumListPage extends BCPFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myForumListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.myForumListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.myForumListPage_listView);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
    	
        swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange), 
        		getResources().getColor(R.color.titlebar_bg_brown));
        swipeRefreshLayout.setEnabled(true);
        
        //여백 설정.
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View row, int position,
					long arg3) {
				
				try {
					Bundle bundle = new Bundle();
					bundle.putSerializable("post", (Post)models.get(position));
					mActivity.showPage(BCPConstants.PAGE_FORUM_DETAIL, bundle);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				
				new Handler().postDelayed(new Runnable() {
			        @Override 
			        public void run() {
			        	
			        	refreshPage();
			        }
			    }, 2000);
			}
		});
	}

	@Override
	public void setSizes() {

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my_forum_list;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_my_forum;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myForumListPage_mainLayout;
	}

	@Override
	public void downloadInfo() {
		
		url = BCPAPIs.FORUM_MY_URL;
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = null;
			int size = 0;

			arJSON = objJSON.getJSONArray("posts");
			size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Post post = new Post(arJSON.getJSONObject(i));
				post.setItemCode(BCPConstants.ITEM_FORUM);
				models.add(post);
			}
			
			if(size < NUMBER_OF_LISTITEMS) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		} finally {
			swipeRefreshLayout.setRefreshing(false);
		}
		
		return false;
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
}
