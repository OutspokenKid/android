package com.byecar.fragments.dealer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Review;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;

public class MyReviewPage extends BCPFragment {

	private ListView listView;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myReviewPage_titleBar);
		
		listView = (ListView) mThisView.findViewById(R.id.myReviewPage_listView);
	}

	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {
		
		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setDivider(null);
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

			}
		});
	}

	@Override
	public void setSizes() {

	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_my_review;
	}

	@Override
	public int getBackButtonResId() {
		
		return R.drawable.episode_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 261;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}
	
	@Override
	public void downloadInfo() {

		url = BCPAPIs.MY_REVIEW_URL + "?dealer_id=" + MainActivity.dealer.getId();
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("reviews");
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Review review = new Review(arJSON.getJSONObject(i));
				review.setItemCode(BCPConstants.ITEM_REVIEW);
				models.add(review);
			}
			
			return true;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
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
	public int getRootViewResId() {

		return R.id.myReviewPage_mainLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
//////////////////// Custom methods.

}
