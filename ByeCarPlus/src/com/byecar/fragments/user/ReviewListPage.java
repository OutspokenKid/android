package com.byecar.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ReviewListPage extends BCPFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private Button btnSearch; 
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.reviewListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.reviewListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.reviewListPage_listView);
		
		btnSearch = (Button) mThisView.findViewById(R.id.reviewListPage_btnSearch);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange), 
        		getResources().getColor(R.color.titlebar_bg_brown));
        swipeRefreshLayout.setEnabled(true);
		
		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(30));
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
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				try {
					Car car = (Car) models.get(position);
					((MainActivity)mActivity).showCarDetailPage(0, car, Car.TYPE_BID);
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
	
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_BID_REVIEW_SEARCH, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_review_list;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_reviewList;
	}

	@Override
	public int getRootViewResId() {

		return R.id.reviewListPage_mainLayout;
	}

	@Override
	public void downloadInfo() {
		
		url = BCPAPIs.CAR_BID_REVIEW_URL;
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		int size = 0;
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			
			size = arJSON.length();
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				car.setItemCode(BCPConstants.ITEM_MY_BIDS_REVIEW);
				models.add(car);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		} finally {
			swipeRefreshLayout.setRefreshing(false);
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

}
